package com.online.travel.planning.online.travel.planning.backend.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.online.travel.planning.online.travel.planning.backend.Model.Event;

public interface EventRepository extends MongoRepository<Event, String>{
     @Query("{ 'eventType' : ?0 }")
    List<Event> findByType(String eventType);
     List<Event> findByEventNameContainingIgnoreCase(String name);

}
