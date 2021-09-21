package de.lukas.stattrackanalyser.view;

import de.lukas.stattrackanalyser.viewModel.MainViewModel;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainView extends BorderPane {
    private final MainViewModel mainViewModel;
    private final Stage stage;

    public MainView(Stage stage) {
        mainViewModel = new MainViewModel();
        this.stage = stage;

        initialize();
    }

    private void initialize() {
        this.setTop(generateTopBar());
    }

    private Node generateTopBar() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("_File");
        MenuItem loadFile = new MenuItem("_Load JSON");
        // TODO: 9/21/21 move this to the mainViewModel & a separate JSON handler
        loadFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            fileChooser.setTitle("Load JSON file");
            File file = fileChooser.showOpenDialog(stage);
        });

        menuFile.getItems().addAll(loadFile);

        menuBar.getMenus().add(menuFile);

        return menuBar;
    }


}
