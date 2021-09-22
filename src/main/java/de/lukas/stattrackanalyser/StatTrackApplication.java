package de.lukas.stattrackanalyser;

import de.lukas.stattrackanalyser.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class StatTrackApplication extends Application {

    public static final String MAIN_STYLESHEET = "/main.css";

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new MainView(stage), 960, 540);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Stat Track Analyser");
        stage.show();
    }

    @Override
    public void init() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/static/Raleway-Regular.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/fonts/static/Raleway-Medium.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/fonts/static/Raleway-Bold.ttf"), 16);
    }

    public static void main(String[] args) {
        launch();
    }
}