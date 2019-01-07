package pl.piotrek.cinemabackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.model.Seat;
import pl.piotrek.cinemabackend.repository.AuditoriumRepository;
import pl.piotrek.cinemabackend.repository.SeanceRepository;
import pl.piotrek.cinemabackend.repository.SeatRepository;
import pl.piotrek.cinemabackend.service.AuditoriumService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditoriumServiceImpl implements AuditoriumService {
    private AuditoriumRepository auditoriumRepository;
    private SeatRepository seatRepository;
    private SeanceRepository seanceRepository;

    @Autowired
    public AuditoriumServiceImpl(AuditoriumRepository auditoriumRepository, SeatRepository seatRepository, SeanceRepository seanceRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.seatRepository = seatRepository;
        this.seanceRepository = seanceRepository;
    }

    @Override
    public void add(Auditorium auditorium) {
        auditoriumRepository.save(auditorium);
    }

    @Override
    public void add(String name, int rows, int cols) {
        Auditorium auditorium = new Auditorium();
        auditorium.setRows(rows);
        auditorium.setCols(cols);
        List<Seat> seats = new ArrayList<>();
        for(int i=0; i<rows; ++i){
            for(int j=0; j<cols; ++j){
                Seat seat = new Seat();
                seat.setRow(i);
                seat.setNumber(j);
                seat.setAuditorium(auditorium);

                seats.add(seat);
            }
        }

        auditorium.setName(name);
        auditorium.setSeats(seats);
        // Seats will persist in DB cascade
        auditoriumRepository.save(auditorium);
    }

    @Override
    public void update(Integer id, String name, Integer rows, Integer cols) {
        Optional<Auditorium> entity = auditoriumRepository.findById(id);
        if(!entity.isPresent()) throw new EntityNotFoundException();

        Auditorium auditorium = entity.get();
        Integer oldRows = auditorium.getRows();
        Integer oldCols = auditorium.getCols();

        if(rows < oldRows) {
            for(int i=rows; i<oldRows; ++i){
                List<Seat> seats = seatRepository.findByRowAndAuditorium(i, auditorium);
                for(Seat s : seats)
                    s.setActive(false);

                seatRepository.saveAll(seats);
            }
        }

        if(cols < oldCols) {
            for (int i = cols; i < oldCols; ++i) {
                List<Seat> seats = seatRepository.findByNumberAndAuditorium(i, auditorium);
                for (Seat s : seats)
                    s.setActive(false);

                seatRepository.saveAll(seats);
            }
        }

        if(rows > oldRows){
            for(int i=oldRows; i<rows; ++i){
                for(int j=0; j<cols; ++j){
                    Seat seat = new Seat();
                    seat.setRow(i);
                    seat.setNumber(j);
                    seat.setAuditorium(auditorium);
                    if(auditorium.getSeats().contains(seat)) {
                        int index = auditorium.getSeats().indexOf(seat);
                        auditorium.getSeats().get(index).setActive(true);
                    } else {
                        auditorium.getSeats().add(seat);
                    }
                }
            }
        }

        if(cols > oldCols){
            for(int i=oldCols; i<cols; ++i){
                for(int j=0; j<rows; ++j){
                    Seat seat = new Seat();
                    seat.setRow(j);
                    seat.setNumber(i);
                    seat.setAuditorium(auditorium);
                    if(auditorium.getSeats().contains(seat)) {
                        int index = auditorium.getSeats().indexOf(seat);
                        auditorium.getSeats().get(index).setActive(true);
                    } else {
                        auditorium.getSeats().add(seat);
                    }
                }
            }
        }


        auditorium.setName(name);
        auditorium.setRows(rows);
        auditorium.setCols(cols);

        auditoriumRepository.save(auditorium);

    }

    @Override
    public Auditorium get(Integer id) {
        return auditoriumRepository.findById(id).get();
    }

    @Override
    public List<Auditorium> getAll() {
        return (List<Auditorium>)auditoriumRepository.findAll();
    }



}
