package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.*;
import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AccommodationPriceCardValidatorServiceTest {

    @MockBean
    public IAccommodationRepository accommodationRepository;
    @MockBean
    public IReservationRepository reservationRepository;
    @MockBean
    public IUserRepository userRepository;

    @Autowired
    private AccommodationValidatorService accommodationValidatorService;

    public static Accommodation validAcommodation;

    @BeforeEach
    public void createAccommodation(){
        Location loc = new Location("LocationAddress", "LocationCity", "LocationCountry", 1.234, 5.678, false);
        List<String> assets = new ArrayList<>();
        assets.add("klima");

        Accommodation accommodation = new Accommodation(1L, "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets, new ArrayList<PriceCard>(), "owner", 5, ReservationConfirmationEnum.MANUAL, new ArrayList<Review>(),
                new ArrayList<String>(), false, AccommodationStatusEnum.PENDING);
        validAcommodation=accommodation;
    }

    @Test
    @DisplayName("Should return false when accommmodation is null")
    public void shouldReturnFalseForNullAccommodation() throws Exception {
        AccommodationPostDTO accommodation = null;
        boolean res = accommodationValidatorService.validatePost(accommodation);
        assertFalse(res);
        verifyNoInteractions(accommodationRepository);
        verifyNoInteractions(userRepository);

    }

    @Test
    @DisplayName("Should return false when cancellationDeadline is zero")
    public void shouldReturnFalseZeroCancellationDeadline(){
        String existingUsername = "existing";
        // Ovde možete postaviti ostale podatke koji su validni
        User user = new User("John", "Doe", existingUsername, "password", RoleEnum.ADMIN,
                "Some Address", "123456789", StatusEnum.ACTIVE, true, true, true, true, true,
                "token", false, false, "favouriteAccommodations");

        AccommodationPostDTO accommodation = new AccommodationPostDTO("ime", "opis",null, 2, 5, TypeEnum.VIP_ROOM,
                new ArrayList<String>(), existingUsername, 0, new ArrayList<String>());

        Mockito.when(userRepository.findById(accommodation.ownerId)).thenReturn(Optional.of(user));

        boolean res = accommodationValidatorService.validatePost(accommodation);

        Mockito.verify(userRepository,times(1)).findById(
                argThat(argument ->
                        argument.equals(existingUsername))
        );
        assertFalse(res);
        verifyNoInteractions(accommodationRepository);
        verifyNoMoreInteractions(userRepository);
    }

    //validate PriceCardPOst funtion tests
    @Test
    @DisplayName("Should return false when accommodation is not present")
    public void shouldReturnFalseWhenAccommodationIsNotPresent(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.empty());

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when start date is not in future")
    public void shouldReturnFalseWhenStartDateIsNotInFuture(){

        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    @Test
    @DisplayName("Should return false when end date is not in future")
    public void shouldReturnFalseWhenEndDateIsNotInFuture(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().minusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    @Test
    @DisplayName("Should return false when end date is before start date")
    public void shouldReturnFalseWhenStartDateIsBeforeEndDate(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().minusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when price is negative ")
    public void shouldReturnFalseWhenNegativePrice(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,-2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    @Test
    @DisplayName("Should return false when price is zero ")
    public void shouldReturnFalseWhenZeroPrice(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,0,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when price type is invalid")
    public void shouldReturnFalseWhenInvalidPriceType(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,null,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when price for that timeSlot already exists ")
    public void shouldReturnFalseWhenPriceAlreadyExists(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);
        validAcommodation.prices.add(new PriceCard(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),priceCardPostDTO.price,priceCardPostDTO.type,false));

        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    private static Stream<Reservation> reservationExistsProvider() {
        // Provide different invalid reservation IDs and corresponding PriceCardPostDTO instances for testing
        LocalDate startDate1 = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime1 = LocalDateTime.of(startDate1, LocalTime.MIN);
        Date startDateAsDate1 = java.util.Date.from(startDateTime1.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate1 = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime1 = LocalDateTime.of(endDate1, LocalTime.MAX);
        Date endDateAsDate1 = java.util.Date.from(endDateTime1.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot1 = new TimeSlotPostDTO(startDateAsDate1, endDateAsDate1);


        LocalDate startDate2 = LocalDate.now().minusDays(5);
        LocalDateTime startDateTime2 = LocalDateTime.of(startDate2, LocalTime.MIN);
        Date startDateAsDate2 = java.util.Date.from(startDateTime2.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate2 = LocalDate.now().plusDays(6);
        LocalDateTime endDateTime2 = LocalDateTime.of(endDate2, LocalTime.MAX);
        Date endDateAsDate2 = java.util.Date.from(endDateTime2.atZone(java.time.ZoneId.systemDefault()).toInstant());

        Boolean deleted2 = false;
        TimeSlotPostDTO timeSlot2 = new TimeSlotPostDTO(startDateAsDate2, endDateAsDate2);

        LocalDate startDate3 = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime3 = LocalDateTime.of(startDate3, LocalTime.MIN);
        Date startDateAsDate3 = java.util.Date.from(startDateTime3.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate3 = LocalDate.now().plusDays(15);
        LocalDateTime endDateTime3 = LocalDateTime.of(endDate3, LocalTime.MAX);
        Date endDateAsDate3 = java.util.Date.from(endDateTime3.atZone(java.time.ZoneId.systemDefault()).toInstant());

        Boolean deleted3 = false;
        TimeSlotPostDTO timeSlot3 = new TimeSlotPostDTO(startDateAsDate3, endDateAsDate3);

        LocalDate startDate4 = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime4 = LocalDateTime.of(startDate4, LocalTime.MIN);
        Date startDateAsDate4 = java.util.Date.from(startDateTime4.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate4 = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime4 = LocalDateTime.of(endDate4, LocalTime.MAX);
        Date endDateAsDate4 = java.util.Date.from(endDateTime4.atZone(java.time.ZoneId.systemDefault()).toInstant());

        Boolean deleted4 = false;
        TimeSlotPostDTO timeSlot4 = new TimeSlotPostDTO(startDateAsDate4, endDateAsDate4);




        return Stream.of(

                new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot1.startDate,timeSlot1.endDate,false),ReservationStatusEnum.APPROVED,5L,2000.0,null),
                new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot2.startDate,timeSlot2.endDate,false),ReservationStatusEnum.APPROVED,5L,2000.0,null),
                new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot3.startDate,timeSlot3.endDate,false),ReservationStatusEnum.APPROVED,5L,2000.0,null),
                new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot4.startDate,timeSlot4.endDate,false),ReservationStatusEnum.APPROVED,5L,2000.0,null)
        );
    }


    @ParameterizedTest
    @MethodSource("reservationExistsProvider")
    @DisplayName("Should return false when confirmed reservation for that timeSlot already exists ")
    public void shouldReturnFalseWhenConfirmedReservationsExists(Reservation reservation){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);
        //pravimo bar jednu rezervaciju u tom intervalu
       List<Reservation> reservations=new ArrayList<Reservation>();
     //  reservations.add(new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),ReservationStatusEnum.APPROVED,5L,2000.0,null));
        reservations.add(reservation);
        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));
        Mockito.when(reservationRepository.findByAccommodationId(priceCardPostDTO.accommodationId)).thenReturn(reservations);
        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verify(reservationRepository).findByAccommodationId(priceCardPostDTO.accommodationId);

    }

    @Test
    @DisplayName("Should return true when all data are valid ")
    public void shouldReturnTrueWhenAllDataAreValid(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPostDTO priceCardPostDTO=new PriceCardPostDTO(timeSlot,2000,PriceTypeEnum.PERGUEST,1L);
        //pravimo bar jednu rezervaciju u tom intervalu
        List<Reservation> reservations=new ArrayList<Reservation>();
        reservations.add(new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),ReservationStatusEnum.PENDING,5L,2000.0,null));

        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));
        Mockito.when(reservationRepository.findByAccommodationId(priceCardPostDTO.accommodationId)).thenReturn(reservations);
        boolean res = accommodationValidatorService.validatePriceCardPost(priceCardPostDTO);

        assertTrue(res);
        verify(accommodationRepository).findById(1L);
        verify(reservationRepository).findByAccommodationId(priceCardPostDTO.accommodationId);

    }



    //VALIDATE PRICECARD PUTVALIDATOR

    @Test
    @DisplayName("Should return false when accommodation is not present")
    public void shouldReturnFalseWhenAccommodationIsNotPresentPUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.empty());

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when start date is not in future")
    public void shouldReturnFalseWhenStartDateIsNotInFuturePUT(){

        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPutDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPutDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPutDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    @Test
    @DisplayName("Should return false when end date is not in future")
    public void shouldReturnFalseWhenEndDateIsNotInFuturePUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().minusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    @Test
    @DisplayName("Should return false when end date is before start date")
    public void shouldReturnFalseWhenStartDateIsBeforeEndDatePUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().minusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when price is negative ")
    public void shouldReturnFalseWhenNegativePricePUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPutDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),-2000,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPutDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPutDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }


    @Test
    @DisplayName("Should return false when price is zero ")
    public void shouldReturnFalseWhenZeroPricePUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),0,PriceTypeEnum.PERGUEST,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when price type is invalid")
    public void shouldReturnFalseWhenInvalidPriceTypePUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,null,1L);


        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Should return false when price for that timeSlot already exists ")
    public void shouldReturnFalseWhenPriceAlreadyExistsPUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);
        validAcommodation.prices.add(new PriceCard(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),priceCardPostDTO.price,priceCardPostDTO.type,false));

        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));

        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verifyNoMoreInteractions(accommodationRepository);
        verifyNoInteractions(reservationRepository);
    }

    @ParameterizedTest
    @MethodSource("reservationExistsProvider")
    @DisplayName("Should return false when confirmed reservation for that timeSlot already exists ")
    public void shouldReturnFalseWhenConfirmedReservationsExistsPUT(Reservation reservation){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);
        //pravimo bar jednu rezervaciju u tom intervalu
        List<Reservation> reservations=new ArrayList<Reservation>();
        //reservations.add(new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),ReservationStatusEnum.APPROVED,5L,2000.0,null));
        reservations.add(reservation);
        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));
        Mockito.when(reservationRepository.findByAccommodationId(priceCardPostDTO.accommodationId)).thenReturn(reservations);
        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertFalse(res);
        verify(accommodationRepository).findById(1L);
        verify(reservationRepository).findByAccommodationId(priceCardPostDTO.accommodationId);

    }

    @Test
    @DisplayName("Should return true when all data are valid ")
    public void shouldReturnTrueWhenAllDataAreValidPUT(){

        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        Date startDateAsDate = java.util.Date.from(startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        Date endDateAsDate = java.util.Date.from(endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());


        Boolean deleted = false;
        TimeSlotPostDTO timeSlot = new TimeSlotPostDTO(startDateAsDate, endDateAsDate);

        PriceCardPutDTO priceCardPostDTO=new PriceCardPutDTO(new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),2000,PriceTypeEnum.PERGUEST,1L);
        //pravimo bar jednu rezervaciju u tom intervalu
        List<Reservation> reservations=new ArrayList<Reservation>();
        reservations.add(new Reservation(1L,validAcommodation,new User(),new TimeSlot(timeSlot.startDate,timeSlot.endDate,false),ReservationStatusEnum.PENDING,5L,2000.0,null));

        Mockito.when(accommodationRepository.findById(priceCardPostDTO.accommodationId)).thenReturn(Optional.of(validAcommodation));
        Mockito.when(reservationRepository.findByAccommodationId(priceCardPostDTO.accommodationId)).thenReturn(reservations);
        boolean res = accommodationValidatorService.validatePriceCardPut(priceCardPostDTO,2L);

        assertTrue(res);
        verify(accommodationRepository).findById(1L);
        verify(reservationRepository).findByAccommodationId(priceCardPostDTO.accommodationId);

    }






























