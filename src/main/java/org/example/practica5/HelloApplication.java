package org.example.practica5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private HelloController controlador;

    @Override
    public void start(Stage ventana) {
        controlador = new HelloController();

        Scene escena = new Scene(controlador.crearVista(), 1000, 600);

        ventana.setTitle("Catálogo de cartas");
        ventana.setScene(escena);
        ventana.show();
    }

    @Override
    public void stop() {
        if (controlador != null) {
            controlador.cerrarMusica();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}