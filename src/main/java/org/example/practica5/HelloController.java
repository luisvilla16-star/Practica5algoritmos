package org.example.practica5;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class HelloController {

    // Aquí guardamos todas las cartas del archivo CSV.
    private ArrayList<Carta> cartas = new ArrayList<>();

    // Aquí guardamos las cartas que se van a mostrar.
    private  ArrayList<Carta> cartasVisibles = new ArrayList<>();

    // Elementos de la pantalla.
    private  FlowPane galeria = new FlowPane();
    private  ComboBox<String> nivel = new ComboBox<>();
    private  ComboBox<String> atributo = new ComboBox<>();
    private  CheckBox ataqueMenorAMayor =
            new CheckBox("De menor a mayor");
    private CheckBox defensaMenorAMayor =
            new CheckBox("De menor a mayor");
    private  Button botonMusica = new Button("🔊");

    // Elementos para reproducir la música.
    private Clip musica;
    private int posicionMusica = 0;

    // Crea la pantalla.
    public Parent crearVista() {
        cargarCartas();
        llenarFiltros();
        cargarMusica();

        Button todas = new Button("Mostrar todas las cartas");
        todas.setOnAction(e -> mostrarTodas());

        Button porNivel = new Button("Mostrar por nivel");
        porNivel.setOnAction(e -> filtrarPorNivel());

        Button porAtributo = new Button("Mostrar por atributo");
        porAtributo.setOnAction(e -> filtrarPorAtributo());

        Button porAtaque = new Button("Ordenar por ataque");
        porAtaque.setOnAction(e -> ordenarPorAtaque());

        Button porDefensa = new Button("Ordenar por defensa");
        porDefensa.setOnAction(e -> ordenarPorDefensa());

        nivel.setPromptText("Elige nivel");
        atributo.setPromptText("Elige atributo");

        VBox grupoNivel = new VBox(
                5, new Label("Elegir nivel"), nivel
        );

        VBox grupoAtributo = new VBox(
                5, new Label("Elegir atributo"), atributo
        );

        HBox filaNivel = new HBox(10, porNivel, grupoNivel);
        HBox filaAtributo = new HBox(10, porAtributo, grupoAtributo);
        HBox filaAtaque = new HBox(10, porAtaque, ataqueMenorAMayor);
        HBox filaDefensa = new HBox(10, porDefensa, defensaMenorAMayor);

        botonMusica.setOnAction(e -> pausarOReanudarMusica());

        VBox menu = new VBox(
                18,
                todas,
                filaNivel,
                filaAtributo,
                filaAtaque,
                filaDefensa,
                botonMusica
        );
        menu.setPadding(new Insets(20));
        menu.setPrefWidth(350);

        galeria.setHgap(15);
        galeria.setVgap(15);
        galeria.setPadding(new Insets(15));

        ScrollPane desplazamiento = new ScrollPane(galeria);
        desplazamiento.setFitToWidth(true);

        BorderPane pantalla = new BorderPane();
        pantalla.setPadding(new Insets(15));
        pantalla.setLeft(menu);
        pantalla.setCenter(desplazamiento);

        mostrarTodas();

        return pantalla;
    }

    // Lee las cartas del CSV.
    private void cargarCartas() {
        CatalogoCartas catalogo = new CatalogoCartas();

        LectorCSV.cargarCSV(
                "C:/Users/LUIS GABRIEL VILLA/Downloads/cartas.csv",
                catalogo
        );

        catalogo.organizarListas();

        Nodo<Carta> inicio = catalogo.getTodas().getInicio();

        if (inicio == null) {
            return;
        }

        // Recorre la lista circular de cartas.
        Nodo<Carta> actual = inicio;

        do {
            cartas.add(actual.getDato());
            actual = actual.getNext();
        } while (actual != inicio);
    }

    // Llena las listas desplegables con niveles y atributos.
    private void llenarFiltros() {
        for (Carta carta : cartas) {
            String nivelCarta = String.valueOf(carta.getNivel());

            if (!nivel.getItems().contains(nivelCarta)) {
                nivel.getItems().add(nivelCarta);
            }

            String atributoCarta = String.valueOf(carta.getAtributo());

            if (!atributo.getItems().contains(atributoCarta)) {
                atributo.getItems().add(atributoCarta);
            }
        }
    }

    private void mostrarTodas() {
        cartasVisibles.clear();
        cartasVisibles.addAll(cartas);
        actualizarGaleria();
    }

    private void filtrarPorNivel() {
        String nivelElegido = nivel.getValue();

        if (nivelElegido == null) {
            return;
        }

        cartasVisibles.clear();

        for (Carta carta : cartas) {
            if (String.valueOf(carta.getNivel()).equals(nivelElegido)) {
                cartasVisibles.add(carta);
            }
        }

        actualizarGaleria();
    }

    private void filtrarPorAtributo() {
        String atributoElegido = atributo.getValue();

        if (atributoElegido == null) {
            return;
        }

        cartasVisibles.clear();

        for (Carta carta : cartas) {
            if (String.valueOf(carta.getAtributo())
                    .equalsIgnoreCase(atributoElegido)) {
                cartasVisibles.add(carta);
            }
        }

        actualizarGaleria();
    }

    private void ordenarPorAtaque() {
        cartasVisibles.clear();
        cartasVisibles.addAll(cartas);

        Comparator<Carta> orden =
                Comparator.comparingDouble(carta -> convertirANumero(carta.getAtaque()));

        if (!ataqueMenorAMayor.isSelected()) {
            orden = orden.reversed();
        }

        cartasVisibles.sort(orden);
        actualizarGaleria();
    }

    private void ordenarPorDefensa() {
        cartasVisibles.clear();
        cartasVisibles.addAll(cartas);

        Comparator<Carta> orden =
                Comparator.comparingDouble(carta -> convertirANumero(carta.getDefensa()));

        if (!defensaMenorAMayor.isSelected()) {
            orden = orden.reversed();
        }

        cartasVisibles.sort(orden);
        actualizarGaleria();
    }

    private double convertirANumero(Object valor) {
        try {
            return Double.parseDouble(String.valueOf(valor));
        } catch (NumberFormatException error) {
            return 0;
        }
    }

    // Dibuja las cartas en la galería.
    private void actualizarGaleria() {
        galeria.getChildren().clear();

        if (cartasVisibles.isEmpty()) {
            galeria.getChildren().add(new Label("No hay cartas."));
            return;
        }

        for (Carta carta : cartasVisibles) {
            galeria.getChildren().add(crearTarjeta(carta));
        }
    }

    private VBox crearTarjeta(Carta carta) {
        Label nombre = new Label(carta.getNombre());

        ImageView imagen = new ImageView();
        imagen.setFitWidth(150);
        imagen.setFitHeight(170);
        imagen.setPreserveRatio(true);

        URL archivoImagen = buscarImagen(carta);

        if (archivoImagen != null) {
            imagen.setImage(new Image(archivoImagen.toExternalForm()));
        }

        Label datos = new Label(
                "Atributo: " + carta.getAtributo()
                        + "\nNivel: " + carta.getNivel()
                        + "\nAtaque: " + carta.getAtaque()
                        + "\nDefensa: " + carta.getDefensa()
        );

        VBox tarjeta = new VBox(8, nombre, imagen, datos);
        tarjeta.setPrefWidth(190);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1;"
        );

        return tarjeta;
    }

    // Busca la imagen correcta dentro de resources/Cartas2.
    private URL buscarImagen(Carta carta) {
        String nombre = carta.getNombre().toLowerCase();
        String archivo;

        if (nombre.contains("bebe")) {
            archivo = "bebe.jpeg";
        } else if (nombre.contains("celtic")
                || nombre.contains("guardián celta")) {
            archivo = "celtic.jpeg";
        } else if (nombre.contains("gaia")) {
            archivo = "gaia.jpeg";
        } else if (nombre.contains("espadach")) {
            archivo = "epadachin.jpeg";
        } else if (nombre.contains("elfo")) {
            archivo = "elfo.jpeg";
        } else if (nombre.contains("kuriboh")
                || nombre.contains("koriboh")) {
            archivo = "koriboh.jpeg";
        } else if (nombre.contains("mago")) {
            archivo = "mago.jpeg";
        } else if (nombre.contains("milenario")) {
            archivo = "milenario.jpeg";
        } else if (nombre.contains("negro")) {
            archivo = "negro.jpeg";
        } else if (nombre.contains("soldado")) {
            archivo = "soldado.jpeg";
        } else if (nombre.contains("tortuga")) {
            archivo = "tortuga.jpeg";
        } else if (nombre.contains("dragon")
                || nombre.contains("dragón")) {
            archivo = "dragon.jpeg";
        } else {
            return null;
        }

        return HelloController.class.getResource("/Cartas2/" + archivo);
    }

    // Carga y reproduce la música en bucle.
    private void cargarMusica() {
        try {
            URL archivo = HelloController.class.getResource("/musica/fondo.wav");

            if (archivo == null) {
                System.out.println("No se encontró la música.");
                return;
            }

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(archivo);

            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception error) {
            System.out.println("No se pudo reproducir la música.");
        }
    }

    private void pausarOReanudarMusica() {
        if (musica == null) {
            return;
        }

        if (musica.isRunning()) {
            posicionMusica = musica.getFramePosition();
            musica.stop();
            botonMusica.setText("🔇");
        } else {
            musica.setFramePosition(posicionMusica);
            musica.loop(Clip.LOOP_CONTINUOUSLY);
            botonMusica.setText("🔊");
        }
    }

    public void cerrarMusica() {
        if (musica != null) {
            musica.stop();
            musica.close();
        }
    }
}