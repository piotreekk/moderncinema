package pl.piotrek.cinema.model.table;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import pl.piotrek.cinema.model.Movie;

public class MovieTableModel extends Movie {
    private ImageView posterImage;
    private Label overviewLabel;

    public MovieTableModel(int id, String title, String overview, String releaseDate, String posterPath) {
        super(id, title, overview, releaseDate, posterPath);
        this.posterImage = new ImageView(posterPath);
        this.overviewLabel = new Label();
        this.overviewLabel.setText(overview);
        this.overviewLabel.setWrapText(true);
    }

    public ImageView getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(ImageView posterImage) {
        this.posterImage = posterImage;
    }

    public Label getOverviewLabel() {
        return overviewLabel;
    }

    public void setOverviewLabel(Label overviewLabel) {
        this.overviewLabel = overviewLabel;
    }
}
