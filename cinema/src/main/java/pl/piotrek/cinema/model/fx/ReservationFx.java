package pl.piotrek.cinema.model.fx;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationFx {
    private StringProperty movieTitle = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private StringProperty auditorium = new SimpleStringProperty();
    private StringProperty seats = new SimpleStringProperty();

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
