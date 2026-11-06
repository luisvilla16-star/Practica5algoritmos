
package org.example.practica5;

public class ListaCircular<T> {

    private Nodo<T> inicio;
    private Nodo<T> fin;

    public void insertarInicio(T dato) {
        Nodo<T> n = new Nodo<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            n.setNext(inicio);
        } else {
            n.setNext(inicio);
            inicio = n;
            fin.setNext(inicio);
        }
    }

    public void insertarFinal(T dato) {
        Nodo<T> n = new Nodo<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            n.setNext(inicio);
        } else {
            fin.setNext(n);
            n.setNext(inicio);
            fin = n;
        }
    }

    public T eliminarInicio() {
        if (inicio == null) {
            return null;
        } else if (inicio == fin) {
            T dato = inicio.getDato();
            inicio = fin = null;
            return dato;
        } else {
            T dato = inicio.getDato();
            inicio = inicio.getNext();
            fin.setNext(inicio);
            return dato;
        }
    }

    public String mostrarLista() {
        String cad = "";

        if (inicio != null) {
            Nodo<T> r = inicio;

            while (r != fin) {
                cad = cad + r.getDato() + " ";
                r = r.getNext();
            }

            cad = cad + r.getDato();
        }

        return cad;
    }

    public Nodo<T> getInicio() {
        return inicio;
    }
}
