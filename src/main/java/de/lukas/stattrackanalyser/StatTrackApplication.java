package de.lukas.stattrackanalyser;

import de.lukas.stattrackanalyser.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StatTrackApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new MainView(stage), 960, 540);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Stat Track Analyser");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}