package com.booking.BookingApp.controllers;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.*;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.TypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PriceCardControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should Update priceCard When making PUT request to endpoint - /priceCards/{id}")
    public void shouldUpdatePriceCard_Valid() {

        Long priceCardId = 1L;
        // For example, assuming you have a PriceCard object to update
        Timestamp startDate = Timestamp.from(Instant.parse("2024-03-19T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2024-03-28T10:57:00Z"));
        Boolean deleted = false;
        TimeSlotPutDTO timeSlot = new TimeSlotPutDTO(startDate, endDate);

        PriceCardPutDTO priceCardPutDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000, PriceTypeEnum.PERGUEST,1L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PriceCardPutDTO> requestEntity = new HttpEntity<>(priceCardPutDTO, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/priceCards/{id}",
                HttpMethod.PUT,
                requestEntity,
                String.class,
               priceCardId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

private static Stream<Arguments> invalidReservationIdsAndPriceCardProvider() {
    // Provide different invalid reservation IDs and corresponding PriceCardPostDTO instances for testing
    LocalDate startDate = LocalDate.now().plusDays(5);
    LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
    java.util.Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

    LocalDate endDate = LocalDate.now().plusDays(10);
    LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
    Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

    LocalDate startDatePast = LocalDate.now().minusDays(5);
    LocalDateTime startDateTimePast = LocalDateTime.of(startDatePast, LocalTime.MIN);
    java.util.Date startDateAsDatePast = java.util.Date.from(startDateTimePast.atZone(java.time.ZoneId.systemDefault()).toInstant());

    LocalDate endDatePast = LocalDate.now().minusDays(10);
    LocalDateTime endDateTimePast = LocalDateTime.of(endDatePast, LocalTime.MAX);
    Date endDateAsDatePast = java.util.Date.from(endDateTimePast.atZone(java.time.ZoneId.systemDefault()).toInstant());


    LocalDate startDateExisting = LocalDate.of(2024, 1, 27);
    LocalDateTime startDateTimeExisting = LocalDateTime.of(startDateExisting, LocalTime.MIN);
    java.util.Date startDateAsDateExisting = java.util.Date.from(startDateTimeExisting.atZone(java.time.ZoneId.systemDefault()).toInstant());

    LocalDate endDateExisting = LocalDate.of(2024, 2, 15);
    LocalDateTime endDateTimeExisting = LocalDateTime.of(endDateExisting, LocalTime.MAX);
    Date endDateAsDateExisting = java.util.Date.from(endDateTimeExisting.atZone(java.time.ZoneId.systemDefault()).toInstant());



    LocalDate startDateExistingRes = LocalDate.of(2021, 2, 8);
    LocalDateTime startDateTimeExistingRes = LocalDateTime.of(startDateExistingRes, LocalTime.MIN);
    java.util.Date startDateAsDateExistingRes = java.util.Date.from(startDateTimeExistingRes.atZone(java.time.ZoneId.systemDefault()).toInstant());

    LocalDate endDateExistingRes = LocalDate.of(2024, 2, 18);
    LocalDateTime endDateTimeExistingRes = LocalDateTime.of(endDateExistingRes, LocalTime.MAX);
    Date endDateAsDateExistingRes = java.util.Date.from(endDateTimeExistingRes.atZone(java.time.ZoneId.systemDefault()).toInstant());

    return Stream.of(

            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDate,endDateAsDate,false), 2000, PriceTypeEnum.PERGUEST, 8L)),
            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDatePast,endDateAsDate,false), 2000, PriceTypeEnum.PERGUEST, 1L)),
            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDate,endDateAsDatePast,false), 2000, PriceTypeEnum.PERGUEST, 1L)),
            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDate,endDateAsDatePast,false), 2000, PriceTypeEnum.PERGUEST, 1L)),
            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDate,endDateAsDate,false), -2000, PriceTypeEnum.PERGUEST, 1L)),
            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDateExisting,endDateAsDateExisting,false), 2000, null, 1L)),
            arguments(1L,  new PriceCardPutDTO(new TimeSlot(startDateAsDateExistingRes,endDateAsDateExistingRes,false), 2000, PriceTypeEnum.PERGUEST, 1L))


    );
}



    @ParameterizedTest
    @MethodSource("invalidReservationIdsAndPriceCardProvider")
    @DisplayName("Should Not Update priceCard When making PUT request to endpoint - /priceCards/{id}")
    public void shouldNotUpdate_InvalidPriceCard(Long invalidPriceCardId,PriceCardPutDTO priceCardPutDTO) {


        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PriceCardPutDTO> requestEntity = new HttpEntity<>(priceCardPutDTO, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/priceCards/{id}",
                HttpMethod.PUT,
                requestEntity,
                String.class,
                invalidPriceCardId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());


    }

    @Test
    @DisplayName("Should Create priceCard When making Post request to endpoint - /priceCards/{id}")
    public void shouldCreatePriceCard_Valid() {


        // For example, assuming you have a PriceCard object to update
        Timestamp startDate = Timestamp.from(Instant.parse("2024-05-19T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2024-05-28T10:57:00Z"));
        Boolean deleted = false;
        TimeSlotPutDTO timeSlot = new TimeSlotPutDTO(startDate, endDate);

        PriceCardPostDTO priceCardPutDTO=new PriceCardPostDTO(new TimeSlotPostDTO(timeSlot.startDate,timeSlot.endDate),2000, PriceTypeEnum.PERGUEST,1L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PriceCardPostDTO> requestEntity = new HttpEntity<>(priceCardPutDTO, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/priceCards",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }





    private static Stream<PriceCardPostDTO>invalidPriceCardProvider() {
        // Provide different invalid reservation IDs and corresponding PriceCardPostDTO instances for testing
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        java.util.Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate startDatePast = LocalDate.now().minusDays(5);
        LocalDateTime startDateTimePast = LocalDateTime.of(startDatePast, LocalTime.MIN);
        java.util.Date startDateAsDatePast = java.util.Date.from(startDateTimePast.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDatePast = LocalDate.now().minusDays(10);
        LocalDateTime endDateTimePast = LocalDateTime.of(endDatePast, LocalTime.MAX);
        Date endDateAsDatePast = java.util.Date.from(endDateTimePast.atZone(java.time.ZoneId.systemDefault()).toInstant());


        LocalDate startDateExisting = LocalDate.of(2024, 1, 27);
        LocalDateTime startDateTimeExisting = LocalDateTime.of(startDateExisting, LocalTime.MIN);
        java.util.Date startDateAsDateExisting = java.util.Date.from(startDateTimeExisting.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateExisting = LocalDate.of(2024, 2, 15);
        LocalDateTime endDateTimeExisting = LocalDateTime.of(endDateExisting, LocalTime.MAX);
        Date endDateAsDateExisting = java.util.Date.from(endDateTimeExisting.atZone(java.time.ZoneId.systemDefault()).toInstant());



        LocalDate startDateExistingRes = LocalDate.of(2024, 2, 8);
        LocalDateTime startDateTimeExistingRes = LocalDateTime.of(startDateExistingRes, LocalTime.MIN);
        java.util.Date startDateAsDateExistingRes = java.util.Date.from(startDateTimeExistingRes.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateExistingRes = LocalDate.of(2024, 2, 18);
        LocalDateTime endDateTimeExistingRes = LocalDateTime.of(endDateExistingRes, LocalTime.MAX);
        Date endDateAsDateExistingRes = java.util.Date.from(endDateTimeExistingRes.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate startDateExistingRes1 = LocalDate.of(2024, 2, 10);
        LocalDateTime startDateTimeExistingRes1 = LocalDateTime.of(startDateExistingRes1, LocalTime.MIN);
        java.util.Date startDateAsDateExistingRes1 = java.util.Date.from(startDateTimeExistingRes1.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateExistingRes1 = LocalDate.of(2024, 2, 15);
        LocalDateTime endDateTimeExistingRes1 = LocalDateTime.of(endDateExistingRes1, LocalTime.MAX);
        Date endDateAsDateExistingRes1 = java.util.Date.from(endDateTimeExistingRes1.atZone(java.time.ZoneId.systemDefault()).toInstant());


        LocalDate startDateExistingRes2 = LocalDate.of(2024, 2, 10);
        LocalDateTime startDateTimeExistingRes2 = LocalDateTime.of(startDateExistingRes2, LocalTime.MIN);
        java.util.Date startDateAsDateExistingRes2 = java.util.Date.from(startDateTimeExistingRes2.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateExistingRes2 = LocalDate.of(2024, 2, 18);
        LocalDateTime endDateTimeExistingRes2 = LocalDateTime.of(endDateExistingRes2, LocalTime.MAX);
        Date endDateAsDateExistingRes2 = java.util.Date.from(endDateTimeExistingRes2.atZone(java.time.ZoneId.systemDefault()).toInstant());

        return Stream.of(

                new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDate,endDateAsDate), 2000, PriceTypeEnum.PERGUEST, 30L),
              new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDatePast,endDateAsDate), 2000, PriceTypeEnum.PERGUEST, 2L),
                 new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDate,endDateAsDatePast), 2000, PriceTypeEnum.PERGUEST, 2L),
                 new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDate,endDateAsDatePast), 2000, PriceTypeEnum.PERGUEST, 2L),
               new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDate,endDateAsDate), -2000, PriceTypeEnum.PERGUEST, 2L),
                new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDate,endDateAsDate), 2000, null, 2L),
                new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDateExistingRes,endDateAsDateExistingRes), 2000, PriceTypeEnum.PERGUEST, 1L),
                new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDateExistingRes1,endDateAsDateExistingRes1), 2000, PriceTypeEnum.PERGUEST, 1L),
                new PriceCardPostDTO(new TimeSlotPostDTO(startDateAsDateExistingRes2,endDateAsDateExistingRes2), 2000, PriceTypeEnum.PERGUEST, 1L)

        );
    }


    @ParameterizedTest
    @MethodSource("invalidPriceCardProvider")
    @DisplayName("Should Not Create priceCard When making Post request to endpoint - /priceCards")
    public void shouldNotCreatePriceCard_InValid(PriceCardPostDTO priceCardPostDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PriceCardPostDTO> requestEntity = new HttpEntity<>(priceCardPostDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/priceCards",
                HttpMethod.POST,
               requestEntity,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should Create cancelation Deadline When making Post request to endpoint /accommodations")
    public void shouldCreateCancellationDeadLine_Valid() {

        LocationPostDTO loc = new LocationPostDTO("LocationAddress", "LocationCity", "LocationCountry", 1.234, 5.678);
        List<String> assets = new ArrayList<>();
        assets.add("klima");

        AccommodationPostDTO accommodation = new AccommodationPostDTO( "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets,  "TESTGOST1@gmail.com", 5,new ArrayList<String>());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AccommodationPostDTO> requestEntity = new HttpEntity<>(accommodation, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/accommodations",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }



    private static Stream<AccommodationPostDTO>invalidCancelationDeadlineProvider() {
        // Provide different invalid reservation IDs and corresponding PriceCardPostDTO instances for testing
        LocationPostDTO loc = new LocationPostDTO("LocationAddress", "LocationCity", "LocationCountry", 1.234, 5.678);
        List<String> assets = new ArrayList<>();
        assets.add("klima");

        AccommodationPostDTO accommodation = new AccommodationPostDTO( "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets,  "TESTGOST1@gmail.com", 5,new ArrayList<String>());


        return Stream.of(
                new AccommodationPostDTO( "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                        assets,  "TESTGOST1@gmail.com", 0,new ArrayList<String>()),
        new AccommodationPostDTO( "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets,  "TESTGOST1@gmail.com", -10,new ArrayList<String>())
        );
    }




    @ParameterizedTest
    @MethodSource("invalidCancelationDeadlineProvider")
    @DisplayName("Should not create cancelation Deadline  When its zero or negative making Post request to endpoint /accommodations")
    public void shouldNotCreateCancellationDeadLine_IValid(AccommodationPostDTO accommodation) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AccommodationPostDTO> requestEntity = new HttpEntity<>(accommodation, headers);


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/accommodations",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

}
