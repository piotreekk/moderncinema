package pl.piotrek.cinema.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.api.dto.SeatDTO;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ChooseSeatsController implements Initializable {
    private CookieRestTemplate cookieRestTemplate;
//    private SeanceDTO seanceDTO;
    private Integer seanceId;

    public ChooseSeatsController(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    @FXML
    private AnchorPane container;
    private ArrayList<Integer> choosenSeats;

    public ArrayList<Integer> getChoosenSeats() {
        return choosenSeats;
    }

    public void setSeance(int id) {
        this.seanceId = id;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String url = ServerInfo.SEANCE_ENDPOINT + "/" + seanceId + "/seat/taken";
        ResponseEntity<ArrayList<SeatDTO>> response = cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<SeatDTO>>() { });
        ArrayList<SeatDTO> takenSeats = response.getBody();

        String url2 = ServerInfo.SEANCE_ENDPOINT + "/get/" + seanceId + "/auditorium";
        ResponseEntity<AuditoriumDTO> response2 = cookieRestTemplate.getForEntity(url2, AuditoriumDTO.class);

        drawCinemaHall(response2.getBody(), takenSeats);
    }

    private void drawCinemaHall(AuditoriumDTO auditoriumDTO, List<SeatDTO> takenSeatDTOS){
        GridPane grid = new GridPane();
        choosenSeats = new ArrayList<>();

        int rows = auditoriumDTO.getRows();
        int cols = auditoriumDTO.getCols();

        for(int i=0; i<rows; ++i) grid.addRow(i);
        for(int i=0; i<cols; ++i) grid.addColumn(i);

        List<SeatDTO> seatDTOS = auditoriumDTO.getSeats();

        for(SeatDTO seatDTO : seatDTOS){
            if(seatDTO.isActive() == false) continue;

            ToggleButton seatIcon = new ToggleButton();
            seatIcon.setText(Integer.toString(seatDTO.getNumber()));
            seatIcon.setPrefSize(50, 50);
            seatIcon.setId(Integer.toString(seatDTO.getId()));

            seatIcon.setOnAction(event -> {
                Integer id_int = Integer.valueOf(seatIcon.getId());
                if(seatIcon.isSelected()){
                    choosenSeats.add(Integer.valueOf(id_int));
                } else choosenSeats.remove(id_int);
            });

            if(takenSeatDTOS.contains(seatDTO)){
                seatIcon.setDisable(true);
            }

            GridPane.setMargin(seatIcon, new Insets(10, 5, 0, 5));
            grid.add(seatIcon, seatDTO.getNumber(), seatDTO.getRow());
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
