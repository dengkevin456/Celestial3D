package com.example.celestial3d.windows;

import com.example.celestial3d.SimulationSingleton;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SkyboxColorPicker extends VBox {
    private static final Font TITLE_FONT = new Font("Arial", 40);
    public SkyboxColorPicker() {
        setSpacing(20);
        setAlignment(Pos.CENTER);
        Label label = new Label("Skybox Color picker");
        label.setFont(TITLE_FONT);
        label.setStyle("-fx-font-weight: bold");

        ColorPicker picker = new ColorPicker();
        picker.setValue(SimulationSingleton.getInstance().skyboxColor.getValue());
        picker.valueProperty().addListener((observable, oldValue, newValue) -> {
            SimulationSingleton.getInstance().skyboxColor.setValue(newValue);
        });
        getChildren().addAll(label, picker);
    }


}
