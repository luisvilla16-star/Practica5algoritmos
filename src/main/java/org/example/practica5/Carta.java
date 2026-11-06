package org.example.practica5;
public class Carta {
    private String nombre;
    private String atributo;
    private int nivel;
    private int ataque;
    private int defensa;

    public Carta(String nombre, String atributo, int nivel, int ataque, int defensa) {
        this.nombre = nombre;
        this.atributo = atributo;
        this.nivel = nivel;
        this.ataque = ataque;
        this.defensa = defensa;
    }

    // Getters correspondientes
    public String getNombre() { return nombre; }
    public String getAtributo() { return atributo; }
    public int getNivel() { return nivel; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }

    @Override
    public String toString() {
        return "Carta{" +
                "Nombre='" + nombre + '\'' +
                ", Atributo='" + atributo + '\'' +
                ", Nivel=" + nivel +
                ", Ataque=" + ataque +
                ", Defensa=" + defensa +
                '}';
    }
}