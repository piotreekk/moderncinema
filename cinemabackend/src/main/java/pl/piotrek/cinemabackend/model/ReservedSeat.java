package pl.piotrek.cinemabackend.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reserved_seat", uniqueConstraints = @UniqueConstraint(columnNames = {"seance_id", "seat_id"}))
public class ReservedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private Seance seance;

}
