package com.example.thedicegame;
import java.util.ArrayList;

public class Simulador{
    private ColaSimple<Estacion> estaciones;
    private int tiempo;
    private int contadorPersonas;
    private int totalProcesados;
    private int turnoActual;
    private ArrayList<Integer> historialThroughput = new ArrayList<>();
    private ArrayList<Integer> historialWIP = new ArrayList<>();
    private ArrayList<Integer> tiemposDeCiclo = new ArrayList<>();

    public Simulador(){
        estaciones = new ColaSimple<>();
        for (int i = 1; i <= 10; i++) {
            estaciones.insertar(new Estacion(i));
        }
    }

    public void inicializar(){
        contadorPersonas = 1;
        tiempo = 0;
        totalProcesados = 0;
        turnoActual = 0;
        historialThroughput.clear();
        historialWIP.clear();
        tiemposDeCiclo.clear();
        for (int i = 0; i < 10; i++) {
            Estacion e = estaciones.eliminar();
            e.getColaActual().limpiar();
            for (int j = 0; j < 4; j++) {
                e.recibir(new Persona(contadorPersonas++, 0));
            }
            estaciones.insertar(e);
        }
        historialWIP.add(calcularPersonasEnSistema());
    }

    public void tirarDados(){
        for (int i = 0; i < 10; i++) {
            Estacion e = estaciones.eliminar();
            e.tirarDado(turnoActual);
            estaciones.insertar(e);
        }
    }

    public void moverPersonas(){
        turnoActual++;
        tiempo++;
        ArrayList<Estacion> lista = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lista.add(estaciones.eliminar());
        }
        for (int i = 9; i >= 0; i--) {
            Estacion actual = lista.get(i);
            int dado = actual.getUltimoValorDado();
            int enCola = actual.getColaActual().size();
            int aMover = Math.min(enCola, dado);

            for (int j = 0; j < aMover; j++) {
                Persona p = actual.extraer();

                if (i == 9) {
                    p.setTiempoSalida(tiempo);
                    tiemposDeCiclo.add(p.getDuracion());
                    totalProcesados++;
                } else {
                    lista.get(i + 1).recibir(p);
                }
            }
            actual.registrarMovimiento(turnoActual, aMover);
        }
        int dadoEntrada = lista.get(0).getUltimoValorDado();
        for (int i = 0; i < dadoEntrada; i++) {
            lista.get(0).recibir(new Persona(contadorPersonas++, tiempo));
        }
        for (Estacion e : lista) {
            estaciones.insertar(e);
        }

        historialThroughput.add(totalProcesados);
        historialWIP.add(calcularPersonasEnSistema());
    }

    public int calcularPersonasEnSistema(){
        int total = 0;
        for (int i = 0; i < 10; i++) {
            Estacion e = estaciones.eliminar();
            total += e.getColaActual().size();
            estaciones.insertar(e);
        }
        return total;
    }
    public ArrayList<Integer> getHistorialThroughput(){
        return historialThroughput;
    }
    public ArrayList<Integer> getHistorialWIP(){
        return historialWIP;
    }
    public ArrayList<Integer> getTiemposDeCiclo(){
        return tiemposDeCiclo;
    }
    public int getTotalProcesados(){
        return totalProcesados;
    }
    public ColaSimple<Estacion> getEstaciones(){
        return estaciones;
    }
}
