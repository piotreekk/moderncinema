package pl.piotrek.cinemabackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name="seat",
        uniqueConstraints= @UniqueConstraint(columnNames={"row", "number", "auditorium_id"})
)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int row;
    private int number;
    private boolean active = true;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "auditorium_id")
    private Auditorium auditorium;

    @JsonIgnore
    @OneToMany(mappedBy = "seat")
    private List<ReservedSeat> reserved_seats;


    public List<ReservedSeat> getReserved_seats() {
        return reserved_seats;
    }

    public void setReserved_seats(List<ReservedSeat> reserved_seats) {
        this.reserved_seats = reserved_seats;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row &&
                number == seat.number &&
                auditorium.equals(seat.auditorium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number, auditorium);
    }
}
