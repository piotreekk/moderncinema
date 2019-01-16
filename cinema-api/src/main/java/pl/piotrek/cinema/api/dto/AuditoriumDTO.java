package pl.piotrek.cinema.api.dto;
// TODO: DO WYNIESIENIA DO MODUŁU API, PAKIETU DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditoriumDTO {

    private int id;
    private String name;
    private Integer rows;
    private Integer cols;
    private List<SeatDTO> seats;
}
