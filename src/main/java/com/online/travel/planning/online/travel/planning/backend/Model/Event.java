package com.online.travel.planning.online.travel.planning.backend.Model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event")
public class Event {
    @Id
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventVenue;
    private String eventOrganizer;
    private String description;
    private Double oneTicketPrice;
    private String eventType;
    private String eventIsFor;
    private Integer numOfTickets;
    private String eventImagePath;
    private String contentType;
    private byte[] imageData;
    private LocalDate dateAdded = LocalDate.now();
    private LocalTime timeAdded = LocalTime.now();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOneTicketPrice() {
        return oneTicketPrice;
    }

    public void setOneTicketPrice(Double oneTicketPrice) {
        this.oneTicketPrice = oneTicketPrice;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventIsFor() {
        return eventIsFor;
    }

    public void setEventIsFor(String eventIsFor) {
        this.eventIsFor = eventIsFor;
    }

    public String getEventImagePath() {
        return eventImagePath;
    }

    public void setEventImagePath(String eventImagePath) {
        this.eventImagePath = eventImagePath;
    }

    public Integer getNumOfTickets() {
        return numOfTickets;
    }

    public void setNumOfTickets(Integer numOfTickets) {
        this.numOfTickets = numOfTickets;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalTime getTimeAdded() {
        return timeAdded;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setTimeAdded(LocalTime timeAdded) {
        this.timeAdded = timeAdded;
    }









    
    
    
    
    
    
    
    
    
   
    
    
    
    


}
