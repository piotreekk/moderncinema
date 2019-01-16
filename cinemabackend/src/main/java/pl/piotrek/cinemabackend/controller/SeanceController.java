package pl.piotrek.cinemabackend.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinema.api.forms.SeanceForm;
import pl.piotrek.cinemabackend.model.Seance;
import pl.piotrek.cinemabackend.model.Seat;
import pl.piotrek.cinemabackend.service.SeanceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/seance")
public class SeanceController {
    private SeanceService seanceService;

    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    @GetMapping("/get/all")
    List<Seance> getAll(){
        return seanceService.getAll();
    }

    @GetMapping("/get/bydate/{date}")
    List<Seance> getByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return seanceService.getByDate(date);
    }

    @GetMapping("/{id}/seat/free")
    List<Seat> getFreeSeats(@PathVariable("id") Integer id){
        return seanceService.getFreeSeats(id);
    }

    @GetMapping("/{id}/seat/taken")
    List<Seat> getTakenSeats(@PathVariable("id") Integer id){
        return seanceService.getTakenSeats(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    Seance add(@RequestBody SeanceForm seance){
        return seanceService.addSeance(seance);
    }

}
