package com.example.thedicegame;
import java.util.ArrayList;
import java.util.List;

public class Simulador {

    private List<Estacion> estaciones;
    private int tiempo;
    private int contadorPersonas;
    private int totalProcesados;
    private int[][] historialDados = new int[21][10];
    private int[][] historialMovidos = new int[21][10];
    private int turnoActual = 0;

    // Listas para las gráficas
    private List<Integer> historialThroughput;
    private List<Integer> historialWIP;
    private List<Integer> tiemposDeCiclo = new ArrayList<>();

    public Simulador() {
        estaciones = new ArrayList<>();
        historialThroughput = new ArrayList<>();
        historialWIP = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            estaciones.add(new Estacion(i + 1));
        }
    }

    public void inicializar() {
        contadorPersonas = 1;
        tiempo = 0;
        totalProcesados = 0;
        historialThroughput.clear();
        historialWIP.clear();
        tiemposDeCiclo.clear();

        turnoActual = 0;
        historialDados = new int[21][10];
        historialMovidos = new int[21][10];

        for (Estacion e : estaciones) {
            e.getColaActual().limpiar();
            //4 personas iniciales para cada estación
            for (int i = 0; i < 4; i++) {
                e.getColaActual().insertar(new Persona(contadorPersonas++, 0));
            }
        }
        historialThroughput.add(0);
        historialWIP.add(calcularPersonasEnSistema());
    }

    public void tirarDados() {
        for (int i = 0; i < 10; i++) {
            estaciones.get(i).tirarDado();
            historialDados[turnoActual + 1][i] = estaciones.get(i).getCapacidad();
        }
    }

    public void moverPersonas() {
        turnoActual++;
        tiempo++;

        // 1. Procesar cada estación 
        List<ColaSimple<Persona>> salidasPorEstacion = new ArrayList<>();
        for (Estacion e : estaciones) {
            salidasPorEstacion.add(e.procesar());
        }

        // 2. Registrar Actividad 
        for (int i = 0; i < 10; i++) {
            // Guardamos cuántos lograron salir de cada estación antes de moverlos
            historialMovidos[turnoActual][i] = salidasPorEstacion.get(i).size();
        }
        // 3. Mover las personas a la siguiente posición
        for (int i = 0; i < estaciones.size(); i++) {

            //1: Entra tanta gente nueva como diga su dado 
            if (i == 0) {
                int capacidadEntrada = estaciones.get(0).getCapacidad();
                for (int j = 0; j < capacidadEntrada; j++) {
                    estaciones.get(0).recibirDirecto(new Persona(contadorPersonas++, tiempo));
                }
            }

            // MOVER DE ESTACIÓN i A i+1
            if (i < estaciones.size() - 1) {
                // Los que salieron de la estación actual entran a la cola de la siguiente
                estaciones.get(i + 1).recibir(salidasPorEstacion.get(i));
            } else {
                // Estación 10
                ColaSimple<Persona> terminados = salidasPorEstacion.get(i);

                while (!terminados.estaVacia()) {
                    Persona p = terminados.eliminar();
                    if (p != null) {
                        p.setTiempoSalida(tiempo);
                        if (p.getTiempoEntrada() > 0) {
                            tiemposDeCiclo.add(p.getDuracion());
                        }

                        totalProcesados++;
                    }
                }
            }
        }

        // 4. Actualizar Colas los que llegaron nuevos pasan a ser actuales para el próximo turno
        for (Estacion e : estaciones) {
            e.actualizarColas();
        }

        // 5. Registrar datos acumulados para las gráficas de línea/barras generales
        historialThroughput.add(totalProcesados);
        historialWIP.add(calcularPersonasEnSistema());
    }

    public int calcularPersonasEnSistema() {
        int total = 0;
        for (Estacion e : estaciones) {
            total += e.getColaActual().size();
        }
        return total;
    }

    // Getters para la Interfaz y Gráficas
    public List<Integer> getHistorialThroughput() { return historialThroughput; }
    public List<Integer> getHistorialWIP() { return historialWIP; }
    public int getTotalProcesados() { return totalProcesados; }
    public List<Estacion> getEstaciones() { return estaciones; }

    public int[] getTablero() {
        int[] tablero = new int[10];
        for (int i = 0; i < 10; i++) {
            tablero[i] = estaciones.get(i).getColaActual().size();
        }
        return tablero;
    }

    public int[] getDados() {
        int[] valores = new int[estaciones.size()];
        for (int i = 0; i < estaciones.size(); i++) {
            valores[i] = estaciones.get(i).getCapacidad();
        }
        return valores;
    }
    public List<Integer> getTiemposDeCiclo() {
        return tiemposDeCiclo;
    }
    public int[][] getHistorialDados() { return historialDados; }
    public int[][] getHistorialMovidos() { return historialMovidos; }
}
