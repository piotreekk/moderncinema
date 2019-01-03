package pl.piotrek.cinemabackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrek.cinemabackend.model.ReservedSeat;

@Repository
public interface ReservedSeatRepository extends CrudRepository<ReservedSeat, Long> {
}
