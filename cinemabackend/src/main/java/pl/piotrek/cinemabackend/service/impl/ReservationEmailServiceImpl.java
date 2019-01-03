package pl.piotrek.cinemabackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.piotrek.cinemabackend.forms.ReservationForm;
import pl.piotrek.cinemabackend.service.EmailSender;
import pl.piotrek.cinemabackend.service.ReservationEmailService;
import pl.piotrek.cinemabackend.service.ReservationService;

@Service
public class ReservationEmailServiceImpl implements ReservationEmailService {
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;
    private final ReservationService reservationService;

    @Autowired
    public ReservationEmailServiceImpl(EmailSender emailSender,
                                       TemplateEngine templateEngine,
                                       ReservationService reservationService){
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.reservationService = reservationService;
    }

    public void send(ReservationForm reservationForm) {
        String details_string = reservationService.getReservationDetails(reservationForm);
        String [] details = details_string.split(";");
        String name = details[0];
        String email = details[1];
        String title = details[2];
        String date = details[3];
        String time = details[4];
        String seats = details[5];

        Context context = new Context();
        context.setVariable("header", "Bilet na seans w ModernCinema");
        context.setVariable("title", title + " - rezerwacja");
        context.setVariable("greeting", "Witaj " + name);
        context.setVariable("movie", "Wybrany film: " + title);
        context.setVariable("date", "Data: " + date + " godzina: " + time);
        context.setVariable("seats","Wybrane miejsca: " + seats);
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(email, "ModernCiname Reservation", body);
    }
}