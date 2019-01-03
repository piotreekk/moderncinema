package pl.piotrek.cinema.model.table;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationTableModel {
    private StringProperty movieTitle;
    private ObjectProperty<LocalDate> date;
    private ObjectProperty<LocalTime> startTime;
    private StringProperty auditorium;
    private StringProperty seats;

    public ReservationTableModel(String movieTitle, LocalDate date, LocalTime startTime, String auditorium, String seats) {
        this.movieTitle = new SimpleStringProperty(movieTitle);
        this.date = new SimpleObjectProperty<>(date);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.auditorium = new SimpleStringProperty(auditorium);
        this.seats = new SimpleStringProperty(seats);
    }

    public String getMovieTitle() {
        return movieTitle.get();
    }

    public StringProperty movieTitleProperty() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle.set(movieTitle);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public LocalTime getStartTime() {
        return startTime.get();
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
    }

    public String getAuditorium() {
        return auditorium.get();
    }

    public StringProperty auditoriumProperty() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium.set(auditorium);
    }

    public String getSeats() {
        return seats.get();
    }

    public StringProperty seatsProperty() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats.set(seats);
    }
}
