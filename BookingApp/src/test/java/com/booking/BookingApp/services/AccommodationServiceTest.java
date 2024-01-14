package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.LocationPostDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.TypeEnum;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IAccommodationRequestRepository;
import com.booking.BookingApp.repositories.ILocationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AccommodationServiceTest {

    @MockBean
    private IAccommodationRepository accommodationRepository;
    @MockBean
    public IAccommodationValidatorService validatorService;
    @MockBean
    public IUserRepository userRepository;
    @MockBean
    public ILocationRepository locationRepository;



    @Autowired
    private AccommodationService accommodationService;

    @Test
    @DisplayName("Should Retrieve Accommodation by Id")
    public void shouldAccommodationPostById() {
        Location loc = new Location("LocationAddress", "LocationCity", "LocationCountry", 1.234, 5.678, false);
        List<String> assets = new ArrayList<>();
        assets.add("klima");
        List<PriceCard> prices = new ArrayList<>();

        // Using java.sql.Timestamp instead of java.util.Date
        Timestamp startDate = Timestamp.from(Instant.parse("2023-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2023-02-15T10:57:00Z"));
        Boolean deleted = false;
        TimeSlot timeSlot = new TimeSlot(1L,startDate, endDate, deleted);

        prices.add(new PriceCard(1L, timeSlot, 2000, PriceTypeEnum.PERGUEST));

        Accommodation accommodation = new Accommodation(1L, "ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets, prices, "owner", 5, ReservationConfirmationEnum.MANUAL, new ArrayList<Review>(),
                new ArrayList<String>(), false, AccommodationStatusEnum.PENDING);

        Mockito.when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));

        Optional<Accommodation> accommodationResponse=accommodationService.findById(1L);
        Assertions.assertThat(accommodationResponse.get().getId()).isEqualTo(accommodation.getId());
        Assertions.assertThat(accommodationResponse.get().getOwnerId()).isEqualTo(accommodation.getOwnerId());
        Assertions.assertThat(accommodationResponse.get().getDeleted()).isEqualTo(accommodation.getDeleted());
        Assertions.assertThat(accommodationResponse.get().getPrices()).isEqualTo(accommodation.getPrices());
        Assertions.assertThat(accommodationResponse.get().getType()).isEqualTo(accommodation.getType());
        Assertions.assertThat(accommodationResponse.get().getAssets()).isEqualTo(accommodation.getAssets());
        Assertions.assertThat(accommodationResponse.get().getCancellationDeadline()).isEqualTo(accommodation.getCancellationDeadline());
        Assertions.assertThat(accommodationResponse.get().getImages()).isEqualTo(accommodation.getImages());
        Assertions.assertThat(accommodationResponse.get().getReviews()).isEqualTo(accommodation.getReviews());
        Assertions.assertThat(accommodationResponse.get().getDescription()).isEqualTo(accommodation.getDescription());
        Assertions.assertThat(accommodationResponse.get().getLocation()).isEqualTo(accommodation.getLocation());
        Assertions.assertThat(accommodationResponse.get().getMaxGuests()).isEqualTo(accommodation.getMaxGuests());
        Assertions.assertThat(accommodationResponse.get().getMinGuests()).isEqualTo(accommodation.getMinGuests());
        Assertions.assertThat(accommodationResponse.get().getName()).isEqualTo(accommodation.getName());
        Assertions.assertThat(accommodationResponse.get().getReservationConfirmation()).isEqualTo(accommodation.getReservationConfirmation());
        Assertions.assertThat(accommodationResponse.get().getStatus()).isEqualTo(accommodation.getStatus());

    }

//    @Test
//    @DisplayName("Should Throw Exception if Post ID doesn't exist")
//    public void shouldNotFoundPostThatDoesntExist() {
//
//        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.empty());
//
//        assertThrows(PostNotFoundException.class, () -> postService.getPost(123L));
//
//        verify(postRepository, times(1)).findById(123L);
//        verifyNoInteractions(postMapper);
//    }
//
    @Test
    @DisplayName("Should Not save invalid accommodation creating request")
    public void shouldNotSaveInvalidAccommodationsCreatingRequest() throws Exception {

        LocationPostDTO loc = new LocationPostDTO("LocationAddress", "LocationCity","str", 1.234, 5.678);
        List<String> assets = new ArrayList<>();
        assets.add("klima");
        List<PriceCard> prices = new ArrayList<>();

        // Using java.sql.Timestamp instead of java.util.Date
        Timestamp startDate = Timestamp.from(Instant.parse("2023-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2023-02-15T10:57:00Z"));
        Boolean deleted = false;
        TimeSlot timeSlot = new TimeSlot(1L, startDate, endDate, deleted);

        prices.add(new PriceCard(1L, timeSlot, 2000, PriceTypeEnum.PERGUEST));

        AccommodationPostDTO accommodation = new AccommodationPostDTO("ime", "opis", loc, 2, 5, TypeEnum.APARTMENT,
                assets, "owner", 5,
                new ArrayList<String>());

        Mockito.when(validatorService.validatePost(accommodation)).thenReturn(false);

        Optional<Accommodation> ac = accommodationService.create(accommodation);
        assertFalse(ac.isPresent());
        verifyNoInteractions(accommodationRepository);

    }
}
