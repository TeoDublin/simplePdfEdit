package app.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class viewMain implements Initializable {

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXButton btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Example behavior
        btnLogin.setOnAction(e -> {
            System.out.println("Email: " + txtEmail.getText());
        });
    }
}
