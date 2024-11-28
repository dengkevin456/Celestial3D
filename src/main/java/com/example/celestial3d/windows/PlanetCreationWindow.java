package com.example.celestial3d.windows;

import com.example.celestial3d.Planet;
import com.example.celestial3d.SimulationSingleton;
import com.example.celestial3d.controls.NumberTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PlanetCreationWindow extends VBox {
    private static final Font titleFont = new Font(SimulationSingleton.getInstance().globalFontName, 40);
    private static final Font subtitleFont = new Font(SimulationSingleton.getInstance().globalFontName, 20);
    public PlanetCreationWindow() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        Label label = new Label("Planet creation wizard");
        label.setFont(titleFont);
        label.setStyle("-fx-font-weight: bold");

        Label sizeLabel = new Label("Size");
        sizeLabel.setFont(subtitleFont);
        sizeLabel.setStyle("-fx-font-weight: bold");

        NumberTextField planetSizeTextField = new NumberTextField();
        planetSizeTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (planetSizeTextField.getText().length() > 7) {
                    String st = planetSizeTextField.getText().substring(0, 7);
                    planetSizeTextField.setText(st);
                }
            }
        });
        planetSizeTextField.setMinSize(30, 20);
        Button button = new Button("Add planet");
        button.setFont(subtitleFont);

        getChildren().addAll(label, sizeLabel, planetSizeTextField, button);

        button.setOnAction((event) -> {
            SimulationSingleton.getInstance().planetList.add(
                    new Planet("Earth2", SimulationSingleton.getInstance().planetList, 300, 0, 0,
                            -1, -1, 2, 10, 25, false)
            );
            SimulationSingleton.getInstance().group.getChildren().add(SimulationSingleton.getInstance().planetList.getLast().getSphereGroup());
        });
    }
}
