package pl.piotrek.cinemabackend.service;


import pl.piotrek.cinemabackend.model.User;

public interface UserService {

    User addUser(User user);
    User getUserByEmail(String email);

}
