package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.view.StageManager;
import pl.piotrek.cinema.view.ViewList;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class DashboardController implements Initializable {
    private User user;

    @Autowired
    public DashboardController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane content;

    @Lazy
    @Autowired
    StageManager stageManager;

    @FXML
    private JFXButton admin_homeBtn;

    @FXML
    private JFXButton accountsBtn;

    @FXML
    private JFXButton importBtn;

    @FXML
    private JFXButton manageBtn;

    @FXML
    private JFXButton auditoriumBtn;

    @FXML
    private JFXButton usr_homeBtn;

    @FXML
    private JFXButton reservationBtn;

    @FXML
    private JFXButton seancesBtn;


    @FXML
    private AnchorPane userPane;

    @FXML
    private AnchorPane adminPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(user.getRole().toLowerCase().equals("admin")){
            userPane.setVisible(false);
            adminPane.setVisible(true);
            stageManager.loadViewOnPane(content, ViewList.ADMIN_HOME);
        } else{
            userPane.setVisible(true);
            adminPane.setVisible(false);
            stageManager.loadViewOnPane(content, ViewList.USER_HOME);
        }

        initUserInfo();
        bindMenuActions();
    }

    private void initUserInfo(){
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getRole());
    }

    private void bindMenuActions(){
        admin_homeBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.ADMIN_HOME));
        importBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.IMPORT_MOVIES));
        manageBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.MANAGE_SEANCES));
        auditoriumBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.AUDITORIUM));
        accountsBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.MANAGE_ACCOUNTS));

        usr_homeBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.USER_HOME));
        reservationBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.RESERVATION));
        seancesBtn.setOnAction(event -> stageManager.loadViewOnPane(content, ViewList.SEANCES));
    }
}
