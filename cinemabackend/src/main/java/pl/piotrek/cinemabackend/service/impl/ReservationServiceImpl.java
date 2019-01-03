package pl.piotrek.cinemabackend.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrek.cinemabackend.forms.ReservationForm;
import pl.piotrek.cinemabackend.model.*;
import pl.piotrek.cinemabackend.repository.*;
import pl.piotrek.cinemabackend.service.ReservationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private SeanceRepository seanceRepository;
    private UserRepository userRepository;
    private SeatRepository seatRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, SeanceRepository seanceRepository, UserRepository userRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.seanceRepository = seanceRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void addReservation(ReservationForm reservationForm) {
        // Kompletuje dane do rezerwacji:
        System.out.println(reservationForm.getUserId());
        User user = userRepository.findById(reservationForm.getUserId()).get();
        Seance seance = seanceRepository.findById(reservationForm.getSeanceId()).get();

        Reservation reservation = new Reservation();
        reservation.setSeance(seance);
        reservation.setUser(user);

        List<ReservedSeat> seats = new ArrayList<>();
        for(Integer seatId : reservationForm.getSeats()){
            Seat seat = seatRepository.findById(seatId).get();
            ReservedSeat reservedSeat = new ReservedSeat();
            reservedSeat.setSeat(seat);
            reservedSeat.setReservation(reservation);
            reservedSeat.setSeance(seance);
            seats.add(reservedSeat);
        }

        reservation.setReservedSeats(seats);
        reservationRepository.save(reservation);
    }


    @Override
    public String getReservationDetails(ReservationForm reservationForm) {
        StringBuilder builder = new StringBuilder();
        Seance seance = seanceRepository.findById(reservationForm.getSeanceId()).get();
        User user = userRepository.findById(reservationForm.getUserId()).get();
        List<Seat> seats = (List<Seat>)seatRepository.findAllById(reservationForm.getSeats());
        StringBuilder seatsbuilder = new StringBuilder();
        for(Seat s : seats)
            seatsbuilder.append(s.getRow()).append(" ").append(s.getNumber()).append(",");

        builder
                .append(user.getFirstName()).append(" ").append(user.getLastName()).append(";")
                .append(user.getEmail()).append(";")
                .append(seance.getMovie().getTitle()).append(";")
                .append(seance.getDate()).append(";")
                .append(seance.getStartTime()).append(";")
                .append(seatsbuilder);

        return builder.toString();

    }

    @Override
    public Reservation get(Integer id) {
        return reservationRepository.findById(id).get();
    }


    @Override
    public List<String> getUserReservations(Integer id) {
        List<Reservation> reservations = reservationRepository.findAllByUserId(id);
        List<String> result = new ArrayList<>();
        for(Reservation r : reservations){
            // Wiem ze to co robię niżej jest bardzo dziwne, ale juz nie kombinuje. Nowy projekt będzie lepszy
            StringBuilder seatsbuilder = new StringBuilder();
            for(ReservedSeat s : r.getReservedSeats())
                seatsbuilder.append(s.getSeat().getRow()).append(" ").append(s.getSeat().getNumber()).append(",");

            String details = new StringBuilder()
                    .append(r.getSeance().getMovie().getTitle()).append(";")
                    .append(r.getSeance().getDate()).append(";")
                    .append(r.getSeance().getStartTime()).append(";")
                    .append(r.getSeance().getAuditorium().getName()).append(";")
                    .append(seatsbuilder).toString();

            result.add(details);
        }

        return result;
    }
}
