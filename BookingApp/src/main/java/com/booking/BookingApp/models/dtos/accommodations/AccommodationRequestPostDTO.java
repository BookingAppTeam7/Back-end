package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;

public class AccommodationRequestPostDTO {
    public Long unapprovedAccommodationId;   //za dobavljanje podatakq o neodobrenom smestaju
    //public AccommodationRequestStatus requestStatus;
    public Long originalAccommodationId; //bice null ako je novo  kreiran a nece ako je izmenjen vec stari

    public AccommodationRequestPostDTO(Long unapprovedAccommodationId,Long originalAccommodationId) {
        this.unapprovedAccommodationId = unapprovedAccommodationId;
        this.originalAccommodationId = originalAccommodationId;
    }

    public Long getUnapprovedAccommodationId() {
        return unapprovedAccommodationId;
    }

    public void setUnapprovedAccommodationId(Long unapprovedAccommodationId) {
        this.unapprovedAccommodationId = unapprovedAccommodationId;
    }

    public Long getOriginalAccommodationId() {
        return originalAccommodationId;
    }

    public void setOriginalAccommodationId(Long originalAccommodationId) {
        this.originalAccommodationId = originalAccommodationId;
    }
}
