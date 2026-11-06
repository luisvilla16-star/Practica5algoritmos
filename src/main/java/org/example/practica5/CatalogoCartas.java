
package org.example.practica5;

import java.util.ArrayList;

public class CatalogoCartas {

    private ListaCircular<Carta> todas;
    private ListaCircular<Carta> porNivel;
    private ListaCircular<Carta> porAtributo;

    private ListaCircularDoble<Carta> porAtaque;
    private ListaCircularDoble<Carta> porDefensa;

    private ArrayList<Carta> auxiliar;

    public CatalogoCartas() {

        todas = new ListaCircular<>();
        porNivel = new ListaCircular<>();
        porAtributo = new ListaCircular<>();

        porAtaque = new ListaCircularDoble<>();
        porDefensa = new ListaCircularDoble<>();

        auxiliar = new ArrayList<>();
    }

    // Agregar una carta
    public void agregarCarta(Carta carta) {

        if (carta != null) {
            todas.insertarFinal(carta);
            auxiliar.add(carta);
        }
    }

    // Organizar las listas
    public void organizarListas() {

        ArrayList<Carta> temporal;
        Carta aux;

        // Ordenar por nivel
        temporal = new ArrayList<>(auxiliar);

        for (int i = 0; i < temporal.size(); i++) {
            for (int j = 0; j < temporal.size() - 1; j++) {

                if (temporal.get(j).getNivel() > temporal.get(j + 1).getNivel()) {
                    aux = temporal.get(j);
                    temporal.set(j, temporal.get(j + 1));
                    temporal.set(j + 1, aux);
                }
            }
        }

        pasarLista(porNivel, temporal);

        // Ordenar por atributo
        temporal = new ArrayList<>(auxiliar);

        for (int i = 0; i < temporal.size(); i++) {
            for (int j = 0; j < temporal.size() - 1; j++) {

                if (temporal.get(j).getAtributo().compareToIgnoreCase(
                        temporal.get(j + 1).getAtributo()) > 0) {

                    aux = temporal.get(j);
                    temporal.set(j, temporal.get(j + 1));
                    temporal.set(j + 1, aux);
                }
            }
        }

        pasarLista(porAtributo, temporal);

        // Ordenar por ataque
        temporal = new ArrayList<>(auxiliar);

        for (int i = 0; i < temporal.size(); i++) {
            for (int j = 0; j < temporal.size() - 1; j++) {

                if (temporal.get(j).getAtaque() > temporal.get(j + 1).getAtaque()) {
                    aux = temporal.get(j);
                    temporal.set(j, temporal.get(j + 1));
                    temporal.set(j + 1, aux);
                }
            }
        }

        pasarListaDoble(porAtaque, temporal);

        // Ordenar por defensa
        temporal = new ArrayList<>(auxiliar);

        for (int i = 0; i < temporal.size(); i++) {
            for (int j = 0; j < temporal.size() - 1; j++) {

                if (temporal.get(j).getDefensa() > temporal.get(j + 1).getDefensa()) {
                    aux = temporal.get(j);
                    temporal.set(j, temporal.get(j + 1));
                    temporal.set(j + 1, aux);
                }
            }
        }

        pasarListaDoble(porDefensa, temporal);
    }

    // Pasar cartas a una lista circular simple
    private void pasarLista(ListaCircular<Carta> lista, ArrayList<Carta> temporal) {

        while (lista.eliminarInicio() != null) {
        }

        for (int i = 0; i < temporal.size(); i++) {
            lista.insertarFinal(temporal.get(i));
        }
    }

    // Pasar cartas a una lista circular doble
    private void pasarListaDoble(ListaCircularDoble<Carta> lista, ArrayList<Carta> temporal) {

        while (lista.eliminarInicio() != null) {
        }

        for (int i = 0; i < temporal.size(); i++) {
            lista.insertarFin(temporal.get(i));
        }
    }

    // Mostrar las cartas
    public String mostrarTodas() {
        return todas.mostrarLista();
    }

    public String mostrarPorNivel() {
        return porNivel.mostrarLista();
    }

    public String mostrarPorAtributo() {
        return porAtributo.mostrarLista();
    }

    // Mostrar por ataque sin modificar ListaCircularDoble
    public String mostrarPorAtaque() {

        ArrayList<Carta> temporal = new ArrayList<>();

        Carta carta;

        while ((carta = porAtaque.eliminarInicio()) != null) {
            temporal.add(carta);
        }

        for (int i = 0; i < temporal.size(); i++) {
            porAtaque.insertarFin(temporal.get(i));
        }

        String cad = "";

        for (int i = 0; i < temporal.size(); i++) {
            cad = cad + temporal.get(i) + " ";
        }

        return cad;
    }

    // Mostrar por defensa sin modificar ListaCircularDoble
    public String mostrarPorDefensa() {

        ArrayList<Carta> temporal = new ArrayList<>();

        Carta carta;

        while ((carta = porDefensa.eliminarInicio()) != null) {
            temporal.add(carta);
        }

        for (int i = 0; i < temporal.size(); i++) {
            porDefensa.insertarFin(temporal.get(i));
        }

        String cad = "";

        for (int i = 0; i < temporal.size(); i++) {
            cad = cad + temporal.get(i) + " ";
        }

        return cad;
    }

    // Obtener las listas
    public ListaCircular<Carta> getTodas() {
        return todas;
    }

    public ListaCircular<Carta> getPorNivel() {
        return porNivel;
    }

    public ListaCircular<Carta> getPorAtributo() {
        return porAtributo;
    }

    public ListaCircularDoble<Carta> getPorAtaque() {
        return porAtaque;
    }

    public ListaCircularDoble<Carta> getPorDefensa() {
        return porDefensa;
    }

    // Cantidad de cartas
    public int cantidadCartas() {
        return auxiliar.size();
    }
}
