package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPostDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.TypeEnum;
import com.booking.BookingApp.repositories.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PriceCardServiceTest {

    @MockBean
    private IPriceCardRepository priceCardRepository;
    @MockBean
    public IAccommodationValidatorService validatorService;
    @MockBean
    public IAccommodationRepository accommodationRepository;
    @MockBean
    public ITimeSlotRepository timeSlotRepository;

    @Captor
    private ArgumentCaptor<TimeSlot> timeSlotArgumentCaptor;

    @Captor
    private ArgumentCaptor<PriceCard> priceCardArgumentCaptor;



    @Autowired
    private PriceCardService priceCardService;


    @Test
    @DisplayName("Should Not create invalid priceCard")
    public void shouldNotCreateInvalidPriceCard() throws Exception {

        Timestamp startDate = Timestamp.from(Instant.parse("2023-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2023-02-15T10:57:00Z"));
        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDate, endDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);
        // Using java.sql.Timestamp instead of java.util.Date
        Mockito.when(validatorService.validatePriceCardPost(priceCardPostDTO)).thenReturn(false);
        Optional<PriceCard> pc= priceCardService.create(priceCardPostDTO);
        assertFalse(pc.isPresent());
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(priceCardRepository);
        verifyNoInteractions(timeSlotRepository);

    }

    @Test
    @DisplayName("Should  create valid priceCard")
    public void shouldCreateValidPriceCard() throws Exception {


        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);


        Location loc = new Location("LocationAddress", "LocationCity", "LocationCountry", 1.234, 5.678, false);
        List<String> assets = new ArrayList<>();
        assets.add("klima");
        List<PriceCard> prices = new ArrayList<>();

        Mockito.when(validatorService.validatePriceCardPost(priceCardPostDTO)).thenReturn(true);
        Accommodation accommodation = new Accommodation(1L, "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets, prices, "owner", 5, ReservationConfirmationEnum.MANUAL, new ArrayList<Review>(),
                new ArrayList<String>(), false, AccommodationStatusEnum.PENDING);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(accommodation));
        Optional<PriceCard> pc= priceCardService.create(priceCardPostDTO);

        assertTrue(pc.isPresent());
        assertEquals(accommodation.prices.size(),1);
        verify(timeSlotRepository,times(1)).save(
                argThat(argument ->
                                argument.getStartDate().equals(timeSlot.startDate) &&
                                        argument.getEndDate().equals(timeSlot.endDate))
                );

        verify(priceCardRepository,times(1)).save(
                argThat(argument ->
                        argument.getTimeSlot().getStartDate().equals(timeSlot.startDate) &&
                                argument.getTimeSlot().getEndDate().equals(timeSlot.endDate)
                )

        );

    }





}
