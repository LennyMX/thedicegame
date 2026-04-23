package com.example.thedicegame;

public class Simulador {

    private Estacion[] estaciones;
    private int tiempo;
    private int contadorPersonas;
    private int totalProcesados;
    private int turnoActual = 0;
    private int[][] historialDados = new int[21][10];
    private int[][] historialMovidos = new int[21][10];
    private int[] historialThroughput = new int[21];
    private int[] historialWIP = new int[21];
    private int[] tiemposDeCiclo = new int[1000];
    private int totalTiemposRegistrados = 0;

    public Simulador() {
        estaciones = new Estacion[10];
        for (int i = 0; i < 10; i++) {
            estaciones[i] = new Estacion(i + 1);
        }
    }

    public void inicializar() {
        contadorPersonas = 1;
        tiempo = 0;
        totalProcesados = 0;
        totalTiemposRegistrados = 0;
        turnoActual = 0;

        for (Estacion e : estaciones) {
            e.getColaActual().limpiar();
            // Llenado inicial: 4 personas por estación
            for (int i = 0; i < 4; i++) {
                e.recibir(new Persona(contadorPersonas++, 0));
            }
        }
        historialThroughput[0] = 0;
        historialWIP[0] = calcularPersonasEnSistema();
    }
    public void tirarDados() {
        if (turnoActual >= 20) return;
        for (int i = 0; i < 10; i++) {
            estaciones[i].tirarDado();
            historialDados[turnoActual + 1][i] = estaciones[i].getCapacidad();
        }
    }
    public void moverPersonas() {
        turnoActual++;
        tiempo++;

        // 1. Primero calculamos cuántos se van a mover de cada estación
        // para que no haya "saltos" de dos estaciones en el mismo turno.
        int[] cantidadAMover = new int[10];
        for (int i = 0; i < 10; i++) {
            int enCola = estaciones[i].getColaActual().size();
            int dado = estaciones[i].getCapacidad();
            // Solo pueden moverse los que ya estaban al iniciar el turno
            cantidadAMover[i] = Math.min(enCola, dado);
            historialMovidos[turnoActual][i] = cantidadAMover[i];
        }

        // 2. Realizamos el movimiento físico
        for (int i = 9; i >= 0; i--) {
            int aMover = cantidadAMover[i];

            for (int j = 0; j < aMover; j++) {
                Persona p = estaciones[i].extraer();

                if (i == 9) { // Es la última estación
                    p.setTiempoSalida(tiempo);
                    if (p.getTiempoEntrada() > 0 && totalTiemposRegistrados < 1000) {
                        tiemposDeCiclo[totalTiemposRegistrados++] = p.getDuracion();
                    }
                    totalProcesados++;
                } else { // Pasa a la siguiente estación
                    estaciones[i + 1].recibir(p);
                }
            }
        }

        // 3. Entrada de nuevas personas a la Estación 1 (según su dado)
        int dadoEntrada = estaciones[0].getCapacidad();
        for (int i = 0; i < dadoEntrada; i++) {
            estaciones[0].recibir(new Persona(contadorPersonas++, tiempo));
        }

        // 4. Registrar datos del turno
        if (turnoActual < 21) {
            historialThroughput[turnoActual] = totalProcesados;
            historialWIP[turnoActual] = calcularPersonasEnSistema();
        }
    }

    public int calcularPersonasEnSistema() {
        int total = 0;
        for (Estacion e : estaciones) {
            total += e.getColaActual().size();
        }
        return total;
    }
    public int[] getHistorialThroughput() { return historialThroughput; }
    public int[] getHistorialWIP() { return historialWIP; }
    public int getTotalProcesados() { return totalProcesados; }
    public Estacion[] getEstaciones() { return estaciones; }
    public int[] getTiemposDeCiclo() { return tiemposDeCiclo; }
    public int[][] getHistorialDados() {
        return historialDados;
    }
    public int[][] getHistorialMovidos() {
        return historialMovidos;
    }
}
