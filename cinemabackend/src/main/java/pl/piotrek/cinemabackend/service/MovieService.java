package pl.piotrek.cinemabackend.service;

import pl.piotrek.cinemabackend.model.Movie;

import java.util.List;

public interface MovieService {
    Movie addMovie(Movie movie);
    List<Movie> getAll();
    List<Movie> getPlayedMovies();
}
