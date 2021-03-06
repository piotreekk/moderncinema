package pl.piotrek.cinemabackend.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinemabackend.mapper.AuditoriumMapper;
import pl.piotrek.cinemabackend.mapper.SeanceMapper;
import pl.piotrek.cinemabackend.model.Seance;
import pl.piotrek.cinemabackend.model.Seat;
import pl.piotrek.cinemabackend.service.SeanceService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seance")
public class SeanceController {
    private SeanceService seanceService;
    private SeanceMapper seanceMapper;
    private AuditoriumMapper auditoriumMapper;

    public SeanceController(SeanceService seanceService, SeanceMapper seanceMapper, AuditoriumMapper auditoriumMapper) {
        this.seanceService = seanceService;
        this.seanceMapper = seanceMapper;
        this.auditoriumMapper = auditoriumMapper;
    }

    @GetMapping("/get/all")
    List<SeanceDTO> getAll(){
        return seanceService.getAll().stream()
                .map(seance -> seanceMapper.seanceToSeanceDto(seance))
                .collect(Collectors.toList());
    }

    @GetMapping("/get/bydate/{date}")
    List<SeanceDTO> getByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return seanceService.getByDate(date).stream()
                .map(seance -> seanceMapper.seanceToSeanceDto(seance))
                .collect(Collectors.toList());
    }

    @GetMapping("/get/{id}/auditorium")
    AuditoriumDTO getAuditorium(@PathVariable("id") Integer seanceId){
        return auditoriumMapper.auditoriumToAuditoriumDto(seanceService.getAuditorium(seanceId));
    }

    @GetMapping("/{id}/seat/free")
    List<Seat> getFreeSeats(@PathVariable("id") Integer senace_id){
        return seanceService.getFreeSeats(senace_id);
    }

    @GetMapping("/{id}/seat/taken")
    List<Seat> getTakenSeats(@PathVariable("id") Integer seance_id){
        return seanceService.getTakenSeats(seance_id);
    }


    @GetMapping("/get/{id}/seat/free/count")
    Integer getFreeSeatsCount(@PathVariable("id") Integer seance_id){
        return seanceService.getFreeSeatsCount(seance_id);
    }
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    Seance add(@RequestBody SeanceForm seance){
        return seanceService.addSeance(seance);
    }

}
