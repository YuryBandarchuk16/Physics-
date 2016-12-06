package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by YuryBandarchuk on 21.11.16.
 */

public class MainWindowController {

    @FXML
    private Button backButton;

    @FXML
    private TextField textField;

    @FXML
    private Button drawButton;

    @FXML
    private void drawButtonClicked() {
    }

    @FXML
    private void stopClicked() {
    }

    @FXML
    private void backButtonClicked() {
        MainWindow.runMain();
    }
}
