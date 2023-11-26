package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.accommodations.FeedbackPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.FeedbackPutDTO;
import com.booking.BookingApp.models.reservations.Feedback;
import com.booking.BookingApp.repositories.IFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FeedbackService implements IFeedbackService{

    @Autowired
    public IFeedbackRepository feedbackRepository;
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        Optional<Feedback> f=feedbackRepository.findById(id);
        if(f.isPresent()) {
            return Optional.of(f.get());
        }
        return null;
    }

    @Override
    public Optional<Feedback> create(FeedbackPostDTO newFeedback) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        Feedback createdFeedback=new Feedback(newId,newFeedback.accommodationId,newFeedback.getReviews());
        return feedbackRepository.save(createdFeedback);
    }

    @Override
    public Feedback update(FeedbackPutDTO updatedFeedback, Long id) throws Exception {
        Feedback result=new Feedback(id,updatedFeedback.accommodationId,updatedFeedback.getReviews());
        return feedbackRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        feedbackRepository.deleteById(id);
    }
}
