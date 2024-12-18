package com.example.celestial3d;

import com.example.celestial3d.windows.CelestialBodyConfigurationWindow;
import com.example.celestial3d.windows.CelestialBodyInformationWindow;
import com.example.celestial3d.windows.InstructionWindow;
import com.example.celestial3d.windows.SkyboxColorPicker;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;

public class SimulationController {


    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private CheckMenuItem fullScreenCheck;

    public CheckMenuItem getFullScreenCheck() {
        return fullScreenCheck;
    }

    // User-defined objects
    private SubScene subScene;
    private PerspectiveCamera camera = new PerspectiveCamera(true);
    private FirstPersonCamera firstPersonCamera = new FirstPersonCamera(camera);


    private AnimationTimer animationTimer;

    private VerticalWindow rightVerticalWindow = new VerticalWindow(new InstructionWindow(Constants.TAB_STRINGS));

    private VerticalWindow leftVerticalWindow = new VerticalWindow(getAboutWindow());


    public SimulationController() {}

    public void handleKeyPress(KeyEvent event) {
        firstPersonCamera.handleKeyPress(event);
    }

    public void handleKeyRelease(KeyEvent event) {
        firstPersonCamera.handleKeyRelease(event);
    }

    public void startAnimation() {
        animationTimer.start();
    }

    public void stopAnimation() {
        animationTimer.stop();
    }

    public void borderPaneSetup() {
        borderPane.setCenter(subScene);
        borderPane.setRight(rightVerticalWindow);
        borderPane.setLeft(leftVerticalWindow);
        subScene.widthProperty().bind(borderPane.widthProperty().subtract(leftVerticalWindow.verticalWindowWidthProperty).subtract(rightVerticalWindow.verticalWindowWidthProperty));
        subScene.heightProperty().bind(borderPane.heightProperty().subtract(menuBar.heightProperty()));
        BorderPane.setAlignment(subScene, Pos.TOP_LEFT);
    }
    @FXML
    public void initialize() {
        subScene = new SubScene(SimulationSingleton.getInstance().group, 800, 800, true, SceneAntialiasing.BALANCED);
        borderPaneSetup();
        subScene.setFill(Color.BLACK);
        SimulationSingleton.getInstance().skyboxColor.addListener((observable, oldValue, newValue) -> {
            subScene.setFill(newValue);
        });

        SimulationSingleton instance = SimulationSingleton.getInstance();
        
        instance.planetList.add(new Planet("Sun", instance.planetList, 80, 0, 0, 0, 0, 0, 50, 80,  true));
        instance.planetList.add(new Planet("Earth6", instance.planetList, -400, 0, 0, -1, 1, 2, 10, 25, false));
        instance.planetList.add(new Planet("Jupiter", instance.planetList, 100, 0, 0, -1, 0, 2, 30, 40, false));

        for (Planet p: instance.planetList) {
            SimulationSingleton.getInstance().group.getChildren().add(p.getSphereGroup());
        }
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Planet p: instance.planetList) {
                    p.process();
                    p.rotateText(firstPersonCamera.getCameraX(), firstPersonCamera.getCameraY(), firstPersonCamera.getCameraZ());
                }

                firstPersonCamera.updateCameraPositionAndRotation();
            }
        };

        subScene.setCamera(camera);
        subScene.setOnMousePressed(event -> firstPersonCamera.handleMousePress(event));
        subScene.setOnMouseDragged(event -> firstPersonCamera.handleMouseDrag(event));

        bindFullScreenCheckMenu();

    }

    private void bindFullScreenCheckMenu() {
        fullScreenCheck.selectedProperty().setValue(SimulationSingleton.getInstance().fullScreenProperty.getValue());
    }


    public void displayClosingAlertBox() {
        stopAnimation();
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        a.setTitle("Quit information");
        a.setHeaderText("You are about to exit the application");
        Optional<ButtonType> result = a.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
        // TODO: Fix cancel closing bug
    }

    public void resetToOrigin(ActionEvent event) {
        this.firstPersonCamera.resetToOrigin();
    }

    public void closeApplication(ActionEvent event) {
        displayClosingAlertBox();
    }

    public void setFullScreen(ActionEvent event) {
        SimulationSingleton.getInstance().toggleFullScreen();
    }

    // Window creator
    public void skyBoxColorPickerWindow(ActionEvent event) {
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(new Button("Button1"), new Button("Button2"));
        leftVerticalWindow.setWindow(new SkyboxColorPicker());
    }

    public void instructionWindow(ActionEvent event) {
        rightVerticalWindow.setWindow(new InstructionWindow(Constants.TAB_STRINGS));
    }

    public void planetCreationWindow(ActionEvent event) {
        leftVerticalWindow.setWindow(new CelestialBodyConfigurationWindow(firstPersonCamera));
    }

    public void aboutWindow(ActionEvent event) {
        leftVerticalWindow.setWindow(getAboutWindow());
    }

    public void settingsWindow(ActionEvent event) {
        rightVerticalWindow.setWindow(getSettingsWindow());
    }

    private ScrollPane getAboutWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("about-view.fxml"));
            return loader.load();
        }
        catch (IOException e) {
            return new ScrollPane(new Label("The about window doesn't exist."));
        }
    }

    public void accelerationTimeWindow(ActionEvent event) {
        rightVerticalWindow.setWindow(getAccelerationTimeWindow());
    }

    public ScrollPane getAccelerationTimeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("acceleration-view.fxml"));
            return loader.load();
        }
        catch (IOException e) {
            return new ScrollPane(new Label("The accel-time window doesn't exist."));
        }
    }

    public ScrollPane getSettingsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
            return loader.load();
        }
        catch (IOException e) {
            return new ScrollPane(new Label("The settings window doesn't exist."));
        }
    }

    public void celestialBodyInformationWindow(ActionEvent event) {
        leftVerticalWindow.setWindow(new CelestialBodyInformationWindow(firstPersonCamera));
    }
}
