package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by YuryBandarchuk on 15.11.16.
 */

public class MainMenuController {

    @FXML
    private Button dynamicsButton;

    @FXML
    public void dynamicsButtonClicked() {
        Main.runDynamics();
    }
}
