package com.online.travel.planning.online.travel.planning.backend.Service;



import java.util.List;
import java.util.Optional;

import com.online.travel.planning.online.travel.planning.backend.Model.Reservation;

public interface ReservationService {
   List<Reservation> getAllReservations();
    List<Reservation> getReservationByUserId(String userId);
    Optional<Reservation> getReservationById(String reservationId);
    Reservation addReservation(Reservation reservation);
    Reservation updateReservation(String reservationId, Reservation reservationDetail);
    void deleteReservation(String reservationId);
    double getTotalChargeByCurrentDate();
   double getTotalCharge();
   int getTotalPersonByCurrentDate();
   int getTotalPerson();
}
