package pl.piotrek.cinema.util.converter;

import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinema.model.fx.UserFx;

public class UserConverter {
    public static UserFx UserDtoToUser(UserDTO u){
        UserFx userFx = new UserFx();
        userFx.setId(u.getId());
        userFx.setFirstName(u.getFirstName());
        userFx.setLastName(u.getLastName());
        userFx.setEmail(u.getEmail());
        userFx.setActive(u.isEnabled());

        return userFx;
    }

    public static UserDTO userToUserDto(UserFx userFx){
        return null;
    }
}
