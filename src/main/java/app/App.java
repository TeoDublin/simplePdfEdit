package app;

import app.classes.ResourceUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(ResourceUtils.getResource("app/views/viewMain.fxml"));
        Scene scene = new Scene(root, 1000, 800);

        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(ResourceUtils.getResourceAsExternalForm("app/css/style.css"));

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
