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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql({"classpath:test-data-reservation.sql"})
    @DisplayName("Should Confirm Reservation When making PUT request to endpoint - /api/reservations/confirm/{id}")
    public void shouldConfirmReservation() {

        Long reservationId = 1L;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        // Asserting that the HTTP status code is OK (200)
        assertEquals(HttpStatus.OK,
                responseEntity.getStatusCode(), "Expected HTTP status 200 OK, but got " + responseEntity.getStatusCodeValue() + "\nResponse body: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      //  assertEquals("Reservation updated! Check database of accommodation and reservation", responseEntity.getBody());

        // You might want to add additional assertions based on the behavior of your service method.
        // For example, check if the reservation is confirmed in the database.
    }






}
