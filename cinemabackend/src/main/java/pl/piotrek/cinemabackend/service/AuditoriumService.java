package pl.piotrek.cinemabackend.service;


import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.model.Seat;

import java.util.List;

public interface AuditoriumService{
    void add(Auditorium auditorium);
    void add(String name, int rows, int cols);
    void update(Integer id, String name, Integer rows, Integer cols);
    Auditorium get(Integer id);
    List<Auditorium> getAll();

}
