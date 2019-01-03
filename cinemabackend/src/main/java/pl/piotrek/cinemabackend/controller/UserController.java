package pl.piotrek.cinemabackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.repository.UserRepository;
import pl.piotrek.cinemabackend.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/adduser")
    User newUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping("/test")
    String welcome(){
        return "Witam!";
    }

    @PostMapping("/get")
    User getUser(@RequestBody String email){
        return userService.getUserByEmail(email);

    }


}