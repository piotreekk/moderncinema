package pl.piotrek.cinemabackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.service.AuditoriumService;

import java.util.List;

@RestController
@RequestMapping("/auditorium")
public class AuditoriumController {
    private AuditoriumService auditoriumService;

    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Auditorium added!")
    void add(@RequestParam String name, @RequestParam Integer rows, @RequestParam Integer cols){
        auditoriumService.add(name, rows, cols);
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
