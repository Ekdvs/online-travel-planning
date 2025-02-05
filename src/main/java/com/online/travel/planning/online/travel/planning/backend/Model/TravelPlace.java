package com.online.travel.planning.online.travel.planning.backend.Model;
import org.springframework.data.annotation.Id;
import java.time.LocalDate;
import java.time.LocalTime;



public class TravelPlace {
    @Id
    private String placeId;
    private String placeName;
    private String location;
    private String description;
    private LocalDate dateAdded = LocalDate.now();
    private LocalTime timeAdded = LocalTime.now();
    private String placeImagePath;
    private String contentType;
    private byte[] imageData;
    private String category;

    public String getPlaceImagePath() {
        return placeImagePath;
    }

    public void setPlaceImagePath(String placeImagePath) {
        this.placeImagePath = placeImagePath;
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

    public LocalDate getDateAdded() {

        return dateAdded;
    }
    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(LocalTime timeAdded) {
        this.timeAdded = timeAdded;
    }
    public String getPlaceId() {
        return placeId;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }





}
