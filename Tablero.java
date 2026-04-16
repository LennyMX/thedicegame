import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Tablero extends JPanel {

    private Simulador simulador;
    private Point[] casillas = new Point[10];
    private Image[] dados = new Image[6];
    private Image persona;

    public Tablero(Simulador simulador) {
        this.simulador = simulador;

        int inicioX = 100;
        int inicioY = 100;
        int espacio = 120;

        // ARRIBA (4)
        casillas[0] = new Point(inicioX, inicioY);
        casillas[1] = new Point(inicioX + espacio, inicioY);
        casillas[2] = new Point(inicioX + 2 * espacio, inicioY);
        casillas[3] = new Point(inicioX + 3 * espacio, inicioY);

        // DERECHA (2)
        casillas[4] = new Point(inicioX + 3 * espacio, inicioY + espacio);
        casillas[5] = new Point(inicioX + 3 * espacio, inicioY + 2 * espacio);

        // ABAJO (4)
        casillas[6] = new Point(inicioX + 3 * espacio, inicioY + 3 * espacio);
        casillas[7] = new Point(inicioX + 2 * espacio, inicioY + 3 * espacio);
        casillas[8] = new Point(inicioX + espacio, inicioY + 3 * espacio);
        casillas[9] = new Point(inicioX, inicioY + 3 * espacio);

        setBackground(Color.WHITE);
        try {
            for (int i = 0; i < 6; i++) {
                dados[i] = new ImageIcon(getClass().getResource("/img/dado" + (i + 1) + ".png")).getImage();
            }
        } catch (Exception e) {
            System.out.println("Error cargando imágenes de dados");
        }

        try {
            persona = new ImageIcon(getClass().getResource("/img/persona.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error cargando imagen de persona");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[] tablero = simulador.getTablero(); 
        int[] dadosValores = simulador.getDados();
        for (int i = 0; i < 10; i++) {

            int x = casillas[i].x;
            int y = casillas[i].y;

            g.setColor(Color.BLACK);
            g.drawRect(x, y, 60, 60);

            dibujarJugadores(g, tablero[i], x, y);

            int valorDado = dadosValores[i];

            if (valorDado > 0) {
                g.drawImage(dados[valorDado - 1], x + 15, y - 35, 30, 30, this);
            }
            g.drawImage(persona, x + 65, y + 10, 40, 40, this);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));

            g.drawString(String.valueOf(tablero[i]), x + 20, y + 80);
        }
        dibujarSalida(g);

    }

    private void dibujarJugadores(Graphics g, int personas, int x, int y) {

        g.setColor(Color.BLUE);

        for (int j = 0; j < personas; j++) {

            int px = x + 5 + (j % 4) * 14;
            int py = y + 5 + (j / 4) * 14;

            g.fillOval(px, py, 12, 12);
        }
    }
    private void dibujarSalida(Graphics g) {

        int total = simulador.getTotalProcesados();

        int inicioX = 50;   // posición izquierda
        int inicioY = 400;  // posición vertical

        int columnas = 5;   // ancho de la pila
        int espacio = 14;

        for (int i = 0; i < total; i++) {

            int col = i % columnas;
            int fila = i / columnas;

            int x = inicioX + col * espacio;
            int y = inicioY - fila * espacio;

            g.setColor(Color.GRAY);
            g.fillOval(x, y, 12, 12);
        }
    }
}
