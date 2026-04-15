import java.util.Random;

public class Dado {
    private Random random;
    private int ultimoValor;

    public Dado() {
        random = new Random();
        ultimoValor = 0;
    }

    public int lanzar() {

        return random.nextInt(6) + 1;
    }
    public int getUltimoValor() {

        return ultimoValor;
    }
}
