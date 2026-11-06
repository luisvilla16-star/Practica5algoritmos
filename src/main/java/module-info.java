module org.example.practica5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example.practica5 to javafx.fxml;
    exports org.example.practica5;

}