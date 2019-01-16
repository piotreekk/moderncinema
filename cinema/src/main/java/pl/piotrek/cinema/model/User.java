package pl.piotrek.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;



@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled = true;
    private String role;

}
