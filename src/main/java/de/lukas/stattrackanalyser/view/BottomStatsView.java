package de.lukas.stattrackanalyser.view;

import de.lukas.stattrackanalyser.model.JsonHolder;
import de.lukas.stattrackanalyser.viewModel.BottomStatsViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BottomStatsView extends HBox {

    private final BottomStatsViewModel bottomStatsViewModel;

    public BottomStatsView(JsonHolder jsonHolder) {
        bottomStatsViewModel = new BottomStatsViewModel(jsonHolder);

        initialize();
    }

    private void initialize() {
        Label amountLabel = new Label();
        amountLabel.textProperty().bind(bottomStatsViewModel.amountProperty());


        this.getChildren().add(amountLabel);
    }
}
