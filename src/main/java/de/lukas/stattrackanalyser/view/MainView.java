package de.lukas.stattrackanalyser.view;

import de.lukas.stattrackanalyser.StatTrackApplication;
import de.lukas.stattrackanalyser.viewModel.MainViewModel;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class MainView extends BorderPane {
    private final MainViewModel mainViewModel;

    public MainView(Stage stage) {
        mainViewModel = new MainViewModel(stage);

        initialize();
    }

    private void initialize() {
        this.getStylesheets().add(StatTrackApplication.MAIN_STYLESHEET);
        this.setTop(generateMenuBar());
    }

    private Node generateMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("_File");
        MenuItem loadFile = new MenuItem("_Load JSON");
        loadFile.setOnAction(event -> {
            switch (mainViewModel.loadJson()) {
                case FILE_ERROR -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load file.");
                    alert.showAndWait();
                }
                case JSON_ERROR -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to read JSON.");
                    alert.showAndWait();
                }
                case SUCCESS -> {
                    this.setBottom(new BottomStatsView(mainViewModel.getJsonHolder()));
                    this.setCenter(new DataView(mainViewModel.getJsonHolder()));
                }
            }
        });

        MenuItem exportAsCSV = new MenuItem("_Export as CSV");
        exportAsCSV.setOnAction(e -> {
            try {
                mainViewModel.exportAsCSV();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't export file.");
                alert.showAndWait();
                System.out.println("Failed to save file: " + ex);
            }
        });
        exportAsCSV.disableProperty().bind(mainViewModel.holdsJson().not());

        MenuItem exit = new MenuItem("E_xit");
        exit.setOnAction(event -> mainViewModel.exit());

        menuFile.getItems().addAll(loadFile, exportAsCSV, new SeparatorMenuItem(), exit);

        menuBar.getMenus().add(menuFile);

        return menuBar;
    }


}
