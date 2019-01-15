package pl.piotrek.cinema.config;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.piotrek.cinema.view.StageManager;

import java.io.IOException;


@Configuration
public class AppJavaConfig {
	
    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @Bean
    // Musi byc lazy, bo w czasie uruchamiania springboota nie ma zarejestrowanego beana Stage
    @Lazy(value = true) //Stage only created after Spring context bootstap
    public StageManager stageManager(Stage stage) throws IOException {
        return new StageManager(stage, springFXMLLoader);
    }


}
