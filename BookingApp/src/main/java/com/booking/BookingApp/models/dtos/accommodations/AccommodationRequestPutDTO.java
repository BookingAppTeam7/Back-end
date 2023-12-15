package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;

public class AccommodationRequestPutDTO {
    public Long unapprovedAccommodationId;   //za dobavljanje podatak ao neodobrenom smestaju
   // public AccommodationRequestStatus requestStatus;
    public Long originalAccommodationId;

    public AccommodationRequestPutDTO(Long unapprovedAccommodationId, Long originalAccommodationId) {
        this.unapprovedAccommodationId = unapprovedAccommodationId;
       ;
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
