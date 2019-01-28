package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.model.AuditoriumFx;
import pl.piotrek.cinema.model.AuditoriumModel;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class AuditoriumController implements Initializable{
    private CookieRestTemplate cookieRestTemplate;

    public AuditoriumController(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
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

    private AuditoriumModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = new AuditoriumModel();
        this.model.auditoriumFxObjectPropertyProperty().get().nameProperty().bind(this.nameInput.textProperty());
        Bindings.bindBidirectional(rowsInput.textProperty(), this.model.auditoriumFxObjectPropertyProperty().get().rowsProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(colsInput.textProperty(), this.model.auditoriumFxObjectPropertyProperty().get().colsProperty(), new NumberStringConverter());

        initTableConfig();
        table.setItems(model.getAuditoriumFxObservableList());
        loadDataFromAPI();
        button.setOnAction(event -> addAuditorium());
    }

    private void loadDataFromAPI(){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<AuditoriumDTO>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<AuditoriumDTO>>(){});
        ArrayList<AuditoriumDTO> responseList = response.getBody();

        for(AuditoriumDTO a : responseList){
            AuditoriumFx auditoriumFx = new AuditoriumFx();
            auditoriumFx.setName(a.getName());
            auditoriumFx.setRows(a.getRows());
            auditoriumFx.setCols(a.getCols());
            model.addAuditoriumToList(auditoriumFx);
        }
    }

    private void addAuditorium(){
        AuditoriumFx auditoriumFx = model.getAuditoriumFxObjectProperty();
        String name = auditoriumFx.getName();
        Integer rows = auditoriumFx.getRows();
        Integer cols = auditoriumFx.getCols();

        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/add";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name", name);
        map.add("rows", rows.toString());
        map.add("cols", cols.toString());
        ResponseEntity<String> response = cookieRestTemplate.postForEntity(url, map, String.class);
        if(response.getStatusCode() == HttpStatus.CREATED){
            model.addAuditoriumToList(auditoriumFx);
        }
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
                    AuditoriumDTO auditoriumDTO = new AuditoriumDTO();
                    auditoriumDTO.setName(name.getText());
                    auditoriumDTO.setRows(Integer.valueOf(rows.getText()));
                    auditoriumDTO.setCols(Integer.valueOf(cols.getText()));
                    performUpdate(auditoriumDTO);
                });
    }

    private void performUpdate(AuditoriumDTO auditoriumDTO){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/update/" + auditoriumDTO.getId() + "?name={name}&rows={rows}&cols={cols}";
        cookieRestTemplate.put(url, null, auditoriumDTO.getName(), auditoriumDTO.getRows(), auditoriumDTO.getCols());
        loadDataFromAPI();
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

        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
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
