package com.example.celestial3d;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.util.Duration;


public class AccelerationChartController {

    @FXML
    private NumberAxis timeAxis;
    @FXML
    private ChoiceBox<Planet> celestialBodyChoiceBox;

    @FXML
    private LineChart<Number, Number> lineChart;

    private BooleanProperty animationPlaying = new SimpleBooleanProperty(true);
    @FXML
    private Button startStopAnimationButton;

    private double time = 0;

    @FXML
    public void initialize() {
        updateChoiceBoxesBasedOnNumberOfPlanets();
        lineChart.disableProperty().bind(Bindings.isNull(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty()));
        updateChart();

        celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateChart();
        });

        animationPlaying.addListener((observable, oldValue, newValue) -> {
            if (newValue) startStopAnimationButton.setText("Stop animation");
            else startStopAnimationButton.setText("Start animation");
        });
    }

    private void updateChart() {
        if (celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get() == null) return;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        timeAxis.setTickUnit(1);
        lineChart.getData().add(series);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            double acceleration = celestialBodyChoiceBox.getSelectionModel().getSelectedItem().getAcceleration();
            series.getData().add(new XYChart.Data<>(time, acceleration));
            if (series.getData().size() > 90) {
                series.getData().clear();
                time = 0;
            }
            time++;
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startStopAnimationButton.setOnAction(event -> {
            animationPlaying.setValue(!animationPlaying.get());
            if (animationPlaying.get()) timeline.play();
            else timeline.stop();
        });
    }

    private void updateChoiceBoxesBasedOnNumberOfPlanets() {
        celestialBodyChoiceBox.itemsProperty().bind(new SimpleListProperty<>(SimulationSingleton.getInstance().planetList));
    }

}
