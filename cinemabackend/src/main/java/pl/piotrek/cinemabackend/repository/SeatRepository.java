package pl.piotrek.cinemabackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.model.Seat;

import java.util.List;

@Repository
public interface SeatRepository extends CrudRepository<Seat, Integer> {
    Seat findByRowAndNumberAndAuditorium(Integer row, Integer number, Auditorium auditorium);
    List<Seat> findByRowAndAuditorium(Integer row, Auditorium auditorium);
    List<Seat> findByNumberAndAuditorium(Integer number, Auditorium auditorium);
}
