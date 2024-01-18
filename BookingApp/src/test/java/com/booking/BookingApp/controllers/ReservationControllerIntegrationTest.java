package com.booking.BookingApp.controllers;

import com.booking.BookingApp.models.dtos.users.JwtAuthenticationRequest;
import com.booking.BookingApp.models.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
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

    private String token;
    private String guestToken;
    private String adminToken;


    @BeforeEach
    public  void login(){
        HttpHeaders headers = new HttpHeaders();
        JwtAuthenticationRequest user=new JwtAuthenticationRequest("novivlasnik@gmail.com","novivlasnik");
        HttpEntity<JwtAuthenticationRequest> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                requestEntity,
                User.class);
        this.token=responseEntity.getBody().getJwt();
        user=new JwtAuthenticationRequest("novigost@gmail.com","novigost");
        requestEntity = new HttpEntity<>(user, headers);
        responseEntity = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                requestEntity,
                User.class);
        this.guestToken=responseEntity.getBody().getJwt();

        user=new JwtAuthenticationRequest("ADMIN@gmail.com","admin");
        requestEntity = new HttpEntity<>(user, headers);
        responseEntity = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                requestEntity,
                User.class);
        this.adminToken=responseEntity.getBody().getJwt();

    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    @DisplayName("Confirm Reservation - /reservations/confirm/{id}")
    public void InvalidAuthorization(Long reservationId,String expectedMessageError,HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        //GUEST
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+guestToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                requestEntity,
                String.class,
                reservationId);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        //ADMIN

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+adminToken);
        requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity1= restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                requestEntity,
                String.class,
                reservationId);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());

    }



    @ParameterizedTest
    @MethodSource("provideTestData")
    @DisplayName("Confirm Reservation - /reservations/confirm/{id}")
    public void shouldConfirmReservation(Long reservationId,String expectedMessageError,HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations/confirm/{id}",
                HttpMethod.PUT,
                requestEntity,
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
