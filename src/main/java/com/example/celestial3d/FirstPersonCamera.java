package com.example.celestial3d;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class FirstPersonCamera {
    private double cameraX = 0;

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }

    public double getCameraZ() {
        return cameraZ;
    }

    private enum StrafeMode {
        Left,
        Right,
        None,
    }

    private enum MoveMode {
        Forward,
        Back,
        None,
    }

    private enum FlyMode {
        Up,
        Down,
        None,
    }

    private enum YawMode {
        Clockwise,
        CounterClockwise,
        None
    }

    private enum PitchMode {
        Clockwise,
        CounterClockwise,
        None
    }

    private StrafeMode strafeMode = StrafeMode.None;
    private MoveMode moveMode = MoveMode.None;
    private FlyMode flyMode = FlyMode.None;

    private YawMode yawMode = YawMode.None;
    private PitchMode pitchMode = PitchMode.None;

    private double cameraY = 0;
    private double cameraZ = 0;

    private double cameraPitch = 0;
    private double cameraYaw = 0;

    private double cameraAcceleration = 0.01f;

    private double lastMouseX = 0, lastMouseY = 0;

    private double mouseSensitivity = 0.2d;
    // Movement speed
    double movementSpeed = 1.0;
    double oSpeed = movementSpeed;
    double rotateSpeed = 1.0;

    private final double SPEED_THRESHOLD = 50f;

    // Rotate objects for pitch and yaw
    private Rotate pitchRotate;
    private Rotate yawRotate;

    private PerspectiveCamera camera;

    public FirstPersonCamera(PerspectiveCamera camera) {
        this.camera = camera;
        this.camera.fieldOfViewProperty().bindBidirectional(SimulationSingleton.getInstance().cameraFOV);
        pitchRotate = new Rotate(0, Rotate.X_AXIS);
        yawRotate = new Rotate(0, Rotate.Y_AXIS);

        this.camera.setNearClip(0.01);
        this.camera.setFarClip(1000);
        this.camera.setFieldOfView(70);

        camera.getTransforms().addAll(
                this.yawRotate,
                this.pitchRotate,
                new Translate(this.cameraX, this.cameraY, this.cameraZ)
        );
    }

    public void handleMousePress(MouseEvent event) {
        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
    }

    public void handleMouseDrag(MouseEvent event) {
        double deltaX = event.getSceneX() - lastMouseX;
        double deltaY = event.getSceneY() - lastMouseY;

        // Update the yaw (left/right) and pitch (up/down) based on mouse movement
        cameraYaw += deltaX * mouseSensitivity;
        cameraPitch -= deltaY * mouseSensitivity;

        // Restrict pitch to avoid flipping upside down (limit range to -90 to 90 degrees)
        cameraPitch = Math.max(-90, Math.min(90, cameraPitch));

        // Apply yaw and pitch rotations to the camera
        yawRotate.setAngle(cameraYaw);
        pitchRotate.setAngle(cameraPitch);

        // Update last known mouse position
        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
    }

    public void move(int direction) {
        if (direction != -1 && direction != 1) return;
        this.cameraX += direction * this.movementSpeed * Math.sin(Math.toRadians(this.cameraYaw));
        this.cameraY -= direction * this.movementSpeed * Math.sin(Math.toRadians(this.cameraPitch));
        this.cameraZ += direction * this.movementSpeed * Math.cos(Math.toRadians(this.cameraYaw));
        this.movementSpeed += this.cameraAcceleration;
    }

    public void strafe(int direction) {
        if (direction != -1 && direction != 1) return;
        this.cameraX += direction * this.movementSpeed * Math.cos(Math.toRadians(this.cameraYaw));
        this.cameraZ -= direction * this.movementSpeed * Math.sin(Math.toRadians(this.cameraYaw));
        this.movementSpeed += this.cameraAcceleration;
    }

    public void fly(int direction) {
        this.cameraY += direction * this.movementSpeed;
    }

    public void rotateCameraYaw(int direction) {
        cameraYaw += direction * rotateSpeed;
    }

    public void rotateCameraPitch(int direction) {
        cameraPitch += direction * rotateSpeed;
    }

    public void updateCameraPositionAndRotation() {
        if (moveMode == MoveMode.Forward) move(1);
        else if (moveMode == MoveMode.Back) move(-1);
        if (strafeMode == StrafeMode.Left) strafe(-1);
        else if (strafeMode == StrafeMode.Right) strafe(1);

        if (flyMode == FlyMode.Up) fly(-1);
        else if (flyMode == FlyMode.Down) fly(1);

        if (yawMode == YawMode.Clockwise) rotateCameraYaw(-1);
        else if (yawMode == YawMode.CounterClockwise) rotateCameraYaw(1);

        if (pitchMode == PitchMode.Clockwise) rotateCameraPitch(-1);
        else if (pitchMode == PitchMode.CounterClockwise) rotateCameraPitch(1);

        // Apply yaw and pitch rotations to the camera
        yawRotate.setPivotX(cameraX);
        yawRotate.setPivotY(cameraY);
        yawRotate.setPivotZ(cameraZ);
        pitchRotate.setPivotX(cameraX);
        pitchRotate.setPivotY(cameraY);
        pitchRotate.setPivotZ(cameraZ);
        yawRotate.setAngle(cameraYaw);
        pitchRotate.setAngle(cameraPitch);
        if (moveMode != MoveMode.None || strafeMode != StrafeMode.None || flyMode != FlyMode.None) {
            // Update camera translation (movement in space)
            camera.getTransforms().removeIf(t -> t instanceof Translate);
            camera.getTransforms().add(
                    new Translate(cameraX, cameraY, cameraZ));

            if (movementSpeed > SPEED_THRESHOLD) {
                movementSpeed = SPEED_THRESHOLD;
            }
        }



    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                // Move forward in the direction the camera is facing
                moveMode = MoveMode.Forward;
                break;
            case S:
                moveMode = MoveMode.Back;
                break;
            case A:
                // Strafe left
                strafeMode = StrafeMode.Left;
                break;
            case D:
                // Strafe right
                strafeMode = StrafeMode.Right;
                break;
            case E:
                flyMode = FlyMode.Up;
                break;
            case Q:
                flyMode = FlyMode.Down;
                break;
            case UP:
                // Look up (increase pitch)
                pitchMode = PitchMode.CounterClockwise;
                break;
            case DOWN:
                // Look down (decrease pitch)
                pitchMode = PitchMode.Clockwise;
                break;
            case LEFT:
                // Turn left (increase yaw)
                yawMode = YawMode.Clockwise;
                break;
            case RIGHT:
                // Turn right (decrease yaw)
                yawMode = YawMode.CounterClockwise;
                break;

        }
    }

    public void handleKeyRelease(KeyEvent event) {

        switch (event.getCode()) {
            case W:
            case S:
                moveMode = MoveMode.None;
                break;
            case A:
            case D:
                strafeMode = StrafeMode.None;
                break;
            case Q:
            case E:
                flyMode = FlyMode.None;
                break;
            case UP, DOWN:
                pitchMode = PitchMode.None;
                break;
            case LEFT, RIGHT:
                yawMode = YawMode.None;
                break;

        }
        movementSpeed = oSpeed;
    }

    public void resetToOrigin() {
        DoubleProperty dbX = new SimpleDoubleProperty(this.cameraX);
        DoubleProperty dbY = new SimpleDoubleProperty(this.cameraY);
        DoubleProperty dbZ = new SimpleDoubleProperty(this.cameraZ);
        KeyFrame keyFrameX = new KeyFrame(Duration.seconds(1.5), new KeyValue(dbX, 0, Interpolator.EASE_OUT));
        KeyFrame keyFrameY = new KeyFrame(Duration.seconds(1.5), new KeyValue(dbY, 0, Interpolator.EASE_OUT));
        KeyFrame keyFrameZ = new KeyFrame(Duration.seconds(1.5), new KeyValue(dbZ, 0, Interpolator.EASE_OUT));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dbX, this.cameraX), new KeyValue(dbY, this.cameraY),
                        new KeyValue(dbZ, this.cameraZ)),
                keyFrameX,
                keyFrameY,
                keyFrameZ
        );
        dbX.addListener((obs, oldVal, newVal) -> {
            this.cameraX = dbX.doubleValue();
        });
        dbY.addListener((obs, oldVal, newVal) -> {
            this.cameraY = dbY.doubleValue();
        });

        dbZ.addListener((obs, oldVal, newVal) -> {
            this.cameraZ = dbZ.doubleValue();
            camera.getTransforms().removeIf(t -> t instanceof Translate);
            camera.getTransforms().add(
                    new Translate(cameraX, cameraY, cameraZ));
        });


        timeline.play();
    }
}
