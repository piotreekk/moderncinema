package pl.piotrek.cinemabackend.util;

import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinemabackend.model.User;


public class UserToDTOConverter implements BaseConverter<User, UserDTO> {

    @Override
    public UserDTO convert(User from) {
        UserDTO user  = new UserDTO();
        user.setId(from.getId());
        user.setFirstName(from.getFirstName());
        user.setLastName(from.getLastName());
        user.setEmail(from.getEmail());
        user.setRole(from.getRole());

        return user;
    }

}
