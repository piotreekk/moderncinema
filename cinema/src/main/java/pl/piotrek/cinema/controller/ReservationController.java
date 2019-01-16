package pl.piotrek.cinema.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.model.ReservationTableModel;
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
    private TableColumn<ReservationTableModel, String> titleCol;

    @FXML
    private TableColumn<ReservationTableModel, LocalDate> dateCol;

    @FXML
    private TableColumn<ReservationTableModel, LocalTime> timeCol;

    @FXML
    private TableColumn<ReservationTableModel, String> auditoriumCol;

    @FXML
    private TableColumn<ReservationTableModel, String> seatsCol;

    @FXML
    private TableView<ReservationTableModel> table;

    private ObservableList<ReservationTableModel> reservations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        initContent();
    }

    private void initContent(){
        String url = ServerInfo.RESERVATION_ENDPOINT + "/user/" + user.getId();
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
        titleCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        auditoriumCol.setCellValueFactory(new PropertyValueFactory<>("auditoriumDTO"));
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("seatDTOS"));
    }

}
