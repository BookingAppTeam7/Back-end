package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.Location;
import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccommodationService implements IAccommodationService{
    @Autowired
    public IAccommodationRepository accommodationRepository;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll(); ///ovde mozda korigovati
    }

    @Override
    public Optional<Accommodation> findById(Long id) {
        return accommodationRepository.findById(id);
    }

    @Override
    public Optional<Accommodation> create(AccommodationPostDTO newAccommodation) throws Exception {

            List<Review> reviews = new ArrayList<>();

            Accommodation createdAccommodation = new Accommodation(
                    newAccommodation.getName(),
                    newAccommodation.getDescription(),
                    new Location(newAccommodation.location.getAddress(), newAccommodation.location.getCity(), newAccommodation.location.getCountry(), newAccommodation.location.getX(), newAccommodation.location.getY()),
                    newAccommodation.getMinGuests(),
                    newAccommodation.getMaxGuests(),
                    newAccommodation.getType(),
                    newAccommodation.getAssets(),
                    newAccommodation.getPrices(),
                    newAccommodation.getAvailability(),
                    newAccommodation.getOwnerId(),
                    newAccommodation.getCancellationDeadline(),
                    newAccommodation.getReservationConfirmation(),
                    reviews,
                    newAccommodation.getImages(),
                    AccommodationStatusEnum.PENDING
            );

        System.out.println("Values: " + createdAccommodation.name);
            return Optional.of(accommodationRepository.save(createdAccommodation));

    }

    @Override
    public Accommodation update(AccommodationPutDTO updatedAccommodation, Long id) throws Exception {
        Accommodation result=new Accommodation(id,updatedAccommodation.name, updatedAccommodation.description, updatedAccommodation.location,updatedAccommodation.minGuests,updatedAccommodation. maxGuests, updatedAccommodation.type, updatedAccommodation.assets, updatedAccommodation.prices, updatedAccommodation.availability,updatedAccommodation.ownerId,updatedAccommodation.status,updatedAccommodation.cancellationDeadline, updatedAccommodation.reservationConfirmation, updatedAccommodation.reviews,updatedAccommodation.images);
        return accommodationRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }
}
