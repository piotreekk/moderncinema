package pl.piotrek.cinema.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.fx.MovieFx;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.io.IOException;

@Component
public class MovieModel {
    private CookieRestTemplate cookieRestTemplate;

    public MovieModel(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    private ObjectProperty<MovieFx> movieFxObjectProperty = new SimpleObjectProperty<>(new MovieFx());
    private ObservableList<MovieFx> movieFxObservableList = FXCollections.observableArrayList();
    private FilteredList<MovieFx> movieFxFilteredList = new FilteredList<>(movieFxObservableList, movieFx -> true);


    public MovieFx getMovieFxObjectProperty() {
        return movieFxObjectProperty.get();
    }

    public ObjectProperty<MovieFx> movieFxObjectPropertyProperty() {
        return movieFxObjectProperty;
    }

    public ObservableList<MovieFx> getMovieFxObservableList() {
        return movieFxObservableList;
    }

    public FilteredList<MovieFx> getMovieFxFilteredList() {
        return movieFxFilteredList;
    }


    public void loadDataFromAPI(String year){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                movieFxObservableList.clear();
                // wyczyszczenie filtrów z poprzedniej sesji
                movieFxFilteredList.setPredicate(movieFx -> true);

                String resourceUrl = "https://api.themoviedb.org/3/discover/movie?api_key=7dd49a6da2d9bfb5ee7b370c9ee02139";
                String completeResourceUrl = resourceUrl + "&primary_release_year=" + year;
                ResponseEntity<String> response = cookieRestTemplate.getForEntity(completeResourceUrl, String.class);

                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode root = mapper.readTree(response.getBody());
                    JsonNode results = root.get("results");

                    results.forEach(jsonNode -> {
                        int id = jsonNode.get("id").asInt();
                        String title = jsonNode.get("original_title").asText();
                        String imagePath = new StringBuilder()
                                .append("http://image.tmdb.org/t/p/w154") // IMAGE BASE URL
                                .append(jsonNode.get("poster_path").asText()).toString();
                        String releaseDate = jsonNode.get("release_date").asText();
                        String overview = jsonNode.get("overview").asText();

                        MovieFx movieFx = new MovieFx();
                        movieFx.setId(id);
                        movieFx.setTitle(title);
                        movieFx.setOverview(overview);
                        movieFx.setReleaseDate(releaseDate);
                        movieFx.setPosterPath(imagePath);

                        addMovieToList(movieFx);
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    private void addMovieToList(MovieFx movieFx) {
        movieFxObservableList.add(movieFx);
    }


    public void addMovie(MovieFx movie){
        String url = ServerInfo.MOVIE_ENDPOINT + "/add";
        MovieDTO request = new MovieDTO(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getReleaseDate(), movie.getPosterPath());
        ResponseEntity<MovieDTO> response = cookieRestTemplate.postForEntity(url, request, MovieDTO.class);

    }

    public void filter(String newValue) {
        movieFxFilteredList.setPredicate(movieFx -> {
            if (newValue == null || newValue.equals("")) return true;

            String lowerCaseFilter = newValue.toLowerCase();
            if (movieFx.getTitle().toLowerCase().contains(lowerCaseFilter)) return true;
            else return movieFx.getOverview().toLowerCase().contains(lowerCaseFilter);
        });
    }
}
