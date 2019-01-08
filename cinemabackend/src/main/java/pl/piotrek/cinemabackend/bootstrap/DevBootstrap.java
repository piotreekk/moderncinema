package pl.piotrek.cinemabackend.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.repository.UserRepository;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    // Autowired repositories should be here
    private UserRepository userRepository;

    public DevBootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // TODO : Odkomentować gdy uruchamiamy pierwszy raz na serwerze.
        initData();
    }

    private void initData(){
        // Data to init
        User user = new User();
        user.setFirstName("Modern");
        user.setLastName("Cinema");
        user.setEmail("modern.cinema@outlook.com");
        user.setRole("admin");
        user.setEnabled(true);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("cinemaproject!@#"));
        userRepository.save(user);

    }
}
