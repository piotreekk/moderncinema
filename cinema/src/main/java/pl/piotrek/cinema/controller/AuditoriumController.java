package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.Auditorium;
import pl.piotrek.cinema.model.table.AuditoriumTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class AuditoriumController implements Initializable{
    @Autowired
    CookieRestTemplate cookieRestTemplate;

    @FXML
    private TableColumn<AuditoriumTableModel, String> nameCol;

    @FXML
    private TableColumn<AuditoriumTableModel, Integer> rowsCol;

    @FXML
    private TableColumn<AuditoriumTableModel, Integer> seatsCountCol;


    @FXML
    private TableColumn<AuditoriumTableModel, MaterialDesignIconView> deleteCol;

    @FXML
    private TableColumn<AuditoriumTableModel, MaterialDesignIconView>  updateCol;

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

    ObservableList<AuditoriumTableModel> auditories;

    ContextMenu menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        loadDataFromAPI();
        table.setItems(auditories);
        button.setOnAction(event -> addAuditorium());
    }

    private void loadDataFromAPI(){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<Auditorium>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Auditorium>>(){});
        ArrayList<Auditorium> responseList = response.getBody();
        ArrayList<AuditoriumTableModel> auditoriesArrayList = new ArrayList<>();
        for(Auditorium a : responseList)
            auditoriesArrayList.add(new AuditoriumTableModel(a));

        auditories = FXCollections.observableArrayList(auditoriesArrayList);
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
        ResponseEntity<String> stringResponseEntity = cookieRestTemplate.postForEntity(url, map, String.class);
    }

    private void updateAuditorium(Auditorium auditorium){
        GridPane pane = new GridPane();
        JFXTextField name = new JFXTextField();
        JFXTextField rows = new JFXTextField();
        JFXTextField cols = new JFXTextField();

        name.setText(auditorium.getName());
        rows.setText(auditorium.getRows().toString());
        cols.setText(auditorium.getCols().toString());
        pane.addRow(0, name);
        pane.addRow(1, rows);
        pane.addRow(2, cols);

        Alert updateDialog = new Alert(Alert.AlertType.CONFIRMATION);
        updateDialog.getDialogPane().setContent(pane);
        updateDialog.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(respone -> {
                    auditorium.setName(name.getText());
                    auditorium.setRows(Integer.valueOf(rows.getText()));
                    auditorium.setCols(Integer.valueOf(cols.getText()));
                    performUpdate(auditorium);
                });

    }

    private void performUpdate(Auditorium auditorium){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/update/" + auditorium.getId() + "?name={name}&rows={rows}&cols={cols}";
        cookieRestTemplate.put(url, null, auditorium.getName(), auditorium.getRows(), auditorium.getCols());
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setTooltip(new Tooltip("Right click on row to show context menu"));
        table.setRowFactory( tv -> {
            TableRow<AuditoriumTableModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.SECONDARY && !row.isEmpty()){
                    AuditoriumTableModel rowData = row.getItem();
                    showContextMenu(event.getScreenX(), event.getScreenY(), rowData.getAuditorium());
                }
            });
            return row ;
        });

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rowsCol.setCellValueFactory(new PropertyValueFactory<>("rows"));
        seatsCountCol.setCellValueFactory(new PropertyValueFactory<>("seatsCount"));
    }

    private void showContextMenu(double x_pos, double y_pos, Auditorium auditorium){
        if(menu != null && menu.isShowing()) menu.hide();
        MenuItem mi1 = new MenuItem("Update");
        mi1.setOnAction(event1 -> updateAuditorium(auditorium));
        menu = new ContextMenu(mi1);
        menu.show(table, x_pos, y_pos);
    }

}
