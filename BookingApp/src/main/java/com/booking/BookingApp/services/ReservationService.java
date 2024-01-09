package com.booking.BookingApp.services;

import com.booking.BookingApp.exceptions.NotFoundException;
import com.booking.BookingApp.exceptions.ValidationException;
import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationGetDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IPriceCardRepository;
import com.booking.BookingApp.repositories.IReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService implements IReservationService{

    @Autowired
    public IReservationRepository reservationRepository;

    @Autowired
    private AccommodationService accommodationService=new AccommodationService();

    @Autowired
    private IPriceCardRepository priceCardRepository;
    private UserService userService=new UserService();
    @Autowired
    public ReservationService(AccommodationService accommodationService,UserService userService) {
        this.accommodationService = accommodationService;
        this.userService=userService;
    }

    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {

        Optional<Reservation> res= reservationRepository.findById(id);
        if(res.isPresent()){
            return res;
        }
        return null;
    }

    @Transactional
    @Override
    public void confirmReservation(Long reservationId) throws Exception {
        System.out.println("USAO U CONFIRM RESERVATION");
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + reservationId));
        Accommodation accommodation=accommodationService.findById(reservation.accommodation.id)
                .orElseThrow(() -> new Exception("Accommodation not found with id: "+reservation.accommodation.id));
        if(reservation.status.equals(ReservationStatusEnum.APPROVED))
            throw new Exception("Reservation already approved!");
        // Check if the reservation is available in the selected time slot
        if (hasAvailableTimeSlot(accommodation,reservation.timeSlot.startDate,reservation.timeSlot.endDate)) {
            reservation.setStatus(ReservationStatusEnum.APPROVED);
            reservationRepository.save(reservation);
            System.out.println("SAVEOVAO RESERVACIJE");
            accommodationService.editPriceCards(accommodation.id,reservation.timeSlot.startDate,reservation.timeSlot.endDate);
            System.out.println("EDITOVAO PRICE CARDS");
        } else {
            throw new Exception("Reservation not available in the selected time slot");
        }
    }
    public boolean hasAvailableTimeSlot(Accommodation accommodation, Date arrival, Date checkout) {
        for (PriceCard priceCard : accommodation.prices) {
            if (isWithinTimeSlot(arrival, checkout, priceCard.timeSlot)) {
                return true;
            }
        }
        return false;
    }
    private boolean isWithinTimeSlot(Date arrival, Date checkout, TimeSlot timeSlot) {
        Date timeSlotStart = timeSlot.getStartDate();
        Date timeSlotEnd = timeSlot.getEndDate();
        return !(arrival.before(timeSlotStart) || checkout.after(timeSlotEnd));
    }
    @Override
    public Optional<Reservation> create(ReservationPostDTO newReservation) throws NotFoundException, ValidationException {
        Long newId= (Long) counter.incrementAndGet();
        Accommodation accommodation = this.accommodationService.findById(newReservation.getAccommodationId())
                .orElseThrow(() -> new NotFoundException("Accommodation not found with id: " + newReservation.getAccommodationId()));
        UserGetDTO user=this.userService.findById(newReservation.getUserId())
                .orElseThrow(()->new NotFoundException("User not found with id: "+newReservation.getUserId()));
        User foundUser=this.userService.findUserById(user.username);
//        .orElseThrow(()->new NotFoundException("User not found with token: "+user.token));
        if(newReservation.timeSlot.startDate.after(newReservation.timeSlot.endDate) || newReservation.timeSlot.startDate.before(new Date()) || newReservation.timeSlot.endDate.before(new Date()))
            throw new ValidationException("Time slot is incorrect!");
        if(!accommodationService.hasAvailableTimeSlot(accommodation,newReservation.timeSlot.getStartDate(),newReservation.timeSlot.getEndDate()))
            throw new ValidationException("Accommodation not available in selected time slot!");

        if(newReservation.getNumberOfGuests()>accommodation.maxGuests || newReservation.getNumberOfGuests()<accommodation.minGuests)
            throw new ValidationException("Accommodation not available for that many guests!");
        //ako postoji potvrdjena rezervacija na isti smestaj ciji se timeslot preklapa sa ovim novim, baci Exception
        List<Reservation> allReservations=reservationRepository.findAll();
        for(Reservation r:allReservations){
            if(r.status.equals(ReservationStatusEnum.APPROVED) && r.accommodation.id.equals(accommodation.id) && timeSlotsOverlap(r.timeSlot,newReservation.timeSlot))
                throw new ValidationException("There already exists confirmed reservation for that accommodation in selected time slot");
        }

        //ovde treba dodati da se u objektu created reservation cuva i price i priceType
        Reservation createdReservation=new Reservation(newId,accommodation,foundUser,newReservation.timeSlot, ReservationStatusEnum.PENDING, newReservation.numberOfGuests,
                    newReservation.price,newReservation.priceType);
        return Optional.of(reservationRepository.save(createdReservation));
    }
    public boolean timeSlotsOverlap(TimeSlot t1,TimeSlot t2){
        return !(t1.endDate.before(t2.startDate) || t1.startDate.after(t2.endDate));
        //if(t1.endDate.before(t2.startDate) || t1.startDate.after(t2.endDate))
        //    return false;
        //return true;
    }
    @Override
    public Reservation update(ReservationPutDTO updatedReservation, Long id) throws Exception {
       // Reservation result=new Reservation(id,updatedReservation.userId,updatedReservation.timeSlot,updatedReservation.status);
       //return reservationRepository.saveAndFlush(result);
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + id));
        //existingReservation.setUserId(updatedReservation.getUserId());
        existingReservation.setTimeSlot(updatedReservation.getTimeSlot());
        existingReservation.setStatus(updatedReservation.getStatus());
        existingReservation.setNumberOfGuests(updatedReservation.getNumberOfGuests());
        // Save and flush the updated reservation
        return reservationRepository.saveAndFlush(existingReservation);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findByAccommodationId(Long id){
        return reservationRepository.findByAccommodationId(id);
    }

    @Override
    public List<Reservation> findByGuestId(String username){
        return reservationRepository.findByUserUsername(username);
//        List<Reservation> reservations=reservationRepository.findByUserUsername(username);
//        List<ReservationGetDTO> result=new ArrayList<>();
//
//        for(Reservation r:reservations){
//            result.add(new ReservationGetDTO(r.id,r.accommodation.id,r.timeSlot,r.status,r.numberOfGuests));
//        }
//        return result;
    }

    @Override
    public void rejectReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + reservationId));
        Accommodation accommodation=accommodationService.findById(reservation.accommodation.id)
                .orElseThrow(() -> new Exception("Accommodation not found with id: "+reservation.accommodation.id));
        if(reservation.status.equals(ReservationStatusEnum.APPROVED))
            throw new Exception("Reservation already approved!");
        if(reservation.status.equals(ReservationStatusEnum.REJECTED))
            throw new Exception("Reservation already rejected!");

        reservationRepository.updateStatus(reservationId,ReservationStatusEnum.REJECTED);

    }

    @Override
    public void cancelReservation(Long reservationId) throws Exception {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + reservationId));
        if(reservation.status.equals(ReservationStatusEnum.CANCELLED))
            throw new Exception("Reservation already cancelled!");
        if(reservation.status.equals(ReservationStatusEnum.REJECTED))
            throw new Exception("Reservation already approved!");
        if(reservation.status.equals(ReservationStatusEnum.PENDING))
            throw new Exception("Reservation is not already approved/rejected!");

        Date startDate = reservation.getTimeSlot().getStartDate();

        if (startDate != null) {
            Date currentDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, -reservation.getAccommodation().getCancellationDeadline());
            Date cancellationAllowedDate = calendar.getTime();

            if (currentDate.after(cancellationAllowedDate)) {
                throw new Exception("Cancellation deadline is passed!");
            }
        }

        //oslobadjanje termina

        boolean updatedExistingPriceCard=false;

        Optional<Accommodation> accommodation=accommodationService.findById(reservation.accommodation.id);
        List<PriceCard> prices=accommodation.get().prices;

        for (PriceCard p : prices) {
            TimeSlot existingTimeSlot = p.timeSlot;

            if (existingTimeSlot.overlapsWith(reservation.timeSlot)) {
                // Overlapping time slots, update the existing price card
                p.timeSlot = existingTimeSlot.mergeWith(reservation.timeSlot);
                priceCardRepository.saveAndFlush(p);
                updatedExistingPriceCard = true;
                break;
            }
        }

        if (!updatedExistingPriceCard) {
            // No overlapping time slots found, create a new price card
            TimeSlot newTimeSlot = new TimeSlot(reservation.timeSlot.startDate, reservation.timeSlot.endDate, false);
            PriceCard newPriceCard = new PriceCard(newTimeSlot, reservation.price, reservation.priceType, false);
            PriceCard savedPriceCard = priceCardRepository.save(newPriceCard);
            prices.add(savedPriceCard);
        }

