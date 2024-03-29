package com.booking.BookingApp.services;

import com.booking.BookingApp.exceptions.NotFoundException;
import com.booking.BookingApp.exceptions.ValidationException;
import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationGetDTO;
import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.models.users.Notification;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IPriceCardRepository;
import com.booking.BookingApp.repositories.IReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static javax.management.Query.eq;

@Service
public class ReservationService implements IReservationService{

    @Autowired
    public IReservationRepository reservationRepository;

    @Autowired
    private AccommodationService accommodationService=new AccommodationService();

    @Autowired
    private IPriceCardRepository priceCardRepository;

    @Autowired
    private IAccommodationRepository accommodationRepository;
    private UserService userService=new UserService();


    @Autowired

    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private INotificationService notificationService;
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
    public Reservation confirmReservation(Long reservationId) throws Exception {

        Reservation result=new Reservation();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + reservationId));
        Accommodation accommodation=accommodationService.findById(reservation.accommodation.id)
                .orElseThrow(() -> new Exception("Accommodation not found with id: "+reservation.accommodation.id));
        User user=userService.findUserById(reservation.getUser().username);
        if(user==null){
            throw new Exception("User not found with id: "+reservation.getUser().username);
        }
        if(reservation.status.equals(ReservationStatusEnum.APPROVED))
            throw new Exception("Reservation already approved!");

        List<Reservation> allReservations=reservationRepository.findByAccommodationId(accommodation.id);
        for(Reservation r:allReservations){
            if(r.status.equals(ReservationStatusEnum.APPROVED) && timeSlotsOverlap(r.timeSlot,reservation.timeSlot))
                throw new ValidationException("There already exists confirmed reservation for this accommodation in selected time slot");
        }

        if (hasAvailableTimeSlot(accommodation,reservation.timeSlot.startDate,reservation.timeSlot.endDate)) {
            reservation.setStatus(ReservationStatusEnum.APPROVED);
            result=reservationRepository.save(reservation);
            editPriceCards(accommodation.id,reservation.timeSlot.startDate,reservation.timeSlot.endDate);
        } else {
            throw new Exception("Accommodation not available in the selected time slot");
        }


        NotificationPostDTO not=new NotificationPostDTO();
        not.setUserId(reservation.user.username);
        not.setType("RESERVATION_APPROVED");
        not.setTime(LocalDateTime.now());
        not.setContent("Reservation in accommodation :"+reservation.accommodation.name.toUpperCase()+" APPROVED by owner "+reservation.accommodation.ownerId+"!");

        User guest=userService.findUserById(reservation.user.username);
        if(guest.ownerRepliedToRequestNotification) {
            this.simpMessagingTemplate.convertAndSend( "/socket-publisher/"+reservation.user.username,not);
        }
        notificationService.create(not);

        return result;
    }

    public boolean hasAvailableTimeSlot(Accommodation accommodation, Date arrival, Date checkout) {
        List<Reservation> reservations=reservationRepository.findByAccommodationId(accommodation.id);
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
    public Optional<Reservation> create(ReservationPostDTO newReservation) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        Accommodation accommodation = this.accommodationService.findById(newReservation.getAccommodationId())
                .orElseThrow(() -> new NotFoundException("Accommodation not found with id: " + newReservation.getAccommodationId()));
        if(!accommodation.status.equals(AccommodationStatusEnum.APPROVED)){
            throw new ValidationException("Accommodation not approved!");
        }
        User user=this.userService.findUserById(newReservation.getUserId());
        if(user==null){
            throw new Exception("User not found with id: "+newReservation.getUserId());
        }
        if(newReservation.timeSlot.startDate.after(newReservation.timeSlot.endDate) || newReservation.timeSlot.startDate.before(new Date()) || newReservation.timeSlot.endDate.before(new Date()))
            throw new ValidationException("Time slot is incorrect!");
        if(!accommodationService.hasAvailableTimeSlot(accommodation,newReservation.timeSlot.getStartDate(),newReservation.timeSlot.getEndDate()))
            throw new ValidationException("Accommodation not available in selected time slot!");

        if(newReservation.getNumberOfGuests()>accommodation.maxGuests || newReservation.getNumberOfGuests()<accommodation.minGuests)
            throw new ValidationException("Accommodation not available for that many guests!");
        //ako postoji potvrdjena rezervacija na isti smestaj ciji se timeslot preklapa sa ovim novim, baci Exception
        List<Reservation> allReservations=findAll();
        for(Reservation r:allReservations){
            if(r.status.equals(ReservationStatusEnum.APPROVED) && r.accommodation.id.equals(accommodation.id) && timeSlotsOverlap(r.timeSlot,newReservation.timeSlot))
                throw new ValidationException("There already exists confirmed reservation for that accommodation in selected time slot");
        }

        //ovde treba dodati da se u objektu created reservation cuva i price i priceType
        Reservation createdReservation=new Reservation(newId,accommodation,user,newReservation.timeSlot, ReservationStatusEnum.PENDING, newReservation.numberOfGuests,
                    newReservation.price,newReservation.priceType);

        NotificationPostDTO not=new NotificationPostDTO();
        not.setUserId(accommodation.ownerId);
        not.setType("RESERVATION_CREATED");
        not.setTime(LocalDateTime.now());
        not.setContent("Created reservation for your accommodation : "+accommodation.name.toUpperCase()+"  by guest "+createdReservation.user.username+"!");

        if(user.reservationRequestNotification) {
            this.simpMessagingTemplate.convertAndSend( "/socket-publisher/"+accommodation.ownerId,not);
        }

        notificationService.create(not);

        return Optional.of(reservationRepository.save(createdReservation));
    }
    public boolean timeSlotsOverlap(TimeSlot t1,TimeSlot t2){
        return !(t1.endDate.before(t2.startDate) || t1.startDate.after(t2.endDate));
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

        NotificationPostDTO not=new NotificationPostDTO();
        not.setUserId(reservation.user.username);
        not.setType("RESERVATION_REJECTED");
        not.setTime(LocalDateTime.now());
        not.setContent("Reservation in accommodation :"+reservation.accommodation.name.toUpperCase()+" REJECTED by owner "+reservation.accommodation.ownerId+"!");

        User user=userService.findUserById(reservation.user.username);
        if(user.ownerRepliedToRequestNotification) {
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + reservation.user.username, not);
        }
        notificationService.create(not);
    }

    @Override
    public void cancelReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + reservationId));
        if(reservation.status.equals(ReservationStatusEnum.CANCELLED))
            throw new Exception("Reservation already cancelled!");
        if(reservation.status.equals(ReservationStatusEnum.REJECTED))
            throw new Exception("Reservation already rejected!");
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
                return;
