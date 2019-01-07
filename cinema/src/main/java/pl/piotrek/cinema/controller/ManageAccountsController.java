package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.config.SpringFXMLLoader;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.model.table.UserTableModel;
import pl.piotrek.cinema.util.CookieRestTemplate;
import pl.piotrek.cinema.view.ViewList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class ManageAccountsController implements Initializable {
    private CookieRestTemplate cookieRestTemplate;
    private SpringFXMLLoader springFXMLLoader;

    @Autowired
    public ManageAccountsController(CookieRestTemplate cookieRestTemplate, SpringFXMLLoader springFXMLLoader) {
        this.cookieRestTemplate = cookieRestTemplate;
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML
    private JFXCheckBox showDisabled_check;

    @FXML
    private TableView<UserTableModel> table;

    @FXML
    private TableColumn fnameCol;

    @FXML
    private TableColumn lnameCol;

    @FXML
    private TableColumn emailCol;

    @FXML
    private TableColumn isActiveCol;

    @FXML
    private AnchorPane headerPane;

    @FXML
    private JFXButton addEmployeeBtn;

    private ObservableList<UserTableModel> employees;

    private ContextMenu menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        loadDataFromAPI(false);
        showDisabled_check.setOnAction(event -> {
            if(showDisabled_check.isSelected())
                loadDataFromAPI(true);
            else
                loadDataFromAPI(false);
        });

        addEmployeeBtn.setOnAction(event -> newEmployee());

        FadeTransition fadeTransition = new FadeTransition();
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setTooltip(new Tooltip("Right click to show context menu"));
        table.setRowFactory( tv -> {
            TableRow<UserTableModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.SECONDARY && !row.isEmpty()){
                    UserTableModel rowData = row.getItem();
                    showContextMenu(event.getScreenX(), event.getScreenY(), rowData.getUser());
                }
            });
            return row ;
        });

        fnameCol.setCellValueFactory(new PropertyValueFactory<UserTableModel, String>("firstName"));
        lnameCol.setCellValueFactory(new PropertyValueFactory<UserTableModel, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<UserTableModel, String>("email"));
        isActiveCol.setCellValueFactory(new PropertyValueFactory<UserTableModel, Boolean>("active"));
    }


    private void showContextMenu(double x_pos, double y_pos, User user){
        if(menu != null && menu.isShowing()) menu.hide();
        MenuItem mi1 = new MenuItem("Update");
        MenuItem mi2 = new MenuItem("Delete");

        mi2.setOnAction(event -> deleteEmployee(user));
        menu = new ContextMenu(mi1, mi2);
        menu.show(table, x_pos, y_pos);
    }

    private void loadDataFromAPI(boolean shouldLoadDisabled){
        String url = ServerInfo.USER_ENDPOINT + "/get/admin";
        if(shouldLoadDisabled == false) url += "?active=true";

        ResponseEntity<ArrayList<User>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<User>>(){});

        ArrayList<User> responseList = response.getBody();
        ArrayList<UserTableModel> employeesArrayList = new ArrayList<>();
        for(User u : responseList)
            employeesArrayList.add(new UserTableModel(u));

        employees = FXCollections.observableArrayList(employeesArrayList);
        table.setItems(employees);
    }

    private void newEmployee(){
        AnchorPane pane = new AnchorPane();
        try {
            Parent viewNode = springFXMLLoader.load(ViewList.NEW_EMPLOYEE.getFxmlPath());
            pane.getChildren().setAll(viewNode);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.getDialogPane().setContent(pane);
            alert.showAndWait()
                    .filter(buttonType -> buttonType == ButtonType.OK)
                    .ifPresent(response -> loadDataFromAPI(showDisabled_check.isSelected()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee(User user){
        String url = ServerInfo.USER_ENDPOINT + "/delete/" + user.getId();
        cookieRestTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

}
