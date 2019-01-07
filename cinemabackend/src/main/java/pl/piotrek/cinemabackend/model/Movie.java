package pl.piotrek.cinemabackend.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Movie")
public class Movie {

    @Id
    private int id;
    private String title;
    @Lob
    private String overview;
    private String releaseDate;
    private String posterPath;

    // umozliwi mi to wybranie filmu i pokazanie jego wszystkich seansow ?

//    @JsonIgnore prawdopodobnie sie przyda aby uniknac petli nieskonczonej dla Jacksona
    @OneToMany(mappedBy = "movie")
    private List<Seance> seance = new ArrayList<>();

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
