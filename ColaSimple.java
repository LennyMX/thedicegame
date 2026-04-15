public class ColaSimple<T> {

    private T[] cola;
    private int inicio;
    private int fin;
    private int size;

    public ColaSimple() {
        cola = (T[]) new Object[10];
        inicio = 0;
        fin = 0;
        size = 0;
    }

    public void insertar(T dato) {
        if (size == cola.length) {
            expandir();
        }

        cola[fin] = dato;
        fin = (fin + 1) % cola.length;
        size++;
    }

    public T eliminar() {
        if (size == 0) return null;

        T dato = cola[inicio];
        inicio = (inicio + 1) % cola.length;
        size--;
        return dato;
    }

    private void expandir() {
        T[] nueva = (T[]) new Object[cola.length * 2];

        for (int i = 0; i < size; i++) {
            nueva[i] = cola[(inicio + i) % cola.length];
        }

        cola = nueva;
        inicio = 0;
        fin = size;
    }

    public boolean estaVacia() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void limpiar() {
        inicio = 0;
        fin = 0;
        size = 0;
    }

}
