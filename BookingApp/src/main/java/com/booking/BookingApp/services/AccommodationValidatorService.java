package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.*;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IReservationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccommodationValidatorService implements IAccommodationValidatorService{
    @Autowired
    public IAccommodationRepository accommodationRepository;
    @Autowired
    public IReservationRepository reservationRepository;
    @Autowired
    public IUserRepository userRepository;
    public boolean validatePost(AccommodationPostDTO accommodation) {
        if (accommodation == null) {
            return false;
            //throw new IllegalArgumentException("Accommodation data is null");
        }
        Optional<User> owner=userRepository.findById(accommodation.ownerId);
        if(!owner.isPresent()){
            return false;
            //throw new IllegalArgumentException("Owner not found");
        }

        if (StringUtils.isEmpty(accommodation.getName())) {
            return false;
            //throw new IllegalArgumentException("Accommodation name cannot be empty");
        }

        if (StringUtils.isEmpty(accommodation.getDescription())) {
            return false;
            //throw new IllegalArgumentException("Accommodation description cannot be empty");
        }

        if (accommodation.getLocation() == null) {
            return false;
            //throw new IllegalArgumentException("Accommodation location cannot be null");
        }

        if (accommodation.getMinGuests() <= 0 || accommodation.getMaxGuests() <= 0 || accommodation.getMinGuests() > accommodation.getMaxGuests()) {
            return false;
            //throw new IllegalArgumentException("Invalid values for minGuests or maxGuests");
        }

        if (accommodation.getType() == null) {
            return false;
            //throw new IllegalArgumentException("Accommodation type cannot be null");
        }
        if (accommodation.getCancellationDeadline() <= 0) {
            return false;
            //throw new IllegalArgumentException("Invalid values for cancellationDeadline");
        }
        if (StringUtils.isEmpty(accommodation.location.getAddress())) {
            return false;
            //throw new IllegalArgumentException("Address can not be empty");
        }
        if (StringUtils.isEmpty(accommodation.location.getCountry())) {
            return false;
            //throw new IllegalArgumentException("Country can not be empty");
        }
        if (StringUtils.isEmpty(accommodation.location.getCity())) {
            return false;
            //throw new IllegalArgumentException("City can not be empty");
        }

        return true;
    }

    @Override
    public boolean validatePut(AccommodationPutDTO updatedAccommodation, Long id) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(id);
        if(!accommodation.isPresent()){
            return false;
            //throw new IllegalArgumentException("Cannot change accommodation with this id - not found!");
        }
        if (updatedAccommodation == null) {
            return false;
            //throw new IllegalArgumentException("Accommodation data is null");
        }
        Optional<User> owner=userRepository.findById(updatedAccommodation.ownerId);
        if(!owner.isPresent()){
            return false;
            //throw new IllegalArgumentException("Owner not found");
        }

        if (StringUtils.isEmpty(updatedAccommodation.getName())) {
            return false;
            //throw new IllegalArgumentException("Accommodation name cannot be empty");
        }

        if (StringUtils.isEmpty(updatedAccommodation.getDescription())) {
            return false;
            //throw new IllegalArgumentException("Accommodation description cannot be empty");
        }

        if (updatedAccommodation.getLocation() == null) {
            return false;
            //throw new IllegalArgumentException("Accommodation location cannot be null");
        }

        if (updatedAccommodation.getMinGuests() <= 0 || updatedAccommodation.getMaxGuests() <= 0 || updatedAccommodation.getMinGuests() > updatedAccommodation.getMaxGuests()) {
            return false;
            //throw new IllegalArgumentException("Invalid values for minGuests or maxGuests");
        }

        if (updatedAccommodation.getType() == null) {
            return false;
            //throw new IllegalArgumentException("Accommodation type cannot be null");
        }
        if (updatedAccommodation.getCancellationDeadline() <= 0) {
            return false;
            //throw new IllegalArgumentException("Invalid values for cancellationDeadline");
        }
        if(updatedAccommodation.getReservationConfirmation()==null){
            return false;
            //throw new IllegalArgumentException("Reservation confirmation cannot be null");
        }
        if (StringUtils.isEmpty(updatedAccommodation.location.getAddress())) {
            return false;
            //throw new IllegalArgumentException("Address can not be empty");
        }
        if (StringUtils.isEmpty(updatedAccommodation.location.getCountry())) {
            return false;
            //throw new IllegalArgumentException("Country can not be empty");
        }
        if (StringUtils.isEmpty(updatedAccommodation.location.getCity())) {
            return false;
            //throw new IllegalArgumentException("City can not be empty");
        }

        List<Reservation> existingReservations=reservationRepository.findByAccommodationId(id);
        Date currentDate = new Date();
        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getStatus() != ReservationStatusEnum.APPROVED && existingReservation.getStatus() != ReservationStatusEnum.INPROCESS && existingReservation.getStatus()!=ReservationStatusEnum.REJECTED) {
                continue;
            }
            if (existingReservation.getTimeSlot().startDate.after(currentDate)) {
                return false;
                //throw new IllegalArgumentException("Overlap with an existing Reservation time slot - reservations are already confirmed ir un process.");
            }
        }


        return true;
    }

    @Override
    public boolean validatePriceCardPost(PriceCardPostDTO newPriceCard) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(newPriceCard.accommodationId);

        if (!accommodation.isPresent()) {
            return false;
            //throw new IllegalArgumentException("Not found accommodation with this id." + newPriceCard.accommodationId.toString());
        }
        Date currentDate = new Date();

        if (newPriceCard.timeSlot.startDate.before(currentDate) || newPriceCard.timeSlot.endDate.before(currentDate)) {
            return false;
            //throw new IllegalArgumentException("Both start date and end date must be in the future.");
        }

        if (newPriceCard.timeSlot.startDate.after(newPriceCard.timeSlot.endDate)) {
            return false;
            //throw new IllegalArgumentException("Start date must be before end date.");
        }

        if(newPriceCard.price<=0){
            return false;
            //throw new IllegalArgumentException("Invalid price value.Price must be positive number.");
        }

        if(newPriceCard.type==null){
            return false;
            //throw new IllegalArgumentException("Price type must be defined.");
        }

        List<PriceCard> existingPrices=accommodation.get().getPrices();
        for (PriceCard existingPrice : existingPrices) {
            if (isTimeSlotOverlap(existingPrice.getTimeSlot(), newPriceCard.getTimeSlot())) {
                return false;
                //throw new IllegalArgumentException("Overlap with an existing PriceCard time slot - price for this period is already defined.");
            }
        }

        List<Reservation> existingReservations=reservationRepository.findByAccommodationId(newPriceCard.accommodationId);
        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getStatus() != ReservationStatusEnum.APPROVED && existingReservation.getStatus() != ReservationStatusEnum.INPROCESS && existingReservation.getStatus()!=ReservationStatusEnum.REJECTED) {
                continue;
            }
            if (isTimeSlotOverlap(existingReservation.getTimeSlot(), newPriceCard.getTimeSlot())) {
                return false;
                //throw new IllegalArgumentException("Overlap with an existing Reservation time slot - reservations are already confirmed.");
            }
        }



        return true;
    }

    @Override
    public boolean validatePriceCardPut(PriceCardPutDTO newPriceCard,Long id) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(newPriceCard.accommodationId);

        if (!accommodation.isPresent()) {
            return false;
            //throw new IllegalArgumentException("Not found accommodation with this id." + newPriceCard.accommodationId.toString());
        }
        Date currentDate = new Date();

        if (newPriceCard.timeSlot.startDate.before(currentDate) || newPriceCard.timeSlot.endDate.before(currentDate)) {
            return false;
            //throw new IllegalArgumentException("Both start date and end date must be in the future.");
        }

        if (newPriceCard.timeSlot.startDate.after(newPriceCard.timeSlot.endDate)) {
            return false;
            //throw new IllegalArgumentException("Start date must be before end date.");
        }

        if(newPriceCard.price<=0){
            return false;
            //throw new IllegalArgumentException("Invalid price value.Price must be positive number.");
        }

        if(newPriceCard.type==null){
            return false;
            //throw new IllegalArgumentException("Price type must be defined.");
        }

        List<PriceCard> existingPrices=accommodation.get().getPrices();
        for (PriceCard existingPrice : existingPrices) {
            if(existingPrice.id!= id) { //da bismo izbegli proveru sa vec postojecim
                if (isTimeSlotOverlap(existingPrice.getTimeSlot(), newPriceCard.getTimeSlot())) {
                    return false;
                    //throw new IllegalArgumentException("Overlap with an existing PriceCard time slot - price for this period is already defined.");
                }
            }
        }

        List<Reservation> existingReservations=reservationRepository.findByAccommodationId(newPriceCard.accommodationId);
        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getStatus() != ReservationStatusEnum.APPROVED && existingReservation.getStatus() != ReservationStatusEnum.INPROCESS && existingReservation.getStatus()!=ReservationStatusEnum.REJECTED) {
                continue;
            }
            if (isTimeSlotOverlap(existingReservation.getTimeSlot(), newPriceCard.getTimeSlot())) {
                return false;
                //throw new IllegalArgumentException("Overlap with an existing Reservation time slot - reservations are already confirmed.");
            }
        }
        return true;
    }
    private boolean isTimeSlotOverlap(TimeSlotPostDTO existingTimeSlot, TimeSlotPostDTO newTimeSlot) {
        return (newTimeSlot.getStartDate().before(existingTimeSlot.getEndDate()) &&
                newTimeSlot.getEndDate().after(existingTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().before(newTimeSlot.getEndDate()) &&
                        existingTimeSlot.getEndDate().after(newTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().equals(newTimeSlot.getEndDate()) ||
                        existingTimeSlot.getEndDate().equals(newTimeSlot.getStartDate()));
    }

    private boolean isTimeSlotOverlap(TimeSlot existingTimeSlot, TimeSlot newTimeSlot) {
        return (newTimeSlot.getStartDate().before(existingTimeSlot.getEndDate()) &&
                newTimeSlot.getEndDate().after(existingTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().before(newTimeSlot.getEndDate()) &&
                        existingTimeSlot.getEndDate().after(newTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().equals(newTimeSlot.getEndDate()) ||
                        existingTimeSlot.getEndDate().equals(newTimeSlot.getStartDate()));
    }

    private boolean isTimeSlotOverlap(TimeSlot existingTimeSlot, TimeSlotPostDTO newTimeSlot) {
        return (newTimeSlot.getStartDate().before(existingTimeSlot.getEndDate()) &&
                newTimeSlot.getEndDate().after(existingTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().before(newTimeSlot.getEndDate()) &&
                        existingTimeSlot.getEndDate().after(newTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().equals(newTimeSlot.getEndDate()) ||
                        existingTimeSlot.getEndDate().equals(newTimeSlot.getStartDate()));
    }


}
