package pl.piotrek.cinema.model;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

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

import java.util.ArrayList;


@Component
public class AuditoriumModel {
    private CookieRestTemplate cookieRestTemplate;

    public AuditoriumModel(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    private ObjectProperty<AuditoriumFx> auditoriumFxObjectProperty = new SimpleObjectProperty<>(new AuditoriumFx());
    private ObservableList<AuditoriumFx> auditoriumFxObservableList = FXCollections.observableArrayList();


    public void addAuditoriumToList(AuditoriumFx auditoriumFx){
        auditoriumFxObservableList.add(auditoriumFx);
    }

    public AuditoriumFx getAuditoriumFxObjectProperty() {
        return auditoriumFxObjectProperty.get();
    }

    public ObjectProperty<AuditoriumFx> auditoriumFxObjectPropertyProperty() {
        return auditoriumFxObjectProperty;
    }

    public ObservableList<AuditoriumFx> getAuditoriumFxObservableList() {
        return auditoriumFxObservableList;
    }


    public void loadDataFromAPI(){
        auditoriumFxObservableList.clear();

        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/get/all";
        ResponseEntity<ArrayList<AuditoriumDTO>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<AuditoriumDTO>>(){});
        ArrayList<AuditoriumDTO> responseList = response.getBody();

        for(AuditoriumDTO a : responseList){
            AuditoriumFx auditoriumFx = new AuditoriumFx();
            auditoriumFx.setId(a.getId());
            auditoriumFx.setName(a.getName());
            auditoriumFx.setRows(a.getRows());
            auditoriumFx.setCols(a.getCols());
            addAuditoriumToList(auditoriumFx);
        }
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
        AuditoriumDTO auditoriumDTO = new AuditoriumDTO();
        auditoriumDTO.setId(auditoriumFx.getId());
        auditoriumDTO.setName(auditoriumFx.getName());
        auditoriumDTO.setRows(auditoriumFx.getRows());
        auditoriumDTO.setCols(auditoriumFx.getCols());

        String url = ServerInfo.AUDITORIUM_ENDPOINT + "/update/" + auditoriumDTO.getId() + "?name={name}&rows={rows}&cols={cols}";
        cookieRestTemplate.put(url, null, auditoriumDTO.getName(), auditoriumDTO.getRows(), auditoriumDTO.getCols());

//        auditoriumFxObservableList.removeIf(a -> a.getName().equals(auditoriumFx.getName()));
        auditoriumFxObservableList.replaceAll(auditoriumFx1 -> auditoriumFx1.getName().equals(auditoriumFx.getName()) ? auditoriumFx : auditoriumFx1);

    }
}

