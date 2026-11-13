
package org.example.practica5;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LectorCSV {

    public static void cargarCSV(String archivo, CatalogoCartas catalogo) {

        int cargadas = 0;

        try (BufferedReader lector = Files.newBufferedReader(
                Paths.get(archivo), StandardCharsets.UTF_8)) {

            String linea = lector.readLine(); // Saltar encabezado

            while ((linea = lector.readLine()) != null) {

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",");

                try {

                    if (datos.length < 6) {
                        continue;
                    }

                    int indiceAtributo = datos.length - 4;

                    String nombre = "";

                    for (int i = 1; i < indiceAtributo; i++) {
                        if (i > 1) {
                            nombre += ",";
                        }

                        nombre += datos[i];
                    }

                    String atributo = datos[indiceAtributo].trim();

                    int nivel = Integer.parseInt(datos[indiceAtributo + 1].trim());
                    int ataque = Integer.parseInt(datos[indiceAtributo + 2].trim());
                    int defensa = Integer.parseInt(datos[indiceAtributo + 3].trim());

                    Carta carta = new Carta(nombre, atributo, nivel, ataque, defensa);

                    catalogo.agregarCarta(carta);

                    cargadas++;

                } catch (NumberFormatException error) {
                    System.out.println("Error en los datos numericos de una carta.");
                }
            }

            System.out.println("Cartas cargadas correctamente: " + cargadas);

        } catch (IOException error) {
            System.out.println("No se pudo leer el archivo CSV.");
        }
    }
}
