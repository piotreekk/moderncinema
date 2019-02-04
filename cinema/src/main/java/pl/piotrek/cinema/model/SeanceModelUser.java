package pl.piotrek.cinema.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.api.forms.ReservationForm;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.fx.SeanceFx;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SeanceModelUser {
    private CookieRestTemplate cookieRestTemplate;

    public SeanceModelUser(CookieRestTemplate cookieRestTemplate) {
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

    public void persistReservation(SeanceFx seanceFx, Integer user_id, List<Integer> choosenSeats, boolean shouldSendMail){
        String url = ServerInfo.RESERVATION_ENDPOINT + "/add";
        if(shouldSendMail)
            url += "?mail=true";
        else
            url += "?mail=false";
        ReservationForm reservation = new ReservationForm();
        reservation.setSeanceId(seanceFx.getId());
        reservation.setSeats(choosenSeats);
        reservation.setUserId(user_id);
        cookieRestTemplate.postForEntity(url, reservation, ReservationForm.class);
    }


    public void loadDataFromAPI(LocalDate date){
        seanceFxObservableList.clear();

        String url = ServerInfo.SEANCE_ENDPOINT + "/get/bydate/{date}";
        ResponseEntity<ArrayList<SeanceDTO>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<SeanceDTO>>(){}, date);

        ArrayList<SeanceDTO> responseList = response.getBody();

        for(SeanceDTO s : responseList){
            SeanceFx seanceFx = new SeanceFx();
            seanceFx.setId(s.getId());
            seanceFx.setMovieTitle(s.getMovie().getTitle());
            seanceFx.setAllSeatsCount(s.getAuditorium().getRows()*s.getAuditorium().getCols());
            seanceFx.setPosterImage(s.getMovie().getPosterPath());

            addSeanceToTable(seanceFx);
        }
    }

    private void addSeanceToTable(SeanceFx seanceFx) {
        seanceFxObservableList.add(seanceFx);
    }


}
