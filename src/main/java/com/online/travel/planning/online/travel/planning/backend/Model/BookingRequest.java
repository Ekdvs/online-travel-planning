package com.online.travel.planning.online.travel.planning.backend.Model;

public class BookingRequest {
    private String eventId;
    private Integer numOfTickets;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getNumOfTickets() {
        return numOfTickets;
    }

    public void setNumOfTickets(Integer numOfTickets) {
        this.numOfTickets = numOfTickets;
    }
}
