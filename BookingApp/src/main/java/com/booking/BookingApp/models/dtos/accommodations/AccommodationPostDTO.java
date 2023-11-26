package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.ReservationConfirmationEnum;
import com.booking.BookingApp.models.enums.TypeEnum;
import com.booking.BookingApp.models.reservations.PriceCard;

import java.util.List;


public class AccommodationPostDTO { //Accommodation model without id and accommodation status
    public String name;
    public String description;
    public String location;
    public int minGuests;
    public int maxGuests;
    public TypeEnum type;
    public List<String> assets;
    public List<PriceCard> prices;
    public List<TimeSlot> availability;
    public Long ownerId;
    public int cancellationDeadline;
    public ReservationConfirmationEnum reservationConfirmation;
    public Long feedbackId;

    public AccommodationPostDTO(String name, String description, String location, int minGuests, int maxGuests, TypeEnum type, List<String> assets, List<PriceCard> prices, List<TimeSlot> availability, Long ownerId, int cancellationDeadline, ReservationConfirmationEnum reservationConfirmation,Long feedbackId) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.prices = prices;
        this.availability = availability;
        this.ownerId = ownerId;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationConfirmation = reservationConfirmation;
        this.feedbackId=feedbackId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public List<TimeSlot> getAvailability() {
        return availability;
    }

    public void setAvailability(List<TimeSlot> availability) {
        this.availability = availability;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
}
