package com.example.celestial3d.windows;

import com.example.celestial3d.Constants;
import com.example.celestial3d.SimulationSingleton;
import com.example.celestial3d.VerticalWindow;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;

import java.util.LinkedHashMap;
import java.util.Map;

public class InstructionWindow extends TabPane {
    private static final Font descriptiveFont = new Font(SimulationSingleton.getInstance().globalFontName, 20);

    public static Map<String, String[]> generalInstructions = Map.ofEntries(
            Map.entry("Camera", new String[] {
                    "Basic movements of the first person camera",
                "Use WASD to move",
                    "Use Q/E to move camera down or up",
                "Hold and drag the mouse to rotate the camera",
                    "Reset the camera to origin on the menu",
                    "You can always reopen instructions by selecting Menu -> Instructions at the top"
            }),
            Map.entry("Celestial configuration", new String[]{
                    "This window helps you modify, create or delete bodies",
                "Go to Window/Celestials/Celestial Configuration",
                    "To create celestial bodies, go to \"Creation\" tab",
                    "To modify celestial bodies, go to \"Modification\" tab",
                    "To delete celestial bodies, go to \"Deletion\" tab"
            }),
            Map.entry("Celestial information", new String[]{
                    "Shows the information of a specific celestial body",
                    "To create celestial bodies, go to \"Creation\" tab",
                    "To modify celestial bodies, go to \"Modification\" tab",
                    "To delete celestial bodies, go to \"Deletion\" tab"
            }),
            Map.entry("General settings", new String[] {
                    "General settings of this application",
                    "Edit/Settings",
                    "Control Camera FOV",
                    "Disable trails",
                    "Etc..."
            })
    );

    public InstructionWindow(String... tabs) {
        setMaxWidth(300);
        for (String tab: tabs) {
            Tab instructionTab = new Tab(tab);
            instructionTab.setClosable(false);
            FlowPane content = generateInstructions(tab);
            content.setPrefWidth(USE_COMPUTED_SIZE);
            instructionTab.setContent(content);
            instructionTab.setStyle("-fx-font-weight: bold");
            getTabs().add(instructionTab);
        }

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                FlowPane pane = (FlowPane) newValue.getContent();
                setPrefSize(pane.getPrefWidth(), pane.getPrefHeight());
            }
        });
    }

    private FlowPane generateInstructions(String key) {
        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.VERTICAL);
        String[] instructions = generalInstructions.getOrDefault(key, new String[]{"..."});
        for (int i = 0; i < instructions.length; i++) {
            Label label = new Label((i + 1) + ". " + instructions[i]);
            label.setFont(descriptiveFont);
            label.setMaxWidth(300);
            label.setWrapText(true);
            flowPane.getChildren().add(label);
        }
        return flowPane;
    }

}
