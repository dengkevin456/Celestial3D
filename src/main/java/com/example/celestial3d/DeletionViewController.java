package com.example.celestial3d;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import java.util.Optional;

public class DeletionViewController {

    @FXML
    private Button deleteAllButton;
    @FXML
    private ChoiceBox<Planet> celestialBodyChoiceBox;

    @FXML
    private Button deleteCelestialBodyButton;

    @FXML
    public void initialize() {
        updateChoiceBoxesBasedOnNumberOfPlanets();
        deleteCelestialBodyButton.disableProperty().bind(Bindings.isNull(celestialBodyChoiceBox.getSelectionModel().selectedItemProperty()));
        deleteAllButton.disableProperty().bind(Bindings.isEmpty(SimulationSingleton.getInstance().planetList));
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

    public void deleteAll(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure you want to delete everything?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Planet p: SimulationSingleton.getInstance().planetList) {
                SimulationSingleton.getInstance().group.getChildren().remove(p.getSphereGroup());
            }
            SimulationSingleton.getInstance().planetList.clear();
            celestialBodyChoiceBox.setValue(null);
        }
    }

    private void updateChoiceBoxesBasedOnNumberOfPlanets() {
        celestialBodyChoiceBox.itemsProperty().bind(new SimpleListProperty<>(SimulationSingleton.getInstance().planetList));
    }
}