//        for(PriceCard p:prices){
//            if(p.timeSlot.endDate.equals(reservation.timeSlot.startDate)){
//                p.timeSlot.endDate=reservation.timeSlot.endDate;
//                priceCardRepository.saveAndFlush(p);
//                break;
//
//            }
//
//            else if(p.timeSlot.startDate.equals(reservation.timeSlot.endDate)){
//                p.timeSlot.startDate=reservation.timeSlot.startDate;
//                priceCardRepository.saveAndFlush(p);
//                break;
//            }
//            else if(reservation.timeSlot.startDate.before(p.timeSlot.startDate) && reservation.timeSlot.endDate.before(p.timeSlot.endDate) && reservation.timeSlot.endDate.after(p.timeSlot.startDate)){
//                p.timeSlot.startDate=reservation.timeSlot.startDate;
//                priceCardRepository.saveAndFlush(p);
//                break;
//
//            }
//            else if(reservation.timeSlot.startDate.before(p.timeSlot.startDate) && reservation.timeSlot.endDate.after(p.timeSlot.endDate) && reservation.timeSlot.endDate.after(p.timeSlot.startDate)){
//                p.timeSlot.startDate=reservation.timeSlot.startDate;
//                p.timeSlot.endDate=reservation.timeSlot.endDate;
//                priceCardRepository.saveAndFlush(p);
//                break;
//
//            }
//            else if(p.timeSlot.startDate.before(reservation.timeSlot.startDate) && p.timeSlot.endDate.after(reservation.timeSlot.endDate) && p.timeSlot.endDate.after(reservation.timeSlot.endDate) && reservation.timeSlot.endDate.after(p.timeSlot.startDate)) {
//                //datum upada u postojeci
//                break;
//
//            }
//            else {
//                TimeSlot timeSlot=new TimeSlot(reservation.timeSlot.startDate,reservation.timeSlot.endDate,false);
//                PriceCard priceCard=new PriceCard(timeSlot,reservation.price,reservation.priceType,false);
//                PriceCard newPriceCard=priceCardRepository.save(priceCard);
//                prices.add(newPriceCard);
//
//                break;
//
//            }

        //}
        reservationRepository.updateStatus(reservationId,ReservationStatusEnum.CANCELLED);

    }
    @Override
    public List<Reservation> searchFilter(String accName, Date startDate, Date endDate, ReservationStatusEnum status){
        List<Reservation> ret=new ArrayList<>();
        List<Reservation> all=findAll();
        for(Reservation r:all){
            boolean accNameMatches=accName==null || accName.isEmpty() || r.accommodation.name.equalsIgnoreCase(accName);
            boolean statusMatches=status==null || r.status.equals(status);
            if(accNameMatches && statusMatches && !startDate.after(r.timeSlot.startDate) && !endDate.before(r.timeSlot.endDate)){
                ret.add(r);
            }
        }
        return ret;
    }
}
