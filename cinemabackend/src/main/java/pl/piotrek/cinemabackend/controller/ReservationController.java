package pl.piotrek.cinemabackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.piotrek.cinemabackend.forms.ReservationForm;
import pl.piotrek.cinemabackend.service.ReservationService;
import pl.piotrek.cinemabackend.service.impl.ReservationEmailServiceImpl;

import java.util.List;


@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;
    private ReservationEmailServiceImpl reservationEmailServiceImpl;

    @Autowired
    public ReservationController(ReservationService reservationService, ReservationEmailServiceImpl reservationEmailServiceImpl) {
        this.reservationService = reservationService;
        this.reservationEmailServiceImpl = reservationEmailServiceImpl;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    void addReservation(@RequestBody ReservationForm reservation, @RequestParam("mail") boolean shouldSendMail){
        reservationService.addReservation(reservation);
        if(shouldSendMail)
            reservationEmailServiceImpl.send(reservation);
    }

    @GetMapping("/user/{id}")
    List<String> getUserReservations(@PathVariable("id") Integer id){
        return reservationService.getUserReservations(id);
    }

}
