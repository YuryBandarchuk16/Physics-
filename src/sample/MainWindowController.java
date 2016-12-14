package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.matchParser.ParserRunner;

/**
 * Created by YuryBandarchuk on 21.11.16.
 */

public class MainWindowController {

    public static double G = 9.8;

    @FXML
    private ColorPicker graphColor, ballColor;

    private void createAlert(String textButton, String text) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setOnCloseRequest(e -> Platform.exit());
        Button b = new Button(textButton);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialogStage.close();
            }
        });
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(new Text(text), b).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.show();
        return;
    }

    @FXML
    private Button backButton;

    @FXML
    private TextField textField;

    @FXML
    private Button drawButton;

    @FXML
    private void drawButtonClicked() {
        String currentText = textField.getText();
        for (char character : currentText.toCharArray()) {
            if (character == ' ') {
                createAlert("OK", "This formula contains forbidden characters (spaces)!");
            }
            if (character == 'x') continue;
            if (character == '!') {
                createAlert("OK", "This formula contains forbidden characters (exclamation sign)!");
            }
            // to be continued
        }
        ParserRunner.setText(currentText);
        MainWindow.sandBox.graphIsReadyToBeDrawn = true;
        MainWindow.sandBox.reInit();
        MainWindow.sandBox.repaint();
        MainWindow.sandBox.ballColor = getColor(ballColor);
        MainWindow.sandBox.graphColor = getColor(graphColor);
        MainWindow.sandBox.balls.clear();
    }

    public java.awt.Color getColor(ColorPicker picker) {
        Color awtColor = picker.getValue();
        double r = awtColor.getRed() * 255.0;
        double g = awtColor.getGreen() * 255.0;
        double b = awtColor.getBlue() * 255.0;
        return new java.awt.Color((int)r, (int)g, (int)b);
    }

    @FXML
    private void stopClicked() {
    }

    @FXML
    private void backButtonClicked() {
        MainWindow.runMain();
    }
}
