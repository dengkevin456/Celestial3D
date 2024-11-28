package com.example.celestial3d.windows;

import com.example.celestial3d.SimulationSingleton;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SkyboxColorPicker extends VBox {
    private static final Font TITLE_FONT = new Font("Arial", 40);
    private static final Font DESCRIPTIVE_FONT = new Font("Arial", 20);
    public SkyboxColorPicker() {
        setSpacing(20);
        setAlignment(Pos.CENTER);
        Label label = new Label("Skybox Color picker");
        label.setFont(TITLE_FONT);
        label.setStyle("-fx-font-weight: bold");

        Label redLabel = new Label("RED");
        redLabel.setFont(DESCRIPTIVE_FONT);
        Slider redSlider = new Slider(0, 255, 0);
        redSlider.setBlockIncrement(1);
        redSlider.setMinorTickCount(1);
        redSlider.setMajorTickUnit(5);
        redSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            SimulationSingleton.getInstance().skyboxRED.setValue(newValue);
        });

        Label greenLabel = new Label("GREEN");
        greenLabel.setFont(DESCRIPTIVE_FONT);
        Slider greenSlider = new Slider(0, 255, 0);
        greenSlider.setBlockIncrement(1);
        greenSlider.setMinorTickCount(1);
        greenSlider.setMajorTickUnit(5);
        greenSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            SimulationSingleton.getInstance().skyboxGreen.setValue(newValue);
        });

        Label blueLabel = new Label("B");
        blueLabel.setFont(DESCRIPTIVE_FONT);
        Slider blueSlider = new Slider(0, 255, 0);
        blueSlider.setSnapToTicks(true);
        blueSlider.setBlockIncrement(1);
        blueSlider.setMinorTickCount(1);
        blueSlider.setMajorTickUnit(5);
        blueSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            SimulationSingleton.getInstance().skyboxBlue.setValue(newValue);
        });

        getChildren().addAll(label, redLabel, redSlider, greenLabel, greenSlider, blueLabel, blueSlider);
    }


}
