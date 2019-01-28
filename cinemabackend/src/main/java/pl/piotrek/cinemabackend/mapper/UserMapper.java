package pl.piotrek.cinemabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinemabackend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDto(User user);
    User userDtoToUser(UserDTO userDTO);

}
