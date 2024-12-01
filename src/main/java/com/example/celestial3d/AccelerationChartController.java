package com.example.celestial3d;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
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
    private ChoiceBox<Planet> immovableCelestialBodyChoiceBox;

    @FXML
    private LineChart<Number, Number> lineChart;

    private BooleanProperty animationPlaying = new SimpleBooleanProperty(true);
    @FXML
    private Button startStopAnimationButton;

    private double time = 0;

    @FXML
    public void initialize() {
        updateChoiceBoxesBasedOnNumberOfPlanets();
        updateSecondChoiceBoxBasedOnNumberOfPlanets();
        lineChart.disableProperty().bind(Bindings.isNull(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty()));
        updateChart();

        celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateChart();
        });

        immovableCelestialBodyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateChart();
        });

        animationPlaying.addListener((observable, oldValue, newValue) -> {
            if (newValue) startStopAnimationButton.setText("Stop animation");
            else startStopAnimationButton.setText("Start animation");
        });
    }

    private void updateChart() {
        if (celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get() == null
        || immovableCelestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get() == null) return;
        time = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().clear();
        series.getData().clear();
        timeAxis.setTickUnit(1);
        lineChart.getData().add(series);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            double acceleration = celestialBodyChoiceBox.getSelectionModel().getSelectedItem().getVelocitySquared() /
                    getRadius(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get().getX(),
                            celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get().getY(),
                            celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get().getZ(),
                            immovableCelestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get().getX(),
                            immovableCelestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get().getY(),
                            immovableCelestialBodyChoiceBox.getSelectionModel().selectedItemProperty().get().getZ())
                    ;
            series.getData().add(new XYChart.Data<>(time, acceleration));
            if (series.getData().size() > 90) {
                series.getData().clear();
                time = 0;
            }
            time++;
        }));

        timeline.stop();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startStopAnimationButton.setOnAction(event -> {
            animationPlaying.setValue(!animationPlaying.get());
            if (animationPlaying.get()) timeline.play();
            else timeline.stop();
        });
    }

    private double getRadius(double x1, double y1, double z1, double x2, double y2, double z2) {
        double radius = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
        return Math.max(radius, 0.01);
    }

    private void updateChoiceBoxesBasedOnNumberOfPlanets() {
        FilteredList<Planet> filteredPlanetList = new FilteredList<>(SimulationSingleton.getInstance().planetList);
        filteredPlanetList.setPredicate(planet -> !planet.imovable);
        celestialBodyChoiceBox.itemsProperty().bind(new SimpleListProperty<>(filteredPlanetList));
    }

    private void updateSecondChoiceBoxBasedOnNumberOfPlanets() {
        FilteredList<Planet> filteredPlanetList = new FilteredList<>(SimulationSingleton.getInstance().planetList);
        filteredPlanetList.setPredicate(planet -> planet.imovable);
        immovableCelestialBodyChoiceBox.itemsProperty().bind(new SimpleListProperty<>(filteredPlanetList));
    }

}
