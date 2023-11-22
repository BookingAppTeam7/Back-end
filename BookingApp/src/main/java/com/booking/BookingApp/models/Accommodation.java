package com.booking.BookingApp.models;

import com.booking.BookingApp.models.enums.TypeEnum;

import java.util.List;


public class Accommodation {
    public Long id;
    public String name;
    public String description;
    public String location;
    public int minGuests;
    public int maxGuests;
    public TypeEnum type;
    public List<String> assets;
    public int price;

    public Accommodation(Long id, String name, String description, String location, int minGuests, int maxGuests, TypeEnum type, List<String> assets,int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.assets = assets;
        this.price=price;
    }
    public Accommodation(){

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
