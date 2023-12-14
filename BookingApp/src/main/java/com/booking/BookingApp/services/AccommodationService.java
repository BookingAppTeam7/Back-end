package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.TypeEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import io.micrometer.common.util.StringUtils;
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

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll(); ///ovde mozda korigovati da vraca u availabilitiju samo tip AVAILABILITY
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
    public Optional<Accommodation> create(AccommodationPostDTO newAccommodation) throws Exception {

        validatorService.validatePost(newAccommodation);

        List<Review> reviews = new ArrayList<>();

        Accommodation createdAccommodation = new Accommodation(
                newAccommodation.getName(),
                newAccommodation.getDescription(),
                new Location(newAccommodation.location.getAddress(), newAccommodation.location.getCity(), newAccommodation.location.getCountry(), newAccommodation.location.getX(), newAccommodation.location.getY()),
                newAccommodation.getMinGuests(),
                newAccommodation.getMaxGuests(),
                newAccommodation.getType(),
                newAccommodation.getAssets(),
//                newAccommodation.getPrices(),
                newAccommodation.getOwnerId(),
                newAccommodation.getCancellationDeadline(),
                ReservationConfirmationEnum.MANUAL,
                reviews,
                newAccommodation.getImages(),
                AccommodationStatusEnum.PENDING,
                false
        );
        return Optional.of(accommodationRepository.save(createdAccommodation));
    }

    @Override
    public Optional<Accommodation> update(AccommodationPutDTO updatedAccommodation, Long id) throws Exception {
        validatorService.validatePut(updatedAccommodation,id);
        Optional<Accommodation> accommodation=accommodationRepository.findById(id);
        if(!accommodation.isPresent()){return null;}
        List<PriceCard>prices=accommodation.get().prices;
        List<Review>reviews=accommodation.get().reviews;
        Accommodation result=new Accommodation(id,updatedAccommodation.name, updatedAccommodation.description, updatedAccommodation.location,updatedAccommodation.minGuests,updatedAccommodation. maxGuests, updatedAccommodation.type, updatedAccommodation.assets, prices,updatedAccommodation.ownerId,updatedAccommodation.cancellationDeadline, updatedAccommodation.reservationConfirmation,reviews,updatedAccommodation.images,false);
        return Optional.of(accommodationRepository.saveAndFlush(result));
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
            boolean cityMatches = city == null || a.location.city.equalsIgnoreCase(city);
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
    private boolean hasAvailableTimeSlot(Accommodation accommodation, Date arrival, Date checkout) {
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
    private AccommodationDetails convertToAccommodationDetails(Accommodation accommodation, Date arrival, Date checkout, int guests) {
        double totalPrice = accommodation.calculateTotalPrice(arrival, checkout, guests);
        double averageRating = accommodation.calculateAverageRating();
        double unitPrice = accommodation.calculateUnitPrice(arrival, checkout);

        return new AccommodationDetails(accommodation,totalPrice,unitPrice,averageRating);
    }
}
