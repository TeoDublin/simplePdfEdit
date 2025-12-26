package app.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class viewMain implements Initializable {

    @FXML public ImageView logo;
    @FXML public Button btnOpen;

    @FXML private BorderPane root;
    @FXML private BorderPane centerHost;

    @FXML private HBox titleBar;
    @FXML private Button btnHome;
    @FXML private Button btnMin;
    @FXML private Button btnMax;
    @FXML private Button btnClose;

    @FXML private VBox homeView;
    @FXML private TabPane tabPane;

    private double xOffset = 0;
    private double yOffset = 0;

    private static final int RESIZE_MARGIN = 6;

    private boolean resizing = false;
    private double startX, startY, startWidth, startHeight;
    private double startStageX, startStageY;
    private ResizeDirection resizeDir = ResizeDirection.NONE;

    private enum ResizeDirection {
        NONE, LEFT, RIGHT, TOP, BOTTOM,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        titleBar.setOnMousePressed(this::handlePressed);
        titleBar.setOnMouseDragged(this::handleDragged);

        btnMin.setOnAction(e -> stage().setIconified(true));
        btnClose.setOnAction(e -> stage().close());
        btnMax.setOnAction(e -> stage().setMaximized(!stage().isMaximized()));

        btnHome.setOnAction(e -> showHome());
        btnOpen.setOnAction(e -> openFile());

        root.setOnMouseMoved(this::detectResizeZone);
        root.setOnMousePressed(this::startResize);
        root.setOnMouseDragged(this::doResize);
        root.setOnMouseReleased(e -> resizing = false);
    }

    private void openFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open PDF");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );

        File file = chooser.showOpenDialog(stage());
        if (file == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/viewOpenFile.fxml"));
            Node content = loader.load();

            viewOpenFile controller = loader.getController();
            controller.loadPdf(file);

            Tab tab = new Tab(file.getName());
            tab.setClosable(true);
            tab.setContent(content);

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            showTabs();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showTabs() {
        homeView.setVisible(false);
        homeView.setManaged(false);

        tabPane.setVisible(true);
        tabPane.setManaged(true);
    }

    private void showHome() {
        tabPane.setVisible(false);
        tabPane.setManaged(false);

        homeView.setVisible(true);
        homeView.setManaged(true);
    }

    private void handlePressed(MouseEvent e) {
        if (resizing) return;
        xOffset = e.getSceneX();
        yOffset = e.getSceneY();
    }

    private void handleDragged(MouseEvent e) {
        if (resizing) return;
        Stage stage = stage();
        stage.setX(e.getScreenX() - xOffset);
        stage.setY(e.getScreenY() - yOffset);
    }

    private void detectResizeZone(MouseEvent e) {
        Stage s = stage();
        double x = e.getX();
        double y = e.getY();
        double w = s.getWidth();
        double h = s.getHeight();

        ResizeDirection dir = ResizeDirection.NONE;

        boolean left = x < RESIZE_MARGIN;
        boolean right = x > w - RESIZE_MARGIN;
        boolean top = y < RESIZE_MARGIN;
        boolean bottom = y > h - RESIZE_MARGIN;

        if (left && top) dir = ResizeDirection.TOP_LEFT;
        else if (right && top) dir = ResizeDirection.TOP_RIGHT;
        else if (left && bottom) dir = ResizeDirection.BOTTOM_LEFT;
        else if (right && bottom) dir = ResizeDirection.BOTTOM_RIGHT;
        else if (left) dir = ResizeDirection.LEFT;
        else if (right) dir = ResizeDirection.RIGHT;
        else if (top) dir = ResizeDirection.TOP;
        else if (bottom) dir = ResizeDirection.BOTTOM;

        resizeDir = dir;
        stage().getScene().setCursor(cursorFor(dir));
    }

    private void startResize(MouseEvent e) {
        if (resizeDir == ResizeDirection.NONE) return;

        resizing = true;

        startX = e.getScreenX();
        startY = e.getScreenY();

        Stage s = stage();
        startStageX = s.getX();
        startStageY = s.getY();

        startWidth = s.getWidth();
        startHeight = s.getHeight();
    }

    private void doResize(MouseEvent e) {
        if (!resizing) return;

        Stage s = stage();
        double dx = e.getScreenX() - startX;
        double dy = e.getScreenY() - startY;

        switch (resizeDir) {
            case RIGHT -> s.setWidth(startWidth + dx);
            case BOTTOM -> s.setHeight(startHeight + dy);

            case BOTTOM_RIGHT -> {
                s.setWidth(startWidth + dx);
                s.setHeight(startHeight + dy);
            }

            case LEFT -> {
                s.setX(startStageX + dx);
                s.setWidth(startWidth - dx);
            }

            case TOP -> {
                s.setY(startStageY + dy);
                s.setHeight(startHeight - dy);
            }

            case TOP_LEFT -> {
                s.setX(startStageX + dx);
                s.setY(startStageY + dy);
                s.setWidth(startWidth - dx);
                s.setHeight(startHeight - dy);
            }

            case TOP_RIGHT -> {
                s.setY(startStageY + dy);
                s.setWidth(startWidth + dx);
                s.setHeight(startHeight - dy);
            }

            case BOTTOM_LEFT -> {
                s.setX(startStageX + dx);
                s.setWidth(startWidth - dx);
                s.setHeight(startHeight + dy);
            }
        }
    }

    private Cursor cursorFor(ResizeDirection dir) {
        return switch (dir) {
            case LEFT, RIGHT -> Cursor.H_RESIZE;
            case TOP, BOTTOM -> Cursor.V_RESIZE;
            case TOP_LEFT, BOTTOM_RIGHT -> Cursor.NW_RESIZE;
            case TOP_RIGHT, BOTTOM_LEFT -> Cursor.NE_RESIZE;
            default -> Cursor.DEFAULT;
        };
    }

    private Stage stage() {
        return (Stage) titleBar.getScene().getWindow();
    }
}
