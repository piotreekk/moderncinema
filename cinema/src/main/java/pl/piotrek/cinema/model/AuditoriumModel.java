package pl.piotrek.cinema.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.fx.AuditoriumFx;
import pl.piotrek.cinema.util.CookieRestTemplate;
import pl.piotrek.cinema.util.converter.AuditoriumConverter;

import java.util.ArrayList;


@Component
public class AuditoriumModel {
    private CookieRestTemplate cookieRestTemplate;

    public AuditoriumModel(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    private ObjectProperty<AuditoriumFx> auditoriumFxObjectProperty = new SimpleObjectProperty<>(new AuditoriumFx());
    private ObservableList<AuditoriumFx> auditoriumFxObservableList = FXCollections.observableArrayList();


    public AuditoriumFx getAuditoriumFxObjectProperty() {
        return auditoriumFxObjectProperty.get();
    }

    public ObjectProperty<AuditoriumFx> auditoriumFxObjectPropertyProperty() {
        return auditoriumFxObjectProperty;
    }

    public ObservableList<AuditoriumFx> getAuditoriumFxObservableList() {
        return auditoriumFxObservableList;
    }


    private void addAuditoriumToList(AuditoriumFx auditoriumFx){
        auditoriumFxObservableList.add(auditoriumFx);
    }

    public void loadDataFromAPI(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                auditoriumFxObservableList.clear();

                String url = ServerInfo.AUDITORIUM_ENDPOINT + "/get/all";
                ResponseEntity<ArrayList<AuditoriumDTO>> response =
                        cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<AuditoriumDTO>>(){});
                ArrayList<AuditoriumDTO> responseList = response.getBody();

//                for(AuditoriumDTO a : responseList){
//                    AuditoriumFx auditoriumFx = AuditoriumConverter.auditoriumDtoToAuditorium(a);
//                    addAuditoriumToList(auditoriumFx);
//                }

                responseList.stream()
                        .map(AuditoriumConverter::auditoriumDtoToAuditorium)
                        .forEach(auditoriumFx -> addAuditoriumToList(auditoriumFx));
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void addAuditorium(AuditoriumFx auditoriumFx){
        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/add";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

        String name = auditoriumFx.getName();
        Integer rows = auditoriumFx.getRows();
        Integer cols = auditoriumFx.getCols();

        map.add("name", name);
        map.add("rows", rows.toString());
        map.add("cols", cols.toString());

        ResponseEntity<AuditoriumDTO> response = cookieRestTemplate.postForEntity(url, map, AuditoriumDTO.class);
        if(response.getStatusCode() == HttpStatus.CREATED){
            addAuditoriumToList(auditoriumFx);
        }
    }


    public void updateAuditorium(AuditoriumFx auditoriumFx){
        AuditoriumDTO auditoriumDTO = AuditoriumConverter.auditoriumToAuditoriumDto(auditoriumFx);

        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/update/" + auditoriumDTO.getId() + "?name={name}&rows={rows}&cols={cols}";
        cookieRestTemplate.put(url, null, auditoriumDTO.getName(), auditoriumDTO.getRows(), auditoriumDTO.getCols());

        auditoriumFxObservableList.replaceAll(auditoriumFx1 -> auditoriumFx1.getName().equals(auditoriumFx.getName()) ? auditoriumFx : auditoriumFx1);

    }
}

