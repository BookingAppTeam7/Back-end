package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPutDTO;
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
    private static AtomicLong counter=new AtomicLong();

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
        return Optional.empty();
    }

//    @Override
//    public Optional<PriceCard> create(PriceCardPostDTO newPriceCard) throws Exception {
//        Long newId= (Long) counter.incrementAndGet();
//
//        PriceCard createdPriceCard=new PriceCard(newId,newPriceCard.timeSlot,newPriceCard.price,newPriceCard.type);
//        return priceCardRepository.save(createdPriceCard);
//    }

    @Override
    public PriceCard update(PriceCardPutDTO updatedPriceCard, Long id) throws Exception {
        PriceCard result=new PriceCard(id,updatedPriceCard.timeSlot,updatedPriceCard.price,updatedPriceCard.type);
        return priceCardRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        priceCardRepository.deleteById(id);

    }

    @Override
    public List<PriceCard> findByAccommodationId(Long id) {
        return priceCardRepository.findByAccommodationId(id);
    }

}
