public class Persona {
    private int id;
    private int tiempoEntrada;
    private int tiempoSalida;
    private boolean esNueva;

    public Persona(int id, int tiempoEntrada) {
        this.id = id;
        this.tiempoEntrada = tiempoEntrada;
        this.esNueva = true;
    }

    public int getId() {

        return id;
    }

    public int getTiempoEntrada() {

        return tiempoEntrada;
    }

    public void setTiempoSalida(int tiempoSalida) {

        this.tiempoSalida = tiempoSalida;
    }

    public int getTiempoSalida() {

        return tiempoSalida;
    }
    public boolean esNueva() {
        return esNueva;
    }
    public void setEsNueva(boolean v) {
        this.esNueva = v;
    }
}
