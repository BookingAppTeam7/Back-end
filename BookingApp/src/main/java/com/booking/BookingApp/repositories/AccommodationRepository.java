package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.Accommodation;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccommodationRepository implements IAccommodationRepository{
    private List<Accommodation> accommodations = new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();


    @Override
    public List<Accommodation> findAll() {
        return this.accommodations;
    }

    @Override
    public Optional<Accommodation> findById(Long id) {
        Optional<Accommodation> res=accommodations.stream().filter(accommodation -> accommodation.getId() == id).findFirst();
        if(res.isPresent()) {

            return res;
        }
        return null;
    }
    public Optional<Accommodation> getById(Long id){
        return accommodations.stream().filter(accommodation -> accommodation.getId() == id).findFirst();
    }

    @Override
    public Optional<Accommodation> save(Accommodation createdAccommodation) {
        this.accommodations.add(createdAccommodation);
        return getById(createdAccommodation.getId());
    }

    @Override
    public Accommodation saveAndFlush(Accommodation updatedAccommodation) {
        Optional<Accommodation> optionalAccommodation=getById(updatedAccommodation.id);

        if(optionalAccommodation.isPresent()){
            Accommodation accommodation=optionalAccommodation.get();
            accommodation.setName(updatedAccommodation.getName());
            accommodation.setDescription(updatedAccommodation.description);
            accommodation.setLocation(updatedAccommodation.location);
            accommodation.setMinGuests(updatedAccommodation.minGuests);
            accommodation.setMaxGuests(updatedAccommodation.maxGuests);
            accommodation.setType(updatedAccommodation.type);
            accommodation.setAssets(updatedAccommodation.assets);
            accommodation.setOwnerId(updatedAccommodation.ownerId);
            accommodation.setStatus(AccommodationStatusEnum.PENDING);
            accommodation.setCancellationDeadline(updatedAccommodation.cancellationDeadline);
            accommodation.setReservationConfirmation(ReservationConfirmationEnum.MANUAL);

            return accommodation;
        }else{
            throw new RuntimeException("Smestaj s ID-om "+updatedAccommodation.id+" nije pronadjen.");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Accommodation> accommodation=getById(id);
        if(accommodation.isPresent()){
            Accommodation accommodationToDelete = accommodation.get();
            accommodations.remove(accommodationToDelete);
        }
    }
}
