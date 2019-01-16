package pl.piotrek.cinema.api.dto;

// TODO: DO WYNIESIENIA DO MODUŁU API, PAKIETU DTO\

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Objects;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatDTO {
    private int id;
    private int row;
    private int number;
    private boolean active;
    private AuditoriumDTO auditorium;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatDTO seatDTO = (SeatDTO) o;
        return id == seatDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
