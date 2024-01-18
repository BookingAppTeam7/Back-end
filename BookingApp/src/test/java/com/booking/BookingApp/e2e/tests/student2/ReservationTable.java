package com.booking.BookingApp.e2e.tests.student2;

public class ReservationTable {
    public String id;
    public String startDate;
    public String endDate;
    public String status;

    public ReservationTable(String id,String startDate, String endDate, String status) {
        this.id=id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReservationTable() {
    }
}
