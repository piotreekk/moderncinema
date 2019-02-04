package pl.piotrek.cinema.model.fx;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalTime;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

public class SeanceFx {
    private int id;
    private StringProperty movieTitle = new SimpleStringProperty();
    private ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private IntegerProperty freeSeatsCount = new SimpleIntegerProperty();
    private IntegerProperty allSeatsCount = new SimpleIntegerProperty();
    private ObjectProperty<ImageView> posterImage = new SimpleObjectProperty<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalTime getStartTime() {
        return startTime.get();
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
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

    public int getFreeSeatsCount() {
        return freeSeatsCount.get();
    }

    public IntegerProperty freeSeatsCountProperty() {
        return freeSeatsCount;
    }

    public void setFreeSeatsCount(int freeSeatsCount) {
        this.freeSeatsCount.set(freeSeatsCount);
    }

    public int getAllSeatsCount() {
        return allSeatsCount.get();
    }

    public IntegerProperty allSeatsCountProperty() {
        return allSeatsCount;
    }

    public void setAllSeatsCount(int allSeatsCount) {
        this.allSeatsCount.set(allSeatsCount);
    }

    public ImageView getPosterImage() {
        return posterImage.get();
    }

    public ObjectProperty<ImageView> posterImageProperty() {
        return posterImage;
    }

    public void setPosterImage(String posterPath) {
        this.posterImage = new SimpleObjectProperty<>(new ImageView((posterPath)));
    }
}
