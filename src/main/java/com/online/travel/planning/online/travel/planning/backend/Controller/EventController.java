package com.online.travel.planning.online.travel.planning.backend.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.online.travel.planning.online.travel.planning.backend.Model.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.travel.planning.online.travel.planning.backend.Model.Event;
import com.online.travel.planning.online.travel.planning.backend.Service.EventService;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/event")
public class EventController {


    
    @Autowired
    private EventService eventService;

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";

    @GetMapping("/getAllEvents")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/searchEvents")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String name) {
        List<Event> events = eventService.searchEventsByName(name);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/getEventById/{id}")
    public Optional<Event> getEventById(@PathVariable("id") String eventId) {
        return eventService.getEventById(eventId);
    }

    @GetMapping("/getEventByType/{eventType}")
    public List<Event> getEventByType(@PathVariable("eventType") String eventType) {
        return eventService.getEventByType(eventType);
    }

    @PostMapping("/addEvent")
    public ResponseEntity<?>addEvent(@RequestPart("event")String eventJson,@RequestPart("imageFile") MultipartFile imagefile) throws IOException {
        try {ObjectMapper objectMapper = new ObjectMapper();
            Event event = objectMapper.readValue(eventJson, Event.class);
            Event eventExist = eventService.addEvent(event,imagefile);
            return new ResponseEntity<>(eventExist, HttpStatus.CREATED);
            

        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateEvent/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable("id") String eventId,
            @RequestPart("event") String eventJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Event updatedEvent = objectMapper.readValue(eventJson, Event.class);
            Event event = eventService.updateEvent(eventId, updatedEvent, imageFile);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteEvent/{id}")
    public String deleteEvent(@PathVariable("id") String eventId) {
        eventService.deleteEvent(eventId);
        return "Event deleted with id " + eventId;
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {// Generate a unique filename
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Create the directory if it doesn't exist
            Path filePath = Paths.get(IMAGE_DIRECTORY, filename);
            Files.createDirectories(filePath.getParent());

            // Save the file to the server
            Files.write(filePath, file.getBytes());

            // Return the accessible file path
            return ResponseEntity.ok("/images/" + filename);
            
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/bookEvent")
    public ResponseEntity<String> bookEvent(@RequestBody BookingRequest bookingRequest) {
        try {
            Event updatedEvent = eventService.bookEvent(bookingRequest.getEventId(), bookingRequest.getNumOfTickets());
            return ResponseEntity.ok("Booking successful. " + updatedEvent.getNumOfTickets() + " tickets remaining.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/availableTickets/{eventId}")
    public ResponseEntity<Integer> getAvailableTickets(@PathVariable String eventId) {
        try {
            Integer availableTickets = eventService.getAvailableTickets(eventId);
            return ResponseEntity.ok(availableTickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Event not found
        }
    }



    




}
