package pl.piotrek.cinema.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.User;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AdminHomeController implements Initializable{
    private User user;

    @FXML
    private Label userLabel;

    @Autowired
    public AdminHomeController(User user){
        this.user = user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLabel.setText(user.getFirstName() + " " + user.getLastName());
    }
}
