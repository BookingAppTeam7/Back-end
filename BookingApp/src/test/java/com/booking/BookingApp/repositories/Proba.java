package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class Proba {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IReviewRepository reviewRepository;

    @Test
    public void shouldSaveUser() {
        //User user = new User(null,"1","2","3","4","5","6");
        System.out.println("ABCF");
        Optional<Review> review  =reviewRepository.findById(1L);
        System.out.println(review);

        int grade=5;
        assertThat(review.get().grade).isEqualTo(grade);
    }
}