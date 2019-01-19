package pl.piotrek.cinema.controller;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.User;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NewEmployeeController implements Initializable {
    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField password;

    @FXML
    private FontAwesomeIconView warning0;

    @FXML
    private FontAwesomeIconView warning1;

    @FXML
    private FontAwesomeIconView warning2;

    @FXML
    private FontAwesomeIconView warning3;

    private boolean validationResult = false;
    private boolean fieldsNotEmpty = false;

    @FXML
    void register(){
        UserDTO user = new UserDTO();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        user.setPassword(password.getText());
        user.setRole("admin");
        user.setEnabled(true);

        fieldsNotEmpty = !firstName.getText().isEmpty() && !lastName.getText().isEmpty() && !email.getText().isEmpty()
                && !password.getText().isEmpty();

        if(validationResult == false || fieldsNotEmpty == false){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setContentText("Walidacja nie przebiegła pomyslnie!");
            alert.showAndWait();
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = ServerInfo.USER_ENDPOINT + "/adduser";

        HttpEntity<UserDTO> request = new HttpEntity<>(user);
        User addedUser = restTemplate.postForObject(url, request, User.class);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText(addedUser.toString());
        alert.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validateFields();

    }

    private class Patterns{
        public static final String NAME = "[a-zA-Z]+";
        public static final String EMAIL = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";

        // 8 znaków, minimum 1 litera i 1 cyfra
        public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    };

    private void validateFields(){
        firstName.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!firstName.getText().matches(Patterns.NAME)){
                    firstName.setStyle("-fx-text-fill: red");
                    validationResult = false;
                    warning0.setVisible(true);
                } else{
                    validationResult = true;
                    firstName.setStyle("-fx-text-fill: white");
                    warning0.setVisible(false);
                }
            }
        });

        lastName.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!lastName.getText().matches(Patterns.NAME)){
                    lastName.setStyle("-fx-text-fill: red");
                    validationResult = false;
                    warning1.setVisible(true);
                } else{
                    validationResult = true;
                    lastName.setStyle("-fx-text-fill: white");
                    warning1.setVisible(false);
                }
            }
        });

        email.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!email.getText().matches(Patterns.EMAIL)){
                    email.setStyle("-fx-text-fill: red");
                    validationResult = false;
                    warning2.setVisible(true);
                } else{
                    validationResult = true;
                    email.setStyle("-fx-text-fill: white");
                    warning2.setVisible(false);
                }
            }
        });

        password.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!password.getText().matches(Patterns.PASSWORD)){
                    password.setStyle("-fx-text-fill: red");
                    validationResult = false;
                    warning3.setVisible(true);
                } else{
                    validationResult = true;
                    password.setStyle("-fx-text-fill: white");
                    warning3.setVisible(false);
                }
            }
        });
    }

}
