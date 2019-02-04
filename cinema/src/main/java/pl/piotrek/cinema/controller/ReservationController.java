package pl.piotrek.cinema.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.ReservationModel;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.model.fx.ReservationFx;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

@Controller
public class ReservationController implements Initializable {

    private User user;
    private ReservationModel model;

    public ReservationController(User user, ReservationModel model) {
        this.user = user;
        this.model = model;
    }

    @FXML
    private AnchorPane headerPane;

    @FXML
    private TableColumn<ReservationFx, String> titleCol;
    @FXML
    private TableColumn<ReservationFx, LocalDate> dateCol;
    @FXML
    private TableColumn<ReservationFx, LocalTime> timeCol;
    @FXML
    private TableColumn<ReservationFx, String> auditoriumCol;
    @FXML
    private TableColumn<ReservationFx, String> seatsCol;
    @FXML
    private TableView<ReservationFx> table;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        table.setItems(model.getReservationFxObservableList());
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
