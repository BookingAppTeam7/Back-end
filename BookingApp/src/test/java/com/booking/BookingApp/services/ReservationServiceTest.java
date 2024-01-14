package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.Notification;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IReservationRepository;
import jakarta.persistence.Column;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationServiceTest {

    @MockBean
    private IReservationRepository reservationRepository;

    @MockBean
    private AccommodationService accommodationService;

    @MockBean
    private UserService userService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private INotificationService notificationService;

    @Captor
    private ArgumentCaptor<NotificationPostDTO> notificationPostArgumentCaptor;
    @Captor
    private ArgumentCaptor<Reservation> reservationArgumentCaptor;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void confirmReservation_WhenReservationNotFound_ShouldThrowException() {
        // Arrange
        Long reservationId = 0L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(reservationId));

        verify(reservationRepository).findById(reservationId);
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(accommodationService);
        verifyNoInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void confirmReservation_WhenAccommodation_NotFound_ShouldThrowException() {

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void confirmReservation_WhenUser_NotFound_ShouldThrowException() {

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(null);

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void confirmReservation_WhenAlreadyApproved_ShouldThrowException() {

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void confirmReservation_WhenPricesNotFound_ShouldThrowException() {

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }


    @Test
    public void confirmReservation_WhenInvalidPricesTimeSlots_ShouldThrowException() {  //nema preklapanja timeSlota rezervacije ni sa jednim cenovnikom

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        LocalDate startDate = LocalDate.now().plusDays(-5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(-1);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot invalidTimeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        PriceCard priceCard=new PriceCard(0L,invalidTimeSlot,15000,PriceTypeEnum.PERGUEST);

        List<PriceCard>prices=new ArrayList<>();
        prices.add(priceCard);

        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");


        startDate = LocalDate.now().plusDays(1);
        startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDate = LocalDate.now().plusDays(5);
        endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }


    @Test
    public void confirmReservation_WhenPartialOverlapStart_ShouldThrowException() {  //delimicno preklapanje - slucaj 1

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(3);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot invalidTimeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        PriceCard priceCard=new PriceCard(0L,invalidTimeSlot,15000,PriceTypeEnum.PERGUEST);

        List<PriceCard>prices=new ArrayList<>();
        prices.add(priceCard);

        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");


        startDate = LocalDate.now().plusDays(1);
        startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDate = LocalDate.now().plusDays(5);
        endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void confirmReservation_WhenPartialOverlapEnd_ShouldThrowException() {  //delimicno preklapanje - slucaj 2

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(7);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot invalidTimeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        PriceCard priceCard=new PriceCard(0L,invalidTimeSlot,15000,PriceTypeEnum.PERGUEST);

        List<PriceCard>prices=new ArrayList<>();
        prices.add(priceCard);

        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");


        startDate = LocalDate.now().plusDays(1);
        startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDate = LocalDate.now().plusDays(5);
        endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }


    @Test
    public void confirmReservation_WhenHasAvailableTimeSlotsSendingNotification_ShouldThrowException() throws Exception {

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        LocalDate startDate = LocalDate.now().plusDays(-1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot invalidTimeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        PriceCard priceCard=new PriceCard(0L,invalidTimeSlot,15000,PriceTypeEnum.PERGUEST);

        List<PriceCard>prices=new ArrayList<>();
        prices.add(priceCard);

        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");


        startDate = LocalDate.now().plusDays(1);
        startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDate = LocalDate.now().plusDays(5);
        endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);
        Reservation approvedReservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);


        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(reservationRepository.save(reservation)).thenReturn(approvedReservation);


        assertDoesNotThrow(() -> reservationService.confirmReservation(1L));
        assertEquals(ReservationStatusEnum.APPROVED,reservationRepository.findById(1L).get().getStatus());

        verify(reservationRepository,times(2)).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService, times(2)).findUserById("GUEST@gmail.com"); //kasnije je pozvana i za slanje notifikacije
        verify(accommodationService).editPriceCards(0L,startDateAsDate,endDateAsDate);
        verify(reservationRepository).save(reservation);
        verify(simpMessagingTemplate).convertAndSend(eq("/socket-publisher/" + reservation.user.username), any(NotificationPostDTO.class));
        verify(notificationService).create(notificationPostArgumentCaptor.capture());
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
    }


    @Test
    public void confirmReservation_WhenHasAvailableTimeSlotsNotSendingNotification_ShouldThrowException() throws Exception {

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        LocalDate startDate = LocalDate.now().plusDays(-1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot invalidTimeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        PriceCard priceCard=new PriceCard(0L,invalidTimeSlot,15000,PriceTypeEnum.PERGUEST);

        List<PriceCard>prices=new ArrayList<>();
        prices.add(priceCard);

        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,false," ",false," ");


        startDate = LocalDate.now().plusDays(1);
        startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDate = LocalDate.now().plusDays(5);
        endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);
        Reservation approvedReservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);


        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(reservationRepository.save(reservation)).thenReturn(approvedReservation);


        assertDoesNotThrow(() -> reservationService.confirmReservation(1L));
        assertEquals(ReservationStatusEnum.APPROVED,reservationRepository.findById(1L).get().getStatus());

        Assertions.assertThat(notificationPostArgumentCaptor.getValue().getContent()).isEqualTo("Reservation in accommodation :"+reservation.accommodation.name.toUpperCase()+" APPROVED by owner "+reservation.accommodation.ownerId+"!");

        verify(reservationRepository,times(2)).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService, times(2)).findUserById("GUEST@gmail.com"); //kasnije je pozvana i za slanje notifikacije
        verify(accommodationService).editPriceCards(0L,startDateAsDate,endDateAsDate);
        verify(reservationRepository).save(reservation);

        verifyNoInteractions(simpMessagingTemplate);
        verify(notificationService).create(notificationPostArgumentCaptor.capture());
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
    }

//treba proveriti cuvanje notifikacija!


}

