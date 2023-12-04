package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="accommodations")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    public Location location;
    public int minGuests;
    public int maxGuests;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)

    public TypeEnum type;
    @ElementCollection
    public List<String> assets;
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    public List<PriceCard> prices;
    public Long ownerId;
    @Enumerated(EnumType.STRING)
    public AccommodationStatusEnum status;
    public int cancellationDeadline;
    @Enumerated(EnumType.STRING)
    public ReservationConfirmationEnum reservationConfirmation;
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    public List<Review> reviews;
    @ElementCollection
    public List<String> images;

    //constructor with id (update)
    public Accommodation(Long id, String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, List<PriceCard> prices, Long ownerId, AccommodationStatusEnum status, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation,List<Review> reviews,List<String>images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = prices;

        if (prices != null) {
            for (PriceCard priceCard : prices) {
                priceCard.setAccommodation(this);
                priceCard.timeSlot.setAccommodation(this);
                priceCard.timeSlot.setType(TimeSlotType.PRICECARD);
            }
        }

        this.ownerId = ownerId;
        this.status = status;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.reviews=reviews;
        this.images=images;
    }

    public Accommodation() {

    }
    //constructor without id (create)

    public Accommodation(String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, List<PriceCard> prices, Long ownerId, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation, List<Review> reviews, List<String> images,AccommodationStatusEnum status) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = (prices != null) ? prices : new ArrayList<>();

        if (prices != null) {
            for (PriceCard priceCard : prices) {
                priceCard.setAccommodation(this);
                priceCard.timeSlot.setAccommodation(this);
                priceCard.timeSlot.setType(TimeSlotType.PRICECARD);
            }
        }
        this.ownerId = ownerId;
        this.status = status;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.reviews = reviews;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public List<String> getAssets() {
        return assets;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }

    public List<PriceCard> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceCard> prices) {
        this.prices = prices;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public AccommodationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatusEnum status) {
        this.status = status;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public ReservationConfirmationEnum getReservationConfirmation() {
        return reservationConfirmation;
    }

    public void setReservationConfirmation(ReservationConfirmationEnum reservationConfirmation) {
        this.reservationConfirmation = reservationConfirmation;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
