package com.online.travel.planning.online.travel.planning.backend.Service;
import com.online.travel.planning.online.travel.planning.backend.Model.TravelPlace;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface TravelPlaceService {
    List<TravelPlace> getAllTravelPlaces();
    Optional<TravelPlace> getTravelPlaceById(String placeId);
    List<TravelPlace> getPlaceByCategory(String category);
    TravelPlace addPlace(TravelPlace travelPlace, MultipartFile imagefile)throws IOException;
    TravelPlace updatePlace(String placeid,TravelPlace travelPlace ,MultipartFile imagefile)throws IOException;
    void deletePlace(String placeid);
    List<TravelPlace> searchPlaceByName(String placeName);
}
