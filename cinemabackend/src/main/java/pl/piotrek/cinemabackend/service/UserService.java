package pl.piotrek.cinemabackend.service;


import pl.piotrek.cinemabackend.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);
    User getUserByEmail(String email);

    List<User> getActiveEmployees();

    List<User> getAllEmployees();
}
