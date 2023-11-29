package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.PriceCard;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PriceCardRepository implements IPriceCardRepository {
    private List<PriceCard> priceCards=new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<PriceCard> findAll() {
        return this.priceCards;
    }

    public Optional<PriceCard> getById(Long id){
        return priceCards.stream().filter(priceCard ->  priceCard.getId() == id).findFirst();
    }

    @Override
    public Optional<PriceCard> findById(Long id) {
        return priceCards.stream().filter(priceCard -> priceCard.getId() == id).findFirst();
    }

    @Override
    public Optional<PriceCard> save(PriceCard newPriceCard) {
        this.priceCards.add(newPriceCard);
        return getById(newPriceCard.getId());
    }

    @Override
    public PriceCard saveAndFlush(PriceCard updatedPriceCard) {
        Optional<PriceCard> optionalPriceCard=getById(updatedPriceCard.id);

        if (optionalPriceCard.isPresent()) {
            PriceCard priceCard = optionalPriceCard.get();
            priceCard.setTimeSlot(updatedPriceCard.getTimeSlot());
            priceCard.setPrice(updatedPriceCard.getPrice());
            priceCard.setType(updatedPriceCard.getType());

            return priceCard;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Optional<PriceCard> priceCard=getById(id);
        if(priceCard.isPresent()){
            PriceCard priceCardToDelete = priceCard.get();
            priceCards.remove(priceCardToDelete);
        }
    }
}
