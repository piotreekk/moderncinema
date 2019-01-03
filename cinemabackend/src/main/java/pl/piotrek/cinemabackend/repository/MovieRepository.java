package pl.piotrek.cinemabackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piotrek.cinemabackend.model.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findAllBySeanceIsNotNull();
}
