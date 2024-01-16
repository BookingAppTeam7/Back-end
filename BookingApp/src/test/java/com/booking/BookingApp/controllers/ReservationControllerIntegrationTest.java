package com.booking.BookingApp.controllers;

import com.booking.BookingApp.services.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_InvalidReservation() {

        Long reservationId = 0L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        String expectedErrorMessage = "Reservation not found with id: " + reservationId;
        String actualErrorMessage = responseEntity.getBody().toString();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_InvalidAccommodation() {

        Long reservationId = 3L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//        System.out.println(responseEntity.getBody().toString());
//        String actualErrorMessage = responseEntity.getBody().toString();
//        assertTrue(actualErrorMessage.startsWith("Cannot read field \"id\" because \"reservation.accommodation\" is null"));

    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_InvalidUser() {

        Long reservationId = 4L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_EmptyPrices() {

        Long reservationId = 9L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_UndefinedPrice() {

        Long reservationId = 5L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_AlreadyApproved() {

        Long reservationId = 6L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_PartialOverlapStart() {

        Long reservationId = 7L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /reservations/confirm/{id}")
    public void shouldConfirmReservation_PartialOverlapEnd() {

        Long reservationId = 8L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}
