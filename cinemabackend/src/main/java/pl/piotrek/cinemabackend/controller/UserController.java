package pl.piotrek.cinemabackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/adduser")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "User added!")
    @ResponseBody
    User add(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping("/get")
    User get(@RequestBody String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/get/admin")
    List<User> getEmployees(@RequestParam(value = "active", required = false) boolean active){
        if(active == true)
            return userService.getActiveEmployees();
        else
            return userService.getAllEmployees();
    }


}