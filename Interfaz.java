package com.example.thedicegame;
import javax.swing.*;
import java.awt.*;

public class Interfaz extends JFrame {

    private Simulador simulador;
    private Tablero panel;
    private JButton btnAccion;
    private JLabel lblTurnosValor;

    private enum Estado {
        START, ROLL, MOVE, FIN
    }

    private Estado estado = Estado.START;
    private int turnos = 0;

    public Interfaz() {
        simulador = new Simulador();

        setTitle("The Dice Game");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel = new Tablero(simulador);
        add(panel, BorderLayout.CENTER);

        JPanel panelLateral = new JPanel();
        panelLateral.setBackground(new Color(60, 34, 34));
        panelLateral.setPreferredSize(new Dimension(200, 0));
        panelLateral.setLayout(new BorderLayout());
        JPanel pnlBotonesGraficas = new JPanel(new GridLayout(5, 1, 0, 10));
        pnlBotonesGraficas.setOpaque(false);
        pnlBotonesGraficas.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));

        String[] nombresStats = {"Activity", "Throughput", "Number in system", "Time in system", "Your performance"};
        for (String nombre : nombresStats) {
            JButton btn = new JButton(nombre);
            btn.setPreferredSize(new Dimension(170, 40));
            btn.setBackground(new Color(210, 200, 190));
            btn.setFocusable(false);
            btn.addActionListener(e -> abrirGrafica(nombre));
            pnlBotonesGraficas.add(btn);
        }
        panelLateral.add(pnlBotonesGraficas, BorderLayout.NORTH);

        panelLateral.add(pnlBotonesGraficas, BorderLayout.NORTH);
        JPanel pnlTurnos = new JPanel();
        pnlTurnos.setLayout(new BoxLayout(pnlTurnos, BoxLayout.Y_AXIS));
        pnlTurnos.setOpaque(false);

        JLabel lblTurnsTitulo = new JLabel("Turns");
        lblTurnsTitulo.setForeground(Color.WHITE);
        lblTurnsTitulo.setFont(new Font("Arial", Font.PLAIN, 24));
        lblTurnsTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTurnosValor = new JLabel("0");
        lblTurnosValor.setForeground(Color.WHITE);
        lblTurnosValor.setFont(new Font("Arial", Font.BOLD, 48));
        lblTurnosValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlTurnos.add(Box.createVerticalGlue());
        pnlTurnos.add(lblTurnsTitulo);
        pnlTurnos.add(lblTurnosValor);
        pnlTurnos.add(Box.createVerticalGlue());

        panelLateral.add(pnlTurnos, BorderLayout.CENTER);
        btnAccion = new JButton("Start");
        btnAccion.setPreferredSize(new Dimension(200, 100));
        btnAccion.setFont(new Font("Arial", Font.PLAIN, 36));
        btnAccion.setBackground(new Color(210, 200, 190));
        btnAccion.setFocusable(false);

        btnAccion.addActionListener(e -> manejarAccion());
        panelLateral.add(btnAccion, BorderLayout.SOUTH);

        add(panelLateral, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void manejarAccion() {
        switch (estado) {
            case START:
                simulador.inicializar();
                estado = Estado.ROLL;
                btnAccion.setText("Roll");
                turnos = 0;
                lblTurnosValor.setText("0");
                break;

            case ROLL:
                simulador.tirarDados();
                estado = Estado.MOVE;
                btnAccion.setText("Move");
                break;

            case MOVE:
                simulador.moverPersonas();
                turnos++;
                lblTurnosValor.setText(String.valueOf(turnos));

                if (turnos >= 20) {
                    estado = Estado.FIN;
                    btnAccion.setText("Reset");
                } else {
                    estado = Estado.ROLL;
                    btnAccion.setText("Roll");
                }
                break;

            case FIN:
                simulador.inicializar();
                estado = Estado.START;
                btnAccion.setText("Start");
                turnos = 0;
                lblTurnosValor.setText("0");
                break;
        }
        panel.repaint();
    }

    private void abrirGrafica(String tipo) {
        switch (tipo) {
            case "Throughput":
                new Graficas("Throughput", simulador.getHistorialThroughput());
                break;

            case "Number in system":
                new Graficas("Number in system", simulador.getHistorialWIP());
                break;

            case "Time in system":
                if (simulador.getTiemposDeCiclo().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay datos de salida aún.");
                } else {
                    new Graficas("Time in system", simulador.getTiemposDeCiclo());
                }
                break;

            case "Activity":
                new GraficaActividad(simulador.getHistorialDados(), simulador.getHistorialMovidos());
                break;
        }
    }
}
