package pl.piotrek.cinema.model;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuditoriumModel {
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

    public void setAuditoriumFxObjectProperty(AuditoriumFx auditoriumFxObjectProperty) {
        this.auditoriumFxObjectProperty.set(auditoriumFxObjectProperty);
    }

    public ObservableList<AuditoriumFx> getAuditoriumFxObservableList() {
        return auditoriumFxObservableList;
    }

    public void setAuditoriumFxObservableList(ObservableList<AuditoriumFx> auditoriumFxObservableList) {
        this.auditoriumFxObservableList = auditoriumFxObservableList;
    }
}

