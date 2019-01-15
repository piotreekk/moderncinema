package pl.piotrek.cinema.model.table;

// TODO: W TYCH MODELACH TEZ TRZEBA WPROWADZIC DUZO ZMIAN. W KONTROLERZE NIE BEDZIE TRZYMANA LISTA OBIEKTOW, TYLKO W TYM OBIEKCIE. JEGO LOGIKA TAKZE

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.*;
import pl.piotrek.cinema.model.Auditorium;

public class AuditoriumTableModel {

    private StringProperty name;
    private IntegerProperty rows;
    private IntegerProperty seatsCount;
    private ObjectProperty<MaterialDesignIconView> update;
    private ObjectProperty<MaterialDesignIconView> delete;

    private Auditorium auditorium;

    public AuditoriumTableModel(Auditorium auditorium) {
        this(auditorium.getName(), auditorium.getRows(),auditorium.getCols()*auditorium.getRows());
        this.auditorium = auditorium;
        delete.get().setId(Integer.toString(auditorium.getId()));
        update.get().setId(Integer.toString(auditorium.getId()));
    }

    public AuditoriumTableModel(String name, Integer rows, Integer seatsCount) {
        this.name = new SimpleStringProperty(name);
        this.rows = new SimpleIntegerProperty(rows);
        this.seatsCount = new SimpleIntegerProperty(seatsCount);

        delete = new SimpleObjectProperty<>(new MaterialDesignIconView(MaterialDesignIcon.DELETE));
        delete.get().setGlyphSize(40);
        update =  new SimpleObjectProperty<>(new MaterialDesignIconView(MaterialDesignIcon.ARROW_DOWN));
        update.get().setGlyphSize(40);
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getRows() {
        return rows.get();
    }

    public IntegerProperty rowsProperty() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows.set(rows);
    }

    public int getSeatsCount() {
        return seatsCount.get();
    }

    public IntegerProperty seatsCountProperty() {
        return seatsCount;
    }

    public void setSeatsCount(int seatsCount) {
        this.seatsCount.set(seatsCount);
    }

    public MaterialDesignIconView getUpdate() {
        return update.get();
    }

    public ObjectProperty<MaterialDesignIconView> updateProperty() {
        return update;
    }

    public void setUpdate(MaterialDesignIconView update) {
        this.update.set(update);
    }

    public MaterialDesignIconView getDelete() {
        return delete.get();
    }

    public ObjectProperty<MaterialDesignIconView> deleteProperty() {
        return delete;
    }

    public void setDelete(MaterialDesignIconView delete) {
        this.delete.set(delete);
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

}

