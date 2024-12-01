package com.example.celestial3d;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class SettingsController {
    @FXML
    private CheckBox planetTrailCheckBox;
    @FXML
    private CheckBox enableCartesianPlaneCheckBox;
    @FXML
    private Slider fovSlider;

    @FXML
    private Slider gravityScaleSlider;
    @FXML
    public void initialize() {
        SimulationSingleton.getInstance().enableTrailVisualizer.bind(planetTrailCheckBox.selectedProperty());
        SimulationSingleton.getInstance().enableCartesianPlane.bind(enableCartesianPlaneCheckBox.selectedProperty());

        fovSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SimulationSingleton.getInstance().cameraFOV.setValue(newValue);
        });

        gravityScaleSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
             Constants.G = 0.0001 * newValue.doubleValue();
        }));
    }
}
