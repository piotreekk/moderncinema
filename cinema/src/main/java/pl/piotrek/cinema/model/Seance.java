package pl.piotrek.cinema.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Seance {

    private int id;
    private Movie movie;
    private LocalTime startTime;
    private LocalDate date;
    private Auditorium auditorium;

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    public void setEverything(Seance seance){
        this.id = seance.id;
        this.movie = seance.movie;
        this.startTime = seance.startTime;
        this.date = seance.date;
        this.auditorium = seance.auditorium;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", movie=" + movie +
                ", startTime=" + startTime +
                ", date=" + date +
                ", auditorium=" + auditorium +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

}
