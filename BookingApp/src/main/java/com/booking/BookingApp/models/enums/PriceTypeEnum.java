package com.booking.BookingApp.models.enums;

public enum PriceTypeEnum {
    PERGUEST(1),
    PERUNIT(2);

    private final int value;

    PriceTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
