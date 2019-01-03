package pl.piotrek.cinemabackend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.piotrek.cinemabackend.model.*;
import pl.piotrek.cinemabackend.repository.ReservationRepository;
import pl.piotrek.cinemabackend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

//import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoriesIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void test(){
        //---------------------USER-------------------------------
        User user = new User();
        user.setFirstName("Piotrek");
        user.setLastName("Strumnik");
        user.setEmail("piotrekstrumnik@gmail.com");
        user.setPassword("piotrek123");

        User persistedUser = entityManager.persist(user);

        assertEquals("User persisted and user are not the same", persistedUser.getEmail(), user.getEmail());

        //-------------------Movie---------------------------------
        Movie movie = new Movie();
        movie.setTitle("Film");
        movie.setOverview("Opis filmu");

        Movie persistedMovie = entityManager.persist(movie);

        assertEquals("Movie persisted and movie are not the same", persistedMovie.getTitle(), movie.getTitle());

        //-------------------Seance-------------------------------
        Seance seance = new Seance();
        seance.setMovie(movie);
        seance.setDate(LocalDate.now());
        seance.setStartTime(LocalTime.now());

        Seance persistedSeance = entityManager.persist(seance);

        assertEquals("Seance persisted and seance are not the same", persistedSeance.getMovie().getTitle(), seance.getMovie().getTitle());



        //------------------Auditorium-------------------------------
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Sala Kinowa");

        Auditorium persistedAuditorium = entityManager.persist(auditorium);

        assertEquals("Auditorium persisted and auditorium are not the same", auditorium, persistedAuditorium);


        //--------------------Seat---------------------------------
        Seat seat = new Seat();
        seat.setRow(0);
        seat.setNumber(1);
        seat.setAuditorium(auditorium);
        Seat persistedSeat = entityManager.persist(seat);

        assertEquals("Seat persisted and seat are not the same", persistedSeat.getNumber(), seat.getNumber());

//
//        //-------------------Reserved Seat -------------------------
//        ReservedSeat reservedSeat = new ReservedSeat();
//        reservedSeat.setSeat(seat);
//        reservedSeat.setSeance(seance);
//        reservedSeat.setReservation(reservation);

        //------------------Reservation-----------------------------
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSeance(seance);

        List<ReservedSeat> reservedSeatList = new ArrayList<>();
        ReservedSeat reservedSeat = new ReservedSeat();
        reservedSeat.setSeance(seance);
        reservedSeatList.add(reservedSeat);

        reservation.setReservedSeats(reservedSeatList);
//        entityManager.persist(reservation);
        reservationRepository.save(reservation);

        List<Reservation> found = (List<Reservation>)reservationRepository.findAll();
        for(Reservation r : found) {
            System.out.println(r.getSeance());
            System.out.println(r.getUser());
            System.out.println(r.getReservedSeats());
        }



    }
}
