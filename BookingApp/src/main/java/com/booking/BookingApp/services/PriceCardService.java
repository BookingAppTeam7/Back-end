package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPutDTO;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IPriceCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PriceCardService implements IPriceCardService{
    @Autowired
    public IPriceCardRepository priceCardRepository;
    @Autowired
    public IAccommodationValidatorService validatorService;
    @Autowired
    public IAccommodationRepository accommodationRepository;

    @Override
    public List<PriceCard> findAll() {
        return priceCardRepository.findAll();
    }

    @Override
    public Optional<PriceCard> findById(Long id) {
        Optional<PriceCard> p=priceCardRepository.findById(id);
        if(p.isPresent()) {
            return p;
        }
        return null;
    }

    @Override
    public Optional<PriceCard> create(PriceCardPostDTO newPriceCard) throws Exception {
        validatorService.validatePriceCard(newPriceCard);
        PriceCard createdPriceCard=new PriceCard(newPriceCard.timeSlot,newPriceCard.price,newPriceCard.type);
        Optional<Accommodation> accommodation=accommodationRepository.findById(newPriceCard.accommodationId);
        accommodation.get().prices.add(createdPriceCard);
        return Optional.of(priceCardRepository.save(createdPriceCard));
    }


    @Override
    public PriceCard update(PriceCard updatedPriceCard) throws Exception {
//        PriceCard result=new PriceCard(id,updatedPriceCard.timeSlot,updatedPriceCard.price,updatedPriceCard.type);
        return priceCardRepository.saveAndFlush(updatedPriceCard);
    }

    @Override
    public void delete(Long id) {
        priceCardRepository.deleteById(id);

    }

//    @Override
//    public List<PriceCard> findByAccommodationId(Long id) {
//        return priceCardRepository.findByAccommodationId(id);
//    }

}
