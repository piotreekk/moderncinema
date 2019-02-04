package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.SpringFXMLLoader;
import pl.piotrek.cinema.model.SeanceModelUser;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.model.fx.SeanceFx;
import pl.piotrek.cinema.view.ViewList;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class SeancesController implements Initializable {
    private User user;
    private SpringFXMLLoader springFXMLLoader;
    private ChooseSeatsController chooseSeatsController;

    private SeanceModelUser model;

    @Autowired
    public SeancesController(User user, SpringFXMLLoader springFXMLLoader, ChooseSeatsController chooseSeatsController, SeanceModelUser model) {
        this.user = user;
        this.springFXMLLoader = springFXMLLoader;
        this.chooseSeatsController = chooseSeatsController;
        this.model = model;
    }

    @FXML
    private TableView<SeanceFx> table;
    @FXML
    private TableColumn<SeanceFx, ImageView> imageCol;
    @FXML
    private TableColumn<SeanceFx, String> movieCol;
    @FXML
    private TableColumn<SeanceFx, LocalTime> timeCol;
    @FXML
    private TableColumn<SeanceFx, String> allSeatsCol;
    @FXML
    private TableColumn<SeanceFx, String> freeSeatsCol;
    @FXML
    private JFXDatePicker dateInput;
    @FXML
    private Label choosenDateInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        dateInput.setValue(LocalDate.now());

        model.loadDataFromAPI(LocalDate.now());

        table.setItems(model.getSeanceFxObservableList());

        dateInput.setOnAction(event -> {
            choosenDateInput.setText(dateInput.getValue().toString());
            model.loadDataFromAPI(dateInput.getValue());
        });

        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                completeReservation();
            }
        });
    }

    private void completeReservation(){
        SeanceFx seanceFx = table.getSelectionModel().getSelectedItem();
        chooseSeatsController.setSeance(seanceFx.getId());
        List<Integer> choosenSeats;
        AnchorPane pane = new AnchorPane();
        try {
            Parent viewNode = springFXMLLoader.load(ViewList.CHOOSE_SEATS.getFxmlPath());
            pane.getChildren().setAll(viewNode);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.getDialogPane().setContent(pane);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() ==  ButtonType.OK){
                choosenSeats = chooseSeatsController.getChoosenSeats();
                Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Do you want send email with confirmation?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                Optional<ButtonType> choice = success.showAndWait();
                if(choice.isPresent()){
                    if(choice.get() == ButtonType.YES)
                        model.persistReservation(seanceFx, user.getId(), choosenSeats, true);
                    else if(choice.get() == ButtonType.NO)
                        model.persistReservation(seanceFx, user.getId(), choosenSeats, false);
                }

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

}
