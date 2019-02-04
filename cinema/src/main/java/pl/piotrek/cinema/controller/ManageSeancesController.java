package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.SeanceModelAdmin;
import pl.piotrek.cinema.model.fx.SeanceFx;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class ManageSeancesController implements Initializable {
    private SeanceModelAdmin model;
    private CookieRestTemplate cookieRestTemplate;

    public ManageSeancesController(SeanceModelAdmin model, CookieRestTemplate cookieRestTemplate) {
        this.model = model;
        this.cookieRestTemplate = cookieRestTemplate;
    }

    @FXML
    private TableView<SeanceFx> table;
    @FXML
    private TableColumn<SeanceFx, LocalDate> dateCol;
    @FXML
    private TableColumn<SeanceFx, LocalTime> timeCol;
    @FXML
    private TableColumn<SeanceFx, String> movieCol;
    @FXML
    private TableColumn<SeanceFx, String> freeSeatsCol;
    @FXML
    private TableColumn<SeanceFx, String> allSeatsCol;
    @FXML
    private JFXDatePicker dateInput;
    @FXML
    private ChoiceBox<MovieDTO> movieChoice;
    @FXML
    private ChoiceBox<AuditoriumDTO> auditoriumChoice;

    @FXML
    private JFXButton addSeanceBtn;
    @FXML
    private JFXTimePicker timeInput;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        initMoviesChoice();
        initAuditoriumChoice();

        model.loadDataFromAPI();
        table.setItems(model.getSeanceFxObservableList());

        addSeanceBtn.setOnAction(event -> {
            SeanceForm seance = prepareSeance();
            model.persistSeance(seance);
        });
    }


    private SeanceForm prepareSeance(){
        MovieDTO movieDTO = movieChoice.getSelectionModel().getSelectedItem();
        LocalDate date = dateInput.getValue();
        LocalTime time = timeInput.getValue();
        AuditoriumDTO auditoriumDTO = auditoriumChoice.getSelectionModel().getSelectedItem();

        SeanceForm seance = new SeanceForm();
        seance.setMovieId(movieDTO.getId());
        seance.setAuditoriumId(auditoriumDTO.getId());
        seance.setDate(date);
        seance.setStartTime(time);

        return seance;
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        freeSeatsCol.setCellValueFactory(new PropertyValueFactory<>("freeSeatsCount"));
        allSeatsCol.setCellValueFactory(new PropertyValueFactory<>("allSeatsCount"));
    }

    private void initMoviesChoice(){
        String url = ServerInfo.MOVIE_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<MovieDTO> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<MovieDTO> >(){});

        if(response.getStatusCode() == HttpStatus.OK){
            ArrayList<MovieDTO> moviesList = response.getBody();

            movieChoice.getItems().addAll(moviesList);
            movieChoice.setConverter(new StringConverter<MovieDTO>() {
                @Override
                public String toString(MovieDTO movieDTO) {
                    return movieDTO.getTitle();
                }

                @Override
                public MovieDTO fromString(String string) {
                    return null;
                }
            });
        }
    }

    private void initAuditoriumChoice(){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<AuditoriumDTO> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<AuditoriumDTO> >(){});

        if(response.getStatusCode() == HttpStatus.OK){
            ArrayList<AuditoriumDTO> auditoriesList = response.getBody();
            auditoriumChoice.getItems().addAll(auditoriesList);
            auditoriumChoice.setConverter(new StringConverter<AuditoriumDTO>() {
                @Override
                public String toString(AuditoriumDTO auditoriumDTO) {
                    return auditoriumDTO.getName();
                }

                @Override
                public AuditoriumDTO fromString(String string) {
                    return null;
                }
            });
        }
    }
}
