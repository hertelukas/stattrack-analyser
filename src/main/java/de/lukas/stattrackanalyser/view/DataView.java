package de.lukas.stattrackanalyser.view;

import de.lukas.stattrackanalyser.StatTrackApplication;
import de.lukas.stattrackanalyser.model.ChartType;
import de.lukas.stattrackanalyser.model.DataType;
import de.lukas.stattrackanalyser.model.Entry;
import de.lukas.stattrackanalyser.model.JsonHolder;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
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
import java.util.*;

public class DataView extends HBox {
    private final JsonHolder jsonHolder;
    private String xAxisKey;
    private ChartType chartType = ChartType.LineChart;
    private boolean hasChart = false;

    private static final String DATE = "Date";
    private static final LocalDateTime EPOCH_START = LocalDateTime.parse("1970-01-01T00:00:00.0");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;


    //UI Components
    private ChoiceBox<String> xChoiceBox;
    private final Map<ChoiceBox<String>, String> yChoiceBoxKeyMap;
    private final VBox yAxisBox;
    private final VBox xAxisBox;

    public DataView(JsonHolder jsonHolder) {
        this.getStylesheets().add(StatTrackApplication.MAIN_STYLESHEET);
        this.jsonHolder = jsonHolder;

        yChoiceBoxKeyMap = new HashMap<>();
        xAxisBox = new VBox();
        yAxisBox = new VBox();

        initialize();
    }

    private void initialize() {
        //General Design
        this.setPadding(new Insets(10));

        Label title = new Label("Setup");
        title.getStyleClass().add("h1");

        //Setup of the menu box
        VBox menuHolder = new VBox();
        menuHolder.setSpacing(20);

        //Choose a chart type, calls initChoiceBoxes();
        VBox chartTypeBox = new VBox();
        chartTypeBox.setSpacing(5);
        Label chartTypeLabel = new Label("Chart type");
        ChoiceBox<ChartType> chartTypeChoiceBox = new ChoiceBox<>(FXCollections.observableList(Arrays.stream(ChartType.values()).toList()));
        chartTypeChoiceBox.setValue(chartType);
        chartTypeChoiceBox.setOnAction(e -> {
            chartType = chartTypeChoiceBox.getValue();
            initChoiceBoxes();
        });
        chartTypeBox.getChildren().addAll(chartTypeLabel, chartTypeChoiceBox);

        //The xAxisBox and yAxis box hold all configuration UI elements to edit these
        xAxisBox.setSpacing(5);
        yAxisBox.setSpacing(5);

        Label xAxisLabel = new Label("x-Axis");
        xAxisBox.getChildren().add(xAxisLabel);

        Label yAxisLabel = new Label("y-Axis");
        yAxisBox.getChildren().add(yAxisLabel);


        initChoiceBoxes();

        menuHolder.getChildren().addAll(title, chartTypeBox, xAxisBox, yAxisBox);


        this.getChildren().add(0, menuHolder);
    }

    //initChoiceBoxes sets up one choice box per axis
    private void initChoiceBoxes() {

        //Remove all x choice boxes, skip label
        while (xAxisBox.getChildren().size() > 1) {
            xAxisBox.getChildren().remove(xAxisBox.getChildren().size() - 1);
        }

        xChoiceBox = new ChoiceBox<>();
        xChoiceBox.setOnAction(event -> {
            xAxisKey = xChoiceBox.getValue();
            updateChart();
        });
        xAxisBox.getChildren().add(xChoiceBox);


        //Remove all y choice boxes, skip label
        while (yAxisBox.getChildren().size() > 1) {
            yAxisBox.getChildren().remove(yAxisBox.getChildren().size() - 1);
        }

        //Remove all y choice boxes from the map
        for (ChoiceBox<String> stringChoiceBox : yChoiceBoxKeyMap.keySet()) {
            yChoiceBoxKeyMap.remove(stringChoiceBox);
        }

        ChoiceBox<String> firstYBox = new ChoiceBox<>();
        yChoiceBoxKeyMap.put(firstYBox, "");
        firstYBox.setOnAction(event -> {
            yChoiceBoxKeyMap.put(firstYBox, firstYBox.getValue());
            updateChart();
        });
        yAxisBox.getChildren().add(firstYBox);

        xChoiceBox.getItems().removeIf(item -> true);

        //Populate the choice boxes based on the line chart type and add special options
        switch (chartType) {
            case LineChart -> {
                xChoiceBox.getItems().addAll(jsonHolder.getUniqueKeys(e -> e.getDataType() == DataType.NUMBER));
                xChoiceBox.getItems().add(0, DATE);
                for (ChoiceBox<String> yChoiceBox : yChoiceBoxKeyMap.keySet()) {
                    yChoiceBox.getItems().addAll(jsonHolder.getUniqueKeys(e -> e.getDataType() == DataType.NUMBER));
                }

                //Button to append new y-axis choice boxes
                Button addYChoiceBox = new Button("Add field");
                addYChoiceBox.setOnAction(e -> {
                    int index = yAxisBox.getChildren().size() - 1; // Put the new choice box in front of the button
                    ChoiceBox<String> temp = new ChoiceBox<>();
                    temp.getItems().addAll(jsonHolder.getUniqueKeys(field -> field.getDataType() == DataType.NUMBER));
                    yChoiceBoxKeyMap.put(temp, "");
                    temp.setOnAction(event -> {
                        yChoiceBoxKeyMap.put(temp, temp.getValue());
                        updateChart();
                    });
                    yAxisBox.getChildren().add(index, temp);
                });
                yAxisBox.getChildren().add(addYChoiceBox);
            }
            case BarChart -> xChoiceBox.getItems().addAll(jsonHolder.getUniqueKeys(e -> e.getDataType() == DataType.STRING || e.getDataType() == DataType.BOOLEAN));
        }

        //Select the first values
        if (xChoiceBox.getItems().size() > 0) {
            xChoiceBox.setValue(xChoiceBox.getItems().get(0));
        }

        for (ChoiceBox<String> yChoiceBox : yChoiceBoxKeyMap.keySet()) {
            if (yChoiceBox.getItems().size() > 0) {
                yChoiceBox.setValue(yChoiceBox.getItems().get(0));
            }
        }
    }

    private void updateChart() {
        if (xAxisKey != null && !xAxisKey.isEmpty()) {
            if (hasChart) {
                // Remove the last object (the chart)
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

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        for (String yAxisKey : yChoiceBoxKeyMap.values()) {

            // Handle dates
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(yAxisKey);

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

            lineChart.getData().add(series);
        }
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
