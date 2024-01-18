package com.booking.BookingApp.controllers;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.users.JwtAuthenticationRequest;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.users.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationControllerCreateIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private String token;
    private RoleEnum role;
    @BeforeEach
    public void login(){
        HttpHeaders headers=new HttpHeaders();
        JwtAuthenticationRequest user=new JwtAuthenticationRequest("TESTGOST1@gmail.com","guest");
        HttpEntity<JwtAuthenticationRequest> requestEntity = new HttpEntity<>(user,headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                requestEntity,
                User.class);
        this.token=responseEntity.getBody().getJwt();
        role=responseEntity.getBody().role;
    }
    @JsonCreator
    public static StatusEnum fromValue(int value) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.ordinal() == value) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Invalid StatusEnum value: " + value);
    }

    @JsonCreator
    public static RoleEnum fromValue1(int value) {
        for (RoleEnum statusEnum :RoleEnum.values()) {
            if (statusEnum.ordinal() == value) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Invalid StatusEnum value: " + value);
    }
    @Test
    @DisplayName("Should create reservation for valid PostDTO- /reservations")
    public void shouldCreateReservation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);

        Timestamp startDate = Timestamp.from(Instant.parse("2024-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2024-02-13T10:57:00Z"));
        TimeSlot timeSlot = new TimeSlot(startDate, endDate,false);
        ReservationPostDTO reservationPostDTO=new ReservationPostDTO(2L,"TESTGOST1@gmail.com",timeSlot,3L,150.0, PriceTypeEnum.PERGUEST);
        HttpEntity<ReservationPostDTO> requestEntity=new HttpEntity<>(reservationPostDTO,headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @ParameterizedTest
    @MethodSource("invalidTestData")
    @DisplayName("Shouldn't create reservation because of invalid data")
    public void shouldNotCreateReservation(ReservationPostDTO reservationPostDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);

        HttpEntity<ReservationPostDTO> requestEntity=new HttpEntity<>(reservationPostDTO,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/reservations",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    private static Stream<ReservationPostDTO> invalidTestData(){
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        java.util.Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot timeSlot=new TimeSlot(startDateAsDate,endDateAsDate,false);

        LocalDate startDate2 = LocalDate.now().plusDays(10);
        LocalDateTime startDateTime2 = LocalDateTime.of(startDate2, LocalTime.MIN);
        java.util.Date startDateAsDate2 = java.util.Date.from(startDateTime2.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate2 = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime2 = LocalDateTime.of(endDate2, LocalTime.MAX);
        Date endDateAsDate2 = java.util.Date.from(endDateTime2.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot timeSlot2=new TimeSlot(startDateAsDate2,endDateAsDate2,false);//end date before start date

        LocalDate startDate3 = LocalDate.now().plusDays(100);
        LocalDateTime startDateTime3 = LocalDateTime.of(startDate3, LocalTime.MIN);
        java.util.Date startDateAsDate3 = java.util.Date.from(startDateTime3.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate3 = LocalDate.now().plusDays(200);
        LocalDateTime endDateTime3 = LocalDateTime.of(endDate3, LocalTime.MAX);
        Date endDateAsDate3 = java.util.Date.from(endDateTime3.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot timeSlot3=new TimeSlot(startDateAsDate3,endDateAsDate3,false);//accommodation not available in this timeslot


        Timestamp startDatets = Timestamp.from(Instant.parse("2024-01-30T10:57:00Z"));
        Timestamp endDatets = Timestamp.from(Instant.parse("2024-02-05T10:57:00Z"));
        TimeSlot timeSlot4 = new TimeSlot(startDatets, endDatets,false);

        return Stream.of(
                new ReservationPostDTO(4L,"TESTGOST1@gmail.com",timeSlot,3L,1000.0,PriceTypeEnum.PERGUEST),//accommodation doesn't exist
                new ReservationPostDTO(3L,"TESTGOST1@gmail.com",timeSlot,3L,1000.0,PriceTypeEnum.PERGUEST),//accommodation not approved
                new ReservationPostDTO(2L,"idontexist@gmail.com",timeSlot,3L,1000.0,PriceTypeEnum.PERGUEST),//guest doesn't exist
                new ReservationPostDTO(2L,"TESTGOST1@gmail.com",timeSlot2,3L,1000.0,PriceTypeEnum.PERGUEST),//timeslot not correct
                new ReservationPostDTO(2L,"TESTGOST1@gmail.com",timeSlot3,3L,1000.0,PriceTypeEnum.PERGUEST),//accommodation not available then
                new ReservationPostDTO(2L,"TESTGOST1@gmail.com",timeSlot4,15L,1000.0,PriceTypeEnum.PERGUEST),//invalid guest number
                new ReservationPostDTO(2L,"TESTGOST1@gmail.com",timeSlot4,3L,1000.0,PriceTypeEnum.PERGUEST)//there exists approved reservation on this accommodation that overlaps with this one
        );
    }
}
