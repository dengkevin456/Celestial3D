package com.example.celestial3d;

import java.util.Map;

public final class Constants {
    private Constants() {}

    public static final String[] TAB_STRINGS = {
        "Camera",
        "Celestial configuration",
            "Celestial information",
            "General settings"
    };

    public static final double G = 0.0001;

    public static final double MAX_RENDER_DISTANCE = 200;
    public static final double FOV_ANGLE = 45;
    public static final double SCALE = 1e-9;

    public static final String DEFAULT_TEXTURE_PATH = "images/skybox_top.png";
    public static final String YELLOW = "\033[0;33m";
}
