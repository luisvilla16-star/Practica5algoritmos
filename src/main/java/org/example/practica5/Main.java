
package org.example.practica5;

public class Main {

    public static void main(String[] args) {

        CatalogoCartas catalogo = new CatalogoCartas();

        // Cargar las cartas del archivo CSV
        LectorCSV.cargarCSV(
                "C:\\Users\\LUIS GABRIEL VILLA\\Downloads\\cartas.csv",
                catalogo
        );

        // Organizar las listas circulares
        catalogo.organizarListas();

        // Mostrar resultados
        System.out.println("TODAS LAS CARTAS:");
        System.out.println(catalogo.mostrarTodas());

        System.out.println("CARTAS POR NIVEL:");
        System.out.println(catalogo.mostrarPorNivel());

        System.out.println("CARTAS POR ATRIBUTO:");
        System.out.println(catalogo.mostrarPorAtributo());

        System.out.println("CARTAS POR ATAQUE:");
        System.out.println(catalogo.mostrarPorAtaque());

        System.out.println("CARTAS POR DEFENSA:");
        System.out.println(catalogo.mostrarPorDefensa());

        System.out.println("Cantidad de cartas: "
                + catalogo.cantidadCartas());
    }
}