//                throw new Exception("Cancellation deadline is passed!");
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

        //ako je za smestaj podesena automatska potvrda zahteva , prihvata se prvi u tom terminu koji je pending

        if(accommodation.get().reservationConfirmation== ReservationConfirmationEnum.AUTOMATIC){
            List<Reservation> reservations=reservationRepository.findByAccommodationId(accommodation.get().id);

            for(Reservation r:reservations){
                if(r.status==ReservationStatusEnum.PENDING && (r.timeSlot.startDate.equals(reservation.timeSlot.startDate) || r.timeSlot.startDate.after(reservation.timeSlot.startDate)) && (r.timeSlot.endDate.equals(reservation.timeSlot.endDate) ||r.timeSlot.endDate.before(reservation.timeSlot.endDate))){
                    confirmReservation(r.id);
                    break;
                }
            }
        }

        NotificationPostDTO not=new NotificationPostDTO();
        not.setUserId(reservation.accommodation.ownerId);
        not.setType("RESERVATION_CANCELLED");
        not.setTime(LocalDateTime.now());
        not.setContent("Reservation with id : "+reservation.id+" in accommodation :"+reservation.accommodation.name.toUpperCase()+" CANCELLED !");

        User user=userService.findUserById(reservation.accommodation.ownerId);
        if(user.reservationCancellationNotification) {
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + user.username, not);
        }

        //this.simpMessagingTemplate.convertAndSend( "/socket-publisher/"+reservation.user.username,not);

        notificationService.create(not);
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

    @Override
    @Transactional
    public void editPriceCards(Long accommodationId, Date reservationStartDate, Date reservationEndDate) {
        Optional<Accommodation> accommodation = accommodationService.findById(accommodationId);
        // Iterate through the existing PriceCards and update them based on the reservation dates
        for (PriceCard priceCard : accommodation.get().getPrices()) {
            if (isWithinTimeSlot(reservationStartDate, reservationEndDate, priceCard.getTimeSlot())) {
                if(isSameDay(reservationStartDate,priceCard.timeSlot.startDate)){
                    priceCard.timeSlot.setStartDate(reservationEndDate);
                    break;
                }
                if(isSameDay(reservationEndDate,priceCard.timeSlot.endDate)){
                    priceCard.timeSlot.setEndDate(reservationStartDate);
                    break;
                }
                // If there is an overlap, split the existing PriceCard into two PriceCards
                PriceCard newPriceCard1 = new PriceCard();
                newPriceCard1.setTimeSlot(new TimeSlot(null, reservationEndDate, priceCard.getTimeSlot().getEndDate(), false));
                newPriceCard1.setPrice(priceCard.getPrice());
                newPriceCard1.setType(priceCard.getType());
                newPriceCard1.setDeleted(false);

                priceCard.getTimeSlot().setEndDate(reservationStartDate);

                // Add the new PriceCard
                accommodation.get().getPrices().add(newPriceCard1);

                break;
            }
        }
        accommodationRepository.save(accommodation.get());
    }
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
