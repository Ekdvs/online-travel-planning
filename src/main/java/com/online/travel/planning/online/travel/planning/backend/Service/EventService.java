package com.online.travel.planning.online.travel.planning.backend.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.online.travel.planning.online.travel.planning.backend.Model.Event;

public interface EventService {
    List<Event> getAllEvents();
    Optional<Event> getEventById(String eventId);
    List<Event> getEventByType(String eventType);
    Event addEvent(Event event, MultipartFile imagefile)throws IOException;
    Event updateEvent(String eventId, Event eventDetails,MultipartFile imageFile)throws IOException;
    void deleteEvent(String eventId);
    List<Event> searchEventsByName(String name);
    Event bookEvent(String eventId, Integer numOfTickets);
    Integer getAvailableTickets(String eventId);


}
