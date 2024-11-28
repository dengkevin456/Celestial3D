package com.example.celestial3d;

import javafx.beans.property.*;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class SimulationSingleton {
    private static SimulationSingleton instance;

    public BooleanProperty fullScreenProperty = new SimpleBooleanProperty(true);
    public IntegerProperty skyboxRED = new SimpleIntegerProperty(0);
    public DoubleProperty skyboxGreen = new SimpleDoubleProperty(0);
    public DoubleProperty skyboxBlue = new SimpleDoubleProperty(0);

    public List<Planet> planetList = new ArrayList<>();

    public String globalFontName = "Sen";

    public final Group group = new Group(new SimulationSpace());
    private SimulationSingleton() {}
    public static SimulationSingleton getInstance() {
        if (instance == null) {
            instance = new SimulationSingleton();
        }
        return instance;
    }

    public void toggleFullScreen() {
        fullScreenProperty.setValue(!fullScreenProperty.getValue());
    }

}
