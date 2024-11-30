package com.example.celestial3d;

import com.example.celestial3d.controls.NumberTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModificationViewController {

    @FXML
    private ChoiceBox<Planet> celestialBodyChoiceBox;

    @FXML
    private HBox celestialBodyColorHBox;

    @FXML
    private TextField celestialBodyNameTextField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private HBox massHBox;

    @FXML
    private VBox modificationVBox;

    @FXML
    private HBox radiusHBox;

    @FXML
    private Button updateCelestialBodyButton;

    @FXML
    private HBox xPositionHBox;

    @FXML
    private HBox xSpeedHBox;

    @FXML
    private HBox yPositionHBox;

    @FXML
    private HBox ySpeedHBox;

    @FXML
    private HBox zPositionHBox;

    @FXML
    private HBox zSpeedHBox;

    @FXML
    private CheckBox keeppositionXCheckBox;

    @FXML
    private CheckBox keeppositionYCheckBox;

    @FXML
    private CheckBox keeppositionZCheckBox;

    @FXML
    private CheckBox keepspeedXCheckBox;

    @FXML
    private CheckBox keepspeedYCheckBox;

    @FXML
    private CheckBox keepspeedZCheckBox;

    private NumberTextField radiusTextField = new NumberTextField();
    private NumberTextField massTextField = new NumberTextField();
    private NumberTextField posXTextField = new NumberTextField(true);
    private NumberTextField posYTextField = new NumberTextField(true);
    private NumberTextField posZTextField = new NumberTextField(true);
    private NumberTextField speedXTextField = new NumberTextField(true);
    private NumberTextField speedYTextField = new NumberTextField(true);
    private NumberTextField speedZTextField = new NumberTextField(true);


    @FXML
    public void initialize() {
        addTextFieldsToHBoxes();
        updateChoiceBoxesBasedOnNumberOfPlanets();
        handleDisableStateOfPositionAndSpeed();
        modificationVBox.disableProperty().bind(
                Bindings.isNull(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty())
        );
        updateCelestialBodyButton.disableProperty().bind(
                Bindings.isNull(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty()));


        celestialBodyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            celestialBodyColorHBox.visibleProperty().bind(new SimpleBooleanProperty(!newValue.imovable));
            if (!newValue.imovable)
                colorPicker.setValue(newValue.getMaterialColor());
            celestialBodyNameTextField.setText(newValue.getName());
            radiusTextField.setText(newValue.getRadius() + "");
            massTextField.setText(newValue.getMass() + "");
            posXTextField.setText(newValue.getX() + "");
            posYTextField.setText(newValue.getY() + "");
            posZTextField.setText(newValue.getZ() + "");
            speedXTextField.setText(newValue.getSx() + "");
            speedYTextField.setText(newValue.getSy() + "");
            speedZTextField.setText(newValue.getSz() + "");
        });
    }

    private void handleDisableStateOfPositionAndSpeed() {
        posXTextField.disableProperty().bind(keeppositionXCheckBox.selectedProperty());
        posYTextField.disableProperty().bind(keeppositionYCheckBox.selectedProperty());
        posZTextField.disableProperty().bind(keeppositionZCheckBox.selectedProperty());
        speedXTextField.disableProperty().bind(keepspeedXCheckBox.selectedProperty());
        speedYTextField.disableProperty().bind(keepspeedYCheckBox.selectedProperty());
        speedZTextField.disableProperty().bind(keepspeedZCheckBox.selectedProperty());
    }

    private List<TextField> getAllTextFieldsInClass() {
        List<TextField> textFields = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field: fields) {
            if (TextField.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true); // Allow access to private fields
                    TextField textField = (TextField) field.get(this);
                    if (textField != null) {
                        textFields.add(textField);
                    }
                }
                catch (IllegalAccessException ignored) {

                }
            }
        }
        return textFields;
    }

    private boolean areFieldsValid() {
        return getAllTextFieldsInClass().stream().map(TextField::getText).noneMatch(String::isEmpty);
    }
    public void updatePlanet(ActionEvent event) {
        if (!areFieldsValid()) return;
        celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setName(celestialBodyNameTextField.getText());
        celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setMass(Double.parseDouble(massTextField.getText()));
        celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setRadius(Double.parseDouble(radiusTextField.getText()));

        if (!keeppositionXCheckBox.isSelected())
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setX(Double.parseDouble(posXTextField.getText()));
        if (!keeppositionYCheckBox.isSelected())
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setY(-Double.parseDouble(posYTextField.getText()));
        if (!keeppositionZCheckBox.isSelected())
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setZ(Double.parseDouble(posZTextField.getText()));
        if (!keepspeedXCheckBox.isSelected())
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setSx(Double.parseDouble(speedXTextField.getText()));
        if (!keepspeedYCheckBox.isSelected())
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setSy(-Double.parseDouble(speedYTextField.getText()));
        if (!keepspeedZCheckBox.isSelected())
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setSz(Double.parseDouble(speedZTextField.getText()));

        if (!celestialBodyChoiceBox.getSelectionModel().getSelectedItem().imovable)
            celestialBodyChoiceBox.getSelectionModel().getSelectedItem().setColor(colorPicker.getValue());
    }

    private void updateChoiceBoxesBasedOnNumberOfPlanets() {
        celestialBodyChoiceBox.itemsProperty().bind(new SimpleListProperty<>(SimulationSingleton.getInstance().planetList));
    }

    private void addTextFieldsToHBoxes() {
        radiusHBox.getChildren().add(radiusTextField);
        massHBox.getChildren().add(massTextField);
        xPositionHBox.getChildren().add(posXTextField);
        yPositionHBox.getChildren().add(posYTextField);
        zPositionHBox.getChildren().add(posZTextField);
        xSpeedHBox.getChildren().add(speedXTextField);
        ySpeedHBox.getChildren().add(speedYTextField);
        zSpeedHBox.getChildren().add(speedZTextField);
    }
}
