package pl.piotrek.cinema.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.Movie;
import pl.piotrek.cinema.model.table.MovieTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class ImportMoviesController implements Initializable {
    CookieRestTemplate cookieRestTemplate;

    public ImportMoviesController(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    @FXML
    private TableView<MovieTableModel> table;

    @FXML
    private TableColumn imageCol;

    @FXML
    private TableColumn titleCol;

    @FXML
    private TableColumn overviewCol;

    @FXML
    private TableColumn dateCol;

    @FXML
    private ChoiceBox yearChoice;

    @FXML
    private JFXTextField keywordField;


    private ObservableList<MovieTableModel> movies;
    private FilteredList<MovieTableModel> filtredMovies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBox();
        initTableConfig();
        loadDataFromAPI();
        table.setItems(filtredMovies);

        yearChoice.setOnAction(event -> {
            loadDataFromAPI();
            table.setItems(filtredMovies);
        });

        keywordField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtredMovies.setPredicate(movieTableModel -> {
                if (newValue == null || newValue.equals("")) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (movieTableModel.getTitle().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (movieTableModel.getOverview().toLowerCase().contains(lowerCaseFilter)) return true;
                return false;
            });
        });

        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                MovieTableModel movie = table.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Text text = new Text(movie.toString());
                text.setWrappingWidth(400);
                alert.getDialogPane().setContent(text);
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> addMovie(movie));
            }
        });
    }

    private void loadDataFromAPI(){
        String resourceUrl = "https://api.themoviedb.org/3/discover/movie?api_key=7dd49a6da2d9bfb5ee7b370c9ee02139";
        String completeResourceUrl = resourceUrl + "&primary_release_year=" + yearChoice.getValue().toString();
        ResponseEntity<String> response = cookieRestTemplate.getForEntity(completeResourceUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode results = root.get("results");
            ArrayList<MovieTableModel> moviesFromJson = new ArrayList<>();
            results.forEach(jsonNode -> {
                int id = jsonNode.get("id").asInt();
                String title = jsonNode.get("original_title").asText();
                String imagePath = new StringBuilder()
                        .append("http://image.tmdb.org/t/p/w154") // IMAGE BASE URL
                        .append(jsonNode.get("poster_path").asText()).toString();
                String releaseDate = jsonNode.get("release_date").asText();
                String overview = jsonNode.get("overview").asText();
                moviesFromJson.add(new MovieTableModel(id, title, overview, releaseDate, imagePath));
            });

            movies = FXCollections.observableArrayList(moviesFromJson);
            filtredMovies = new FilteredList<>(movies, movieTableModel -> true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        imageCol.setCellValueFactory(new PropertyValueFactory<MovieTableModel, ImageView>("posterImage"));
        titleCol.setCellValueFactory(new PropertyValueFactory<MovieTableModel, String>("title"));
        overviewCol.setCellValueFactory(new PropertyValueFactory<MovieTableModel, Label>("overviewLabel"));
        dateCol.setCellValueFactory(new PropertyValueFactory<MovieTableModel, String>("releaseDate"));
    }

    private void initChoiceBox(){
        yearChoice.getItems().addAll("2018", "2017", "2016", "2015", "2014");
        yearChoice.getSelectionModel().selectFirst();

    }

    private void addMovie(MovieTableModel movie){
        String url = ServerInfo.MOVIE_ENDPOINT + "/add";
        Movie request = new Movie(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getReleaseDate(), movie.getPosterPath());
        ResponseEntity<Movie> response = cookieRestTemplate.postForEntity(url, request, Movie.class);
        System.out.println(response);

        //TODO zmienic tak, aby uzywac kodu 201 Created i wyswietlic wiadomosc ze udalo sie stworzyc, lub inny kiedy sie nie udalo
    }

}
