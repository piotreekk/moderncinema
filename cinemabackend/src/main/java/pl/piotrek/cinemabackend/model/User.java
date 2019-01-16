package pl.piotrek.cinemabackend.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
// Taka nazwa tabeli, ponieważ user jest słowem kluczowym w azure db
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private boolean enabled = true;

    private String role;

}
