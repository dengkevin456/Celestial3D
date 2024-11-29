package com.example.celestial3d.windows;

import com.example.celestial3d.Constants;
import com.example.celestial3d.Planet;
import com.example.celestial3d.SimulationSingleton;
import com.example.celestial3d.controls.NumberTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;

public class CelestialBodyConfigurationWindow extends TabPane {
    private static final Font titleFont = new Font(SimulationSingleton.getInstance().globalFontName, 40);
    private static final Font descriptiveFont = new Font(SimulationSingleton.getInstance().globalFontName, 30);

    public CelestialBodyConfigurationWindow() {
        getTabs().addAll(generateCreationTab());
    }

    public Tab generateCreationTab() {
        Tab creationTab = new Tab("Creation");
        creationTab.setClosable(false);
        creationTab.setContent(creationContent());
        creationTab.setStyle("-fx-font-weight: bold");
        return creationTab;
    }

    private VBox creationContent() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        Label creationTitle = new Label("Celestial creation wizard");
        creationTitle.setFont(titleFont);
        creationTitle.setStyle("-fx-font-weight: bold");

        GridPane gridPane = new GridPane(10, 10);

        Label planetName = new Label("Name: ");
        planetName.setFont(descriptiveFont);
        TextField nameField = new TextField();

        Label planetSizeLabel = new Label("Size: ");
        planetSizeLabel.setFont(descriptiveFont);
        NumberTextField planetSizeInput = new NumberTextField();

        Button useCameraPositionButton = new Button("Use camera position");

        Label xLabel = new Label("Pos X: ");
        Label yLabel = new Label("Pos Y: ");
        Label zLabel = new Label("Pos Z: ");

        NumberTextField xField = new NumberTextField();
        NumberTextField yField = new NumberTextField();
        NumberTextField zField = new NumberTextField();

        final int MAX_SIZE = 60;
        xField.setMaxSize(MAX_SIZE, 20);
        xField.setPrefWidth(MAX_SIZE);
        yField.setMaxSize(MAX_SIZE, 20);
        yField.setPrefWidth(MAX_SIZE);
        zField.setMaxSize(MAX_SIZE, 20);
        zField.setPrefWidth(MAX_SIZE);

        NumberTextField sxField = new NumberTextField();
        NumberTextField syField = new NumberTextField();
        NumberTextField szField = new NumberTextField();

        Label sxLabel = new Label("Speed X: ");
        Label syLabel = new Label("Speed Y: ");
        Label szLabel = new Label("Speed Z: ");

        sxField.setMaxSize(MAX_SIZE, 20);
        sxField.setPrefWidth(MAX_SIZE);
        syField.setMaxSize(MAX_SIZE, 20);
        syField.setPrefWidth(MAX_SIZE);
        szField.setMaxSize(MAX_SIZE, 20);
        szField.setPrefWidth(MAX_SIZE);


        Label celestialColor = new Label("Color: ");
        celestialColor.setFont(descriptiveFont);
        ColorPicker picker = new ColorPicker(Color.BLACK);

        Label textureLabel = new Label("Texture: ");
        String texturePath = Constants.DEFAULT_TEXTURE_PATH;
        FileChooser fileDialog = new FileChooser();
        Button selectFileButton = new Button("Select file...");

        selectFileButton.setOnAction(event -> {
            fileDialog.showOpenDialog(selectFileButton.getScene().getWindow());
        });

        fileDialog.setTitle("Select Texture...");
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG files", "*.png"),
                new FileChooser.ExtensionFilter("JPG files", "*.jpg")
        );
        FileChooser.ExtensionFilter filter = fileDialog.getSelectedExtensionFilter();
        if (filter != null) {
            selectFileButton.setText(filter.getDescription());
        }

        CheckBox isStatic = new CheckBox("Is celestial static?");

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(planetName, 0, 0);
        gridPane.add(nameField, 1, 0);

        gridPane.add(planetSizeLabel, 0, 1);
        gridPane.add(planetSizeInput, 1, 1);

        gridPane.addRow(2, xLabel, xField, useCameraPositionButton);
        gridPane.addRow(3, yLabel, yField);
        gridPane.addRow(4, zLabel, zField);
        gridPane.addRow(5, celestialColor, picker);
        gridPane.addRow(6, textureLabel, selectFileButton);
        gridPane.addRow(7, sxLabel, sxField);
        gridPane.addRow(8, syLabel, syField);
        gridPane.addRow(9, szLabel, szField);
        gridPane.addRow(10, isStatic);

        Button createButton = new Button("Create celestial body");
        createButton.setFont(descriptiveFont);

        Label createButtonLabel = new Label("Please fill in all the labels correctly (respect the size!)");
        createButtonLabel.setFont(new Font(SimulationSingleton.getInstance().globalFontName, 12));
        createButtonLabel.setStyle("-fx-text-fill: rgb(255, 0, 0)");


        textureLabel.setFont(descriptiveFont);
        textureLabel.disableProperty().bind(isStatic.selectedProperty().not());
        selectFileButton.disableProperty().bind(isStatic.selectedProperty().not());
        celestialColor.disableProperty().bind(isStatic.selectedProperty());
        picker.disableProperty().bind(isStatic.selectedProperty());

        createButtonLabel.visibleProperty().bind(createButton.disabledProperty());

        createButton.disableProperty().bind(
                textFieldInvalid(nameField, 6).or(
                    textFieldInvalid(planetSizeInput, 6)
                )
        );

        createButton.setOnAction(event -> {
            Planet celestialBody = new Planet(nameField.textProperty().getValue(), SimulationSingleton.getInstance().planetList, 300, 0, 0,
                    -1, -1, 2, 10, 25, isStatic.isSelected());
            celestialBody.setColor(picker.getValue());
            SimulationSingleton.getInstance().planetList.add(
                    celestialBody
            );
            SimulationSingleton.getInstance().group.getChildren().add(SimulationSingleton.getInstance().planetList.getLast().getSphereGroup());
        });

        vbox.getChildren().addAll(creationTitle, gridPane, createButtonLabel, createButton);
        return vbox;
    }


    private static BooleanBinding textFieldInvalid(TextField field, int length) {
        return Bindings.isEmpty(field.textProperty()).or(Bindings.lessThan(length, field.textProperty().length()));
    }

}
