package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IAccommodationRequestRepository;
import com.booking.BookingApp.repositories.ILocationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccommodationService implements IAccommodationService{
    @Autowired
    public IAccommodationRepository accommodationRepository;
    @Autowired
    public IAccommodationValidatorService validatorService;
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    public ILocationRepository locationRepository;
    @Autowired
    public IAccommodationRequestRepository requestRepository;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }
    @Override
    public List<Accommodation> findAllApproved(){
        List<Accommodation> all=accommodationRepository.findAll();
        List<Accommodation> ret=new ArrayList<>();
        for(Accommodation a:all)
            if(a.status.equals(AccommodationStatusEnum.APPROVED))
                ret.add(a);

        return ret;

    }
    @Override
    public Optional<Accommodation> findById(Long id) {
        Optional<Accommodation> res=accommodationRepository.findById(id);
        if(res.isPresent()){
            return res;
        }
        return null;
    }

    @Override
    public List<Accommodation> findByStatus(AccommodationStatusEnum status) {
        return accommodationRepository.findByStatus(status);
    }

    @Override
    public List<Accommodation> findByOwnerId(String ownerId) {
        Optional<User> user=userRepository.findById(ownerId);
        if(user.isPresent()) {
            return accommodationRepository.findByOwnerId(ownerId);
        }
        return null;
    }

    @Override
    public Optional<Accommodation> create(AccommodationPostDTO newAccommodation) throws Exception {
        if(!validatorService.validatePost(newAccommodation)){return Optional.empty();}
        List<Review> reviews = new ArrayList<>();

        Location newLocation=new Location(newAccommodation.location.getAddress(), newAccommodation.location.getCity(), newAccommodation.location.getCountry(), newAccommodation.location.getX(), newAccommodation.location.getY(),false);
        Location createdLocation=locationRepository.save(newLocation);

        Accommodation createdAccommodation = new Accommodation(
                newAccommodation.getName(),
                newAccommodation.getDescription(),
                createdLocation,
                newAccommodation.getMinGuests(),
                newAccommodation.getMaxGuests(),
                newAccommodation.getType(),
                newAccommodation.getAssets(),
                newAccommodation.getOwnerId(),
                newAccommodation.getCancellationDeadline(),
                ReservationConfirmationEnum.MANUAL,
                reviews,
                newAccommodation.getImages(),
                AccommodationStatusEnum.PENDING,  //postavljen status na pending - ceka se odobrenje ili odbijanje zahteva
                false
        );
        //createdAccommodation.setLocation(createdLocation);

        //kreiramo zahtev sada sa statusom PENDING_CREATED

        Accommodation created=accommodationRepository.save(createdAccommodation);

        AccommodationRequest request=new AccommodationRequest(created.id, AccommodationRequestStatus.PENDING_CREATED,null);
        requestRepository.save(request);
        return Optional.of(created);
    }

    @Override
    public Optional<Accommodation> update(AccommodationPutDTO updatedAccommodation, Long id) throws Exception {
        Optional<Accommodation> accommodation=accommodationRepository.findById(id);
        if(!accommodation.isPresent()){return null;}

        if(!validatorService.validatePut(updatedAccommodation,id)){
            return Optional.empty();
        }

        //accommodationRepository.updateStatus(id,AccommodationStatusEnum.PENDING);

        //treba prvo kreirati izmenjeni smestaj
        Location newLocation=new Location(updatedAccommodation.location.getAddress(), updatedAccommodation.location.getCity(), updatedAccommodation.location.getCountry(), updatedAccommodation.location.getX(), updatedAccommodation.location.getY(),false);
        Location createdLocation=locationRepository.save(newLocation);

        List<PriceCard>prices=accommodation.get().prices;
        Accommodation createdAccommodation = new Accommodation(
                updatedAccommodation.getName(),
                updatedAccommodation.getDescription(),
                createdLocation,
                updatedAccommodation.getMinGuests(),
                updatedAccommodation.getMaxGuests(),
                updatedAccommodation.getType(),
                updatedAccommodation.getAssets(),
                updatedAccommodation.getOwnerId(),
                updatedAccommodation.getCancellationDeadline(),
                updatedAccommodation.getReservationConfirmation(),
                updatedAccommodation.getImages(),
                AccommodationStatusEnum.PENDING,  //postavljen status na pending - ceka se odobrenje ili odbijanje zahteva
                false
        );
        //createdAccommodation.setLocation(createdLocation);

        //kreiramo zahtev sada sa statusom PENDING_CREATED

        Accommodation created=accommodationRepository.save(createdAccommodation);
        AccommodationRequest request=new AccommodationRequest(created.id, AccommodationRequestStatus.PENDING_EDITED,id);
        requestRepository.save(request);

//        List<PriceCard>prices=accommodation.get().prices;
//        List<Review>reviews=accommodation.get().reviews;
//
//        Location originalLocation=accommodation.get().location;
//        Location updatedLocation=new Location(originalLocation.id,updatedAccommodation.location.address,updatedAccommodation.location.city,updatedAccommodation.location.country,updatedAccommodation.location.x,updatedAccommodation.location.y,false);
//        locationRepository.saveAndFlush(updatedLocation);
//
//        Accommodation result=new Accommodation(id,updatedAccommodation.name, updatedAccommodation.description, updatedLocation,updatedAccommodation.minGuests,updatedAccommodation. maxGuests, updatedAccommodation.type, updatedAccommodation.assets, prices,updatedAccommodation.ownerId,updatedAccommodation.cancellationDeadline, updatedAccommodation.reservationConfirmation,reviews,updatedAccommodation.images,false,AccommodationStatusEnum.PENDING);
        return Optional.of(accommodationRepository.saveAndFlush(created));
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }

    @Override
    public List<AccommodationDetails> search(String city, int guests, Date arrivalDate, Date checkoutDate){
        List<Accommodation> retAcc = new ArrayList<>();
        List<Accommodation> allAcc = findAll();

        for (Accommodation a : allAcc) {
            boolean cityMatches = city == null || city.isEmpty() ||  a.location.city.equalsIgnoreCase(city);
            boolean guestsInRange = guests == -1 || (a.minGuests <= guests && a.maxGuests >= guests);

            if (cityMatches && a.status.equals(AccommodationStatusEnum.APPROVED) && guestsInRange) {
                if (hasAvailableTimeSlot(a, arrivalDate, checkoutDate)) {
                    retAcc.add(a);
                }
            }
        }

        List<AccommodationDetails> retDet = new ArrayList<>();
        for (Accommodation a : retAcc)
            retDet.add(convertToAccommodationDetails(a, arrivalDate, checkoutDate, guests));

        return retDet;
    }
    @Override
    public List<AccommodationDetails> filter(List<AccommodationDetails> searched, List<String> assets, TypeEnum type, double minTotalPrice, double maxTotalPrice){
        List<AccommodationDetails> ret = new ArrayList<>();

        for (AccommodationDetails ad : searched) {
            boolean typeCondition = type == null || ad.getAccommodation().getType().equals(type);
            boolean minPriceCondition = minTotalPrice == -1 || ad.getTotalPrice() >= minTotalPrice;
            boolean maxPriceCondition = maxTotalPrice == -1 || ad.getTotalPrice() <= maxTotalPrice;
            boolean assetsCondition = assets == null || ad.getAccommodation().getAssets().containsAll(assets);

            if (typeCondition && minPriceCondition && maxPriceCondition && assetsCondition) {
                ret.add(ad);
            }
        }

        return ret;
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
        timeSlotStart.setHours(0);
        timeSlotStart.setMinutes(0);
        timeSlotStart.setSeconds(0);
        Date timeSlotEnd = timeSlot.getEndDate();
        timeSlotEnd.setHours(0);
        timeSlotEnd.setMinutes(0);
        timeSlotEnd.setSeconds(0);
        arrival.setHours(0);
        arrival.setMinutes(0);
        arrival.setSeconds(0);
        checkout.setHours(0);
        checkout.setMinutes(0);
        checkout.setSeconds(0);
        return !(arrival.before(timeSlotStart) || checkout.after(timeSlotEnd));
    }
    private AccommodationDetails convertToAccommodationDetails(Accommodation accommodation, Date arrival, Date checkout, int guests) {
        double totalPrice = accommodation.calculateTotalPrice(arrival, checkout, guests);
        double averageRating = accommodation.calculateAverageRating();
        double unitPrice = accommodation.calculateUnitPrice(arrival, checkout);

        return new AccommodationDetails(accommodation,totalPrice,unitPrice,averageRating);
    }

    public Optional<Accommodation> updateStatus(Long accommodationId, AccommodationStatusEnum status) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(accommodationId);
        if(!accommodation.isPresent()){
            return Optional.empty();
        }
        int updatedRows = accommodationRepository.updateStatus(accommodationId, status);
        if (updatedRows > 0) {
            return accommodationRepository.findById(accommodationId);
        }
        return Optional.empty();

    }
    public Optional<Accommodation> updateImages(Long accommodationId,List<String> newImages){
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(accommodationId);
        if (optionalAccommodation.isPresent()) {
            Accommodation accommodation = optionalAccommodation.get();
            accommodation.getImages().addAll(newImages);
            return Optional.of(accommodationRepository.save(accommodation));

        }
        return Optional.empty();
    }
    public Optional<Accommodation> addReview(Long accommodationId, Review review){
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(accommodationId);
        if (optionalAccommodation.isPresent()) {
            Accommodation accommodation = optionalAccommodation.get();
            accommodation.getReviews().add(review);
            return Optional.of(accommodationRepository.save(accommodation));

        }
        return Optional.empty();
    }
    @Override
    @Transactional
    public void editPriceCards(Long accommodationId, Date reservationStartDate, Date reservationEndDate) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new RuntimeException("Accommodation not found with id: " + accommodationId));
        // Iterate through the existing PriceCards and update them based on the reservation dates
        for (PriceCard priceCard : accommodation.getPrices()) {
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
                accommodation.getPrices().add(newPriceCard1);

                break;
            }
        }
        accommodationRepository.save(accommodation);
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
