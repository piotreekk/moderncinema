package pl.piotrek.cinemabackend.service;

import pl.piotrek.cinema.api.forms.ReservationForm;

public interface ReservationEmailService {
    void send(ReservationForm reservationForm);
}
