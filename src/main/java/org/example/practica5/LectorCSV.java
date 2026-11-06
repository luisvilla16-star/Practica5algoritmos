
package org.example.practica5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorCSV {

    public static void cargarCSV(String archivo, CatalogoCartas catalogo) {

        try {

            BufferedReader lector = new BufferedReader(
                    new FileReader(archivo));

            String linea;

            // Saltar encabezados
            lector.readLine();

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split(",");

                String nombre = datos[1];
                String atributo = datos[2];
                int nivel = Integer.parseInt(datos[3]);
                int ataque = Integer.parseInt(datos[4]);
                int defensa = Integer.parseInt(datos[5]);

                Carta carta = new Carta(
                        nombre,
                        atributo,
                        nivel,
                        ataque,
                        defensa
                );

                catalogo.agregarCarta(carta);
            }

            lector.close();

            System.out.println("Archivo cargado correctamente.");

        } catch (IOException e) {

            System.out.println("Error al leer el archivo.");

        } catch (NumberFormatException e) {

            System.out.println("Error en los datos numericos del CSV.");

        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println("Hay una fila incompleta en el CSV.");
        }
    }
}
