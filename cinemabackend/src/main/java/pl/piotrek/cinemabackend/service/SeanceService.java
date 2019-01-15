package pl.piotrek.cinemabackend.service;

import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinemabackend.model.Seance;
import pl.piotrek.cinemabackend.model.Seat;

import java.time.LocalDate;
import java.util.List;

public interface SeanceService {
    List<Seance> getAll();
    List<Seance> getByDate(LocalDate date);
    Seance addSeance(SeanceForm seanceForm);

    List<Seat> getFreeSeats(Integer seance_id);
    List<Seat> getTakenSeats(Integer seance_id);

}
