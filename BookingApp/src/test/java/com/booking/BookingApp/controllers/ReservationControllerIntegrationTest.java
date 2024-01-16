package com.booking.BookingApp.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_Valid() {

        Long reservationId = 1L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Reservation updated! Check database of accommodation and reservation", responseEntity.getBody());

    }

    @ParameterizedTest
    @MethodSource("provideTestDataForInvalidData")
    @DisplayName("Confirm Reservation with invalid data - invalid reservation/accommodation/user - /reservations/confirm/{id}")
    public void shouldConfirmReservation_InvalidData(Long reservationId,String expectedMessageError) {

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody().toString());
        String actualErrorMessage = responseEntity.getBody().toString();
        assertTrue(actualErrorMessage.contains(expectedMessageError));
    }

    @Test
    @DisplayName("Confirm Reservation Already Approved - /reservations/confirm/{id}")
    public void shouldConfirmReservation_AlreadyApproved() {
        Long reservationId = 6L;
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody().toString());
        String actualErrorMessage = responseEntity.getBody().toString();
        assertTrue(actualErrorMessage.contains("Reservation already approved!"));
    }


    @ParameterizedTest
    @MethodSource("provideTestDataForNotDefinedPrices")
    @DisplayName("Confirm Reservation with Undefined Prices - /reservations/confirm/{id}")
    public void shouldConfirmReservation_UndefinedPrices(Long reservationId, String expectedErrorMessage) {

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody().toString());
        String actualErrorMessage = responseEntity.getBody().toString();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }


    private static Stream<Arguments> provideTestDataForNotDefinedPrices() {
        return Stream.of(
                arguments(7L, "Accommodation not available in the selected time slot"),  //partial overlap start date
                arguments(8L, "Accommodation not available in the selected time slot"), //partial overlap end date
                arguments(5L, "Accommodation not available in the selected time slot"),  //undefined prices
                arguments(9L, "Accommodation not available in the selected time slot") //no overlaps with existing price cards time slots
        );
    }

    private static Stream<Arguments> provideTestDataForInvalidData() {
        return Stream.of(
                arguments(0L,"Reservation not found with id: " + 0L ),  //reservation not found
                arguments(3L, "Cannot read field \\\"id\\\" because \\\"reservation.accommodation\\\" is null"), //not found accommodation
                arguments(4L, "Cannot read field \\\"username\\\"")  //not found user
        );
    }

}