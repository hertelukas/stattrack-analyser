package de.lukas.stattrackanalyser.view;

import de.lukas.stattrackanalyser.model.ChartType;
import de.lukas.stattrackanalyser.model.DataType;
import de.lukas.stattrackanalyser.model.Entry;
import de.lukas.stattrackanalyser.model.JsonHolder;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataView extends HBox {
    private final JsonHolder jsonHolder;
    private String xAxisKey;
    private String yAxisKey;
    private ChartType chartType = ChartType.LineChart;
    private boolean hasChart = false;

    private static final String DATE = "Date";
    private static final LocalDateTime EPOCH_START = LocalDateTime.parse("1970-01-01T00:00:00.0");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private ChoiceBox<String> xChoiceBox;
    private ChoiceBox<String> yChoiceBox;

    public DataView(JsonHolder jsonHolder) {
        this.jsonHolder = jsonHolder;

        initialize();
    }

    private void initialize() {
        VBox menuHolder = new VBox();

        Label chartTypeLabel = new Label("Chart type");
        ChoiceBox<ChartType> chartTypeChoiceBox = new ChoiceBox<>(FXCollections.observableList(Arrays.stream(ChartType.values()).toList()));
        chartTypeChoiceBox.setValue(chartType);
        chartTypeChoiceBox.setOnAction(e -> {
            chartType = chartTypeChoiceBox.getValue();
            updateChoiceBoxes();
        });

        Label xAxisLabel = new Label("x-Axis");
        xChoiceBox = new ChoiceBox<>();
        xChoiceBox.setOnAction(event -> {
            xAxisKey = xChoiceBox.getValue();
            updateChart();
        });

        Label yAxisLabel = new Label("y-Axis");
        yChoiceBox = new ChoiceBox<>();
        yChoiceBox.setOnAction(event -> {
            yAxisKey = yChoiceBox.getValue();
            updateChart();
        });

        updateChoiceBoxes();


        menuHolder.getChildren().addAll(chartTypeLabel, chartTypeChoiceBox, xAxisLabel, xChoiceBox, yAxisLabel, yChoiceBox);

        this.getChildren().add(0, menuHolder);
    }

    private void updateChoiceBoxes() {
        //Remove all items
        xChoiceBox.getItems().removeIf(item -> true);
        yChoiceBox.getItems().removeIf(item -> true);

        xChoiceBox.setDisable(false);
        yChoiceBox.setDisable(false);

        switch (chartType) {
            case LineChart -> {
                xChoiceBox.getItems().addAll(jsonHolder.getUniqueKeys(e -> e.getDataType() == DataType.NUMBER));
                xChoiceBox.getItems().add(0, DATE);
                yChoiceBox.getItems().addAll(jsonHolder.getUniqueKeys(e -> e.getDataType() == DataType.NUMBER));
                yChoiceBox.getItems().add(0, DATE);

            }
            case BarChart -> {
                xChoiceBox.getItems().addAll(jsonHolder.getUniqueKeys(e -> e.getDataType() == DataType.STRING || e.getDataType() == DataType.BOOLEAN));
                yChoiceBox.setDisable(true);
            }
        }

        if (xChoiceBox.getItems().size() > 0) {
            xChoiceBox.setValue(xChoiceBox.getItems().get(0));
        }

        if (yChoiceBox.getItems().size() > 0) {
            yChoiceBox.setValue(yChoiceBox.getItems().get(0));
        }
    }

    private void updateChart() {
        if (xAxisKey != null && !xAxisKey.isEmpty() && yAxisKey != null && !yAxisKey.isEmpty()) {
            if (hasChart) {
                // Remove the last object
                this.getChildren().remove(this.getChildren().size() - 1);
            }
            switch (chartType) {
                case LineChart -> {
                    Node chart = generateLineChart();
                    HBox.setHgrow(chart, Priority.ALWAYS);
                    this.getChildren().add(chart);
                    hasChart = true;
                }
                case BarChart -> {
                    System.out.println("Not yet implemented");
                    hasChart = false;
                }
            }
        }
    }

    private Node generateLineChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(xAxisKey);
        yAxis.setLabel(yAxisKey);

        // Handle dates
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        List<Number> xAxisData = new ArrayList<>();
        List<Number> yAxisData = new ArrayList<>();

        getData(xAxisData, xAxisKey, xAxis);
        getData(yAxisData, yAxisKey, yAxis);

        for (int i = 0; i < xAxisData.size() && i < yAxisData.size(); i++) {
            if (xAxisData.get(i) == null || yAxisData.get(i) == null) {
                continue;
            }
            series.getData().add(new XYChart.Data<>(xAxisData.get(i), yAxisData.get(i)));
        }

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(series);

        return lineChart;
    }

    private void getData(List<Number> axisData, String axisKey, NumberAxis axis) {
        if (axisKey.equals(DATE)) {
            axis.setForceZeroInRange(false);
            axis.setTickLabelFormatter(new StringConverter<>() {
                @Override
                public String toString(Number object) {
                    return LocalDateTime.ofEpochSecond(object.longValue(), 0, ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
                }

                @Override
                public Number fromString(String string) {
                    return null;
                }
            });
            for (LocalDateTime date : jsonHolder.getDates()) {
                axisData.add(ChronoUnit.SECONDS.between(EPOCH_START, date));
            }
        } else {
            for (Entry.Field field : jsonHolder.getFields(axisKey, DataType.NUMBER)) {
                if (field == null) {
                    axisData.add(null);
                } else {
                    axisData.add(((Entry.NumberField) field).getValue());
                }
            }
        }
    }


}
