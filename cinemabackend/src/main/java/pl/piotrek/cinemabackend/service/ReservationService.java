package pl.piotrek.cinemabackend.service;

import pl.piotrek.cinema.api.forms.ReservationForm;
import pl.piotrek.cinemabackend.model.Reservation;

import java.util.List;

public interface ReservationService {
    void addReservation(ReservationForm reservationForm);
    String getReservationDetails(ReservationForm reservationForm);
    Reservation get(Integer id);
    List<String> getUserReservations(Integer id);
}
