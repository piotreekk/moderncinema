package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.config.SpringFXMLLoader;
import pl.piotrek.cinema.model.ReservationForm;
import pl.piotrek.cinema.model.Seance;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.model.table.SeanceTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;
import pl.piotrek.cinema.view.ViewList;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class SeancesController implements Initializable {
    private User user;
    private CookieRestTemplate cookieRestTemplate;
    private SpringFXMLLoader springFXMLLoader;
    private ChooseSeatsController chooseSeatsController;

    @Autowired
    public SeancesController(User user, CookieRestTemplate cookieRestTemplate, SpringFXMLLoader springFXMLLoader, ChooseSeatsController chooseSeatsController) {
        this.user = user;
        this.cookieRestTemplate = cookieRestTemplate;
        this.springFXMLLoader = springFXMLLoader;
        this.chooseSeatsController = chooseSeatsController;
    }

    @FXML
    private TableView<SeanceTableModel> table;

    @FXML
    private TableColumn<SeanceTableModel, ImageView> imageCol;

    @FXML
    private TableColumn<SeanceTableModel, String> movieCol;

    @FXML
    private TableColumn<SeanceTableModel, LocalTime> timeCol;

    @FXML
    private TableColumn<SeanceTableModel, String> allSeatsCol;

    @FXML
    private TableColumn<SeanceTableModel, String> freeSeatsCol;

    @FXML
    private JFXDatePicker dateInput;

    @FXML
    private Label choosenDateInput;

    private ObservableList<SeanceTableModel> seances;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        dateInput.setValue(LocalDate.now());
        loadDataFromAPI(LocalDate.now());
        table.setItems(seances);

        dateInput.setOnAction(event -> {
            choosenDateInput.setText(dateInput.getValue().toString());
            loadDataFromAPI(dateInput.getValue());
            table.setItems(seances);
        });

        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                completeReservation();
            }
        });
    }

    private void persistReservation(Seance seance, List<Integer> choosenSeats){
        String url = ServerInfo.RESERVATION_ENDPOINT + "/add";
        ReservationForm reservation = new ReservationForm();
        reservation.setSeanceId(seance.getId());
        reservation.setSeats(choosenSeats);
        reservation.setUserId(user.getId());
        cookieRestTemplate.postForEntity(url, reservation, ReservationForm.class);
    }

    private void completeReservation(){
        Seance seance = table.getSelectionModel().getSelectedItem().getSeance();
        List<Integer> choosenSeats;
        AnchorPane pane = new AnchorPane();
        try {
            Parent viewNode = springFXMLLoader.load(ViewList.CHOOSE_SEATS.getFxmlPath());
            pane.getChildren().setAll(viewNode);

            chooseSeatsController.init(seance);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.getDialogPane().setContent(pane);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() ==  ButtonType.OK){
                choosenSeats = chooseSeatsController.getChoosenSeats();
                persistReservation(seance, choosenSeats);
            } else{
                Alert failed = new Alert(Alert.AlertType.ERROR);
                failed.setTitle("Errrororor");
                failed.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        imageCol.setCellValueFactory(new PropertyValueFactory<>("posterImage"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        freeSeatsCol.setCellValueFactory(new PropertyValueFactory<>("freeSeatsCount"));
        allSeatsCol.setCellValueFactory(new PropertyValueFactory<>("allSeatsCount"));
    }

    private void loadDataFromAPI(LocalDate date){
        String url = ServerInfo.SEANCE_ENDPOINT + "/get/bydate/{date}";
        ResponseEntity<ArrayList<Seance>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Seance>>(){}, date);

        ArrayList<Seance> responseList = response.getBody();
        ArrayList<SeanceTableModel> seancesArrayList = new ArrayList<>();

        for(Seance s : responseList)
            seancesArrayList.add(new SeanceTableModel(s));

        seances = FXCollections.observableArrayList(seancesArrayList);
    }

}
