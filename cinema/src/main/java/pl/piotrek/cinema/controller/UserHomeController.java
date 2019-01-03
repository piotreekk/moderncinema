package pl.piotrek.cinema.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.model.Movie;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class UserHomeController implements Initializable {

    private CookieRestTemplate cookieRestTemplate;

    @Autowired
    public UserHomeController(CookieRestTemplate cookieRestTemplate){
        this.cookieRestTemplate = cookieRestTemplate;
    }

    @FXML
    private AnchorPane headerPane;

    @FXML
    private ScrollPane contentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initContent();
    }

    private void initContent(){
        String url = "http://localhost:8080/movie/get/all";
        ResponseEntity<List<Movie> > response = cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Movie>>(){});
        if(response.getStatusCode() != HttpStatus.OK) return;

        GridPane grid = new GridPane();

        int rows = (int)Math.ceil(response.getBody().size()/5d);
        for(int i=0; i<rows; ++i) grid.addRow(i);
        for(int i=0; i<5; ++i) grid.addColumn(i);

        int n = 0;
        for(Movie m : response.getBody()) {
            VBox card = new VBox();
            Label label = new Label(m.getTitle());
            label.setWrapText(true);
            label.setMaxWidth(100);
            ImageView image = new ImageView(m.getPosterPath());
            card.getChildren().addAll(image, label);
            grid.setMargin(card, new Insets(10, 5, 0, 5));
            int row = rows%3;
            if(row == 0) grid.addRow(row);

            grid.add(card, n%5, n/5);
            n++;
        }

        contentPane.setContent(grid);
        contentPane.setFitToWidth(true);
        contentPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        AnchorPane.setTopAnchor(contentPane, 20.0);
        AnchorPane.setRightAnchor(contentPane, .0);
        AnchorPane.setLeftAnchor(contentPane, .0);
        AnchorPane.setBottomAnchor(contentPane, .0);

    }



}
