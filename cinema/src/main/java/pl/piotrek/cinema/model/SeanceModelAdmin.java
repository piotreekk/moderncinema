package pl.piotrek.cinema.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.fx.SeanceFx;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.util.ArrayList;

@Component
public class SeanceModelAdmin {
    private CookieRestTemplate cookieRestTemplate;

    public SeanceModelAdmin(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    private ObjectProperty<SeanceFx> seanceModelObjectProperty = new SimpleObjectProperty<>(new SeanceFx());
    private ObservableList<SeanceFx> seanceFxObservableList = FXCollections.observableArrayList();


    public SeanceFx getSeanceModelObjectProperty() {
        return seanceModelObjectProperty.get();
    }
    public ObjectProperty<SeanceFx> seanceModelObjectPropertyProperty() {
        return seanceModelObjectProperty;
    }
    public ObservableList<SeanceFx> getSeanceFxObservableList() {
        return seanceFxObservableList;
    }


    public void persistSeance(SeanceForm seance){
        String url = ServerInfo.SEANCE_ENDPOINT + "/add";
        try {
            ResponseEntity<SeanceDTO> response = cookieRestTemplate.postForEntity(url, seance, SeanceDTO.class);

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                SeanceDTO newSeanceDTO = response.getBody();
                SeanceFx seanceFx = new SeanceFx();
                seanceFx.setId(newSeanceDTO.getId());
                seanceFx.setMovieTitle(newSeanceDTO.getMovie().getTitle());
                seanceFx.setAllSeatsCount(newSeanceDTO.getAuditorium().getRows()*newSeanceDTO.getAuditorium().getCols());
                seanceFx.setDate(newSeanceDTO.getDate());
                seanceFx.setStartTime(newSeanceDTO.getStartTime());
                addSeanceToTable(seanceFx);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Error occured while adding new senace!");
                alert.showAndWait();
            }
        } catch (RestClientException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Server not responding!");
            alert.showAndWait();
        }
    }


    private void loadDataFromAPI(){
        seanceFxObservableList.clear();

        String url = ServerInfo.SEANCE_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<SeanceDTO> > response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<SeanceDTO>>(){});

        ArrayList<SeanceDTO> responseList = response.getBody();
        for(SeanceDTO s : responseList){
            SeanceFx seanceFx = new SeanceFx();
            seanceFx.setId(s.getId());
            seanceFx.setMovieTitle(s.getMovie().getTitle());
            seanceFx.setAllSeatsCount(s.getAuditorium().getRows()*s.getAuditorium().getCols());
            seanceFx.setDate(s.getDate());
            seanceFx.setStartTime(s.getStartTime());
            addSeanceToTable(seanceFx);
        }

    }

    private void addSeanceToTable(SeanceFx seanceFx) {
        seanceFxObservableList.add(seanceFx);
    }


}
