package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IReservationRepository;

import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public  Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
    public  List<String> assets=new ArrayList<>();
    public List<Review> reviews=new ArrayList<>();
    public List<String>images=new ArrayList<>();

    @Test
    public void confirmReservation_WhenReservationNotFound_ShouldThrowException() {
        // Arrange
        Long reservationId = 0L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(reservationId));

        assertAll(
                () -> assertEquals("Reservation not found with id: " + reservationId,exception.getMessage())
        );

        verify(reservationRepository).findById(reservationId);
        verifyNoMoreInteractions(reservationRepository);
        verifyNoInteractions(accommodationService);
        verifyNoInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void confirmReservation_WhenAccommodation_NotFound_ShouldThrowException() {

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",this.location,2,5, TypeEnum.APARTMENT,this.assets,null,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,this.images,false, AccommodationStatusEnum.APPROVED);

        Reservation reservation=new Reservation(1L,accommodation, null,null,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals("Accommodation not found with id: "+reservation.accommodation.id, exception.getMessage())
        );

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

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",this.location,2,5, TypeEnum.APARTMENT,this.assets,null,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,this.reviews,this.images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        Reservation reservation=new Reservation(1L,accommodation,user,null,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(null);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals("User not found with id: "+reservation.getUser().username, exception.getMessage())
        );

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

        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",this.location,2,5, TypeEnum.APARTMENT,assets,null,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,this.reviews,this.images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");

        Reservation reservation=new Reservation(1L,accommodation,user,null,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals("Reservation already approved!", exception.getMessage())
        );

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

        List<PriceCard>prices=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",this.location,2,5, TypeEnum.APARTMENT,this.assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,this.reviews,this.images,false, AccommodationStatusEnum.APPROVED);

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
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals("Accommodation not available in the selected time slot", exception.getMessage())
        );

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
      //  verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @Test  //no overlap with existing price cards
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
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals("Accommodation not available in the selected time slot", exception.getMessage())
        );

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
     //   verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    private static Stream<Arguments> provideTestDataForPartialOverlapExceptions() {
        return Stream.of(
                Arguments.of(  //overlap with start
                        LocalDate.now().plusDays(3),  // priceCard startDate
                        LocalDate.now().plusDays(5),  // priceCard endDate
                        LocalDate.now().plusDays(2),             // reservation StartDate
                        LocalDate.now().plusDays(5),  // reservationEndDate
                        "Accommodation not available in the selected time slot"
                ),
                Arguments.of( //overlap with end
                        LocalDate.now().plusDays(1),  // priceCard startDate
                        LocalDate.now().plusDays(3),  // priceCard endDate
                        LocalDate.now().plusDays(2),             // reservation StartDate
                        LocalDate.now().plusDays(5),  // reservationEndDate
                        "Accommodation not available in the selected time slot"
                )
        );
    }

    private static Stream<Arguments> provideValidTestData() {
        return Stream.of(
                Arguments.of(   //=> 2 new PriceCards
                        LocalDate.now().plusDays(-3),  // priceCard startDate
                        LocalDate.now().plusDays(6),  // priceCard endDate
                        LocalDate.now().plusDays(2),             // reservation StartDate
                        LocalDate.now().plusDays(5),  // reservationEndDate
                        false,  //without sending notification,
                        LocalDate.now().plusDays(-3),//new PriceCard1 startDate
                        LocalDate.now().plusDays(2),//new PriceCard1 endDate
                        LocalDate.now().plusDays(5),//new PriceCard2 startDate
                        LocalDate.now().plusDays(6)//new PriceCard2 endDate
                ),
                Arguments.of(   //=> 2 new PriceCards
                        LocalDate.now().plusDays(-1),  // priceCard startDate
                        LocalDate.now().plusDays(10),  // priceCard endDate
                        LocalDate.now().plusDays(2),             // reservation StartDate
                        LocalDate.now().plusDays(5),  // reservationEndDate
                        true,  //sending notification
                        LocalDate.now().plusDays(-1),//new PriceCard1 startDate
                        LocalDate.now().plusDays(2),//new PriceCard1 endDate
                        LocalDate.now().plusDays(5),//new PriceCard2 startDate
                        LocalDate.now().plusDays(10)//new PriceCard2 endDate
                ),
                Arguments.of( //overlap with end  => one new PriceCard
                        LocalDate.now().plusDays(2),  // priceCard startDate
                        LocalDate.now().plusDays(10),  // priceCard endDate
                        LocalDate.now().plusDays(2),             // reservation StartDate
                        LocalDate.now().plusDays(5),  // reservationEndDate
                        true,  //sending notification
                        LocalDate.now().plusDays(5),//new PriceCard1 startDate
                        LocalDate.now().plusDays(10),//new PriceCard1 endDate
                        null,
                        null
                ),
                Arguments.of( //overlap with start  => one new PriceCard
                        LocalDate.now().plusDays(2),  // priceCard startDate
                        LocalDate.now().plusDays(10),  // priceCard endDate
                        LocalDate.now().plusDays(5),             // reservation StartDate
                        LocalDate.now().plusDays(10),  // reservationEndDate
                        true,  //sending notification
                        LocalDate.now().plusDays(2),//new PriceCard1 startDate
                        LocalDate.now().plusDays(5),//new PriceCard1 endDate
                        null,
                        null
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForPartialOverlapExceptions")
    public void confirmReservation_WhenPartialOverlap_ShouldThrowException(
            LocalDate accommodationStartDate,
            LocalDate accommodationEndDate,
            LocalDate reservationStartDate,
            LocalDate reservationEndDate,
            String expectedErrorMessage
    ) {
        LocalDateTime startDateTime = LocalDateTime.of(reservationStartDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDateTime endDateTime = LocalDateTime.of(reservationEndDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot reservationTimeSlot = new TimeSlot(0L, startDateAsDate, endDateAsDate, false);

        startDateTime = LocalDateTime.of(accommodationStartDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDateTime = LocalDateTime.of(accommodationEndDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot accommodationTimeSlot = new TimeSlot(0L, startDateAsDate, endDateAsDate, false);

        PriceCard priceCard = new PriceCard(0L, accommodationTimeSlot, 15000, PriceTypeEnum.PERGUEST);

        List<PriceCard> prices = new ArrayList<>();
        prices.add(priceCard);

        Accommodation accommodation = new Accommodation(0L, "TestIme", "TestOpis", this.location, 2, 5, TypeEnum.APARTMENT, assets, prices, "OWNER@gmail.com", 30, ReservationConfirmationEnum.MANUAL, this.reviews, this.images, false, AccommodationStatusEnum.APPROVED);

        User user = new User("TestIme", "TestPrezime", "GUEST@gmail.com", "test", RoleEnum.GUEST, "TestAdresa", "123456789", StatusEnum.ACTIVE, false, false, false, false, true, " ", false, " ");

        Reservation reservation = new Reservation(1L, accommodation, user, reservationTimeSlot, ReservationStatusEnum.PENDING, 3L, 15000, PriceTypeEnum.PERGUEST);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals(expectedErrorMessage, exception.getMessage())
        );

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
//        verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }


    @Test  //overlap with existing price cards but also overlap with existing reservations
    public void confirmReservation_WhenAccommodationNotAvailable_ShouldThrowException() {  //nema preklapanja timeSlota rezervacije ni sa jednim cenovnikom

        Location location=new Location(0L,"TestAdresa","TestGrad","TestDrzava",1.0,1.0,false);
        List<String> assets=new ArrayList<>();

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot validTimeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);

        PriceCard priceCard=new PriceCard(0L,validTimeSlot,15000,PriceTypeEnum.PERGUEST);

        List<PriceCard>prices=new ArrayList<>();
        prices.add(priceCard);

        List<Review> reviews=new ArrayList<>();
        List<String>images=new ArrayList<>();
        Accommodation accommodation=new Accommodation(0L,"TestIme","TestOpis",location,2,5, TypeEnum.APARTMENT,assets,prices,"OWNER@gmail.com",30, ReservationConfirmationEnum.MANUAL,reviews,images,false, AccommodationStatusEnum.APPROVED);

        User user=new User("TestIme","TestPrezime", "GUEST@gmail.com","test",RoleEnum.GUEST,"TestAdresa","123456789", StatusEnum.ACTIVE,false,false,false,false,true," ",false," ");


        startDate = LocalDate.now().plusDays(6);
        startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDate = LocalDate.now().plusDays(8);
        endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        TimeSlot timeSlot=new TimeSlot(0L,startDateAsDate,endDateAsDate,false);
        Reservation reservation=new Reservation(1L,accommodation,user,timeSlot,ReservationStatusEnum.PENDING,3L,15000,PriceTypeEnum.PERGUEST);

        Reservation existingReservation=new Reservation(2L,accommodation,user,timeSlot,ReservationStatusEnum.APPROVED,5L,15000,PriceTypeEnum.PERGUEST);
        List<Reservation> existingReservations=new ArrayList<>();
        existingReservations.add(existingReservation);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(reservationRepository.findByAccommodationId(accommodation.id)).thenReturn(existingReservations);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> reservationService.confirmReservation(1L));

        assertAll(
                () -> assertEquals("There already exists confirmed reservation for this accommodation in selected time slot", exception.getMessage())
        );

        verify(reservationRepository).findById(1L);
        verify(accommodationService).findById(0L);
        verify(userService).findUserById("GUEST@gmail.com");
        verify(reservationRepository).findByAccommodationId(0L);
        //   verifyNoMoreInteractions(reservationRepository);
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(simpMessagingTemplate);
        verifyNoInteractions(notificationService);
    }

    @ParameterizedTest
    @MethodSource("provideValidTestData")
    public void confirmReservation_WhenHasAvailableTimeSlots(
            LocalDate accommodationStartDate,
            LocalDate accommodationEndDate,
            LocalDate reservationStartDate,
            LocalDate reservationEndDate,
            boolean sendNotification,
            LocalDate priceCard1StartDate,
            LocalDate priceCard1EndDate,
            LocalDate priceCard2StartDate,
            LocalDate priceCard2EndDate
    ) throws Exception {

        LocalDateTime startDateTime = LocalDateTime.of(reservationStartDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDateTime endDateTime = LocalDateTime.of(reservationEndDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot reservationTimeSlot = new TimeSlot(0L, startDateAsDate, endDateAsDate, false);

        startDateTime = LocalDateTime.of(accommodationStartDate, LocalTime.MIN);
        startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        endDateTime = LocalDateTime.of(accommodationEndDate, LocalTime.MAX);
        endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        TimeSlot accommodationTimeSlot = new TimeSlot(0L, startDateAsDate, endDateAsDate, false);

        PriceCard priceCard = new PriceCard(0L, accommodationTimeSlot, 15000, PriceTypeEnum.PERGUEST);

        List<PriceCard> prices = new ArrayList<>();
        prices.add(priceCard);

        Accommodation accommodation = new Accommodation(0L, "TestIme", "TestOpis", this.location, 2, 5, TypeEnum.APARTMENT, assets, prices, "OWNER@gmail.com", 30, ReservationConfirmationEnum.MANUAL, this.reviews, this.images, false, AccommodationStatusEnum.APPROVED);

        User user = new User("TestIme", "TestPrezime", "GUEST@gmail.com", "test", RoleEnum.GUEST, "TestAdresa", "123456789", StatusEnum.ACTIVE, false, false, false, false, true, " ", false, " ");

        Reservation reservation = new Reservation(1L, accommodation, user, reservationTimeSlot, ReservationStatusEnum.PENDING, 3L, 15000, PriceTypeEnum.PERGUEST);

        Reservation approvedReservation=new Reservation(1L,accommodation,user,reservationTimeSlot,ReservationStatusEnum.APPROVED,3L,15000,PriceTypeEnum.PERGUEST);

        List<Reservation> existingReservations=new ArrayList<>();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(accommodationService.findById(0L)).thenReturn(Optional.of(accommodation));
        when(userService.findUserById("GUEST@gmail.com")).thenReturn(user);
        when(reservationRepository.save(reservation)).thenReturn(approvedReservation);
        when(reservationRepository.findByAccommodationId(accommodation.id)).thenReturn(existingReservations);


        Reservation result=reservationService.confirmReservation(1L);

        assertEquals(reservation.getId(),result.getId());
        assertEquals(ReservationStatusEnum.APPROVED,result.getStatus());

        //checking new availability intervals

        if(priceCard2StartDate!=null && priceCard2EndDate!=null) {
            assertEquals(accommodation.prices.size(), 2);
            assertEquals(accommodation.prices.get(0).timeSlot.startDate, java.util.Date.from((LocalDateTime.of(priceCard1StartDate, LocalTime.MIN)).atZone(java.time.ZoneId.systemDefault()).toInstant()));
            assertEquals(accommodation.prices.get(0).timeSlot.endDate, java.util.Date.from((LocalDateTime.of(priceCard1EndDate, LocalTime.MIN)).atZone(java.time.ZoneId.systemDefault()).toInstant()));
            assertEquals(accommodation.prices.get(1).timeSlot.startDate, java.util.Date.from((LocalDateTime.of(priceCard2StartDate, LocalTime.MAX)).atZone(java.time.ZoneId.systemDefault()).toInstant()));
            assertEquals(accommodation.prices.get(1).timeSlot.endDate, java.util.Date.from((LocalDateTime.of(priceCard2EndDate, LocalTime.MAX)).atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        else{
            assertEquals(accommodation.prices.size(), 1);
            assertEquals(accommodation.prices.get(0).timeSlot.startDate.getDate(), java.util.Date.from((LocalDateTime.of(priceCard1StartDate, LocalTime.MAX)).atZone(java.time.ZoneId.systemDefault()).toInstant()).getDate());
            assertEquals(accommodation.prices.get(0).timeSlot.endDate.getDate(), java.util.Date.from((LocalDateTime.of(priceCard1EndDate, LocalTime.MAX)).atZone(java.time.ZoneId.systemDefault()).toInstant()).getDate());
        }

        verify(reservationRepository,times(1)).findById(1L);
        verify(accommodationService,times(2)).findById(0L);
        verify(reservationRepository,times(2)).findByAccommodationId(0L);
        verify(userService, times(2)).findUserById("GUEST@gmail.com"); //kasnije je pozvana i za slanje notifikacije

        verify(reservationRepository).save(reservation);
        verify(notificationService).create(
                argThat(argument ->
                        argument.getUserId().equals(reservation.user.username) &&
                                argument.getType().equals("RESERVATION_APPROVED") &&
                                argument.getContent().equals("Reservation in accommodation :"+reservation.accommodation.name.toUpperCase()+" APPROVED by owner "+reservation.accommodation.ownerId+"!")
                )
        );
        if(sendNotification) {
            verify(simpMessagingTemplate).convertAndSend(eq("/socket-publisher/" + reservation.user.username), any(NotificationPostDTO.class));
        }
        verifyNoMoreInteractions(accommodationService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(notificationService);
    }
}