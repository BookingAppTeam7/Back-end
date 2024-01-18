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
    @ParameterizedTest
    @MethodSource("provideTestData")
    @DisplayName("Confirm Reservation - /reservations/confirm/{id}")
    public void shouldConfirmReservation(Long reservationId,String expectedMessageError,HttpStatus status) {

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                reservationId);

        assertEquals(status, responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody().toString());
        String actualErrorMessage = responseEntity.getBody().toString();
        assertTrue(actualErrorMessage.contains(expectedMessageError));
    }


    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                arguments(1L,"Reservation updated! Check database of accommodation and reservation",HttpStatus.OK),
                arguments(0L,"Reservation not found with id: " + 0L,HttpStatus.BAD_REQUEST ),  //reservation not found
                arguments(3L, "Cannot read field \\\"id\\\" because \\\"reservation.accommodation\\\" is null",HttpStatus.BAD_REQUEST ), //not found accommodation
                arguments(4L, "Cannot read field \\\"username\\\"",HttpStatus.BAD_REQUEST ),  //not found user
                arguments(6L, "Reservation already approved!",HttpStatus.BAD_REQUEST ),
                arguments(7L, "Accommodation not available in the selected time slot",HttpStatus.BAD_REQUEST ),  //partial overlap start date
                arguments(8L, "Accommodation not available in the selected time slot",HttpStatus.BAD_REQUEST ), //partial overlap end date
                arguments(5L, "Accommodation not available in the selected time slot",HttpStatus.BAD_REQUEST ),  //undefined prices
                arguments(9L, "Accommodation not available in the selected time slot",HttpStatus.BAD_REQUEST )  //no overlaps with existing price cards time slots
        );
    }

}
