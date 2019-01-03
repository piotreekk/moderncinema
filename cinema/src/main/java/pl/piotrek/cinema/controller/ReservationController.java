package pl.piotrek.cinema.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.ReservationForm;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.model.table.ReservationTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ReservationController implements Initializable {
    private CookieRestTemplate cookieRestTemplate;
    private User user;

    public ReservationController(CookieRestTemplate cookieRestTemplate, User user) {
        this.cookieRestTemplate = cookieRestTemplate;
        this.user = user;
    }

    @FXML
    private AnchorPane headerPane;

    @FXML
    private TableColumn titleCol;

    @FXML
    private TableColumn dateCol;

    @FXML
    private TableColumn timeCol;

    @FXML
    private TableColumn auditoriumCol;

    @FXML
    private TableColumn seatsCol;

    @FXML
    private TableView table;

    private ObservableList<ReservationTableModel> reservations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        initContent();
    }

    private void initContent(){
        String url = "http://localhost:8080/reservation/user/" + user.getId();
        ResponseEntity<List<String>> response = cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>(){});
        if(response.getStatusCode() != HttpStatus.OK) return;

        ArrayList<ReservationTableModel> list = new ArrayList<>();
        for(String rs : response.getBody()) {
            String [] details = rs.split(";");
            String title = details[0];
            LocalDate date = LocalDate.parse(details[1]);
            LocalTime startTime = LocalTime.parse(details[2]);
            String auditorium = details[3];
            String seatsString = details[4];

            list.add(new ReservationTableModel(title, date, startTime, auditorium, seatsString));
        }

        reservations = FXCollections.observableArrayList(list);
        table.setItems(reservations);

    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        titleCol.setCellValueFactory(new PropertyValueFactory<ReservationTableModel, String>("movieTitle"));
        dateCol.setCellValueFactory(new PropertyValueFactory<ReservationTableModel, LocalDate>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<ReservationTableModel, LocalTime>("startTime"));
        auditoriumCol.setCellValueFactory(new PropertyValueFactory<ReservationTableModel, String>("auditorium"));
        seatsCol.setCellValueFactory(new PropertyValueFactory<ReservationTableModel, String>("seats"));
    }

}
