package com.example.celestial3d;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("simulation-view.fxml"));
        BorderPane parent = loader.load();

        Scene scene = new Scene(parent, parent.getMinWidth(), parent.getMinHeight());
        stage.setMinWidth(800);
        stage.setMinHeight(800);
        SimulationController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKeyPress);
        scene.setOnKeyReleased(controller::handleKeyRelease);

        stage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) controller.startAnimation();
            else controller.stopAnimation();
        });

        stage.setOnCloseRequest(windowEvent -> {
            controller.displayClosingAlertBox();
        });

        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            parent.getCenter().autosize();
        });

        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            parent.getCenter().autosize();
        });


        // TODO: Fix the full screen issue


        stage.setTitle("JavaFX simulation");
        stage.setScene(scene);
        stage.setFullScreen(SimulationSingleton.getInstance().fullScreenProperty.getValue());
        stage.fullScreenProperty().addListener((obs, oldValue, newValue) -> {
            SimulationSingleton.getInstance().fullScreenProperty.setValue(newValue);
            controller.getFullScreenCheck().selectedProperty().setValue(newValue);
        });
        SimulationSingleton.getInstance().fullScreenProperty.addListener((obs, oldValue, newValue) -> {
            stage.setFullScreen(newValue);
        });

        stage.show();
    }

    public static void main(String[] args) {
        System.out.print(Constants.YELLOW);
        boolean supported = Platform.isSupported(ConditionalFeature.SCENE3D);
        if (!supported) System.out.println("WARNING: 3D is not supported on your machine!");
        launch(args);
    }
}
