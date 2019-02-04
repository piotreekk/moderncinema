package pl.piotrek.cinemabackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.model.Seance;
import pl.piotrek.cinemabackend.model.Seat;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SeanceRepository extends CrudRepository<Seance, Integer> {
    List<Seance> findByDate(LocalDate date);

    @Query(value = "SELECT seat FROM Seance seance JOIN seance.auditorium a JOIN a.seats seat LEFT JOIN seat.reserved_seats r_seat WHERE seance = ?1 AND r_seat IS NULL")
    List<Seat> findFreeSeats(Seance seance);

    @Query(value =  "SELECT seat FROM Seance seance JOIN seance.reservedSeats r_seat JOIN r_seat.seat seat WHERE seance = ?1")
    List<Seat> findTakenSeats(Seance seance);

    @Query(value =  "SELECT auditorium FROM Seance seance WHERE seance.id = ?1")
    Auditorium findAuditoriumBySeanceId(Integer id);

}
