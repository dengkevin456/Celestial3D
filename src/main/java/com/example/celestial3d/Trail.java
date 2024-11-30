package com.example.celestial3d;

import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayDeque;
import java.util.Queue;

public class Trail {
    private static final double TRAIL_RADIUS = 1.6;
    private static final int MAX_TRAIL_LENGTH = 300;
    private Sphere celestialBody;
    private double frameCount = 0;

    public Group getTrailGroup() {
        return trailGroup;
    }

    private Group trailGroup;

    private Queue<Sphere> trailPoints;

    public Trail(Sphere celestialBody, Queue<Sphere> trailPoints) {
        this.celestialBody = celestialBody;
        this.trailPoints = trailPoints;
        this.trailGroup = new Group();
    }

    public void updateMovement() {
        double x = celestialBody.getTranslateX();
        double y = celestialBody.getTranslateY();
        double z = celestialBody.getTranslateZ();

        PhongMaterial material = new PhongMaterial();

        material.setDiffuseColor(Color.rgb((int) (155 - Math.sin(Math.toRadians(frameCount)) * 100),
                (int) (155 - Math.cos(Math.toRadians(frameCount)) * 100), (int) (155 + Math.cos(Math.toRadians(frameCount)) * 100)));
        PointLight light = new PointLight();
        Sphere trailPoint = new Sphere(TRAIL_RADIUS);
        light.setTranslateX(trailPoint.getTranslateX());
        light.setTranslateY(trailPoint.getTranslateY());
        light.setTranslateZ(trailPoint.getTranslateZ());
        trailPoint.setMaterial(material);
        trailPoint.setTranslateX(x);
        trailPoint.setTranslateY(y);
        trailPoint.setTranslateZ(z);

        trailGroup.getChildren().add(trailPoint);
        trailPoints.add(trailPoint);

        if (trailPoints.size() > MAX_TRAIL_LENGTH) {
            Sphere oldPoint = trailPoints.poll();
            trailGroup.getChildren().remove(oldPoint);
        }

        if (frameCount >= 720) frameCount = 0;
        frameCount += 0.6;
    }
}
