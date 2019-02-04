package pl.piotrek.cinemabackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinemabackend.mapper.MovieMapper;
import pl.piotrek.cinemabackend.model.Movie;
import pl.piotrek.cinemabackend.service.MovieService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private MovieService movieService;
    private MovieMapper movieMapper;

    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Movie added!")
    @ResponseBody
    Movie addMovie(@RequestBody MovieDTO movie){
        return movieService.addMovie(movieMapper.movieDtoToMovie(movie));
    }

    @GetMapping("/get/all")
    @ResponseStatus(value = HttpStatus.OK)
    List<MovieDTO> getAll(){
        return movieService.getAll()
                .stream()
                .map(movie -> movieMapper.movieToMovieDto(movie))
                .collect(Collectors.toList());
    }

    @GetMapping("/get/played")
    List<MovieDTO> getPlayed(){

        return movieService.getPlayedMovies()
                .stream()
                .map(movie -> movieMapper.movieToMovieDto(movie))
                .collect(Collectors.toList());
    }
}
