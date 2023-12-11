package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IReservationRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccommodationValidatorService implements IAccommodationValidatorService{
    @Autowired
    public IAccommodationRepository accommodationRepository;
    @Autowired
    public IReservationRepository reservationRepository;
    public void validate(AccommodationPostDTO accommodation) {
        if (accommodation == null) {
            throw new IllegalArgumentException("Accommodation data is null");
        }

        if (StringUtils.isEmpty(accommodation.getName())) {
            throw new IllegalArgumentException("Accommodation name cannot be empty");
        }

        if (StringUtils.isEmpty(accommodation.getDescription())) {
            throw new IllegalArgumentException("Accommodation description cannot be empty");
        }

        if (accommodation.getLocation() == null) {
            throw new IllegalArgumentException("Accommodation location cannot be null");
        }

        if (accommodation.getMinGuests() <= 0 || accommodation.getMaxGuests() <= 0 || accommodation.getMinGuests() > accommodation.getMaxGuests()) {
            throw new IllegalArgumentException("Invalid values for minGuests or maxGuests");
        }

        if (accommodation.getType() == null) {
            throw new IllegalArgumentException("Accommodation type cannot be null");
        }
        if (accommodation.getCancellationDeadline() <= 0) {
            throw new IllegalArgumentException("Invalid values for cancellationDeadline");
        }
    }

    @Override
    public void validatePriceCard(PriceCardPostDTO newPriceCard) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(newPriceCard.accommodationId);

        if (!accommodation.isPresent()) {
            throw new IllegalArgumentException("Not found accommodation with this id." + newPriceCard.accommodationId.toString());
        }
        Date currentDate = new Date();

        if (newPriceCard.timeSlot.startDate.before(currentDate) || newPriceCard.timeSlot.endDate.before(currentDate)) {
            throw new IllegalArgumentException("Both start date and end date must be in the future.");
        }

        if (newPriceCard.timeSlot.startDate.after(newPriceCard.timeSlot.endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        if(newPriceCard.price<=0){
            throw new IllegalArgumentException("Invalid price value.Price must be positive number.");
        }

        if(newPriceCard.type==null){
            throw new IllegalArgumentException("Price type must be defined.");
        }

        List<PriceCard> existingPrices=accommodation.get().getPrices();
        for (PriceCard existingPrice : existingPrices) {
            if (isTimeSlotOverlap(existingPrice.getTimeSlot(), newPriceCard.getTimeSlot())) {
                throw new IllegalArgumentException("Overlap with an existing PriceCard time slot - price for this period is already defined.");
            }
        }

        List<Reservation> existingReservations=reservationRepository.findByAccommodationId(newPriceCard.accommodationId);
        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getStatus() != ReservationStatusEnum.APPROVED && existingReservation.getStatus() != ReservationStatusEnum.INPROCESS) {
                continue;
            }
            if (isTimeSlotOverlap(existingReservation.getTimeSlot(), newPriceCard.getTimeSlot())) {
                throw new IllegalArgumentException("Overlap with an existing Reservation time slot - reservations are already confirmed.");
            }
        }
    }
    private boolean isTimeSlotOverlap(TimeSlot existingTimeSlot, TimeSlot newTimeSlot) {
        return (newTimeSlot.getStartDate().before(existingTimeSlot.getEndDate()) &&
                newTimeSlot.getEndDate().after(existingTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().before(newTimeSlot.getEndDate()) &&
                        existingTimeSlot.getEndDate().after(newTimeSlot.getStartDate())) ||
                (existingTimeSlot.getStartDate().equals(newTimeSlot.getEndDate()) ||
                        existingTimeSlot.getEndDate().equals(newTimeSlot.getStartDate()));
    }
}
