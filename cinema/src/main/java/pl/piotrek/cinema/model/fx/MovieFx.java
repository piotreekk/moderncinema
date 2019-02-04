package pl.piotrek.cinema.model.fx;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class MovieFx {
    private int id;
    private StringProperty title = new SimpleStringProperty();
    private StringProperty overview = new SimpleStringProperty();
    private StringProperty releaseDate = new SimpleStringProperty();
    private StringProperty posterPath = new SimpleStringProperty();
    private ObjectProperty<ImageView> posterImage = new SimpleObjectProperty<>();
    private ObjectProperty<Label> overviewLabel = new SimpleObjectProperty<>(new Label());


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getOverview() {
        return overview.get();
    }

    public StringProperty overviewProperty() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview.set(overview);
        this.overviewLabel.get().setText(overview);
        this.overviewLabel.get().setWrapText(true);
    }

    public String getReleaseDate() {
        return releaseDate.get();
    }

    public StringProperty releaseDateProperty() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public String getPosterPath() {
        return posterPath.get();
    }

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath.set(posterPath);
        this.posterImage = new SimpleObjectProperty<>(new ImageView(posterPath));
    }

    public ImageView getPosterImage() {
        return posterImage.get();
    }

    public ObjectProperty<ImageView> posterImageProperty() {
        return posterImage;
    }

    public Label getOverviewLabel() {
        return overviewLabel.get();
    }

    public ObjectProperty<Label> overviewLabelProperty() {
        return overviewLabel;
    }

}
