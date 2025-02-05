package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;

import com.online.travel.planning.online.travel.planning.backend.Model.TravelGuide;
import com.online.travel.planning.online.travel.planning.backend.Repository.TravelGuideRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.TravelGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelGuideServiceImplementation implements TravelGuideService {

    @Autowired
    private TravelGuideRepository travelGuideRepository;

    @Override
    public List<TravelGuide> getAvailableGuides() {
        return travelGuideRepository.findByIsAvailableTrue();
    }

    @Override
    public TravelGuide addGuide(TravelGuide guide) {
        return travelGuideRepository.save(guide);
    }

    @Override
    public TravelGuide updateGuideAvailability(String guideId, boolean isAvailable) {
        Optional<TravelGuide> guideOptional = travelGuideRepository.findById(guideId);
        if (guideOptional.isPresent()) {
            TravelGuide guide = guideOptional.get();
            guide.setAvailable(isAvailable);
            return travelGuideRepository.save(guide);
        } else {
            throw new RuntimeException("Travel Guide not found");
        }
    }

    @Override
    public void deleteGuide(String guideId) {
        if (travelGuideRepository.existsById(guideId)) {
            travelGuideRepository.deleteById(guideId);
        } else {
            throw new RuntimeException("Travel Guide not found");
        }
    }
}
