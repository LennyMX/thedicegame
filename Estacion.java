public class Estacion {
    private int id;
    private ColaSimple<Persona> colaActual;
    private ColaSimple<Persona> colaSiguiente;
    private Dado dado;
    private int capacidad;

    public Estacion(int id) {
        this.id= id;
        this.colaActual = new ColaSimple<>();
        this.colaSiguiente = new ColaSimple<>();
        this.dado = new Dado();
    }

    public void tirarDado() {
        capacidad = dado.lanzar();
    }

    public ColaSimple<Persona> procesar() {
        ColaSimple<Persona> salida = new ColaSimple<>();

        int procesados = 0;

        while (!colaActual.estaVacia() && procesados < capacidad) {
            salida.insertar(colaActual.eliminar());
            procesados++;
        }

        return salida;
    }

    public void recibir(ColaSimple<Persona> personas) {
        while (!personas.estaVacia()) {
            colaSiguiente.insertar(personas.eliminar());
        }
    }

    public void actualizarColas() {
        // Los que llegaron nuevos se unen a los que se quedaron atrapados
        while (!colaSiguiente.estaVacia()) {
            colaActual.insertar(colaSiguiente.eliminar());
        }
    }

    public ColaSimple<Persona> getColaActual() {
        return colaActual;
    }

    public int getCapacidad() {
        return capacidad;
    }
    public void recibirDirecto(Persona p) {
        colaSiguiente.insertar(p);
    }

}
