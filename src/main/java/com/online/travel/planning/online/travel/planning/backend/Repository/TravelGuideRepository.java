package com.online.travel.planning.online.travel.planning.backend.Repository;

import com.online.travel.planning.online.travel.planning.backend.Model.TravelGuide;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TravelGuideRepository extends MongoRepository<TravelGuide, String> {
    List<TravelGuide> findByIsAvailableTrue();  // Get only available guides
}
