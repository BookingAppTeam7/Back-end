package com.booking.BookingApp.models.accommodations;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE location SET deleted = true WHERE id = ?")
@Where(clause="deleted=false")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String address;
    public String city;
    public String country;
    public double x;
    public double y;
    @Column(name="deleted",columnDefinition = "boolean default false")
    private Boolean deleted;

    public Location(Long id, String address, String city, String country, double x, double y,Boolean deleted) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.country = country;
        this.x = x;
        this.y = y;
        this.deleted=deleted;
    }

    public Location() {

    }

    public Location(String address, String city, String country, double x, double y) {
        this.address=address;
        this.city=city;
        this.country=country;
        this.x=x;
        this.y=y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
