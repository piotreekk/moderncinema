package pl.piotrek.cinemabackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinemabackend.model.Seance;
import pl.piotrek.cinemabackend.model.Seat;
import pl.piotrek.cinemabackend.repository.AuditoriumRepository;
import pl.piotrek.cinemabackend.repository.MovieRepository;
import pl.piotrek.cinemabackend.repository.SeanceRepository;
import pl.piotrek.cinemabackend.service.SeanceService;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeanceServiceImpl implements SeanceService {
    private SeanceRepository seanceRepository;
    private MovieRepository movieRepository;
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    public SeanceServiceImpl(SeanceRepository seanceRepository, MovieRepository movieRepository, AuditoriumRepository auditoriumRepository) {
        this.seanceRepository = seanceRepository;
        this.movieRepository = movieRepository;
        this.auditoriumRepository = auditoriumRepository;
    }

    @Override
    public List<Seance> getAll() {
        return (List<Seance>) seanceRepository.findAll();
    }

    @Override
    public List<Seance> getByDate(LocalDate date) {
        return seanceRepository.findByDate(date);
    }

    @Override
    public Seance addSeance(SeanceForm seanceForm) {
        Seance seance = new Seance();
        seance.setMovie(movieRepository.findById(seanceForm.getMovieId()).get());
        seance.setAuditorium(auditoriumRepository.findById(seanceForm.getAuditoriumId()).get());
        seance.setDate(seanceForm.getDate());
        seance.setStartTime(seanceForm.getStartTime());

        return seanceRepository.save(seance);
    }

    @Override
    public List<Seat> getFreeSeats(Integer seance_id) {
        Seance seance = seanceRepository.findById(seance_id).get();
        return seanceRepository.findFreeSeats(seance);
    }

    @Override
    public List<Seat> getTakenSeats(Integer seance_id) {
        Seance seance = seanceRepository.findById(seance_id).get();
        return seanceRepository.findTakenSeats(seance);
    }


}
