
package org.example.practica5;

public class ListaCircularDoble<T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    public void insertarInicio(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            inicio.setAnd(fin);
            fin.setSig(inicio);
        } else {
            n.setSig(inicio);
            n.setAnd(fin);
            inicio.setAnd(n);
            inicio = n;
            fin.setSig(inicio);
        }
    }

    public void insertarFin(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            fin.setSig(inicio);
            inicio.setAnd(fin);
        } else {
            n.setAnd(fin);
            n.setSig(inicio);
            fin.setSig(n);
            fin = n;
            inicio.setAnd(fin);
        }
    }

    public T eliminarInicio() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio == fin) {
            T dato = inicio.getInfo();
            inicio = fin = null;
            return dato;
        } else {
            T dato = inicio.getInfo();
            inicio = inicio.getSig();
            inicio.setAnd(fin);
            fin.setSig(inicio);
            return dato;
        }
    }

    public T eliminarFinal() {
        if (inicio == null) {
            System.out.println("Lista Vacia");
            return null;
        } else if (inicio == fin) {
            T dato = inicio.getInfo();
            inicio = fin = null;
            return dato;
        } else {
            T dato = fin.getInfo();
            fin = fin.getAnd();
            fin.setSig(inicio);
            inicio.setAnd(fin);
            return dato;
        }
    }
}
