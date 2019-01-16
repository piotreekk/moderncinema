package pl.piotrek.cinemabackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
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
    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Seance> seance;


}