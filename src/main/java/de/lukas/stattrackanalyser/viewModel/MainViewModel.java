package de.lukas.stattrackanalyser.viewModel;

import de.lukas.stattrackanalyser.model.JsonHolder;
import de.lukas.stattrackanalyser.model.LoadStatus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainViewModel {

    private Stage stage;
    private JsonHolder jsonHolder;
    private final BooleanProperty holdsJson;

    public MainViewModel(Stage stage) {
        this.stage = stage;

        holdsJson = new SimpleBooleanProperty(false);

        jsonHolder = new JsonHolder();
    }

    public JsonHolder getJsonHolder() {
        return jsonHolder;
    }

    public LoadStatus loadJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setTitle("Load JSON file");
        LoadStatus result = jsonHolder.loadFile(fileChooser.showOpenDialog(stage));
        holdsJson.set(result == LoadStatus.SUCCESS);
        return result;
    }

    public void exportAsCSV() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv", "*.csv"));
        fileChooser.setTitle("Export as CSV");

        File saveLocation = fileChooser.showSaveDialog(stage);
        BufferedWriter writer = new BufferedWriter(new FileWriter(saveLocation));
        writer.write(jsonHolder.getAsCSV());
        writer.close();
    }


    public BooleanProperty holdsJson() {
        return holdsJson;
    }

    public void exit() {
        System.exit(0);
    }

}
