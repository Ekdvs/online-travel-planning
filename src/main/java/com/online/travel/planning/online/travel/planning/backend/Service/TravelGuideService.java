package com.online.travel.planning.online.travel.planning.backend.Service;

import com.online.travel.planning.online.travel.planning.backend.Model.TravelGuide;

import java.util.List;

public interface TravelGuideService {
    List<TravelGuide> getAvailableGuides();
    TravelGuide addGuide(TravelGuide guide);
    TravelGuide updateGuideAvailability(String guideId, boolean isAvailable);
    void deleteGuide(String guideId);
}
