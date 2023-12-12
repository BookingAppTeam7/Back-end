package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="accommodations")
//@SQLDelete(sql
//    = "UPDATE accommodations"
//    + " SET deleted = true "
//    + "WHERE id = ?")
@SQLDelete(sql
        = "UPDATE accommodations "
        + "SET deleted = true "
        + "WHERE id = ? "
        +"UPDATE location SET deleted=true WHERE accommodation_id=?"
        )

@Where(clause="deleted=false")

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
    @OneToMany(cascade = CascadeType.ALL)
    public List<PriceCard> prices;

    public String ownerId;
    @Enumerated(EnumType.STRING)
    public AccommodationStatusEnum status;
    public int cancellationDeadline;
    @Enumerated(EnumType.STRING)
    public ReservationConfirmationEnum reservationConfirmation;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Review> reviews;
    @ElementCollection
    public List<String> images;

    @Column(name="deleted",columnDefinition = "boolean default false")
    private Boolean deleted;

    //constructor with id (update)
    public Accommodation(Long id, String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, List<PriceCard> prices,String ownerId,int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation,List<Review> reviews,List<String>images,Boolean deleted,AccommodationStatusEnum status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = prices;
//        this.reservations=reservations;

//        if (prices != null) {
//            for (PriceCard priceCard : prices) {
//                priceCard.setAccommodation(this);
//                priceCard.timeSlot.setAccommodation(this);
//                priceCard.timeSlot.setType(TimeSlotType.PRICECARD);
//            }
//        }

        this.ownerId = ownerId;
        this.status = AccommodationStatusEnum.PENDING;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.reviews=reviews;
        this.images=images;
        this.deleted=deleted;
        this.status=status;
    }

    public Accommodation() {

    }
    //constructor without id (create)

    public Accommodation(String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets,String ownerId, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation, List<Review> reviews, List<String> images,AccommodationStatusEnum status,Boolean deleted) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        //this.prices = (prices != null) ? prices : new ArrayList<>();


        this.ownerId = ownerId;
        this.status = status;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.reviews = reviews;
        this.images = images;
        this.deleted=deleted;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
