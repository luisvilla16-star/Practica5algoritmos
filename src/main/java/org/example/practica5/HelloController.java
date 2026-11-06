
package org.example.practica5;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Locale;
import java.util.ArrayList;
import java.util.stream.Stream;

public class HelloController {

    private ArrayList<Carta> cartas = new ArrayList<>();
    private ArrayList<Carta> cartasVisibles = new ArrayList<>();

    private FlowPane galeria = new FlowPane();

    private ComboBox<String> nivel = new ComboBox<>();
    private ComboBox<String> atributo = new ComboBox<>();

    private CheckBox ataqueMenorAMayor =
            new CheckBox("De menor a mayor");

    private CheckBox defensaMenorAMayor =
            new CheckBox("De menor a mayor");

    private Button botonMusica = new Button("🔊");

    private Button anterior = new Button("Anterior");
    private Button siguiente = new Button("Siguiente");

    private Label contador = new Label();

    private int posicionCarta = 0;

    private Clip musica;
    private int posicionMusica = 0;


    // Crear la interfaz
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

        anterior.setOnAction(e -> {

            if (posicionCarta > 0) {
                posicionCarta--;
                actualizarGaleria();
            }
        });

        siguiente.setOnAction(e -> {

            if (posicionCarta < cartasVisibles.size() - 1) {
                posicionCarta++;
                actualizarGaleria();
            }
        });

        HBox navegacion = new HBox(
                15, anterior, contador, siguiente
        );

        navegacion.setAlignment(Pos.CENTER);
        navegacion.setPadding(new Insets(15));

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
        galeria.setAlignment(Pos.CENTER);

        ScrollPane desplazamiento = new ScrollPane(galeria);
        desplazamiento.setFitToWidth(true);

        Label titulo = new Label("CATÁLOGO DE CARTAS Yu-Gi-Oh!");

