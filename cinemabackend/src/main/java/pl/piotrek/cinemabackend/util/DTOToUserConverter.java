package pl.piotrek.cinemabackend.util;

import org.springframework.stereotype.Component;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinemabackend.model.User;

@Component
public class DTOToUserConverter implements BaseConverter<UserDTO, User> {

    @Override
    public User convert(UserDTO from) {
        User user  = new User();
        user.setId(from.getId());
        user.setFirstName(from.getFirstName());
        user.setLastName(from.getLastName());
        user.setEmail(from.getEmail());
        user.setPassword(from.getPassword());
        user.setRole(from.getRole());

        return user;
    }

}
