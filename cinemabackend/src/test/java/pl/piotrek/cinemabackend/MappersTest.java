package pl.piotrek.cinemabackend;

import org.junit.Test;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.api.dto.UserDTO;
import pl.piotrek.cinemabackend.mapper.SeanceMapper;
import pl.piotrek.cinemabackend.mapper.UserMapper;
import pl.piotrek.cinemabackend.model.*;
import pl.piotrek.cinemabackend.util.SeanceToDTOConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MappersTest {

    @Test
    public void shouldMapUserToUserDto() {
        // given
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEnabled(true);

        // when
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDto(user);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void shouldMapeanceToSeanceDTO() {
        Seat seat = new Seat();
        seat.setRow(1);

        List<Seat> seats = new ArrayList<>();
        seats.add(seat);

        Auditorium auditorium = new Auditorium();
        auditorium.setName("Auditory");

        auditorium.setSeats(seats);
        seat.setAuditorium(auditorium);

        Seance seance = new Seance();
        seance.setStartTime(LocalTime.now());
        seance.setDate(LocalDate.now());
        seance.setAuditorium(auditorium);
        seance.setMovie(new Movie());
//        seance.setAuditorium(AuditoriumMapper.INSTANCE.auditoriumToAuditoriumDto(auditorium));

//        SeanceToDTOConverter converter = new SeanceToDTOConverter();
//        SeanceDTO seanceDTO = converter.convert(seance);

        SeanceDTO seanceDTO = SeanceMapper.INSTANCE.seanceToSeanceDto(seance);

    }


}