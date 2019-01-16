package pl.piotrek.cinema.model;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinema.api.dto.SeanceDTO;

import java.time.LocalDate;
import java.time.LocalTime;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

public class SeanceTableModel{
    private SeanceDTO seanceDTO;

    private StringProperty movieTitle;
    private ObjectProperty<LocalTime> startTime;
    private ObjectProperty<LocalDate> date;
    private IntegerProperty freeSeatsCount;
    private IntegerProperty allSeatsCount;
    private ObjectProperty<ImageView> posterImage;

    public SeanceTableModel(SeanceDTO seanceDTO){
        this(seanceDTO.getMovie(), seanceDTO.getStartTime(), seanceDTO.getDate());
        this.seanceDTO = seanceDTO;
    }

    public SeanceTableModel(MovieDTO movieDTO, LocalTime startTime, LocalDate date) {
        this.movieTitle = new SimpleStringProperty(movieDTO.getTitle());
        this.allSeatsCount = new SimpleIntegerProperty(10);
        this.freeSeatsCount = new SimpleIntegerProperty(10);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.date = new SimpleObjectProperty<>(date);
        this.posterImage = new SimpleObjectProperty<>(new ImageView(movieDTO.getPosterPath()));
    }

    public SeanceDTO getSeanceDTO(){
        return seanceDTO;
    }

    public ImageView getPosterImage() {
        return posterImage.get();
    }

    public ObjectProperty<ImageView> posterImageProperty() {
        return posterImage;
    }

    public void setPosterImage(ImageView posterImage) {
        this.posterImage.set(posterImage);
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
}
