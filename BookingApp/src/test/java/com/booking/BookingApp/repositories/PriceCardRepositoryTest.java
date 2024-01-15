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

//    @Test
//    public void shouldSaveAccommodation(){
//
//        Location loc=new Location();
//        List<String> assets=new ArrayList<>();
//        assets.add("klima");
//        List<PriceCard>prices=new ArrayList<>();
////
//        Date startDate = new Date(2023,2,10,10,57);
//        Date endDate = new Date(2023,2,15,10,57);
//        Boolean deleted = false;
//        TimeSlot timeSlot = new TimeSlot(startDate, endDate, deleted);
//
//
//        prices.add(new PriceCard(1L,timeSlot,2000, PriceTypeEnum.PERGUEST));
//        Accommodation accommodation=new Accommodation(1L,"ime","opis",loc,2,5, TypeEnum.APARTMENT,
//                assets,prices,"owner",5, ReservationConfirmationEnum.MANUAL,new ArrayList<Review>(),new ArrayList<String>(),false,AccommodationStatusEnum.PENDING);
//        Accommodation savedAccommodation=accommodationRepository.save(accommodation);
//        assertThat(savedAccommodation).usingRecursiveComparison().ignoringFields("id").isEqualTo(accommodation);

    //}

    @Test
    public void shouldSavePriceCard() {


        // Using java.sql.Timestamp instead of java.util.Date
        Timestamp startDate = Timestamp.from(Instant.parse("2023-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2023-02-15T10:57:00Z"));
        Boolean deleted = false;
        TimeSlot timeSlot = new TimeSlot(1L,startDate, endDate, deleted);

//            public PriceCard(Long id, TimeSlot timeSlot, double price, PriceTypeEnum type,Boolean deleted) {
//            this.id = id;
//            this.timeSlot=timeSlot;
//            this.price = price;
//            this.type = type;
//            this.deleted=deleted;
//        }
        PriceCard priceCard=new PriceCard(0L,timeSlot,2000,PriceTypeEnum.PERGUEST,false);

        PriceCard savedPriceCard = priceCardRepository.save(priceCard);

        assertThat(savedPriceCard).usingRecursiveComparison().ignoringFields("id").isEqualTo(priceCard);
    }

    @Test
    @Sql("classpath:test-data-accommodation.sql")
    public void shouldSavePriceCardThroughSqlFile() {
        Optional<PriceCard> test =priceCardRepository.findById(0L);
        assertThat(test).isNotEmpty();
    }
}