//    @Test
//    @DisplayName("Should return false when owner id not exists")
//    public void shouldReturnFalseForNotExistingId() throws Exception {
//        AccommodationPostDTO accommodation = new AccommodationPostDTO("ime", "opis", new LocationPostDTO(), 2, 5, TypeEnum.VIP_ROOM,
//                new ArrayList<String>(), "notexisting", 2, new ArrayList<String>());
//        Mockito.when(userRepository.findById("notexisting")).thenReturn(Optional.empty());
//
//        boolean res = accommodationValidatorService.validatePost(accommodation);
//        assertFalse(res);
//        verifyNoInteractions(accommodationRepository);
//        verifyNoInteractions(userRepository);
//
//    }
//
//    @Test
//    @DisplayName("Should return false when name is empty")
//    public void shouldReturnFalseWhenNameIsEmpty() throws Exception {
//        String existingUsername = "existing";
//        User user = new User("John", "Doe", existingUsername, "password", RoleEnum.ADMIN,
//                "Some Address", "123456789", StatusEnum.ACTIVE, true, true, true, true, true,
//                "token", false, false, "favouriteAccommodations");
//        AccommodationPostDTO accommodation = new AccommodationPostDTO("", "opis", new LocationPostDTO(), 2, 5, TypeEnum.VIP_ROOM,
//                new ArrayList<String>(), "existing", 2, new ArrayList<String>());
//
//        Mockito.when(userRepository.findById("existing")).thenReturn(Optional.of(user));
//
//        boolean res = accommodationValidatorService.validatePost(accommodation);
//        Mockito.verify(userRepository,times(1)).findById(
//                argThat(argument ->
//                        argument.equals(existingUsername))
//        );
//        assertFalse(res);
//        verifyNoInteractions(accommodationRepository);
//        verifyNoMoreInteractions(userRepository);
//
//    }
//
//    @Test
//    @DisplayName("Should return false when description is empty")
//    public void shouldReturnFalseWhenDescriptionIsEmpty(){
//        String existingUsername = "existing";
//        // Ovde možete postaviti ostale podatke koji su validni
//        User user = new User("John", "Doe", existingUsername, "password", RoleEnum.ADMIN,
//                "Some Address", "123456789", StatusEnum.ACTIVE, true, true, true, true, true,
//                "token", false, false, "favouriteAccommodations");
//
//        AccommodationPostDTO accommodation = new AccommodationPostDTO("ime", "", new LocationPostDTO(), 2, 5, TypeEnum.VIP_ROOM,
//                new ArrayList<String>(), existingUsername, 2, new ArrayList<String>());
//
//        Mockito.when(userRepository.findById(accommodation.ownerId)).thenReturn(Optional.of(user));
//
//        boolean res = accommodationValidatorService.validatePost(accommodation);
//
//       Mockito.verify(userRepository,times(1)).findById(
//               argThat(argument ->
//                       argument.equals(existingUsername))
//     );
//        assertFalse(res);
//        verifyNoInteractions(accommodationRepository);
//        verifyNoMoreInteractions(userRepository);
//    }
//
//
//    @Test
//    @DisplayName("Should return false when Location  is null")
//    public void shouldReturnFalseWhenLocationIsNull(){
//        String existingUsername = "existing";
//        // Ovde možete postaviti ostale podatke koji su validni
//        User user = new User("John", "Doe", existingUsername, "password", RoleEnum.ADMIN,
//                "Some Address", "123456789", StatusEnum.ACTIVE, true, true, true, true, true,
//                "token", false, false, "favouriteAccommodations");
//
//        AccommodationPostDTO accommodation = new AccommodationPostDTO("ime", "opis",null, 2, 5, TypeEnum.VIP_ROOM,
//                new ArrayList<String>(), existingUsername, 2, new ArrayList<String>());
//
//        Mockito.when(userRepository.findById(accommodation.ownerId)).thenReturn(Optional.of(user));
//
//        boolean res = accommodationValidatorService.validatePost(accommodation);
//
//        Mockito.verify(userRepository,times(1)).findById(
//                argThat(argument ->
//                        argument.equals(existingUsername))
//        );
//        assertFalse(res);
//        verifyNoInteractions(accommodationRepository);
//        verifyNoMoreInteractions(userRepository);
//    }
//
//    @Test
//    @DisplayName("Should return false when cancellationDeadline is negative")
//    public void shouldReturnFalseWhenNegativeCancellationDeadline(){
//        String existingUsername = "existing";
//        // Ovde možete postaviti ostale podatke koji su validni
//        User user = new User("John", "Doe", existingUsername, "password", RoleEnum.ADMIN,
//                "Some Address", "123456789", StatusEnum.ACTIVE, true, true, true, true, true,
//                "token", false, false, "favouriteAccommodations");
//
//        AccommodationPostDTO accommodation = new AccommodationPostDTO("ime", "opis",null, 2, 5, TypeEnum.VIP_ROOM,
//                new ArrayList<String>(), existingUsername, -10, new ArrayList<String>());
//
//        Mockito.when(userRepository.findById(accommodation.ownerId)).thenReturn(Optional.of(user));
//
//        boolean res = accommodationValidatorService.validatePost(accommodation);
//
//        Mockito.verify(userRepository,times(1)).findById(
//                argThat(argument ->
//                        argument.equals(existingUsername))
//        );
//        assertFalse(res);
//        verifyNoInteractions(accommodationRepository);
//        verifyNoMoreInteractions(userRepository);
//    }

}
