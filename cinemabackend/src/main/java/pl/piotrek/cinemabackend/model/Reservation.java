package pl.piotrek.cinemabackend.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Seance seance;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST)
    private List<ReservedSeat> reservedSeats;

}
