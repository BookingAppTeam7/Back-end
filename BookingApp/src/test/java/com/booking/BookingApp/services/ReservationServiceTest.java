package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IReservationRepository;

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
import java.util.concurrent.atomic.AtomicLong;

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
    private static AtomicLong counter=new AtomicLong();

    @Test
    public void createReservation_WhenAccommodation_NotFound_ShouldThrowException(){
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

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verifyNoInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenAccommodation_NotApproved_ShouldThrowException(){
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.PENDING);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verifyNoInteractions(userService);
        verifyNoInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenUser_NotFound_ShouldThrowException(){
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

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(null);

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenTimeSlot_Invalid_ShouldThrowException(){
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verifyNoInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenAccommodation_NotAvailable_ShouldThrowException(){
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();

        LocalDate startDateAcc = LocalDate.now().plusDays(7);
        LocalDateTime startDateTimeAcc = LocalDateTime.of(startDateAcc, LocalTime.MIN);
        Date startDateAsDateAcc = java.util.Date.from(startDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateAcc = LocalDate.now().plusDays(10);
        LocalDateTime endDateTimeAcc = LocalDateTime.of(endDateAcc, LocalTime.MAX);
        Date endDateAsDateAcc = java.util.Date.from(endDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());
        TimeSlot timeSlotAcc=new TimeSlot(0L,startDateAsDateAcc,endDateAsDateAcc,false);
        prices.add(new PriceCard(0L,timeSlotAcc,1000,PriceTypeEnum.PERGUEST,false));

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(1L,startDateAsDate,endDateAsDate,false);

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verify(accommodationService).hasAvailableTimeSlot(accommodation,newReservation.timeSlot.startDate, newReservation.timeSlot.endDate);
        verifyNoInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenGuestNumber_Invalid_ShouldThrowException(){
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();

        LocalDate startDateAcc = LocalDate.now().plusDays(1);
        LocalDateTime startDateTimeAcc = LocalDateTime.of(startDateAcc, LocalTime.MIN);
        Date startDateAsDateAcc = java.util.Date.from(startDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateAcc = LocalDate.now().plusDays(10);
        LocalDateTime endDateTimeAcc = LocalDateTime.of(endDateAcc, LocalTime.MAX);
        Date endDateAsDateAcc = java.util.Date.from(endDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());
        TimeSlot timeSlotAcc=new TimeSlot(0L,startDateAsDateAcc,endDateAsDateAcc,false);
        prices.add(new PriceCard(0L,timeSlotAcc,1000,PriceTypeEnum.PERGUEST,false));

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(1L,startDateAsDate,endDateAsDate,false);

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,7L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verify(accommodationService).hasAvailableTimeSlot(accommodation,newReservation.timeSlot.startDate, newReservation.timeSlot.endDate);
        verifyNoInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenAlreadyExists_ApprovedReservation_ShouldThrowException(){
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        List<Reservation> allReservations=new ArrayList<>();

        LocalDate startDateAcc = LocalDate.now().plusDays(1);
        LocalDateTime startDateTimeAcc = LocalDateTime.of(startDateAcc, LocalTime.MIN);
        Date startDateAsDateAcc = java.util.Date.from(startDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateAcc = LocalDate.now().plusDays(10);
        LocalDateTime endDateTimeAcc = LocalDateTime.of(endDateAcc, LocalTime.MAX);
        Date endDateAsDateAcc = java.util.Date.from(endDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());
        TimeSlot timeSlotAcc=new TimeSlot(0L,startDateAsDateAcc,endDateAsDateAcc,false);
        prices.add(new PriceCard(0L,timeSlotAcc,1000,PriceTypeEnum.PERGUEST,false));

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(7);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot timeSlot=new TimeSlot(1L,startDateAsDate,endDateAsDate,false);


        LocalDate startDateRes = LocalDate.now().plusDays(5);
        LocalDateTime startDateTimeRes = LocalDateTime.of(startDateRes, LocalTime.MIN);
        Date startDateAsDateRes = java.util.Date.from(startDateTimeRes.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateRes = LocalDate.now().plusDays(9);
        LocalDateTime endDateTimeRes = LocalDateTime.of(endDateRes, LocalTime.MAX);
        Date endDateAsDateRes = java.util.Date.from(endDateTimeRes.atZone(java.time.ZoneId.systemDefault()).toInstant());
        TimeSlot timeSlotRes=new TimeSlot(3L,startDateAsDateRes,endDateAsDateRes,false);

        allReservations.add(new Reservation(3L,accommodation,user,timeSlotRes,ReservationStatusEnum.APPROVED,3L,1000,PriceTypeEnum.PERUNIT));

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(accommodationService.hasAvailableTimeSlot(any(), any(), any())).thenReturn(true);
        when(reservationRepository.findAll()).thenReturn(allReservations);

        assertThrows(Exception.class, () -> reservationService.create(newReservation));

        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verify(accommodationService).hasAvailableTimeSlot(accommodation,newReservation.timeSlot.startDate, newReservation.timeSlot.endDate);
        verify(reservationRepository).findAll();

        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }
    @Test
    public void createReservation_WhenEverythingOk_SendingNotification() throws Exception {
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        List<Reservation> allReservations=new ArrayList<>();

        LocalDate startDateAcc = LocalDate.now().plusDays(1);
        LocalDateTime startDateTimeAcc = LocalDateTime.of(startDateAcc, LocalTime.MIN);
        Date startDateAsDateAcc = java.util.Date.from(startDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateAcc = LocalDate.now().plusDays(10);
        LocalDateTime endDateTimeAcc = LocalDateTime.of(endDateAcc, LocalTime.MAX);
        Date endDateAsDateAcc = java.util.Date.from(endDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());
        TimeSlot timeSlotAcc=new TimeSlot(0L,startDateAsDateAcc,endDateAsDateAcc,false);
        prices.add(new PriceCard(0L,timeSlotAcc,1000,PriceTypeEnum.PERGUEST,false));

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,true,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(1L,startDateAsDate,endDateAsDate,false);

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);
        Long newId= (Long) counter.incrementAndGet();
        Reservation createdReservation=new Reservation(newId,accommodation,user,newReservation.timeSlot, ReservationStatusEnum.PENDING, newReservation.numberOfGuests,
                newReservation.price,newReservation.priceType);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(reservationRepository.findAll()).thenReturn(allReservations);
        when(accommodationService.hasAvailableTimeSlot(any(), any(), any())).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(createdReservation);

        Optional<Reservation> result=reservationService.create(newReservation);
        if(result.isPresent()){
            assertEquals(ReservationStatusEnum.PENDING,result.get().getStatus());
            assertEquals(newReservation.getAccommodationId(),result.get().accommodation.id);
            assertEquals(newReservation.getUserId(),result.get().getUser().username);
        }


        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verify(accommodationService).hasAvailableTimeSlot(accommodation,newReservation.timeSlot.startDate, newReservation.timeSlot.endDate);
        verify(reservationRepository).findAll();
        verify(reservationRepository).save(any());
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verify(simpMessagingTemplate).convertAndSend(eq("/socket-publisher/" + createdReservation.accommodation.ownerId), any(NotificationPostDTO.class));
        verify(notificationService).create(any());
        verifyNoMoreInteractions(simpMessagingTemplate);
    }
    @Test
    public void createReservation_WhenEverythingOk_NotSendingNotification() throws Exception {
        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();
        List<PriceCard>prices=new ArrayList<>();
        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        List<Reservation> allReservations=new ArrayList<>();

        LocalDate startDateAcc = LocalDate.now().plusDays(1);
        LocalDateTime startDateTimeAcc = LocalDateTime.of(startDateAcc, LocalTime.MIN);
        Date startDateAsDateAcc = java.util.Date.from(startDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDateAcc = LocalDate.now().plusDays(10);
        LocalDateTime endDateTimeAcc = LocalDateTime.of(endDateAcc, LocalTime.MAX);
        Date endDateAsDateAcc = java.util.Date.from(endDateTimeAcc.atZone(java.time.ZoneId.systemDefault()).toInstant());
        TimeSlot timeSlotAcc=new TimeSlot(0L,startDateAsDateAcc,endDateAsDateAcc,false);
        prices.add(new PriceCard(0L,timeSlotAcc,1000,PriceTypeEnum.PERGUEST,false));

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(5);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(1L,startDateAsDate,endDateAsDate,false);

        ReservationPostDTO newReservation=new ReservationPostDTO(0L,"GUEST@gmail.com",timeSlot,3L,15000,PriceTypeEnum.PERGUEST);
        Long newId= (Long) counter.incrementAndGet();
        Reservation createdReservation=new Reservation(newId,accommodation,user,newReservation.timeSlot, ReservationStatusEnum.PENDING, newReservation.numberOfGuests,
                newReservation.price,newReservation.priceType);

        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(reservationRepository.findAll()).thenReturn(allReservations);
        when(accommodationService.hasAvailableTimeSlot(any(), any(), any())).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(createdReservation);

        Optional<Reservation> result=reservationService.create(newReservation);
        if(result.isPresent()){
            assertEquals(ReservationStatusEnum.PENDING,result.get().getStatus());
            assertEquals(newReservation.getAccommodationId(),result.get().accommodation.id);
            assertEquals(newReservation.getUserId(),result.get().getUser().username);
        }


        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verify(accommodationService).hasAvailableTimeSlot(accommodation,newReservation.timeSlot.startDate, newReservation.timeSlot.endDate);
        verify(reservationRepository).findAll();
        verify(reservationRepository).save(any());
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verify(notificationService).create(any());
    }

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
    public void confirmReservation_WhenHasAvailableTimeSlotsSendingNotification() throws Exception {

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


        Reservation result=reservationService.confirmReservation(1L);

        assertEquals(reservation.getId(),result.getId());
        assertEquals(ReservationStatusEnum.APPROVED,result.getStatus());

        verify(reservationRepository,times(1)).findById(1L);
        verify(accommodationService,times(1)).findById(0L);
        verify(userService, times(2)).findUserById("GUEST@gmail.com"); //kasnije je pozvana i za slanje notifikacije
        verify(accommodationService).editPriceCards(0L,startDateAsDate,endDateAsDate);
        verify(reservationRepository).save(reservation);
        verify(notificationService).create(
                argThat(argument ->
                        argument.getUserId().equals(reservation.user.username) &&
                                argument.getType().equals("RESERVATION_APPROVED") &&
                                argument.getContent().equals("Reservation in accommodation :"+reservation.accommodation.name.toUpperCase()+" APPROVED by owner "+reservation.accommodation.ownerId+"!")
                )
        );
        verify(simpMessagingTemplate).convertAndSend(eq("/socket-publisher/" + reservation.user.username), any(NotificationPostDTO.class));
        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
    }


    @Test
    public void confirmReservation_WhenHasAvailableTimeSlotsNotSendingNotification() throws Exception {

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

        Reservation result=reservationService.confirmReservation(1L);

        assertEquals(reservation.getId(),result.getId());
        assertEquals(ReservationStatusEnum.APPROVED,result.getStatus());

        verify(reservationRepository,times(1)).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService, times(2)).findUserById("GUEST@gmail.com"); //kasnije je pozvana i za slanje notifikacije
        verify(accommodationService).editPriceCards(0L,startDateAsDate,endDateAsDate);
        verify(reservationRepository).save(reservation);
        verifyNoInteractions(simpMessagingTemplate);
        verify(notificationService).create(
                argThat(argument ->
                        argument.getUserId().equals(reservation.user.username) &&
                                argument.getType().equals("RESERVATION_APPROVED") &&
                                argument.getContent().equals("Reservation in accommodation :"+reservation.accommodation.name.toUpperCase()+" APPROVED by owner "+reservation.accommodation.ownerId+"!")
                )
        );

        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
    }

}

