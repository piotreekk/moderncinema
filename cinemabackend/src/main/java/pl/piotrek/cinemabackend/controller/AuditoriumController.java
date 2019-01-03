package pl.piotrek.cinemabackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinemabackend.model.Auditorium;
import pl.piotrek.cinemabackend.model.Seat;
import pl.piotrek.cinemabackend.service.AuditoriumService;

import java.util.List;

@RestController
@RequestMapping("/auditorium")
public class AuditoriumController {
    @Autowired
    AuditoriumService auditoriumService;

    @PostMapping("/add")
    void addAuditorium(@RequestParam String name, @RequestParam Integer rows, @RequestParam Integer cols){
        auditoriumService.add(name, rows, cols);
    }

    @PutMapping("/update/{id}")
    void updateAuditorium(@PathVariable("id") Integer id, @RequestParam String name, @RequestParam Integer rows, @RequestParam Integer cols){
        auditoriumService.update(id, name, rows, cols);
    }

    @GetMapping("/get")
    Auditorium getAuditiorium(@RequestParam Integer id){
        return auditoriumService.get(id);
    }

    @GetMapping("/get/all")
    List<Auditorium> getAuditiories(){
        return auditoriumService.getAll();
    }


}
