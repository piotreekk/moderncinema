package pl.piotrek.cinema;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.piotrek.cinema.view.StageManager;
import pl.piotrek.cinema.view.ViewList;

@SpringBootApplication
public class CinemaApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private StageManager stageManager;


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception{
        springContext = SpringApplication.run(CinemaApplication.class);

    }

    @Override
    public void start(Stage stage) throws Exception {
        stageManager = springContext.getBean(StageManager.class, stage);
        stageManager.switchScene(ViewList.LOGIN);
    }

    @Override
    public void stop() {
            springContext.close();
    }
}
