package com.example.celestial3d;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Planet {
    private Group planetGroup = new Group();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        sphere.setTranslateX(this.x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        sphere.setTranslateY(this.y);
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
        sphere.setTranslateZ(this.z);
    }

    public double getRadius() {
        return radius;
    }

    public Color getMaterialColor() {
        if (this.imovable) return Color.WHITE;
        return planetColor;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.sphere.setRadius(this.radius);
        PhongMaterial material = new PhongMaterial(Color.rgb(255, 255,255));
        if (this.imovable) {
            if (this.texturePath == null) {
                setDefaultTexture();
            }
            else {
                Image i = new Image("file:" + this.texturePath);
                material.setSelfIlluminationMap(i);
                material.setDiffuseMap(i);
                this.sphere.setMaterial(material);
            }
        }
        else {
            material.setDiffuseColor(planetColor);
            this.sphere.setMaterial(material);
        }
    }

    public void setDefaultTexture() {
        if (!this.imovable) return;
        PhongMaterial material = new PhongMaterial(Color.rgb(255, 255, 255));
        this.texturePath = null;
        Image i = new Image(Objects.requireNonNull(
                Launcher.class.getResource(Constants.DEFAULT_TEXTURE_PATH)).toExternalForm());
        material.setSelfIlluminationMap(i);
        material.setDiffuseMap(i);
        this.sphere.setMaterial(material);
    }
    public void setTexture(String textureLink) {
        if (!this.imovable) return;
        PhongMaterial material = new PhongMaterial(Color.rgb(255, 255,255));
        this.texturePath = textureLink;
        Image i = new Image("file:" + this.texturePath);
        material.setSelfIlluminationMap(i);
        material.setDiffuseMap(i);
        this.sphere.setMaterial(material);
    }

    public double getSx() {
        return sx;
    }

    public void setSx(double sx) {
        this.sx = sx;
    }

    public double getSy() {
        return sy;
    }

    public void setSy(double sy) {
        this.sy = sy;
    }

    public double getSz() {
        return sz;
    }

    public void setSz(double sz) {
        this.sz = sz;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    private String name;

    private Text text = new Text("");
    // private Pane pane = new BorderPane(text);
    private Sphere sphere;
    private double x, y, z, radius, sx, sy, sz, mass;
    private Color planetColor;
    boolean imovable;
    String texturePath = null;
    private Trail planetTrail;
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
        PhongMaterial material = new PhongMaterial(Color.rgb(255, 255,255));
        if (this.imovable) {
            Image i = new Image(Objects.requireNonNull(Launcher.class.getResource(Constants.DEFAULT_TEXTURE_PATH)).toExternalForm());
            material.setSelfIlluminationMap(i);
            material.setDiffuseMap(i);

        }
        else {
            planetColor = Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255),
                    (int) (Math.random() * 255));
            material.setDiffuseColor(planetColor);
        }
        this.sphere.setMaterial(material);

        this.text.setFont(new Font("Arial", 10));
        this.text.setText(this.name);
        this.text.setFill(Color.WHITE);

        DropShadow glow = new DropShadow();
        glow.setOffsetX(0);
        glow.setOffsetY(0);
        glow.setRadius(10);
        glow.setSpread(0.7);
        glow.setColor(Color.LIMEGREEN);
        this.text.setEffect(glow);


        this.planetTrail = new Trail(this.sphere, new ArrayDeque<>());
        this.planetGroup.getChildren().addAll(this.sphere, this.text, this.planetTrail.getTrailGroup());

        this.addPointLight();


        sphere.setTranslateX(this.x);
        sphere.setTranslateY(this.y);
        sphere.setTranslateZ(this.z);
    }

    @Override
    public String toString() {
        return this.name + ", " + this.mass + "kg";
    }

    public void setColor(Color color) {
        if (this.imovable) return;
        planetColor = color;
        PhongMaterial material = new PhongMaterial(planetColor);
        material.setDiffuseColor(planetColor);
        this.sphere.setMaterial(material);
    }

    public void process() {
        if (!this.imovable) {
            for (Planet planet: planets) {
                if (!planet.equals(this)) {
                    double dx = planet.x - this.x;
                    double dy = planet.y - this.y;
                    double dz = planet.z - this.z;

                    double distanceSquared = dx * dx + dy * dy + dz * dz;
                    double distance = Math.sqrt(distanceSquared);

                    if (distance < 0.1) {
                        distance = 0.1;
                    }

                    double force = Constants.G  * this.mass * planet.mass / distanceSquared;
                    // Acceleration = F / m
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

            if (SimulationSingleton.getInstance().enableTrailVisualizer.getValue())
                planetTrail.updateMovement();
            else planetTrail.getTrailGroup().getChildren().clear();
        }
    }
    public double getVelocitySquared() {
        if (this.imovable) return 0;
        return (this.sx * this.sx + this.sy * this.sy + this.sz * this.sz);
    }
    public void addPointLight() {
        if (!this.imovable) return;
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(this.x);
        pointLight.setTranslateY(this.y);
        pointLight.setTranslateZ(this.z);
        pointLight.translateXProperty().bind(new SimpleDoubleProperty(this.x));
        pointLight.translateYProperty().bind(new SimpleDoubleProperty(this.y));
        pointLight.translateZProperty().bind(new SimpleDoubleProperty(this.z));
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
