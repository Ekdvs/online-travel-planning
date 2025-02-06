package com.online.travel.planning.online.travel.planning.backend.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.travel.planning.online.travel.planning.backend.Model.Reservation;
import com.online.travel.planning.online.travel.planning.backend.Service.ReservationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin("https://ceylon-travelernetlifyapp.vercel.app/")
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;



   @GetMapping("/getAllReservations")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

     @GetMapping("/getReservationById/{id}")
    public Optional<Reservation> getReservationById(@PathVariable("id") String reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    @GetMapping("/getReservationByUserId/{id}")
    public List<Reservation> getReservationByUserId(@PathVariable("id") String userId) {
        return reservationService.getReservationByUserId(userId);
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        System.out.println("Received Reservation: " + reservation);
        System.out.println("User Email: " + reservation.getUserEmail());
        System.out.println("Package ID: " + reservation.getPackgeId());
        // Add more logging to check the values
        return ResponseEntity.ok(reservationService.addReservation(reservation));
    }

    @PutMapping("/updateReservation/{id}")
   public Reservation updateReservation(@PathVariable("id") String reservationId,
                                        @RequestBody Reservation reservation) {
        return reservationService.updateReservation(reservationId, reservation);
    }

    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable String reservationId) {
       try {
            reservationService.deleteReservation(reservationId);
            return ResponseEntity.ok("Reservation canceled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
   }

   @GetMapping("/totalCharge/today")
   public ResponseEntity<Double> getTotalChargeByCurrentDate() {
        return ResponseEntity.ok(reservationService.getTotalChargeByCurrentDate());
    }

    @GetMapping("/totalCharge")
    public ResponseEntity<Double> getTotalCharge() {
        return ResponseEntity.ok(reservationService.getTotalCharge());
    }

    @GetMapping("/totalTickets/today")
    public ResponseEntity<Integer> getTotalPersonByCurrentDate() {
        return ResponseEntity.ok(reservationService.getTotalPerson());
    }

    @GetMapping("/totalTickets")
    public ResponseEntity<Integer> getTotalTickets() {
        return ResponseEntity.ok(reservationService.getTotalPerson());
    }






}
