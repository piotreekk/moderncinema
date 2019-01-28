package pl.piotrek.cinema.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuditoriumFx {
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty rows = new SimpleIntegerProperty();
    private IntegerProperty cols = new SimpleIntegerProperty();


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

    public int getCols() {
        return cols.get();
    }

    public IntegerProperty colsProperty() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols.set(cols);
    }
}
