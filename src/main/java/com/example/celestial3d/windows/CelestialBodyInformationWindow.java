package com.example.celestial3d.windows;

import com.example.celestial3d.FirstPersonCamera;
import com.example.celestial3d.Planet;
import com.example.celestial3d.SimulationSingleton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public class CelestialBodyInformationWindow extends ScrollPane {
    private static final Font titleFont = new Font(SimulationSingleton.getInstance().globalFontName, 35);
    private FirstPersonCamera firstPersonCamera;
    private FlowPane flowPane = new FlowPane();
    private VBox vbox = new VBox(10);
    private ChoiceBox<Planet> choiceBox = new ChoiceBox<>();
    public CelestialBodyInformationWindow(FirstPersonCamera firstPersonCamera) {
        setMaxWidth(350);
        this.firstPersonCamera = firstPersonCamera;
        this.flowPane.setAlignment(Pos.CENTER);
        configureVBox();
        updateChoiceBoxBasedOnNumberOfPlanets();
        setContent(flowPane);
    }

    private void configureVBox() {
        Label titleLabel = new Label("Celestial information");
        titleLabel.setFont(titleFont);
        this.flowPane.setOrientation(Orientation.VERTICAL);
        this.flowPane.getChildren().addAll(titleLabel, this.choiceBox, this.vbox);
        vbox.setPadding(new Insets(5));
        vbox.disableProperty().bind(Bindings.isNull(this.choiceBox.getSelectionModel().selectedItemProperty()));

        Label mass = new Label("0"), radius = new Label("0");

        mass.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = choiceBox.getSelectionModel().getSelectedItem().getMass() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));

        radius.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = choiceBox.getSelectionModel().getSelectedItem().getRadius() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));

        HBox massRadiusHBox = new HBox(10, new Label("Mass: "), mass, new Label("Radius: "), radius);

        Label xPos = new Label("0"), yPos = new Label("0"), zPos = new Label("0");
        HBox positionHBox = new HBox(10, new Label("Position (x, y, z): "), xPos, yPos, zPos);

        xPos.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = (int) choiceBox.getSelectionModel().getSelectedItem().getX() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));

        yPos.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = (int) choiceBox.getSelectionModel().getSelectedItem().getY() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));

        zPos.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = (int) choiceBox.getSelectionModel().getSelectedItem().getZ() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));

        Button goToPosition = new Button("Go to celestial body position");
        goToPosition.setOnAction(event -> {
            firstPersonCamera.goToPosition(Integer.parseInt(xPos.getText()), Integer.parseInt(yPos.getText()),
                    Integer.parseInt(zPos.getText()));
        });

        Label xSpeed = new Label("0"), ySpeed = new Label("0"), zSpeed = new Label("0");
        HBox speedHBox = new HBox(10, new Label("Speed: (x, y, z): "), xSpeed, ySpeed, zSpeed);
        xSpeed.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = choiceBox.getSelectionModel().getSelectedItem().getSx() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));
        ySpeed.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = choiceBox.getSelectionModel().getSelectedItem().getSy() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));
        zSpeed.textProperty().bind(Bindings.createStringBinding(() -> {
            String selected = "NaN";
            if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                selected = choiceBox.getSelectionModel().getSelectedItem().getSz() + "";
            }
            return selected;
        }, choiceBox.getSelectionModel().selectedItemProperty()));

        vbox.getChildren().addAll(massRadiusHBox, new Separator(Orientation.HORIZONTAL), positionHBox, goToPosition,
                new Separator(Orientation.HORIZONTAL), speedHBox);
    }


    private void updateChoiceBoxBasedOnNumberOfPlanets() {
        this.choiceBox.itemsProperty().bind(new SimpleListProperty<>(SimulationSingleton.getInstance().planetList));
    }
}
