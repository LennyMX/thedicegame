import javax.swing.*;
import java.awt.*;

public class Interfaz extends JFrame {

    private Simulador simulador;
    private Tablero panel;

    private enum Estado {
        START, ROLL, MOVE, FIN
    }

    private Estado estado = Estado.START;
    private int turnos = 0;

    public Interfaz() {

        simulador = new Simulador();

        setTitle("Simulador Visual");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new Tablero(simulador);
        add(panel, BorderLayout.CENTER);

        JPanel botones = new JPanel();

        JLabel lblRonda = new JLabel("Ronda: 0 / 20");
        botones.add(lblRonda);

        JButton btnAccion = new JButton("START");
        botones.add(btnAccion);

        add(botones, BorderLayout.SOUTH);


        btnAccion.addActionListener(e -> {

            switch (estado) {

                case START:
                    simulador.inicializar();
                    estado = Estado.ROLL;
                    btnAccion.setText("ROLL");
                    break;

                case ROLL:
                    simulador.tirarDados();
                    estado = Estado.MOVE;
                    btnAccion.setText("MOVE");
                    break;

                case MOVE:
                    simulador.moverPersonas();
                    turnos++;

                    if (turnos >= 20) {
                        estado = Estado.FIN;
                        btnAccion.setText("FIN");
                    } else {
                        estado = Estado.ROLL;
                        btnAccion.setText("ROLL");
                    }
                    lblRonda.setText("Ronda: " + turnos + " / 20");
                    break;

                case FIN:
                    simulador.inicializar();
                    estado = Estado.START;
                    turnos = 0;
                    lblRonda.setText("Ronda: 0 / 20");
                    btnAccion.setText("START");
                    break;
            }

            panel.repaint();
        });

        setVisible(true);
    }
}
