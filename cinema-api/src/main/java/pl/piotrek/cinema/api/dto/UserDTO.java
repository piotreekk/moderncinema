package pl.piotrek.cinema.api.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private boolean enabled;

}
