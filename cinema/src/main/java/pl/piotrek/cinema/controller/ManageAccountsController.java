package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.SpringFXMLLoader;
import pl.piotrek.cinema.model.UserModel;
import pl.piotrek.cinema.model.fx.UserFx;
import pl.piotrek.cinema.view.ViewList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ManageAccountsController implements Initializable {
    private SpringFXMLLoader springFXMLLoader;
    private UserModel model;

    @Autowired
    public ManageAccountsController(SpringFXMLLoader springFXMLLoader, UserModel model) {
        this.springFXMLLoader = springFXMLLoader;
        this.model = model;
    }

    @FXML
    private TableView<UserFx> table;
    @FXML
    private TableColumn<UserFx, String> fnameCol;
    @FXML
    private TableColumn<UserFx, String> lnameCol;
    @FXML
    private TableColumn<UserFx, String> emailCol;
    @FXML
    private TableColumn<UserModel, Boolean> isActiveCol;

    @FXML
    private JFXButton addEmployeeBtn;

    @FXML
    private JFXCheckBox showDisabled_check;

    private ContextMenu menu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableConfig();
        model.loadDataFromAPI();

        table.setItems(model.getUserFxFilteredList());

        showDisabled_check.setOnAction(event -> {
            if(showDisabled_check.isSelected())
                model.showDisabledAccounts(true);
            else
                model.showDisabledAccounts(false);
        });

        addEmployeeBtn.setOnAction(event -> newEmployee());
    }

    private void initTableConfig(){
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setTooltip(new Tooltip("Right click to show context menu"));
        table.setRowFactory( tv -> {
            TableRow<UserFx> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.SECONDARY && !row.isEmpty()){
                    UserFx rowData = row.getItem();
                    showContextMenu(event.getScreenX(), event.getScreenY(), rowData);
                }
            });
            return row ;
        });

        fnameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lnameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        isActiveCol.setCellValueFactory(new PropertyValueFactory<>("active"));
    }


    private void showContextMenu(double x_pos, double y_pos, UserFx userFx){
        if(menu != null && menu.isShowing()) menu.hide();
        MenuItem mi1 = new MenuItem("Update");
        MenuItem mi2 = new MenuItem("Delete");

        mi2.setOnAction(event -> model.deleteEmployee(userFx));
        menu = new ContextMenu(mi1, mi2);
        menu.show(table, x_pos, y_pos);
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
                    .ifPresent(response -> model.loadDataFromAPI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
