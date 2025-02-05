package com.online.travel.planning.online.travel.planning.backend.Controller;

import com.online.travel.planning.online.travel.planning.backend.Model.TravelGuide;
import com.online.travel.planning.online.travel.planning.backend.Service.TravelGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travel-guides")
@CrossOrigin(origins = "http://localhost:3000")
public class TravelGuideController {

    @Autowired
    private TravelGuideService travelGuideService;

    @GetMapping("/available")
    public ResponseEntity<List<TravelGuide>> getAvailableGuides() {
        return ResponseEntity.ok(travelGuideService.getAvailableGuides());
    }

    @PostMapping("/add")
    public ResponseEntity<TravelGuide> addGuide(@RequestBody TravelGuide guide) {
        return ResponseEntity.ok(travelGuideService.addGuide(guide));
    }

    @PutMapping("/update-availability/{guideId}")
    public ResponseEntity<?> updateGuideAvailability(
            @PathVariable String guideId,
            @RequestParam boolean isAvailable
    ) {
        try {
            TravelGuide updatedGuide = travelGuideService.updateGuideAvailability(guideId, isAvailable);
            return ResponseEntity.ok(updatedGuide);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{guideId}")
    public ResponseEntity<?> deleteGuide(@PathVariable String guideId) {
        try {
            travelGuideService.deleteGuide(guideId);
            return ResponseEntity.ok("Travel guide deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}