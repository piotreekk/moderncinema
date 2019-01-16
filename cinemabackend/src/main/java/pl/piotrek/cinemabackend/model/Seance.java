package pl.piotrek.cinemabackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "Seance")
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

//    @DateTimeFormat(iso = DateTimeFormatter.ISO_DATE)
    private LocalTime startTime;
    private LocalDate date;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "auditorium_id")
    private Auditorium auditorium;

    @JsonIgnore
    @OneToMany(mappedBy = "seance")
    private List<ReservedSeat> reservedSeats;

}
