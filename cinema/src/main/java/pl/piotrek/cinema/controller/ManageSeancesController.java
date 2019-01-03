package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import pl.piotrek.cinema.model.Auditorium;
import pl.piotrek.cinema.model.Movie;
import pl.piotrek.cinema.model.Seance;
import pl.piotrek.cinema.model.SeanceForm;
import pl.piotrek.cinema.model.table.SeanceTableModel;
import pl.piotrek.cinema.model.table.SeanceTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ManageSeancesController implements Initializable {

    @Autowired
    CookieRestTemplate cookieRestTemplate;

    @FXML
    private TableView table;

    @FXML
    private TableColumn dateCol;

    @FXML
    private TableColumn timeCol;

    @FXML
    private TableColumn movieCol;

    @FXML
    private TableColumn freeSeatsCol;

    @FXML
    private TableColumn allSeatsCol;

    @FXML
    private JFXDatePicker dateInput;

    @FXML
    private ChoiceBox<Movie> movieChoice;


    @FXML
    private ChoiceBox<Auditorium> auditoriumChoice;


    @FXML
    private JFXTextField seatsInput;

    @FXML
    private JFXButton addSeanceBtn;

    @FXML
    private JFXTimePicker timeInput;

    private ObservableList<SeanceTableModel> seances;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        loadDataFromAPI();
        initMoviesChoice();
        initAuditoriumChoice();
        table.setItems(seances);

        addSeanceBtn.setOnAction(event -> {
            Movie movie = movieChoice.getSelectionModel().getSelectedItem();
            LocalDate date = dateInput.getValue();
            LocalTime time = timeInput.getValue();
            Auditorium auditorium = auditoriumChoice.getSelectionModel().getSelectedItem();

            SeanceForm seance = new SeanceForm();
            seance.setMovieId(movie.getId());
            seance.setAuditoriumId(auditorium.getId());
            seance.setDate(date);
            seance.setStartTime(time);

            // TODO
            // Przed dodaniem seansu trzeba bedzie sprawdzic czy w danej sali nie odbywał się od 3h inny seans. Nie mam
            // w api bezposredniego pola o trwaniu seansu, wiec trzeba zrobic dluzsza przerwe
            String url = "http://localhost:8080/seance/add";
            try {
                ResponseEntity<Seance> response = cookieRestTemplate.postForEntity(url, seance, Seance.class);

                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    Seance newSeance = response.getBody();
                    seances.addAll(new SeanceTableModel(newSeance));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Error occured while adding new senace!");
                    alert.showAndWait();

                }
            } catch (RestClientException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Server not responding!");
                alert.showAndWait();
            }

        });
    }

    private void loadDataFromAPI(){
        String url = "http://localhost:8080/seance/get/all";

        ResponseEntity<ArrayList<Seance> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<Seance>>(){});

        ArrayList<Seance> responseList = response.getBody();
        ArrayList<SeanceTableModel> seancesArrayList = new ArrayList<>();
        for(Seance s : responseList)
            seancesArrayList.add(new SeanceTableModel(s));

        seances = FXCollections.observableArrayList(seancesArrayList);
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dateCol.setCellValueFactory(new PropertyValueFactory<SeanceTableModel, LocalDate>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<SeanceTableModel, LocalTime>("startTime"));
        movieCol.setCellValueFactory(new PropertyValueFactory<SeanceTableModel, String>("movieTitle"));
        freeSeatsCol.setCellValueFactory(new PropertyValueFactory<SeanceTableModel, String>("freeSeatsCount"));
        allSeatsCol.setCellValueFactory(new PropertyValueFactory<SeanceTableModel, String>("allSeatsCount"));
    }

    private void initMoviesChoice(){
        String url = "http://localhost:8080/movie/get/all";
        ResponseEntity<ArrayList<Movie> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Movie> >(){});

        if(response.getStatusCode() == HttpStatus.OK){
            ArrayList<Movie> moviesList = response.getBody();

            movieChoice.getItems().addAll(moviesList);
            movieChoice.setConverter(new StringConverter<Movie>() {
                @Override
                public String toString(Movie movie) {
                    return movie.getTitle();
                }

                @Override
                public Movie fromString(String string) {
                    return null;
                }
            });
        }
    }

    private void initAuditoriumChoice(){
        String url = "http://localhost:8080/auditorium/get/all";
        ResponseEntity<ArrayList<Auditorium> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Auditorium> >(){});

        if(response.getStatusCode() == HttpStatus.OK){
            ArrayList<Auditorium> auditoriesList = response.getBody();
            auditoriumChoice.getItems().addAll(auditoriesList);
            auditoriumChoice.setConverter(new StringConverter<Auditorium>() {
                @Override
                public String toString(Auditorium auditorium) {
                    return auditorium.getName();
                }

                @Override
                public Auditorium fromString(String string) {
                    return null;
                }
            });
        }
    }
}
