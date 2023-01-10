package com.example.turfbooking.ui.mybooking;

public class TurfBookingModel {
    private String turfId;
    private String createdDateTime;
    private String bookingDateTime;
    private String bookingDate;
    private String bookingTimeslot;
    private String createdBy;
    private String bookingStatus;
    private String availabilityStatus;
    private String duration;

    public String getTurfId() {
        return turfId;
    }

    public void setTurfId(String turfId) {
        this.turfId = turfId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTimeslot() {
        return bookingTimeslot;
    }

    public void setBookingTimeslot(String bookingTimeslot) {
        this.bookingTimeslot = bookingTimeslot;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
