package pl.piotrek.cinema.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auditorium {

    private int id;
    private String name;
    private Integer rows;
    private Integer cols;

    private List<Seat> seats;

//    private int seatsCount;


    @Override
    public String toString() {
        return "Auditorium{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rows=" + rows +
                ", cols=" + cols +
                ", seats=" + seats +
                '}';
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
