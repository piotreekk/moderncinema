package pl.piotrek.cinema.config;

public abstract class ServerInfo {
//    public static final String BASE_URL = "http://localhost:8080";
    public static final String BASE_URL = "http://cinematest.azurewebsites.net";
    public static final String AUDITORIUM_ENDPOINT = BASE_URL + "/auditorium";
    public static final String SEANCE_ENDPOINT = BASE_URL + "/seance";
    public static final String USER_ENDPOINT = BASE_URL + "/user";
    public static final String MOVIE_ENDPOINT = BASE_URL + "/movie";
    public static final String RESERVATION_ENDPOINT = BASE_URL + "/reservation";

}
