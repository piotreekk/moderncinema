package pl.piotrek.cinema.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeanceDTO {

    private int id;
    private MovieDTO movie;
    private LocalTime startTime;
    private LocalDate date;
    private AuditoriumDTO auditorium;

}
