package org.example.practica5;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    public static void cargarCSV(String archivo, CatalogoCartas catalogo) {
        int cargadas = 0;
        int numeroLinea = 0;

        try (BufferedReader lector = Files.newBufferedReader(
                Paths.get(archivo), StandardCharsets.UTF_8)) {

            String linea = lector.readLine(); // encabezado
            numeroLinea++;

            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] datos = separarCampos(linea);
                    if (datos.length < 6) {
                        System.err.println("Fila incompleta en la línea " + numeroLinea);
                        continue;
                    }

                    int indiceAtributo = datos.length - 4;
                    String nombre = String.join(",", java.util.Arrays.copyOfRange(
                            datos, 1, indiceAtributo
                    )).trim();
                    String atributo = datos[indiceAtributo].trim();
                    int nivel = Integer.parseInt(datos[indiceAtributo + 1].trim());
                    int ataque = Integer.parseInt(datos[indiceAtributo + 2].trim());
                    int defensa = Integer.parseInt(datos[indiceAtributo + 3].trim());

                    catalogo.agregarCarta(new Carta(
                            nombre, atributo, nivel, ataque, defensa
                    ));
                    cargadas++;
                } catch (NumberFormatException error) {
                    System.err.println("Datos numéricos inválidos en la línea " + numeroLinea);
                }
            }

            System.out.println("Cartas cargadas correctamente: " + cargadas);
        } catch (IOException error) {
            System.err.println("No se pudo leer el archivo CSV: " + error.getMessage());
        }
    }

    // Admite comas dentro de campos entrecomillados y CSV sin comillas.
    private static String[] separarCampos(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char actual = linea.charAt(i);

            if (actual == '"') {
                if (entreComillas && i + 1 < linea.length()
                        && linea.charAt(i + 1) == '"') {
                    campo.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (actual == ',' && !entreComillas) {
                campos.add(campo.toString());
                campo.setLength(0);
            } else {
                campo.append(actual);
            }
        }

        campos.add(campo.toString());
        return campos.toArray(new String[0]);
    }
}
