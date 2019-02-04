package pl.piotrek.cinema.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.fx.ReservationFx;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationModel {
    private CookieRestTemplate cookieRestTemplate;

    public ReservationModel(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    private SimpleObjectProperty<ReservationFx> reservationFxSimpleObjectProperty = new SimpleObjectProperty<>(new ReservationFx());
    private ObservableList<ReservationFx> reservationFxObservableList = FXCollections.observableArrayList();

    public ReservationFx getReservationFxSimpleObjectProperty() {
        return reservationFxSimpleObjectProperty.get();
    }

    public SimpleObjectProperty<ReservationFx> reservationFxSimpleObjectPropertyProperty() {
        return reservationFxSimpleObjectProperty;
    }

    public ObservableList<ReservationFx> getReservationFxObservableList() {
        return reservationFxObservableList;
    }

    public void loadDataFromAPI(Integer user_id){
        String url = ServerInfo.RESERVATION_ENDPOINT + "/user/" + user_id;
        ResponseEntity<List<String>> response = cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>(){});
        if(response.getStatusCode() != HttpStatus.OK) return;

        ArrayList<ReservationFx> list = new ArrayList<>();
        for(String rs : response.getBody()) {
            String [] details = rs.split(";");
            String title = details[0];
            LocalDate date = LocalDate.parse(details[1]);
            LocalTime startTime = LocalTime.parse(details[2]);
            String auditorium = details[3];
            String seatsString = details[4];

            ReservationFx reservationFx = new ReservationFx();
            reservationFx.setAuditorium(auditorium);
            reservationFx.setDate(date);
            reservationFx.setStartTime(startTime);
            reservationFx.setMovieTitle(title);
            reservationFx.setSeats(seatsString);

            addReservationToList(reservationFx);
        }
    }

    private void addReservationToList(ReservationFx reservationFx){
        reservationFxObservableList.add(reservationFx);
    }

}
