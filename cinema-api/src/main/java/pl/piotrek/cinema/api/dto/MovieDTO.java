package pl.piotrek.cinema.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
// TODO: DO WYNIESIENIA DO MODUŁU API, PAKIETU DTO


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private int id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;

    public MovieDTO(int id, String title, String overview, String releaseDate, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public MovieDTO(){}


}
