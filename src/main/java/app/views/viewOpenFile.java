package app.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.File;

public class viewOpenFile {

    @FXML public BorderPane root;
    @FXML private Pane pdfLoad;

    public void loadPdf(File file) {
        pdfLoad.getChildren().clear();

        Label label = new Label("Loaded: " + file.getAbsolutePath());
        label.getStyleClass().add("pdf-label");

        pdfLoad.getChildren().add(label);
    }
}
