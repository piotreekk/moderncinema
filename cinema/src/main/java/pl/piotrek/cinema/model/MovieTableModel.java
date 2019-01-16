package pl.piotrek.cinema.model;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.Data;


@Data
public class MovieTableModel{
    private int id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private ImageView posterImage;
    private Label overviewLabel;

    public MovieTableModel(int id, String title, String overview, String releaseDate, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.posterImage = new ImageView(posterPath);
        this.overviewLabel = new Label();
        this.overviewLabel.setText(overview);
        this.overviewLabel.setWrapText(true);
    }

}
