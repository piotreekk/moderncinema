package pl.piotrek.cinemabackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
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
