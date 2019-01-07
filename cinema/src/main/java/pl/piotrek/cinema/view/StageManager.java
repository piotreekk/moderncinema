package pl.piotrek.cinema.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.piotrek.cinema.config.SpringFXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class StageManager {
    private Stage primaryStage;
    private SpringFXMLLoader springFXMLLoader;

    public StageManager(Stage primaryStage, SpringFXMLLoader springFXMLLoader) {
        this.primaryStage = primaryStage;
        this.springFXMLLoader = springFXMLLoader;
    }

    public void switchScene(ViewList view){
        Parent rootNode = loadView(view.getFxmlPath());
        show(rootNode, view.getTitle());
    }


    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);

//        scene.getStylesheets().add("style.css");

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            System.out.println("Nie mozna zaladowac sceny");
        }
    }

    public void loadViewOnPane(AnchorPane root, ViewList view){
        try {
            Parent viewNode = springFXMLLoader.load(view.getFxmlPath());
            root.getChildren().setAll(viewNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Scene prepareScene(Parent rootnode){
        Scene scene =  primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }

        scene.setRoot(rootnode);
        return scene;
    }

    private Parent loadView(String fxmlPath){
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlPath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            System.out.println("Nie mozna zaladowac pliku fxml o nazwie " + fxmlPath);
        }

        return rootNode;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
