package com.example.celestial3d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

public class SimulationSpace extends Group {


    private double spacing;
    private int count;
    private final PhongMaterial MATERIAL = new PhongMaterial(Color.GRAY);

    public SimulationSpace() {
        getChildren().add(create3DCoordinateGrid(10, 20));
    }

    private Group create3DCoordinateGrid(int gridCount, double spacing) {
        Group grid = new Group();

        // Create material for the grid cylinders
        PhongMaterial grayMaterial = new PhongMaterial(Color.WHITE);

        // Create grid cells for XZ plane (Y = 0)
        for (int i = -gridCount; i <= gridCount; i++) {
            for (int j = -gridCount; j <= gridCount; j++) {
                // Create small cylinders at (x, 0, z) for the XZ plane
                grid.getChildren().add(createCylinder(i * spacing, 0, j * spacing, grayMaterial));


            }
        }

        return grid;
    }

    private Cylinder createCylinder(double x, double y, double z, PhongMaterial material) {
        // Create a small cylinder to represent a grid point
        Cylinder cylinder = new Cylinder(0.5, 1);  // Small cylinder, 0.5 radius, 1 height

        // Position the cylinder
        cylinder.setTranslateX(x);
        cylinder.setTranslateY(y);
        cylinder.setTranslateZ(z);

        // Apply the material to the cylinder
        cylinder.setMaterial(material);

        return cylinder;
    }


}