        titulo.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #D4AF37;"
        );

        BorderPane pantalla = new BorderPane();

        pantalla.setPadding(new Insets(15));
        pantalla.setTop(titulo);
        pantalla.setLeft(menu);

        BorderPane.setAlignment(titulo, Pos.CENTER);

        VBox centro = new VBox(desplazamiento, navegacion);
        centro.setAlignment(Pos.CENTER);

        pantalla.setCenter(centro);

        mostrarTodas();

        return pantalla;
    }


    // Cargar cartas del archivo CSV
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

        Nodo<Carta> actual = inicio;

        do {
            cartas.add(actual.getDato());
            actual = actual.getNext();

        } while (actual != inicio);
    }


    // Llenar los filtros
    private void llenarFiltros() {

        for (Carta carta : cartas) {

            String nivelCarta = String.valueOf(carta.getNivel());
            String atributoCarta = String.valueOf(carta.getAtributo());

            if (!nivel.getItems().contains(nivelCarta)) {
                nivel.getItems().add(nivelCarta);
            }

            if (!atributo.getItems().contains(atributoCarta)) {
                atributo.getItems().add(atributoCarta);
            }
        }
    }


    // Mostrar todas las cartas
    private void mostrarTodas() {

        cartasVisibles.clear();
        cartasVisibles.addAll(cartas);

        posicionCarta = 0;

        actualizarGaleria();
    }


    // Filtrar por nivel
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

        posicionCarta = 0;

        actualizarGaleria();
    }


    // Filtrar por atributo
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

        posicionCarta = 0;

        actualizarGaleria();
    }


    // Ordenar por ataque
    private void ordenarPorAtaque() {

        cartasVisibles.clear();
        cartasVisibles.addAll(cartas);

        for (int i = 0; i < cartasVisibles.size(); i++) {

            for (int j = i + 1; j < cartasVisibles.size(); j++) {

                int ataque1 = cartasVisibles.get(i).getAtaque();
                int ataque2 = cartasVisibles.get(j).getAtaque();

                if (ataqueMenorAMayor.isSelected()) {

                    if (ataque1 > ataque2) {

                        Carta temporal = cartasVisibles.get(i);

                        cartasVisibles.set(i, cartasVisibles.get(j));
                        cartasVisibles.set(j, temporal);
                    }

                } else {

                    if (ataque1 < ataque2) {

                        Carta temporal = cartasVisibles.get(i);

                        cartasVisibles.set(i, cartasVisibles.get(j));
                        cartasVisibles.set(j, temporal);
                    }
                }
            }
        }

        posicionCarta = 0;

        actualizarGaleria();
    }


    // Ordenar por defensa
    private void ordenarPorDefensa() {

        cartasVisibles.clear();
        cartasVisibles.addAll(cartas);

        for (int i = 0; i < cartasVisibles.size(); i++) {

            for (int j = i + 1; j < cartasVisibles.size(); j++) {

                int defensa1 = cartasVisibles.get(i).getDefensa();
                int defensa2 = cartasVisibles.get(j).getDefensa();

                if (defensaMenorAMayor.isSelected()) {

                    if (defensa1 > defensa2) {

                        Carta temporal = cartasVisibles.get(i);

                        cartasVisibles.set(i, cartasVisibles.get(j));
                        cartasVisibles.set(j, temporal);
                    }

                } else {

                    if (defensa1 < defensa2) {

                        Carta temporal = cartasVisibles.get(i);

                        cartasVisibles.set(i, cartasVisibles.get(j));
                        cartasVisibles.set(j, temporal);
                    }
                }
            }
        }

        posicionCarta = 0;

        actualizarGaleria();
    }


    // Mostrar las tres cartas
    private void actualizarGaleria() {

        galeria.getChildren().clear();

        if (cartasVisibles.isEmpty()) {

            galeria.getChildren().add(new Label("No hay cartas."));

            contador.setText("0 / 0");

            anterior.setDisable(true);
            siguiente.setDisable(true);

            return;
        }

        // Imagen anterior
        ImageView imagenAnterior = new ImageView();

        imagenAnterior.setFitWidth(100);
        imagenAnterior.setFitHeight(150);
        imagenAnterior.setPreserveRatio(true);

        if (posicionCarta > 0) {

            URL archivoAnterior = buscarImagen(
                    cartasVisibles.get(posicionCarta - 1)
            );

            if (archivoAnterior != null) {
                imagenAnterior.setImage(
                        new Image(archivoAnterior.toExternalForm())
                );
            }
        }

        // Imagen siguiente
        ImageView imagenSiguiente = new ImageView();

        imagenSiguiente.setFitWidth(100);
        imagenSiguiente.setFitHeight(150);
        imagenSiguiente.setPreserveRatio(true);

        if (posicionCarta < cartasVisibles.size() - 1) {

            URL archivoSiguiente = buscarImagen(
                    cartasVisibles.get(posicionCarta + 1)
            );

            if (archivoSiguiente != null) {
                imagenSiguiente.setImage(
                        new Image(archivoSiguiente.toExternalForm())
                );
            }
        }

        // Carta actual
        Carta carta = cartasVisibles.get(posicionCarta);

        VBox tarjeta = crearTarjeta(carta);

        HBox fila = new HBox(
                25,
                imagenAnterior,
                tarjeta,
                imagenSiguiente
        );

        fila.setAlignment(Pos.CENTER);

        galeria.getChildren().add(fila);

        contador.setText(
                (posicionCarta + 1) + " / " + cartasVisibles.size()
        );

        anterior.setDisable(posicionCarta == 0);

        siguiente.setDisable(
                posicionCarta == cartasVisibles.size() - 1
        );
    }


    // Crear la tarjeta de la carta actual
    private VBox crearTarjeta(Carta carta) {

        Label nombre = new Label(carta.getNombre());

        nombre.setWrapText(true);
        nombre.setMaxWidth(260);

        ImageView imagen = new ImageView();

        imagen.setFitWidth(250);
        imagen.setFitHeight(300);
        imagen.setPreserveRatio(true);

        URL archivoImagen = buscarImagen(carta);

        if (archivoImagen != null) {
            imagen.setImage(new Image(archivoImagen.toExternalForm()));
        }

        String datosTexto =
                "Atributo: " + carta.getAtributo() +
                        "\nNivel: " + carta.getNivel() +
                        "\nAtaque: " + carta.getAtaque() +
                        "\nDefensa: " + carta.getDefensa();

        Label datos = new Label(datosTexto);

        VBox tarjeta = new VBox(10, nombre, imagen, datos);

        tarjeta.setPrefWidth(290);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setAlignment(Pos.CENTER);

        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1;"
        );

        return tarjeta;
    }


    // Buscar la imagen de la carta
    private URL buscarImagen(Carta carta) {

        String nombre = normalizarNombreArchivo(carta.getNombre());

        if (nombre.equals("baby dragon")) {
            nombre = "bebe dragon";

        } else if (nombre.equals("giant soldier of stone")) {
            nombre = "soldado gigante de piedra";

        } else if (nombre.equals("winged dragon guardian of the fortress 1")) {
            nombre = "winged dragon";
        }

        final String nombreBuscado = nombre;

        Path carpeta = Paths.get(
                System.getProperty("user.home"),
                "Documents",
                "Cartas2"
        );

        if (Files.isDirectory(carpeta)) {

            try (Stream<Path> archivos = Files.list(carpeta)) {

                Path imagen = archivos
                        .filter(ruta -> Files.isRegularFile(ruta))
                        .filter(ruta -> normalizarNombreArchivo(
                                nombreSinExtension(ruta.getFileName().toString())
                        ).equals(nombreBuscado))
                        .findFirst()
                        .orElse(null);

                if (imagen != null) {
                    return imagen.toUri().toURL();
                }

            } catch (Exception error) {

                System.out.println(
                        "No se pudo buscar la imagen de " + carta.getNombre()
                );
            }
        }

        String archivoRecurso = nombreRecursoAntiguo(nombreBuscado);

        if (archivoRecurso != null) {

            return HelloController.class.getResource(
                    "/Cartas2/" + archivoRecurso
            );
        }

        return null;
    }


    // Nombres de algunas imágenes antiguas
    private String nombreRecursoAntiguo(String nombre) {

        if (nombre.equals("dragon blanco de ojos azules")) return "dragon.jpeg";
        if (nombre.equals("mago oscuro")) return "mago.jpeg";
        if (nombre.equals("celtic guardian")) return "celtic.jpeg";
        if (nombre.equals("kuriboh")) return "koriboh.jpeg";
        if (nombre.equals("dragon meteoro")) return "dragon.jpeg";
        if (nombre.equals("bebe dragon")) return "bebe.jpeg";
        if (nombre.equals("elfo mistico")) return "elfo.jpeg";
        if (nombre.equals("dragon milenario")) return "milenario.jpeg";
        if (nombre.equals("tortuga catapulta")) return "tortuga.jpeg";
        if (nombre.equals("espadachin de la llama")) return "epadachin.jpeg";
        if (nombre.equals("soldado gigante de piedra")) return "soldado.jpeg";
        if (nombre.equals("dragon negro de ojos rojos")) return "negro.jpeg";

        return null;
    }


    // Quitar la extensión del archivo
    private String nombreSinExtension(String nombre) {

        int punto = nombre.lastIndexOf('.');

        if (punto > 0) {
            return nombre.substring(0, punto);
        }

        return nombre;
    }


    // Normalizar nombres para encontrar las imágenes
    private String normalizarNombreArchivo(String nombre) {

        String sinAcentos = Normalizer
                .normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return sinAcentos.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }


    // Cargar música
    private void cargarMusica() {

        try {

            URL archivo = HelloController.class.getResource(
                    "/musica/fondo.wav"
            );

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


    // Pausar o reproducir música
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


    // Cerrar música
    public void cerrarMusica() {

        if (musica != null) {

            musica.stop();
            musica.close();
        }
    }
}