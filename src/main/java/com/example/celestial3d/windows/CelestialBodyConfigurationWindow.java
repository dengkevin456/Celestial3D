package com.example.celestial3d.windows;

import com.example.celestial3d.*;
import com.example.celestial3d.controls.NumberTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class CelestialBodyConfigurationWindow extends TabPane {
    private static final Font titleFont = new Font(SimulationSingleton.getInstance().globalFontName, 35);
    private static final Font descriptiveFont = new Font(SimulationSingleton.getInstance().globalFontName, 25);

    private static String texturePath = null;
    private FirstPersonCamera firstPersonCamera;
    public CelestialBodyConfigurationWindow(FirstPersonCamera firstPersonCamera) {
        setTabMinWidth(USE_COMPUTED_SIZE);
        setEffect(new InnerShadow());
        getTabs().addAll(generateCreationTab(), generateModificationTab(), generateDeletionTab());
        this.firstPersonCamera = firstPersonCamera;
    }

    public Tab generateCreationTab() {
        Tab creationTab = new Tab("Creation");
        creationTab.setClosable(false);
        creationTab.setContent(creationContent());
        creationTab.setStyle("-fx-font-weight: bold");
        return creationTab;
    }

    public Tab generateModificationTab() {
        Tab modificationTab = new Tab("Modification");
        modificationTab.setClosable(false);
        modificationTab.setStyle("-fx-font-weight: bold");

        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("modification-view.fxml"));
            ScrollPane parent = loader.load();
            modificationTab.setContent(parent);
        }
        catch (IOException e) {
            modificationTab.setContent(new Label("Unable to generate modification content!"));
        }

        return modificationTab;
    }

    public Tab generateDeletionTab() {
        Tab deletionTab = new Tab("Deletion");
        deletionTab.setClosable(false);
        deletionTab.setStyle("-fx-font-weight: bold");
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("deletion-view.fxml"));
            ScrollPane parent = loader.load();
            deletionTab.setContent(parent);
        }
        catch (IOException e) {
            deletionTab.setContent(new Label("Unable to generate deletion content!"));
        }
        return deletionTab;
    }

    private ScrollPane creationContent() {
        VBox vbox = new VBox();
        ScrollPane scrollPane = new ScrollPane(vbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        Label creationTitle = new Label("Celestial creation wizard");
        creationTitle.fontProperty().bind(new SimpleObjectProperty<Font>(titleFont));
        creationTitle.setStyle("-fx-font-weight: bold");

        GridPane gridPane = new GridPane(10, 10);
        gridPane.setPadding(new Insets(5));

        Label planetName = new Label("Name: ");
        planetName.setFont(descriptiveFont);
        TextField nameField = new TextField();

        Label planetSizeLabel = new Label("Size: ");
        planetSizeLabel.setFont(descriptiveFont);
        NumberTextField planetSizeInput = new NumberTextField();

        Label planetMassLabel = new Label("Mass: ");
        planetMassLabel.setFont(descriptiveFont);
        NumberTextField planetMassInput = new NumberTextField();

        Button useCameraPositionButton = new Button("Use camera position");

        Label xLabel = new Label("Pos X: ");
        Label yLabel = new Label("Pos Y: ");
        Label zLabel = new Label("Pos Z: ");

        NumberTextField xField = new NumberTextField(true);
        NumberTextField yField = new NumberTextField(true);
        NumberTextField zField = new NumberTextField(true);

        final int MAX_SIZE = 60;
        xField.setMaxSize(MAX_SIZE, 20);
        xField.setPrefWidth(MAX_SIZE);
        yField.setMaxSize(MAX_SIZE, 20);
        yField.setPrefWidth(MAX_SIZE);
        zField.setMaxSize(MAX_SIZE, 20);
        zField.setPrefWidth(MAX_SIZE);

        NumberTextField sxField = new NumberTextField(true);
        NumberTextField syField = new NumberTextField(true);
        NumberTextField szField = new NumberTextField(true);

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
        FileChooser fileDialog = new FileChooser();
        Button selectFileButton = new Button("Select file...");
        ImageView texturePreview = new ImageView();
        texturePreview.setPreserveRatio(true);
        texturePreview.setFitWidth(100);
        texturePreview.setFitHeight(100);


        selectFileButton.setOnAction(event -> {
            File selectedFile = fileDialog.showOpenDialog(selectFileButton.getScene().getWindow());
            if (selectedFile != null) {
                selectFileButton.setText(selectedFile.getName());
                texturePath = selectedFile.getAbsolutePath();
                texturePreview.setImage(new Image("file:" + selectedFile.getAbsolutePath()));
            }
            else {
                selectFileButton.setText("Select File...");
                texturePath = null;
                texturePreview.setImage(null);
            }
        });

        fileDialog.setTitle("Select Texture...");
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images files", "*.png", "*.jpg")
        );


        CheckBox isStatic = new CheckBox("Is celestial static?");

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(planetName, 0, 0);
        gridPane.add(nameField, 1, 0);

        gridPane.add(planetSizeLabel, 0, 1);
        gridPane.add(planetSizeInput, 1, 1);
        gridPane.addRow(2, planetMassLabel, planetMassInput);

        gridPane.addRow(3, xLabel, xField, useCameraPositionButton);
        gridPane.addRow(4, yLabel, yField);
        gridPane.addRow(5, zLabel, zField);
        gridPane.addRow(6, celestialColor, picker);
        gridPane.addRow(7, textureLabel, selectFileButton, texturePreview);
        gridPane.addRow(8, sxLabel, sxField);
        gridPane.addRow(9, syLabel, syField);
        gridPane.addRow(10, szLabel, szField);
        gridPane.addRow(11, isStatic);

        Button createButton = new Button("Create celestial body");
        createButton.setFont(descriptiveFont);

        Label createButtonLabel = new Label("Please fill in all the labels correctly (respect the size!)");
        createButtonLabel.setVisible(false);
        createButtonLabel.setFont(new Font(SimulationSingleton.getInstance().globalFontName, 12));
        createButtonLabel.setStyle("-fx-text-fill: rgb(255, 0, 0)");


        textureLabel.setFont(descriptiveFont);
        textureLabel.disableProperty().bind(isStatic.selectedProperty().not());
        selectFileButton.disableProperty().bind(isStatic.selectedProperty().not());
        celestialColor.disableProperty().bind(isStatic.selectedProperty());
        picker.disableProperty().bind(isStatic.selectedProperty());


        createButton.disableProperty().bind(
                textFieldInvalid(nameField, 6).or(
                    textFieldInvalid(planetSizeInput, 6)
                )
        );

        createButton.setOnAction(event -> {
            try {
                Planet celestialBody = new Planet(nameField.textProperty().getValue(), SimulationSingleton.getInstance().planetList,
                        Double.parseDouble(xField.getText()), Double.parseDouble(yField.getText()), Double.parseDouble(zField.getText()),
                        Double.parseDouble(!isStatic.isSelected() ? sxField.getText() : "0"),
                        Double.parseDouble(!isStatic.isSelected() ? syField.getText() : "0"),
                        Double.parseDouble(!isStatic.isSelected() ? szField.getText() : "0"), Double.parseDouble(planetSizeInput.getText()),
                        Double.parseDouble(planetMassInput.getText()), isStatic.isSelected());
                if (!isStatic.isSelected())
                    celestialBody.setColor(picker.getValue());
                else {
                    if (texturePath == null) {
                        celestialBody.setDefaultTexture();
                    }
                    else {
                        celestialBody.setTexture(texturePath);
                    }
                }
                SimulationSingleton.getInstance().planetList.add(
                        celestialBody
                );
                SimulationSingleton.getInstance().group.getChildren().add(SimulationSingleton.getInstance().planetList.getLast().getSphereGroup());
                createButtonLabel.setVisible(true);
                createButtonLabel.setStyle("-fx-text-fill: green");
                createButtonLabel.setText("Success!");
            }
            catch (NumberFormatException e) {
                createButtonLabel.setVisible(true);
                createButtonLabel.setStyle("-fx-text-fill: red");
                createButtonLabel.setText("Please ensure that everything is filled properly.");
            }
        });

        useCameraPositionButton.setOnAction(event -> {
            xField.setText((int) this.firstPersonCamera.getCameraX() + "");
            yField.setText((int) this.firstPersonCamera.getCameraY() + "");
            zField.setText((int) this.firstPersonCamera.getCameraZ() + "");
        });

        vbox.getChildren().addAll(creationTitle, gridPane, createButtonLabel, createButton);
        return scrollPane;
    }

    private static BooleanBinding textFieldInvalid(TextField field, int length) {
        return Bindings.isEmpty(field.textProperty()).or(Bindings.lessThan(length, field.textProperty().length()));
    }

}
