package pl.piotrek.cinema.util;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

@Component
public class CookieRestTemplate extends RestTemplate {
    private String cookie;


    public void setCookie(String cookie){
        this.cookie = cookie;
    }
    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        ClientHttpRequest request = super.createRequest(url, method);

        request.getHeaders().add("Cookie", cookie);
        return request;
    }
}