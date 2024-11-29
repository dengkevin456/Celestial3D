package com.example.celestial3d;

import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.List;

public class Planet {
    private Group planetGroup = new Group();
    private String name;

    private Text text = new Text("");
    // private Pane pane = new BorderPane(text);
    private Sphere sphere;
    private double x, y, z, radius, sx, sy, sz, mass;
    boolean imovable;
    private List<Planet> planets;
    public Planet(String name, List<Planet> planets, double x, double y, double z, double sx, double sy, double sz, double radius, double mass, boolean imovable) {
        this.name = name;
        this.planets = planets;
        this.x = x;
        this.y = y;
        this.z = z;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.radius = radius;
        this.mass = mass;
        this.imovable = imovable;
        this.sphere = new Sphere(this.radius);
        this.sphere.setTranslateX(this.x);
        this.sphere.setTranslateY(this.y);
        this.sphere.setTranslateZ(this.z);
        PhongMaterial material = new PhongMaterial(Color.rgb(255, 255,255));
        if (this.imovable) {
            Image i = new Image("file:skybox_top.png");
            material.setSelfIlluminationMap(i);
            material.setDiffuseMap(i);

        }
        else {
            material.setDiffuseColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255),
                                (int) (Math.random() * 255)));
        }
        this.sphere.setMaterial(material);

        this.text.setFont(new Font("Arial", 10));
        this.text.setText(this.name);
        this.text.setFill(Color.WHITE);
        this.planetGroup.getChildren().addAll(this.sphere, this.text);

        this.addPointLight();
    }

    public void setColor(Color color) {
        if (this.imovable) return;
        PhongMaterial material = new PhongMaterial(Color.rgb(255, 255,255));
        material.setDiffuseColor(color);
    }

    public void process() {
        if (!this.imovable) {
            for (Planet planet: planets) {
                if (!planet.equals(this)) {
                    double dx = planet.x - this.x;
                    double dy = planet.y - this.y;
                    double dz = planet.z - this.z;
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    if (distance < 0.1) {
                        distance = 0.1;
                    }

                    double force = Constants.G  * this.mass * planet.mass / (distance * distance);
                    double directionX = (planet.x - this.x) * force * distance / this.mass;
                    double directionY = (planet.y - this.y) * force * distance / this.mass;
                    double directionZ = (planet.z - this.z) * force * distance / this.mass;
                    this.sx += directionX;
                    this.sy += directionY;
                    this.sz += directionZ;
                }
            }

            this.x += this.sx;
            this.y += this.sy;
            this.z += this.sz;
            sphere.setTranslateX(this.x);
            sphere.setTranslateY(this.y);
            sphere.setTranslateZ(this.z);
        }
    }

    public void addPointLight() {
        if (!this.imovable) return;
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(this.x);
        pointLight.setTranslateY(this.y);
        pointLight.setTranslateZ(this.z);
        this.planetGroup.getChildren().add(pointLight);
    }

    public void rotateText(double cameraX, double cameraY, double cameraZ) {
        this.text.setText(this.name + "(" + (int) (sphere.getTranslateX()) + ", " + (int)(-sphere.getTranslateY()) + ", " + (int) sphere.getTranslateZ() + ")");
        this.text.setTranslateX(this.x);
        this.text.setTranslateZ(this.z);
        this.text.setTranslateY(sphere.getTranslateY() - this.radius - 20);

        double deltaX = this.x - cameraX, deltaY = this.y - cameraY, deltaZ = this.z - cameraZ;
        double yaw = Math.toDegrees(Math.atan2(deltaX, deltaZ));
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitch = Math.toDegrees(Math.atan2(deltaY, distance));

        text.getTransforms().setAll(
                new Rotate(sphere.getRotationAxis().getY() + yaw, 0, 0, 0, Rotate.Y_AXIS),
                new Rotate(sphere.getRotationAxis().getX() - pitch, 0, 0, 0, Rotate.X_AXIS)
                );
    }

    public Group getSphereGroup() {
        return this.planetGroup;
    }
}
