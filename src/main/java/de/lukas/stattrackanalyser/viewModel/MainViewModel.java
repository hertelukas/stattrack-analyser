package de.lukas.stattrackanalyser.viewModel;

import de.lukas.stattrackanalyser.model.JsonHolder;
import de.lukas.stattrackanalyser.model.LoadStatus;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewModel {

    private Stage stage;
    private JsonHolder jsonHolder;

    public MainViewModel(Stage stage) {
        this.stage = stage;

        jsonHolder = new JsonHolder();
    }

    public JsonHolder getJsonHolder() {
        return jsonHolder;
    }

    public LoadStatus loadJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setTitle("Load JSON file");
        return jsonHolder.loadFile(fileChooser.showOpenDialog(stage));
    }

    public void exit() {
        System.exit(0);
    }

}
