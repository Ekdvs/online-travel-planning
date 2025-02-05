package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.online.travel.planning.online.travel.planning.backend.Model.TravelGuide;
import com.online.travel.planning.online.travel.planning.backend.Repository.TravelGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.travel.planning.online.travel.planning.backend.Model.Reservation;
import com.online.travel.planning.online.travel.planning.backend.Repository.ReservationRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.ReservationService;



@Service
public class ReservationServiceImplementation implements ReservationService{

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TravelGuideRepository travelGuideRepository;

    

   @Override
   public List<Reservation> getAllReservations() {
            return reservationRepository.findAll();
   }

    @Override
       public List<Reservation> getReservationByUserId(String userEmail){
        return reservationRepository.findReservationByUserEmail(userEmail);
    }

    @Override
    public Optional<Reservation> getReservationById(String reservationId) {
       return reservationRepository.findById(reservationId);
   }

    @Override
    public Reservation addReservation(Reservation reservation) {

        return reservationRepository.save(reservation);

    }

   @Override
   public Reservation updateReservation(String reservationId, Reservation reservationDetail) {
       return reservationRepository.findById(reservationId).map(reservation -> {
            reservation.setPackgeId(reservationDetail.getPackgeId());
            reservation.setEventId(reservationDetail.getEventId());
            reservation.setUserEmail(reservationDetail.getUserEmail());
            reservation.setReservationDate(reservationDetail.getReservationDate());
            reservation.setReservationTime(reservationDetail.getReservationTime());
            reservation.setNumOfPerson(reservationDetail.getNumOfPerson());
            reservation.setPerPersonCharge(reservationDetail.getPerPersonCharge());
          reservation.setTotalCharge(reservationDetail.getTotalCharge());
            reservation.setStatus(reservationDetail.getStatus());
            return reservationRepository.save(reservation);
        }).orElseThrow(() -> new RuntimeException("Reservation not found with id " + reservationId));
    }

    @Override
    public void deleteReservation(String reservationId) {

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();

            // If a travel guide was booked, release them
            if (reservation.getTravelGuideId() != null) {
                Optional<TravelGuide> guideOptional = travelGuideRepository.findById(reservation.getTravelGuideId());
                guideOptional.ifPresent(guide -> {
                    guide.setAvailable(true);
                    travelGuideRepository.save(guide);
                });
            }
            reservationRepository.deleteById(reservationId);
        } else {
            throw new RuntimeException("Reservation not found");
        }
    }


     @Override
    public double getTotalChargeByCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return reservationRepository.findTotalChargesByDate(currentDate)
               .stream()
                .mapToDouble(Reservation::getTotalCharge)
                .sum();
    }

    @Override
    public double getTotalCharge() {
        return reservationRepository.findAllTotalCharges()
                .stream()
                .mapToDouble(Reservation::getTotalCharge)
               .sum();
   }

    @Override
    public int getTotalPersonByCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return reservationRepository.findNumOfPersonByDate(currentDate)
                .stream()
                .mapToInt(Reservation::getNumOfPerson)
                .sum();
    }

    @Override
    public int getTotalPerson() {
        return reservationRepository.findAllNumOfPerson()
                .stream()
                .mapToInt(Reservation::getNumOfPerson)
                .sum();
    }


    

}
