package pl.piotrek.cinemabackend.service;

import pl.piotrek.cinemabackend.forms.ReservationForm;

public interface ReservationEmailService {
    void send(ReservationForm reservationForm);
}
