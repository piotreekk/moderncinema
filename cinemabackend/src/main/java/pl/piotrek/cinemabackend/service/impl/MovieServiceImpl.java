package pl.piotrek.cinemabackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrek.cinemabackend.model.Movie;
import pl.piotrek.cinemabackend.repository.MovieRepository;
import pl.piotrek.cinemabackend.service.MovieService;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> getAll() {
        return (List<Movie>) movieRepository.findAll();
    }

    @Override
    public List<Movie> getPlayedMovies() {
        return movieRepository.findAllBySeanceIsNotNull();
    }


}
