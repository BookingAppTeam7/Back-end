package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccommodationService implements IAccommodationService{
    @Autowired
    public IAccommodationRepository accommodationRepository;
    @Autowired
    public IAccommodationValidatorService validatorService;
    @Autowired
    public IUserRepository userRepository;

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
        Accommodation result=new Accommodation(id,updatedAccommodation.name, updatedAccommodation.description, updatedAccommodation.location,updatedAccommodation.minGuests,updatedAccommodation. maxGuests, updatedAccommodation.type, updatedAccommodation.assets, prices,updatedAccommodation.ownerId,updatedAccommodation.cancellationDeadline, updatedAccommodation.reservationConfirmation,reviews,updatedAccommodation.images,false,AccommodationStatusEnum.PENDING);
        return Optional.of(accommodationRepository.saveAndFlush(result));
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }

    @Override
    public Optional<Accommodation> updateStatus(Long accommodationId, AccommodationStatusEnum status) {
        int updatedRows = accommodationRepository.updateStatus(accommodationId, status);
        if (updatedRows > 0) {
            return accommodationRepository.findById(accommodationId);
        }
        return Optional.empty();
    }
}
