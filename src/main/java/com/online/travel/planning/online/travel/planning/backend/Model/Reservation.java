package com.online.travel.planning.online.travel.planning.backend.Model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservation")
public class Reservation {
    @Id
    private String reservationId;
    private String userEmail;
    private String packgeId;
    private String eventId;
    private Integer numOfPerson;
    private Double totalCharge;
    private String travelGuideId;
    private Double perPersonCharge;
    private String status = "Available";
    private LocalDate reservationDate = LocalDate.now();
    private LocalTime reservationTime = LocalTime.now();

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPackgeId() {
        return packgeId;
    }

    public void setPackgeId(String packgeId) {
        this.packgeId = packgeId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getNumOfPerson() {
        return numOfPerson;
    }

    public void setNumOfPerson(Integer numOfPerson) {
        this.numOfPerson = numOfPerson;
    }

    public Double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Double totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getTravelGuideId() {
        return travelGuideId;
    }

    public void setTravelGuideId(String travelGuideId) {
        this.travelGuideId = travelGuideId;
    }

    public Double getPerPersonCharge() {
        return perPersonCharge;
    }

    public void setPerPersonCharge(Double perPersonCharge) {
        this.perPersonCharge = perPersonCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
