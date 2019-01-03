package pl.piotrek.cinemabackend.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piotrek.cinemabackend.model.Auditorium;

public interface AuditoriumRepository extends CrudRepository<Auditorium, Integer> {

}
