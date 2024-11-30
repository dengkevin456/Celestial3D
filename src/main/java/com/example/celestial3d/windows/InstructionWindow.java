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
    private static final Font titleFont = new Font(SimulationSingleton.getInstance().globalFontName, 80);
    private static final Font descriptiveFont = new Font(SimulationSingleton.getInstance().globalFontName, 20);

    public static Map<String, String[]> generalInstructions = Map.ofEntries(
            Map.entry("Camera", new String[] {
                "Use WASD to move",
                    "Use Q/E to move camera down or up",
                "Hold and drag the mouse to rotate the camera",
                    "Reset the camera to origin on the menu"
            }),
            Map.entry("Planet", new String[]{
                "Go to Window/Celestial Creator",
                    "Further instructions need to be implemented..."
            })
    );

    public InstructionWindow(String... tabs) {
        for (String tab: tabs) {
            Tab instructionTab = new Tab(tab);
            instructionTab.setClosable(false);
            instructionTab.setContent(generateInstructions(tab));
            instructionTab.setStyle("-fx-font-weight: bold");
            getTabs().add(instructionTab);
        }
    }

    private FlowPane generateInstructions(String key) {
        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.VERTICAL);
        String[] instructions = generalInstructions.getOrDefault(key, new String[]{"..."});
        for (int i = 0; i < instructions.length; i++) {
            Label label = new Label((i + 1) + ". " + instructions[i]);
            label.setFont(descriptiveFont);
            flowPane.getChildren().add(label);
        }
        return flowPane;
    }

}
