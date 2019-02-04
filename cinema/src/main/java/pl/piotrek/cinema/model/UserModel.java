package pl.piotrek.cinema.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinema.config.ServerInfo;
import pl.piotrek.cinema.model.fx.UserFx;
import pl.piotrek.cinema.util.CookieRestTemplate;

import java.util.ArrayList;

@Component
public class UserModel {
    private CookieRestTemplate cookieRestTemplate;

    public UserModel(CookieRestTemplate cookieRestTemplate) {
        this.cookieRestTemplate = cookieRestTemplate;
    }

    private ObjectProperty<UserFx> userFxObjectProperty = new SimpleObjectProperty<>(new UserFx());
    private ObservableList<UserFx> userFxObservableList = FXCollections.observableArrayList();
    private FilteredList<UserFx> userFxFilteredList = new FilteredList<>(userFxObservableList, userFx -> userFx.isActive());

    public void addUserToList(UserFx userFx){
        userFxObservableList.add(userFx);
    }

    public UserFx getUserFxObjectProperty() {
        return userFxObjectProperty.get();
    }

    public ObjectProperty<UserFx> userFxObjectPropertyProperty() {
        return userFxObjectProperty;
    }

    public ObservableList<UserFx> getUserFxObservableList() {
        return userFxObservableList;
    }

    public FilteredList<UserFx> getUserFxFilteredList() {
        return userFxFilteredList;
    }

    public void showDisabledAccounts(boolean shouldShow) {
        userFxFilteredList.setPredicate(userFx -> {
            if(shouldShow) return true;
            return userFx.isActive();
        });

    }

    public void loadDataFromAPI(){
        userFxObservableList.clear();

        String url = ServerInfo.USER_ENDPOINT + "/get/admin";

        ResponseEntity<ArrayList<UserDTO>> response =
                cookieRestTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<UserDTO>>(){});

        ArrayList<UserDTO> responseList = response.getBody();

        for(UserDTO u : responseList){
            UserFx userFx = new UserFx();
            userFx.setId(u.getId());
            userFx.setFirstName(u.getFirstName());
            userFx.setLastName(u.getLastName());
            userFx.setEmail(u.getEmail());
            userFx.setActive(u.isEnabled());

            addUserToList(userFx);
        }
    }


    public void deleteEmployee(UserFx userFx){
        userFxObservableList.replaceAll(u -> {
            if(u.getId() == userFx.getId()){
                u.setActive(false);
            }
            return u;
        });

        String url = ServerInfo.USER_ENDPOINT + "/delete/" + userFx.getId();
        cookieRestTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

}
