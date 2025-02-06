package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.online.travel.planning.online.travel.planning.backend.Model.Event;
import com.online.travel.planning.online.travel.planning.backend.Model.User;
import com.online.travel.planning.online.travel.planning.backend.Repository.EventRepository;
import com.online.travel.planning.online.travel.planning.backend.Repository.UserRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.Email_Service;
import com.online.travel.planning.online.travel.planning.backend.Service.EventService;

@Service
public class EventServiceImplementation implements EventService{

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Email_Service emailService;

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findAll();
    
        for (Event event : events) {
            String imagePath = event.getEventImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fullPath = getAccessibleUrl("https://online-travel-planning-production.up.railway.app" + imagePath);
                event.setEventImagePath(fullPath);
            }
           

        }

        return events;
    }

    @Override
    public Optional<Event> getEventById(String eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public List<Event> getEventByType(String eventType) {
        List<Event> events = eventRepository.findByType(eventType);
        for (Event event : events) {
            String imagePath = event.getEventImagePath();

            if (imagePath != null && !imagePath.isEmpty()) {
                String fullPath = getAccessibleUrl("https://online-travel-planning-production.up.railway.app" + imagePath);
                event.setEventImagePath(fullPath);
            }
        }
        return events;
    }

    private String getAccessibleUrl(String... urls) {
        for (String url : urls) {
            if (isUrlAccessible(url)) {
                return url;
            }
        }
        return null; // or handle it if neither URL is accessible
    }

    private boolean isUrlAccessible(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

     @Override
    public Event addEvent(Event event, MultipartFile imagefile)throws IOException {

            event.setEventImagePath(imagefile.getOriginalFilename());
            event.setContentType(imagefile.getContentType());
            event.setImageData(imagefile.getBytes());
            Event newevent= eventRepository.save(event);
        List<User> allUsers = userRepository.findAll();
// Prepare the email content
String subject = "New Event Added: " +event.getEventName();
String message =
        "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                ".container { max-width: 800px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }" +
                ".header { background-color: #135bf2; color: white; padding: 15px; border-radius: 8px 8px 0 0; text-align: center; }" +
                ".content { padding: 20px; font-size: 16px; color: #333; }" +
                ".content p { line-height: 1.6; }" +
                ".footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #dddddd; text-align: center; font-size: 13px; color: #777; }" +
                ".footer p { margin: 5px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1 style='color: white;'>Exciting Upcoming Event!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Hello,</p>" +
                "<p>We are thrilled to announce an exciting event:</p>" +
                "<p><strong>" + event.getEventName() + "</strong></p>" +
                "<p>Date: " + event.getEventDate() + "</p>" +
                "<p>Time: " + event.getEventTime() + "</p>" +
                "<p>Venue: " + event.getEventVenue() + "</p>" +
                "<p>Organizer: " + event.getEventOrganizer() + "</p>" +
                "<p>Description: " + event.getDescription() + "</p>" +
                "<p>Ticket Price: $" + event.getOneTicketPrice() + "</p>" +
                "<br>" +
                "<p>Don't miss this opportunity. Book your tickets now!</p>" +
                "<p><strong>Travel Planning Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 online-travel-planning LK. All rights reserved.</p>" +
                "<p>Contact us at ceylontravelplanning@gmail.com</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
                allUsers.forEach(user -> {
                    if (user.getUserRole().equals("user") || user.getUserRole().equals("GUIDE")) {
                       emailService.sendEmail(user.getUserEmail(), subject, message);
                    }
                });
        


        return newevent;

    }
    @Override
        public Event updateEvent(String eventId, Event eventDetails, MultipartFile imageFile) throws IOException {
            Optional<Event> existingEventOpt = eventRepository.findById(eventId);

            if (!existingEventOpt.isPresent()) {
                throw new NoSuchElementException("Event with ID " + eventId + " not found.");
            }

        Event event = existingEventOpt.get();

        // Update event details
        event.setEventName(eventDetails.getEventName());
        event.setEventDate(eventDetails.getEventDate());
        event.setEventTime(eventDetails.getEventTime());
        event.setEventVenue(eventDetails.getEventVenue());
        event.setEventOrganizer(eventDetails.getEventOrganizer());
        event.setOneTicketPrice(eventDetails.getOneTicketPrice());
        event.setDescription(eventDetails.getDescription());
        event.setEventType(eventDetails.getEventType());
        event.setEventIsFor(eventDetails.getEventIsFor());
        event.setNumOfTickets(eventDetails.getNumOfTickets());
        // Add any additional attributes here.

        // Update image file if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            event.setEventImagePath(imageFile.getOriginalFilename());
            event.setContentType(imageFile.getContentType());
            event.setImageData(imageFile.getBytes());
        }

        // Save the updated event
        return eventRepository.save(event);
    }



    @Override
    public void deleteEvent(String eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public List<Event> searchEventsByName(String name) {
        return eventRepository.findByEventNameContainingIgnoreCase(name);
    }

    @Override
    public Event bookEvent(String eventId, Integer numOfTickets) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();

            // Check if there are enough available tickets
            if (event.getNumOfTickets() >= numOfTickets) {
                // Update the number of available tickets
                event.setNumOfTickets(event.getNumOfTickets() - numOfTickets);
                return eventRepository.save(event); // Save the updated event
            } else {
                throw new IllegalStateException("Not enough tickets available.");
            }
        } else {
            throw new IllegalArgumentException("Event not found.");
        }
    }
    @Override
    public Integer getAvailableTickets(String eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (eventOpt.isPresent()) {
            return eventOpt.get().getNumOfTickets(); // Return the available tickets
        } else {
            throw new IllegalArgumentException("Event not found.");
        }
    }

}
