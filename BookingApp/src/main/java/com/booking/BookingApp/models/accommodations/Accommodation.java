package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="accommodations")
//@SQLDelete(sql
//    = "UPDATE accommodations"
//    + " SET deleted = true "
//    + "WHERE id = ?")
//@SQLDelete(sql
//        = "UPDATE accommodations "
//        + "SET deleted = true "
//        + "WHERE id = ? "
//        +"UPDATE location SET deleted=true WHERE accommodation_id=?"
//        )


@SQLDelete(sql = "UPDATE accommodations SET deleted = true WHERE id = ?")
//@SQLDelete(sql = "UPDATE location SET deleted = true WHERE accommodation_id = ?")

@Where(clause="deleted=false")

public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String description;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "location_id")
@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "location_id", referencedColumnName = "id", foreignKey = @ForeignKey(
        name = "fk_accommodation_location",
        foreignKeyDefinition = "FOREIGN KEY (location_id) REFERENCES location(id) ON DELETE CASCADE"))
    public Location location;
    public int minGuests;
    public int maxGuests;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public TypeEnum type;
    @ElementCollection
    public List<String> assets;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
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

    public Accommodation(Long id, String name, String description,Location location,int minGuests,int maxGuests,TypeEnum type,List<String> assets,List<PriceCard> prices,String ownerId, AccommodationStatusEnum status,int cancellationDeadline,ReservationConfirmationEnum reservationConfirmation, List<Review> reviews,List<String> images,Boolean deleted){
        this.id=id;
        this.name=name;
        this.description=description;
        this.location=location;
        this.minGuests=minGuests;
        this.maxGuests=maxGuests;
        this.type=type;
        this.assets=assets;
        this.prices=prices;
        this.ownerId=ownerId;
        this.status=status;
        this.cancellationDeadline=cancellationDeadline;
        this.reservationConfirmation=reservationConfirmation;
        this.deleted=deleted;
        this.reviews=reviews;
        this.images=images;
    }

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

    public Accommodation(Long id, String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, String ownerId, AccommodationStatusEnum status, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation,List<String> images, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = prices;
        this.ownerId = ownerId;
        this.status = status;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.images = images;
        this.deleted = deleted;
    }

    public Accommodation(String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, String ownerId, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation, List<PriceCard> prices, List<Review> reviews, List<String> images, AccommodationStatusEnum status, boolean deleted) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = prices;
        this.ownerId = ownerId;
        this.status = status;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.reviews = reviews;
        this.images = images;
        this.deleted = deleted;
    }

    public Accommodation(String name, String description, Location location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, String ownerId, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation, List<String> images, AccommodationStatusEnum status, boolean deleted) {
        this.name=name;
        this.description=description;
        this.location=location;
        this.minGuests=minGuests;
        this.maxGuests=maxGuests;
        this.type=type;
        this.assets=assets;
        this.ownerId=ownerId;
        this.cancellationDeadline=cancellationDeadline;
        this.reservationConfirmation=reservationConfirmation;
        this.images=images;
        this.status=status;
        this.deleted=deleted;

    }

    public double calculateUnitPrice(Date arrival, Date checkout){
        double unitPrice=0.0;
        for (PriceCard p:this.prices) {
            if(isWithinTimeSlot(arrival,checkout,p.timeSlot)){
                unitPrice= p.price;
            }
        }
        return unitPrice;
    }
    public double calculateAverageRating(){
        double averageRating=0.0;
        if(this.reviews.isEmpty())
            return 0.0;
        for(Review r:this.reviews){
            averageRating+=r.grade;
        }
        return averageRating/this.reviews.size();
    }
    public double calculateTotalPrice(Date arrival, Date checkout, int guests){
        double totalPrice=0.0;
        Instant instant1 =arrival.toInstant();
        LocalDate a1=instant1.atZone(ZoneId.systemDefault()).toLocalDate();
        Instant instant2 =checkout.toInstant();
        LocalDate a2=instant2.atZone(ZoneId.systemDefault()).toLocalDate();
        long totalDays = ChronoUnit.DAYS.between(a1, a2);
        for (PriceCard p:this.prices) {
            if(isWithinTimeSlot(arrival,checkout,p.timeSlot)){
                if(p.type.equals(PriceTypeEnum.PERUNIT)){
                    totalPrice=totalDays*p.price;
                }
                else if(p.type.equals(PriceTypeEnum.PERGUEST)){
                    totalPrice=totalDays*p.price*guests;
                }
            }
        }
        return totalPrice;
    }
    private boolean isWithinTimeSlot(Date arrival, Date checkout, TimeSlot timeSlot) {
        Date timeSlotStart = timeSlot.getStartDate();
        Date timeSlotEnd = timeSlot.getEndDate();
        return !(arrival.before(timeSlotStart) || checkout.after(timeSlotEnd));
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

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", type=" + type +
                ", assets=" + assets +
                ", prices=" + prices +
                ", ownerId='" + ownerId + '\'' +
                ", status=" + status +
                ", cancellationDeadline=" + cancellationDeadline +
                ", reservationConfirmation=" + reservationConfirmation +
                ", reviews=" + reviews +
                ", images=" + images +
                ", deleted=" + deleted +
                '}';
    }
}
