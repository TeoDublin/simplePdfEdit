package app;

import app.classes.ResourceUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@SuppressWarnings("unused")

public class App extends Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(ResourceUtils.getResourceAsExternalForm("app/css/style.css"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
