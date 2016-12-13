package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.matchParser.MatchParser;

import java.awt.*;

/**
 * Created by Viktoria on 21.11.16.
 */
public class MainWindow extends Application {

    public static SandBox sandBox;
    private static Stage primaryStage;
    private static Thread mainThread;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        MainWindow.primaryStage = primaryStage;
        sandBox = new SandBox(new Dimension(800, 800), new MatchParser());
        mainThread = new Thread(sandBox);
        mainThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void close() {
        primaryStage.close();
    }

    public static void runMain() {
        Platform.runLater(() -> {
            try {
                new Main().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sandBox.close();
        mainThread.stop();
        close();
    }
}
