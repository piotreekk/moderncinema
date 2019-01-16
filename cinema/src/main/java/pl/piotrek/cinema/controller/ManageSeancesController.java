package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
import org.springframework.web.client.RestClientException;
import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.api.dto.MovieDTO;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.model.SeanceTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class ManageSeancesController implements Initializable {
    private CookieRestTemplate cookieRestTemplate;

    public ManageSeancesController(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    @FXML
    private TableView<SeanceTableModel> table;

    @FXML
    private TableColumn<SeanceTableModel, LocalDate> dateCol;

    @FXML
    private TableColumn<SeanceTableModel, LocalTime> timeCol;

    @FXML
    private TableColumn<SeanceTableModel, String> movieCol;

    @FXML
    private TableColumn<SeanceTableModel, String> freeSeatsCol;

    @FXML
    private TableColumn<SeanceTableModel, String> allSeatsCol;

    @FXML
    private JFXDatePicker dateInput;

    @FXML
    private ChoiceBox<MovieDTO> movieChoice;

    @FXML
    private ChoiceBox<AuditoriumDTO> auditoriumChoice;

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
            SeanceForm seance = prepareSeance();
            persistSeance(seance);
        });
    }

    private void loadDataFromAPI(){
        String url = ServerInfo.SEANCE_ENDPOINT + "/get/all";

        ResponseEntity<ArrayList<SeanceDTO> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<SeanceDTO>>(){});

        ArrayList<SeanceDTO> responseList = response.getBody();
        ArrayList<SeanceTableModel> seancesArrayList = new ArrayList<>();
        for(SeanceDTO s : responseList)
            seancesArrayList.add(new SeanceTableModel(s));

        seances = FXCollections.observableArrayList(seancesArrayList);
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

    private void persistSeance(SeanceForm seance){
        // TODO
        // Przed dodaniem seansu trzeba bedzie sprawdzic czy w danej sali nie odbywał się od 3h inny seans. Nie mam
        // w api bezposredniego pola o trwaniu seansu, wiec trzeba zrobic dluzsza przerwe
        String url = ServerInfo.SEANCE_ENDPOINT + "/add";
        try {
            ResponseEntity<SeanceDTO> response = cookieRestTemplate.postForEntity(url, seance, SeanceDTO.class);

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                SeanceDTO newSeanceDTO = response.getBody();
                seances.addAll(new SeanceTableModel(newSeanceDTO));
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
