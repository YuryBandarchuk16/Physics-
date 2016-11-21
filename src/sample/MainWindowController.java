package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/**
 * Created by Viktoria on 21.11.16.
 */

public class MainWindowController {

    @FXML
    private static Canvas canvas;

    /*
    public static void initCanvas() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    */

    @FXML
    private Button backButton;

    @FXML
    private void backButtonClicked() { MainWindow.runMain(); }
}
