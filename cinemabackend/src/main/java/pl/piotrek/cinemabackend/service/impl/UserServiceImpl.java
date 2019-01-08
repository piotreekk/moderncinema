package pl.piotrek.cinemabackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.repository.UserRepository;
import pl.piotrek.cinemabackend.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getActiveEmployees() {
        return userRepository.findAllByRoleAndEnabledTrue("admin");
    }

    @Override
    public List<User> getAllEmployees() {
        return userRepository.findAllByRole("admin");
    }

    @Override
    public void softDeleteUser(Integer id) {
        User user = userRepository.findById(id).get();
        user.setEnabled(false);
        userRepository.save(user);
    }
}
