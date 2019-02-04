package pl.piotrek.cinemabackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinemabackend.mapper.UserMapper;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/adduser")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "User added!")
    @ResponseBody
    UserDTO add(@RequestBody UserDTO userDTO){
        User user = userMapper.userDtoToUser(userDTO);
        User persisted = userService.addUser(user);
        return userMapper.userToUserDto(persisted);
    }

    @PostMapping("/get")
    UserDTO get(@RequestBody String email){
        UserDTO response = userMapper.userToUserDto(userService.getUserByEmail(email));
        return response;
    }

    @GetMapping("/get/admin")
    List<UserDTO> getEmployees(){
        return userService.getAllEmployees().stream()
                .map(user -> userMapper.userToUserDto(user))
                .collect(Collectors.toList());
    }

    @GetMapping("/delete/{id}")
    void delete(@PathVariable("id") Integer id){
        userService.softDeleteUser(id);
    }


}