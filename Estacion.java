package com.example.thedicegame;
import java.util.ArrayList;

public class Estacion {
    private int id;
    private ColaSimple<Persona> cola;
    private Dado dado;
    private int UltimoValorDado;
    private ArrayList<Integer> historialDados = new ArrayList<>();
    private ArrayList<Integer> historialMovidos = new ArrayList<>();

    public Estacion(int id) {
        this.id = id;
        this.cola = new ColaSimple<>();
        this.dado = new Dado();
    }

    public void tirarDado(int turno) {
        this.UltimoValorDado = dado.lanzar();
        historialDados.add(UltimoValorDado);
    }

    public void registrarMovimiento(int turno, int cantidad) {
        historialMovidos.add(cantidad);
    }

    public int getUltimoValorDado() {
        return UltimoValorDado;
    }
    public ArrayList<Integer> getHistorialDados() {
        return historialDados;
    }
    public ArrayList<Integer> getHistorialMovidos() {
        return historialMovidos;
    }
    public void recibir(Persona p) {
        if (p != null)
            cola.insertar(p);
    }
    public Persona extraer() {
        return cola.eliminar();
    }
    public int getId() {
        return id;
    }
    public ColaSimple<Persona> getColaActual() {
        return cola;
    }
}
