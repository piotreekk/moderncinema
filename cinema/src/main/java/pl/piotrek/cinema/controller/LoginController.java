package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.User;
import pl.piotrek.cinema.util.CookieRestTemplate;
import pl.piotrek.cinema.view.StageManager;
import pl.piotrek.cinema.view.ViewList;

@Controller
public class LoginController {

    @Autowired
    @Lazy
    // StageManager będzie dostępny dopiero po wywołaniu metody start w mainie. Autowiring odbywa się już w inicie,
    // kiedy to nie wiemy jeszcze o istnieniu StageManagera
    private StageManager stageManager;
    private CookieRestTemplate cookieRestTemplate;
    private User user;

    public LoginController(CookieRestTemplate cookieRestTemplate, User user) {
        this.cookieRestTemplate = cookieRestTemplate;
        this.user = user;
    }

    @FXML private JFXTextField loginField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton loginButton;
    @FXML private JFXButton registerButton;
    @FXML private Label message;

    @FXML
    public void login(){
        // Wysyłam POST do serwera, aby uzyskać ciasteczko z danymi logowania
        String username = loginField.getText();
        String password = passwordField.getText();

        RestTemplate restTemplate = new RestTemplate();
        String url = ServerInfo.BASE_URL + "/login";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        ResponseEntity<String> response = restTemplate.postForEntity(url, map, String.class);
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Dołączam ciasteczko do swojego REST Clienta ( Singleton )
        cookieRestTemplate.setCookie(cookie);

        try {
            UserDTO loggedUser = cookieRestTemplate.postForObject(ServerInfo.USER_ENDPOINT + "/get", new HttpEntity<>(username), UserDTO.class);
            user.setId(loggedUser.getId());
            user.setFirstName(loggedUser.getFirstName());
            user.setLastName(loggedUser.getLastName());
            user.setEmail(loggedUser.getEmail());
            user.setPassword(loggedUser.getPassword());
            user.setRole(loggedUser.getRole());

            stageManager.switchScene(ViewList.DASHBOARD);

        } catch(HttpClientErrorException.Unauthorized e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setResizable(true);
            alert.setTitle("Access Denied!");
            alert.setHeaderText("Authorization failed.");

            Label alertMessage = new Label("Login and password don't match. Try another, or sign up.");
            alertMessage.setWrapText(true);

            alert.getDialogPane().setContent(alertMessage);
            alert.showAndWait();

            clearFields();

        } catch(HttpClientErrorException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Access Denied!");
            alert.setHeaderText("Authorization failed.");

            Label alertMessage = new Label("Unknown error occured. Please contact with administrator or try again soon.");
            alertMessage.setText(alertMessage.getText() + "/n" + e.getResponseBodyAsString());
            alertMessage.setWrapText(true);

            alert.getDialogPane().setContent(alertMessage);
            alert.showAndWait();

            clearFields();
        }
    }

    private void clearFields(){
        loginField.clear();
        passwordField.clear();
    }

    public void register(){
        stageManager.switchScene(ViewList.REGISTER);
    }
}
