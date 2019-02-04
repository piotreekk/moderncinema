package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.AuditoriumModel;
import pl.piotrek.cinema.model.fx.AuditoriumFx;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AuditoriumController implements Initializable{
    private AuditoriumModel model;

    public AuditoriumController(AuditoriumModel model) {
        this.model = model;
    }

    @FXML
    private TableView<AuditoriumFx> table;
    @FXML
    private TableColumn<AuditoriumFx, String> nameCol;
    @FXML
    private TableColumn<AuditoriumFx, Integer> rowsCol;
    @FXML
    private TableColumn<AuditoriumFx, Integer> seatsCountCol;


    @FXML
    private JFXTextField nameInput;
    @FXML
    private JFXTextField rowsInput;
    @FXML
    private JFXTextField colsInput;

    @FXML
    private JFXButton button;

    private ContextMenu menu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.loadDataFromAPI();

        Bindings.bindBidirectional(nameInput.textProperty(), this.model.auditoriumFxObjectPropertyProperty().get().nameProperty());
        Bindings.bindBidirectional(rowsInput.textProperty(), this.model.auditoriumFxObjectPropertyProperty().get().rowsProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(colsInput.textProperty(), this.model.auditoriumFxObjectPropertyProperty().get().colsProperty(), new NumberStringConverter());

        initTableConfig();
        table.setItems(model.getAuditoriumFxObservableList());
        button.setOnAction(event -> addAuditorium());
    }

    // Chciałem zobaczyc jak działają bindingi w javie, dlatego w dodawaniu sali nie tworze nowego obiektu i nie ładuje do niego wszystkich pól
    // po kolei, tylko pobieram obiekt z modelu do którego zbindowane są pola
    private void addAuditorium(){
        AuditoriumFx auditoriumFx = model.getAuditoriumFxObjectProperty();
        model.addAuditorium(auditoriumFx);
    }

    private void updateAuditorium(AuditoriumFx auditoriumFx){
        GridPane pane = new GridPane();
        JFXTextField name = new JFXTextField();
        JFXTextField rows = new JFXTextField();
        JFXTextField cols = new JFXTextField();

        name.setText(auditoriumFx.getName());
        rows.setText(Integer.toString(auditoriumFx.getRows()));
        cols.setText(Integer.toString(auditoriumFx.getCols()));

        pane.addRow(0, name);
        pane.addRow(1, rows);
        pane.addRow(2, cols);

        Alert updateDialog = new Alert(Alert.AlertType.CONFIRMATION);
        updateDialog.getDialogPane().setContent(pane);
        updateDialog.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(respone -> {
                    auditoriumFx.setName(name.getText());
                    auditoriumFx.setRows(Integer.valueOf(rows.getText()));
                    auditoriumFx.setCols(Integer.valueOf(cols.getText()));

                    model.updateAuditorium(auditoriumFx);
                });
    }


    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setTooltip(new Tooltip("Right click on row to show context menu"));
        table.setRowFactory( tv -> {
            TableRow<AuditoriumFx> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.SECONDARY && !row.isEmpty()){
                    AuditoriumFx rowData = row.getItem();
                    showContextMenu(event.getScreenX(), event.getScreenY(), rowData);
                }
            });
            return row ;
        });

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rowsCol.setCellValueFactory(new PropertyValueFactory<>("rows"));
        seatsCountCol.setCellValueFactory(new PropertyValueFactory<>("cols"));
    }

    private void showContextMenu(double x_pos, double y_pos, AuditoriumFx auditoriumFx){
        if(menu != null && menu.isShowing()) menu.hide();
        MenuItem mi1 = new MenuItem("Update");
        mi1.setOnAction(event1 -> updateAuditorium(auditoriumFx));
        menu = new ContextMenu(mi1);
        menu.show(table, x_pos, y_pos);
    }
}
