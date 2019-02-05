package pl.piotrek.cinemabackend.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.model.Movie;
import pl.piotrek.cinemabackend.model.Seance;
import pl.piotrek.cinemabackend.model.User;
import pl.piotrek.cinemabackend.repository.AuditoriumRepository;
import pl.piotrek.cinemabackend.repository.MovieRepository;
import pl.piotrek.cinemabackend.repository.SeanceRepository;
import pl.piotrek.cinemabackend.repository.UserRepository;
import pl.piotrek.cinemabackend.service.AuditoriumService;

import java.time.LocalDate;
import java.time.LocalTime;

//@Profile("development")
@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private UserRepository userRepository;
    private AuditoriumService auditoriumService;
    private MovieRepository movieRepository;
    private SeanceRepository seanceRepository;

    public DevBootstrap(UserRepository userRepository, AuditoriumRepository auditoriumRepository, AuditoriumService auditoriumService, MovieRepository movieRepository, SeanceRepository seanceRepository) {
        this.userRepository = userRepository;
        this.auditoriumService = auditoriumService;
        this.movieRepository = movieRepository;
        this.seanceRepository = seanceRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData(){
        // Administrator
        User user = new User();
        user.setFirstName("Modern");
        user.setLastName("Cinema");
        user.setEmail("modern.cinema@outlook.com");
        user.setRole("admin");
        user.setEnabled(true);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("cinemaproject!@#"));
        userRepository.save(user);

        // sale kinowe
        Auditorium auditorium = auditoriumService.add("Sala Testowa", 10, 10);
        Auditorium auditorium1 = auditoriumService.add("Sala Testowa 2", 10, 10);
        Auditorium auditorium2 = auditoriumService.add("Sala Testowa 3", 10, 10);
        Auditorium auditorium3 = auditoriumService.add("Sala Testowa 4", 10, 10);

        Movie movie = new Movie();
        movie.setId(297802);
        movie.setTitle("Aquaman");
        movie.setOverview("Once home to the most advanced civilization on Earth, the city of Atlantis is now an underwater kingdom ruled by the power-hungry King Orm. With a vast army at his disposal, Orm plans to conquer the remaining oceanic people -- and then the surface world. Standing in his way is Aquaman, Orm's half-human, half-Atlantean brother and true heir to the throne. With help from royal counselor Vulko, Aquaman must retrieve the legendary Trident of Atlan and embrace his destiny as protector of the deep");
        movie.setPosterPath("http://image.tmdb.org/t/p/w154/5Kg76ldv7VxeX9YlcQXiowHgdX6.jpg");
        movieRepository.save(movie);


        Seance seance = new Seance();
        seance.setMovie(movie);
        seance.setAuditorium(auditorium);
        seance.setDate(LocalDate.now());
        seance.setStartTime(LocalTime.now());
        seanceRepository.save(seance);


    }
}
