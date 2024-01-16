package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.TypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PriceCardRepositoryTest {
    @Autowired
    private IPriceCardRepository priceCardRepository;

    @Test
    public void shouldSavePriceCard() {

        Timestamp startDate = Timestamp.from(Instant.parse("2023-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2023-02-15T10:57:00Z"));
        Boolean deleted = false;
        TimeSlot timeSlot = new TimeSlot(1L,startDate, endDate, deleted);

        PriceCard priceCard=new PriceCard(0L,timeSlot,2000,PriceTypeEnum.PERGUEST,false);

        PriceCard savedPriceCard = priceCardRepository.save(priceCard);

        assertThat(savedPriceCard).usingRecursiveComparison().ignoringFields("id").isEqualTo(priceCard);
    }

    @Test
    public void shouldSavePriceCardThroughSqlFile() {
        Optional<PriceCard> test =priceCardRepository.findById(1L);
        assertThat(test).isNotEmpty();
    }
}
