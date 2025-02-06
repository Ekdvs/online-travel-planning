package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;
import com.online.travel.planning.online.travel.planning.backend.Model.TravelPlace;
import com.online.travel.planning.online.travel.planning.backend.Model.User;
import com.online.travel.planning.online.travel.planning.backend.Repository.TravelPlaceRepository;
import com.online.travel.planning.online.travel.planning.backend.Repository.UserRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.Email_Service;
import com.online.travel.planning.online.travel.planning.backend.Service.TravelPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TravelPlaceServiceImplementation implements TravelPlaceService{
    @Autowired
    private TravelPlaceRepository travelPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Email_Service emailService;

    @Override
    public TravelPlace addPlace(TravelPlace travelPlace, MultipartFile imagefile)throws IOException {

        travelPlace.setPlaceImagePath(imagefile.getOriginalFilename());
        travelPlace.setContentType(imagefile.getContentType());
        travelPlace.setImageData(imagefile.getBytes());
        TravelPlace savedTravelPlace = travelPlaceRepository.save(travelPlace);

        //get all users
        List<User> allUsers = userRepository.findAll();
        // Prepare the email content
        String subject = "New Travel Place Added: " + travelPlace.getPlaceName();
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
                        "<h1 style='color: white;'>Exciting New Travel Place!</h1>" +
                        "</div>" +
                        "<div class='content'>" +
                        "<p>Hello,</p>" +
                        "<p>We are thrilled to announce the addition of a new travel destination:</p>" +
                        "<p><strong>" + travelPlace.getPlaceName() + "</strong></p>" +
                        "<p>Location: " + travelPlace.getLocation() + "</p>" +
                        "<p>Description: " + travelPlace.getDescription() + "</p>" +
                        "<br>" +
                        "<p>Plan your next adventure now!</p>" +
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


        return savedTravelPlace;
    }

    @Override
    public List<TravelPlace> getAllTravelPlaces() {
        List<TravelPlace> places = travelPlaceRepository.findAll();
        for (TravelPlace place : places) {
            String imagePath=place.getPlaceImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fullPath = getAccessibleUrl("https://online-travel-planning-production.up.railway.app" + imagePath);
                place.setPlaceImagePath(fullPath);
            }
        }
        return places;
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
    public void deletePlace(String placeId) {
        travelPlaceRepository.deleteById(placeId);
    }

    @Override
    public Optional<TravelPlace> getTravelPlaceById(String placeId) {
    return travelPlaceRepository.findById(placeId);
    }

    @Override
    public List<TravelPlace> getPlaceByCategory(String category) {
        List<TravelPlace> places = travelPlaceRepository.findByCategory(category);

        for (TravelPlace place : places) {
            String imagePath=place.getPlaceImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fullPath = getAccessibleUrl(imagePath);
                place.setPlaceImagePath(fullPath);
            }
        }
        return places;
    }

    @Override
    public List<TravelPlace> searchPlaceByName(String name) {
        return travelPlaceRepository.findByplaceNameContainingIgnoreCase(name);
    }

    @Override
    public TravelPlace updatePlace(String placeid, TravelPlace placeDetails, MultipartFile imageFile) throws IOException {
        Optional<TravelPlace> existingPlaceOpt = travelPlaceRepository.findById(placeid);

        if (!existingPlaceOpt.isPresent()) {
            throw new NoSuchElementException("Place with ID " + placeid + " not found.");
        }
        TravelPlace place = existingPlaceOpt.get();

        //update place
        place.setPlaceName(placeDetails.getPlaceName());
        place.setLocation(placeDetails.getLocation());
        place.setDescription(placeDetails.getDescription());

        // Update image file if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            place.setPlaceImagePath(imageFile.getOriginalFilename());
            place.setContentType(imageFile.getContentType());
            place.setImageData(imageFile.getBytes());
        }
        return travelPlaceRepository.save(place);
    }




}
