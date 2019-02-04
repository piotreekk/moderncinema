package pl.piotrek.cinemabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinemabackend.model.Movie;


@Mapper(componentModel = "spring")
public interface MovieMapper{
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDTO movieToMovieDto(Movie movie);
    Movie movieDtoToMovie(MovieDTO movieDTO);
}
