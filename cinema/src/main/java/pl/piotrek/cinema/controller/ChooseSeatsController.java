package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.Auditorium;
import pl.piotrek.cinema.model.Seance;
import pl.piotrek.cinema.model.Seat;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChooseSeatsController{
    private CookieRestTemplate cookieRestTemplate;

    public ChooseSeatsController(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    @FXML
    private AnchorPane container;

    private ArrayList<Integer> choosenSeats;

    public ArrayList<Integer> getChoosenSeats() {
        return choosenSeats;
    }

    public void init(Seance seance) {
        String url = ServerInfo.SEANCE_ENDPOINT + "/" + seance.getId() + "/seats/taken";
        ResponseEntity<ArrayList<Seat>> response = cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Seat>>() { });
        ArrayList<Seat> takenSeats = response.getBody();
        drawCinemaHall(seance.getAuditorium(), takenSeats);
    }

    private void drawCinemaHall(Auditorium auditorium, List<Seat> takenSeats){
        GridPane grid = new GridPane();
        choosenSeats = new ArrayList<>();

        int rows = auditorium.getRows();
        int cols = auditorium.getCols();

        for(int i=0; i<rows; ++i) grid.addRow(i);
        for(int i=0; i<cols; ++i) grid.addColumn(i);

        List<Seat> seats = auditorium.getSeats();

        for(Seat seat : seats){
            if(seat.isActive() == false) continue;

            ToggleButton seatIcon = new ToggleButton();
            seatIcon.setText(Integer.toString(seat.getNumber()));
            seatIcon.setPrefSize(50, 50);
            seatIcon.setId(Integer.toString(seat.getId()));

            seatIcon.setOnAction(event -> {
                Integer id_int = Integer.valueOf(seatIcon.getId());
                if(seatIcon.isSelected()){
                    choosenSeats.add(Integer.valueOf(id_int));
                } else if(choosenSeats.contains(id_int)){
                    choosenSeats.remove(id_int);
                }
            });

            if(takenSeats.contains(seat)){
                seatIcon.setDisable(true);
            }

            grid.setMargin(seatIcon, new Insets(10, 5, 0, 5));
            grid.add(seatIcon, seat.getNumber(), seat.getRow());
        }

        grid.addRow(rows);
        grid.addRow(rows+1);

        // Ekran
        JFXButton btn = new JFXButton();
        btn.setText("Ekran");
        btn.minWidthProperty().bind(grid.widthProperty().multiply(0.7));
        btn.setStyle("-fx-background-color: black");
        GridPane.setHalignment(btn, HPos.CENTER);

        // Odstep miedzy ekranem a fotelami
        Pane spring = new Pane();
        spring.minHeightProperty().bind(container.heightProperty().multiply(0.1));
        grid.add(spring, 0, rows);
        grid.add(btn, 0, rows+1, cols, 1);

        AnchorPane.setLeftAnchor(grid, 10.0);
        AnchorPane.setRightAnchor(grid, 10.0);
        grid.setAlignment(Pos.CENTER);

        container.getChildren().addAll(grid);
    }
}
