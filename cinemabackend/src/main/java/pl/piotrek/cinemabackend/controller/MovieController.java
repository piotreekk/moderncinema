package pl.piotrek.cinemabackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinemabackend.model.Movie;
import pl.piotrek.cinemabackend.service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Movie added!")
    @ResponseBody
    Movie addMovie(@RequestBody Movie movie){
        return movieService.addMovie(movie);
    }

    @GetMapping("/get/all")
    @ResponseStatus(value = HttpStatus.OK)
    List<Movie> getAll(){
        return movieService.getAll();
    }

    @GetMapping("/get/played")
    List<Movie> getPlayed(){
        return movieService.getPlayedMovies();
    }
}
