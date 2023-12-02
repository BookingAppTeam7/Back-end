package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
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
    private static AtomicLong counter=new AtomicLong();


    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Optional<Accommodation> findById(Long id) {
        return accommodationRepository.findById(id);
    }

    @Override
    public Optional<Accommodation> create(AccommodationPostDTO newAccommodation) throws Exception {
        Long newId=(Long) counter.incrementAndGet();
        List<Review> reviews=new ArrayList<>();
        Accommodation createdAccommodation=new Accommodation(newId,newAccommodation.name, newAccommodation.description, newAccommodation.location,newAccommodation.minGuests,newAccommodation. maxGuests, newAccommodation.type, newAccommodation.assets, newAccommodation.prices, newAccommodation.availability,newAccommodation.ownerId,AccommodationStatusEnum.PENDING,newAccommodation.cancellationDeadline, newAccommodation.reservationConfirmation,reviews,newAccommodation.images);
        return accommodationRepository.save(createdAccommodation);
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
