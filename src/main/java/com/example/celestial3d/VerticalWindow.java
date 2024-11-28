package com.example.celestial3d;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class VerticalWindow extends FlowPane {
    private final byte PADDING = 10;

    public DoubleProperty verticalWindowWidthProperty = new SimpleDoubleProperty(widthProperty().doubleValue());
    public VerticalWindow() {
        setVisible(false);
        setup();
    }

    public VerticalWindow(Node node) {
        setup();
        setWindow(node);
    }

    private void setup() {
        setBackground(Background.fill(Color.WHITE));
        setOrientation(Orientation.VERTICAL);
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(5);
        innerShadow.setOffsetY(5);
        innerShadow.setColor(Color.GRAY);
        setEffect(innerShadow);
    }

    public void setWindow(Node node) {
        getChildren().clear();
        addCloseButton();
        getChildren().add(node);
        setVisible(true);
        setPadding(new Insets(PADDING));
        verticalWindowWidthProperty.bind(widthProperty());
    }

    private void addCloseButton() {
        Button button = new Button("X");
        HBox closeHBox = new HBox(button);
        closeHBox.setAlignment(Pos.CENTER_RIGHT);
        getChildren().add(closeHBox);
        button.setOnAction((event) -> {
            this.setVisible(false);
            setPadding(new Insets(0));
            verticalWindowWidthProperty.bind(new SimpleDoubleProperty(0));
            getChildren().clear();
        });
    }
}
