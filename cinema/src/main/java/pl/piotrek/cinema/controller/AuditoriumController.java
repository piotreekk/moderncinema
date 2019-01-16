package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.model.AuditoriumTableModel;
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
    private TableColumn<AuditoriumTableModel, String> nameCol;

    @FXML
    private TableColumn<AuditoriumTableModel, Integer> rowsCol;

    @FXML
    private TableColumn<AuditoriumTableModel, Integer> seatsCountCol;

    @FXML
    private JFXTextField nameInput;

    @FXML
    private JFXTextField rowsInput;

    @FXML
    private JFXTextField colsInput;

    @FXML
    private JFXButton button;

    @FXML
    private TableView<AuditoriumTableModel> table;

    private ObservableList<AuditoriumTableModel> auditories;

    private ContextMenu menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        loadDataFromAPI();
        button.setOnAction(event -> addAuditorium());
    }

    private void loadDataFromAPI(){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<AuditoriumDTO>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<AuditoriumDTO>>(){});
        ArrayList<AuditoriumDTO> responseList = response.getBody();
        ArrayList<AuditoriumTableModel> auditoriesArrayList = new ArrayList<>();
        for(AuditoriumDTO a : responseList)
            auditoriesArrayList.add(new AuditoriumTableModel(a));

        auditories = FXCollections.observableArrayList(auditoriesArrayList);
        table.setItems(auditories);
    }

    private void addAuditorium(){
        String name = nameInput.getText();
        String rows = rowsInput.getText();
        String cols = colsInput.getText();

        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/add";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name", name);
        map.add("rows", rows);
        map.add("cols", cols);
        ResponseEntity<String> response = cookieRestTemplate.postForEntity(url, map, String.class);
        if(response.getStatusCode() == HttpStatus.CREATED){
            loadDataFromAPI();
        }
    }

    private void updateAuditorium(AuditoriumDTO auditoriumDTO){
        GridPane pane = new GridPane();
        JFXTextField name = new JFXTextField();
        JFXTextField rows = new JFXTextField();
        JFXTextField cols = new JFXTextField();

        name.setText(auditoriumDTO.getName());
        rows.setText(auditoriumDTO.getRows().toString());
        cols.setText(auditoriumDTO.getCols().toString());
        pane.addRow(0, name);
        pane.addRow(1, rows);
        pane.addRow(2, cols);

        Alert updateDialog = new Alert(Alert.AlertType.CONFIRMATION);
        updateDialog.getDialogPane().setContent(pane);
        updateDialog.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(respone -> {
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
            TableRow<AuditoriumTableModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.SECONDARY && !row.isEmpty()){
                    AuditoriumTableModel rowData = row.getItem();
                    showContextMenu(event.getScreenX(), event.getScreenY(), rowData.getAuditoriumDTO());
                }
            });
            return row ;
        });

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rowsCol.setCellValueFactory(new PropertyValueFactory<>("rows"));
        seatsCountCol.setCellValueFactory(new PropertyValueFactory<>("seatsCount"));
    }

    private void showContextMenu(double x_pos, double y_pos, AuditoriumDTO auditoriumDTO){
        if(menu != null && menu.isShowing()) menu.hide();
        MenuItem mi1 = new MenuItem("Update");
        mi1.setOnAction(event1 -> updateAuditorium(auditoriumDTO));
        menu = new ContextMenu(mi1);
        menu.show(table, x_pos, y_pos);
    }
}
