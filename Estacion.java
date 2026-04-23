package com.example.thedicegame;
public class Estacion {
    private int id;
    private ColaSimple<Persona> cola;
    private Dado dado;
    private int capacidad;

    public Estacion(int id) {
        this.id = id;
        this.cola = new ColaSimple<>();
        this.dado = new Dado();
    }
    public void tirarDado() {
        this.capacidad = dado.lanzar();
    }
    public void recibir(Persona p) {
        if (p != null) {
            cola.insertar(p);
        }
    }
    public Persona extraer() {
        return cola.eliminar();
    }
    public int getCapacidad() {
        return capacidad;
    }

    public int getId() {
        return id;
    }

    public ColaSimple<Persona> getColaActual() {
        return cola;
    }
}
