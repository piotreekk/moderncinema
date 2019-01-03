package pl.piotrek.cinemabackend.service;

public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}