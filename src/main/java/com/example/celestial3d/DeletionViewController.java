package com.example.celestial3d;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;

import java.util.Optional;

public class DeletionViewController {

    @FXML
    private ChoiceBox<Planet> celestialBodyChoiceBox;

    @FXML
    private Button deleteCelestialBodyButton;

    @FXML
    public void initialize() {
        updateChoiceBoxesBasedOnNumberOfPlanets();
        deleteCelestialBodyButton.disableProperty().bind(Bindings.isNull(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty()));
    }

    public void deletePlanet(ActionEvent event) {
        Planet planetToDelete = celestialBodyChoiceBox.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure you want to delete the planet " + planetToDelete.getName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SimulationSingleton.getInstance().planetList.remove(planetToDelete);
            SimulationSingleton.getInstance().group.getChildren().remove(planetToDelete.getSphereGroup());
            celestialBodyChoiceBox.setValue(null);
        }

    }

    private void updateChoiceBoxesBasedOnNumberOfPlanets() {
        celestialBodyChoiceBox.itemsProperty().bind(new SimpleListProperty<>(SimulationSingleton.getInstance().planetList));
    }
}
