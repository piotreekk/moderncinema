package pl.piotrek.cinemabackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinemabackend.mapper.AuditoriumMapper;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.service.AuditoriumService;

import java.util.List;

@RestController
@RequestMapping("/auditorium")
public class AuditoriumController {
    private AuditoriumService auditoriumService;
    private AuditoriumMapper mapper;

    public AuditoriumController(AuditoriumService auditoriumService, AuditoriumMapper mapper) {
        this.auditoriumService = auditoriumService;
        this.mapper = mapper;
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Auditorium added!")
    @ResponseBody
    AuditoriumDTO add(@RequestParam String name, @RequestParam Integer rows, @RequestParam Integer cols){
        Auditorium auditorium = auditoriumService.add(name, rows, cols);
        return mapper.auditoriumToAuditoriumDto(auditorium);
    }

    @PutMapping("/update/{id}")
    void update(@PathVariable("id") Integer id, @RequestParam String name, @RequestParam Integer rows, @RequestParam Integer cols){
        auditoriumService.update(id, name, rows, cols);
    }

    @GetMapping("/get/all")
    List<Auditorium> getAll(){
        return auditoriumService.getAll();
    }


}
