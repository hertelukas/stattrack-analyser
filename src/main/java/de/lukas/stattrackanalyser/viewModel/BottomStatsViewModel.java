package de.lukas.stattrackanalyser.viewModel;

import de.lukas.stattrackanalyser.model.JsonHolder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BottomStatsViewModel {

    private final JsonHolder jsonHolder;

    public BottomStatsViewModel(JsonHolder jsonHolder) {
        this.jsonHolder = jsonHolder;
    }

    public StringProperty amountProperty() {
        return new SimpleStringProperty(jsonHolder.getAmountOfEntries() + " Entries");
    }
}
