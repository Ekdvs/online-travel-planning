package com.online.travel.planning.online.travel.planning.backend.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.online.travel.planning.online.travel.planning.backend.Model.TravelPlace;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TravelPlaceRepository extends MongoRepository <TravelPlace,String> {
    //Optional<TravelPlace> findByTravelPlaceName(String travelPlacename);
   // TravelPlace findByTravelPlaceName(String travelPlaceName);
   // Optional<TravelPlace>findTravelPlaceByUserId(String userId);

    @Query("{ 'eventType' : ?0 }")
    List<TravelPlace> findByCategory(String category);
    public List<TravelPlace> findByplaceNameContainingIgnoreCase(String placeName);


}
