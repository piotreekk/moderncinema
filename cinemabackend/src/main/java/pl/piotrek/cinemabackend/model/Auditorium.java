package pl.piotrek.cinemabackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Integer rows;
    private Integer cols;

    @JsonBackReference
    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY)
    private List<Seance> seances;

    @JsonManagedReference
    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.ALL)
    private List<Seat> seats;

}
