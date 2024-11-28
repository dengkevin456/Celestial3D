module com.example.celestial3d {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.celestial3d to javafx.fxml;
    exports com.example.celestial3d;
}