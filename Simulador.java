import java.util.ArrayList;
import java.util.List;

public class Simulador {

    private List<Estacion> estaciones;
    private int tiempo;
    private int contadorPersonas;
    private int totalProcesados;

    public Simulador() {
        estaciones = new ArrayList<>();
        tiempo = 0;
        contadorPersonas = 1;
        totalProcesados = 0;

        for (int i = 0; i < 10; i++) {
            estaciones.add(new Estacion(i + 1));
        }
    }

    public void inicializar() {

        for (Estacion e : estaciones) {
            e.getColaActual().limpiar();
        }

        contadorPersonas = 1;
        tiempo = 0;
        totalProcesados = 0;

        for (Estacion e : estaciones) {
            for (int i = 0; i < 4; i++) {
                e.getColaActual().insertar(
                        new Persona(contadorPersonas++, tiempo)
                );
            }
        }
    }

    public void tirarDados() {
        for (Estacion e : estaciones) {
            e.tirarDado();
        }
    }

    public void moverPersonas() {

        tiempo++;

        List<ColaSimple<Persona>> salidas = new ArrayList<>();

        for (Estacion e : estaciones) {
            salidas.add(e.procesar());
        }

        for (int i = 0; i < estaciones.size(); i++) {

            if (i == 0) {
                int capacidadJugador1 = estaciones.get(0).getCapacidad();
                for (int j = 0; j < capacidadJugador1; j++) {
                    // Entran al sistema, pero se quedan en colaSiguiente para el próximo turno
                    estaciones.get(0).recibirDirecto(new Persona(contadorPersonas++, tiempo));
                }
            }

            if (i < estaciones.size() - 1) {
                estaciones.get(i + 1).recibir(salidas.get(i));
            } else {
                while (!salidas.get(i).estaVacia()) {
                    Persona p = salidas.get(i).eliminar();
                    if (p != null) {
                        p.setTiempoSalida(tiempo);
                        totalProcesados++;
                    }
                }
            }
        }

        for (Estacion e : estaciones) {
            e.actualizarColas();
        }
    }

    public List<Estacion> getEstaciones() {
        return estaciones;
    }

    public int getTiempo() {
        return tiempo;
    }
    public int[] getTablero() {

        int[] tablero = new int[10];

        for (int i = 0; i < 10; i++) {
            tablero[i] = estaciones.get(i).getColaActual().size();
        }

        return tablero;
    }
    public int getTotalProcesados() {
        return totalProcesados;
    }

    public int[] getDados() {

        int[] valores = new int[estaciones.size()];

        for (int i = 0; i < estaciones.size(); i++) {
            valores[i] = estaciones.get(i).getCapacidad();
        }

        return valores;
    }
}
