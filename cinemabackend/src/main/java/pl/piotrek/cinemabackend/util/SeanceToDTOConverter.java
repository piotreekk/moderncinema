package pl.piotrek.cinemabackend.util;

import org.springframework.stereotype.Component;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.api.dto.SeatDTO;
import pl.piotrek.cinemabackend.model.Seance;

import java.util.stream.Collectors;

@Component
public class SeanceToDTOConverter implements BaseConverter<Seance, SeanceDTO> {

    @Override
    public SeanceDTO convert(Seance from) {
        SeanceDTO seanceDTO = new SeanceDTO();
        seanceDTO.setId(from.getId());

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(from.getMovie().getId());
        movieDTO.setTitle(from.getMovie().getTitle());
        movieDTO.setOverview(from.getMovie().getOverview());
        movieDTO.setPosterPath(from.getMovie().getPosterPath());
        movieDTO.setReleaseDate(from.getMovie().getReleaseDate());


        seanceDTO.setMovie(movieDTO);
        seanceDTO.setDate(from.getDate());
        seanceDTO.setStartTime(from.getStartTime());

        AuditoriumDTO auditoriumDTO = new AuditoriumDTO();
        auditoriumDTO.setId(from.getAuditorium().getId());
        auditoriumDTO.setName(from.getAuditorium().getName());
        auditoriumDTO.setCols(from.getAuditorium().getCols());
        auditoriumDTO.setRows(from.getAuditorium().getRows());


        auditoriumDTO.setSeats(from.getAuditorium().getSeats().stream()
                .map(seat -> {
                    SeatDTO seatDTO = new SeatDTO();
                    seatDTO.setId(seat.getId());
                    seatDTO.setActive(seat.isActive());
                    seatDTO.setNumber(seat.getNumber());
                    seatDTO.setRow(seat.getRow());

                    return seatDTO;
                })
                .collect(Collectors.toList()));


        seanceDTO.setAuditorium(auditoriumDTO);

        return seanceDTO;
    }

}
