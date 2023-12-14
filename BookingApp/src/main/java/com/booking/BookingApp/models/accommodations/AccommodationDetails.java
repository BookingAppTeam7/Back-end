package com.booking.BookingApp.models.accommodations;

public class AccommodationDetails {
    private Accommodation accommodation;
    private double totalPrice;
    private double unitPrice;
    private double averageRating;

    public AccommodationDetails(Accommodation accommodation, double totalPrice, double unitPrice, double averageRating) {
        this.accommodation = accommodation;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "AccommodationDetails{" +
                "accommodation=" + accommodation +
                ", totalPrice=" + totalPrice +
                ", unitPrice=" + unitPrice +
                ", averageRating=" + averageRating +
                '}';
    }

    public AccommodationDetails() {
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
