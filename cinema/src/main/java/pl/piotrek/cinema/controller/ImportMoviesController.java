package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.MovieModel;
import pl.piotrek.cinema.model.fx.MovieFx;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ImportMoviesController implements Initializable {
    private MovieModel model;

    public ImportMoviesController(MovieModel model) {
        this.model = model;
    }

    @FXML
    private TableView<MovieFx> table;
    @FXML
    private TableColumn<MovieFx, ImageView> imageCol;
    @FXML
    private TableColumn<MovieFx, String> titleCol;
    @FXML
    private TableColumn<MovieFx, Label> overviewCol;
    @FXML
    private TableColumn<MovieFx, String> dateCol;
    @FXML
    private ChoiceBox<String> yearChoice;
    @FXML
    private JFXTextField keywordField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBox();
        initTableConfig();
        table.setItems(model.getMovieFxFilteredList());

        model.loadDataFromAPI(yearChoice.getValue());

        yearChoice.setOnAction(event -> {
            model.loadDataFromAPI(yearChoice.getValue());
        });

        // listener do filtrowania danych
        keywordField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.filter(newValue);
        });

        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                MovieFx movie = table.getSelectionModel().getSelectedItem();
                addMovie(movie);
            }
        });
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        imageCol.setCellValueFactory(new PropertyValueFactory<>("posterImage"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        overviewCol.setCellValueFactory(new PropertyValueFactory<>("overviewLabel"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
    }

    private void initChoiceBox(){
        yearChoice.getItems().addAll("2018", "2017", "2016", "2015", "2014");
        yearChoice.getSelectionModel().selectFirst();

    }

    private void addMovie(MovieFx movie){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Text text = new Text("Czy chcesz dodac film " + movie.getTitle() + " do bazy filmów kina?");
        text.setWrappingWidth(400);
        alert.getDialogPane().setContent(text);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> model.addMovie(movie));

    }
}
