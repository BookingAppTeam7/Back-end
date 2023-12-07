package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.repositories.IAccommodationRepository;
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

            List<Review> reviews = new ArrayList<>();
            //List<Reservation> reservations = new ArrayList<>();

//            List<PriceCard> prices=newAccommodation.getPrices();
//            List<PriceCard> modifiedPrices=new ArrayList<>();
//
//            for(PriceCard b:prices) {
//                TimeSlot bTimeSlot=b.timeSlot;
//                Date bStart=bTimeSlot.startDate;
//                Date bEnd=bTimeSlot.endDate;
//                for(PriceCard a:prices){
//                    TimeSlot aTimeSlot=a.timeSlot;
//                    Date aStart=aTimeSlot.startDate;
//                    Date aEnd=aTimeSlot.endDate;
//                }
//
//            }

            //treba korigovati datume od cenovnika i dostupnosti i postaviti u newAccommodation.prices i newAccommodation.availability

            //ako su timeslotovi od cenovnika isti,treba napraviti jedan cenovnik koji ce imati poslednju unetu cenu za taj time slot
            //ako datum cenovnika B upaada u datum cenovnika A:
                // a_startDate - b_startDate cena iz cenovnika a
                //b_startDate - b_endDate cena iz cenovnika b
                //b_endDate - a_endDate cena iz cenovnika a

            //ako je datum cenovnika B zauzeo deo cenovnika A  //prvo B pa onda A  (b_startDate<a_startDate i b_endDate<a_endDate i b_endDate>a_startDate)
                //b_startDate - b_endDate cena iz cenovnika b
                //b_endDate - a_endDate cena iz cenovnika A
                /// ILI
                ////prvo A pa onda B  (a_startDate<b_startDate i b_startDate<a_endDate i b_endDate>a_endDate)
                // a_startDate-b_startDate - cena iz cenovnika a
                //b_startDate - b_endDate - cena iz cenovnika b

            //ako nema preklapanja to je novi cenovnik i  ne treba mu korigovati datume

        Accommodation createdAccommodation = new Accommodation(
                    newAccommodation.getName(),
                    newAccommodation.getDescription(),
                    new Location(newAccommodation.location.getAddress(), newAccommodation.location.getCity(), newAccommodation.location.getCountry(), newAccommodation.location.getX(), newAccommodation.location.getY()),
                    newAccommodation.getMinGuests(),
                    newAccommodation.getMaxGuests(),
                    newAccommodation.getType(),
                    newAccommodation.getAssets(),
                    newAccommodation.getPrices(),
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
    public Accommodation update(AccommodationPutDTO updatedAccommodation, Long id) throws Exception {
        Accommodation result=new Accommodation(id,updatedAccommodation.name, updatedAccommodation.description, updatedAccommodation.location,updatedAccommodation.minGuests,updatedAccommodation. maxGuests, updatedAccommodation.type, updatedAccommodation.assets, updatedAccommodation.prices,updatedAccommodation.ownerId,updatedAccommodation.cancellationDeadline, updatedAccommodation.reservationConfirmation, updatedAccommodation.reviews,updatedAccommodation.images,false);
        return accommodationRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }
}
