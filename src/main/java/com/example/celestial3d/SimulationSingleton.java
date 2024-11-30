package com.example.celestial3d;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SimulationSingleton {
    private static SimulationSingleton instance;

    public BooleanProperty fullScreenProperty = new SimpleBooleanProperty(true);

    public ObjectProperty<Color> skyboxColor = new SimpleObjectProperty<>(Color.BLACK);

    public ObservableList<Planet> planetList = FXCollections.observableList(new ArrayList<>());

    public String globalFontName = "Sen";

    private static final SimulationSpace space = new SimulationSpace();
    public final Group group = new Group(space);


    // Global settings
    public BooleanProperty enableTrailVisualizer = new SimpleBooleanProperty(true);
    public BooleanProperty enableCartesianPlane = new SimpleBooleanProperty(true);

    public DoubleProperty cameraFOV = new SimpleDoubleProperty(60);
    private SimulationSingleton() {}
    public static SimulationSingleton getInstance() {
        if (instance == null) {
            instance = new SimulationSingleton();
            space.visibleProperty().bind(instance.enableCartesianPlane);
        }
        return instance;
    }

    public void toggleFullScreen() {
        fullScreenProperty.setValue(!fullScreenProperty.getValue());
    }

}
