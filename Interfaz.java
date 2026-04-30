package com.example.thedicegame;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
            // Variable final para evitar errores en la lambda
            final String seleccion = nombre;
            btn.addActionListener(e -> abrirGrafica(seleccion));
            pnlBotonesGraficas.add(btn);
        }
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

        // Referencia directa al método de la instancia
        btnAccion.addActionListener(e -> this.manejarAccion());
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
                ArrayList<Integer> listaT = simulador.getHistorialThroughput();
                int[] arrT = new int[listaT.size()];
                for(int i=0; i<listaT.size(); i++) arrT[i] = listaT.get(i);
                new Graficas("Throughput", arrT);
                break;

            case "Number in system":
                ArrayList<Integer> listaW = simulador.getHistorialWIP();
                int[] arrW = new int[listaW.size()];
                for(int i=0; i<listaW.size(); i++) arrW[i] = listaW.get(i);
                new Graficas("Number in system", arrW);
                break;

            case "Time in system":
                if (simulador.getTotalProcesados() == 0) {
                    JOptionPane.showMessageDialog(this, "No hay datos de salida aún.");
                } else {
                    ArrayList<Integer> listaC = simulador.getTiemposDeCiclo();
                    int[] arrC = new int[listaC.size()];
                    for(int i=0; i<listaC.size(); i++) arrC[i] = listaC.get(i);
                    new Graficas("Time in system", arrC);
                }
                break;

            case "Your performance":
                int total = simulador.getTotalProcesados();
                String mensaje = "Total Units Shipped: " + total + "\n" +
                        "Target: 53 units\n" +
                        "Efficiency: " + String.format("%.1f", (total / 53.0) * 100) + "%";
                JOptionPane.showMessageDialog(this, mensaje, "Performance Results", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Activity":
                int[][] todosLosDados = new int[21][10];
                int[][] todosLosMovidos = new int[21][10];

                for (int i = 0; i < 10; i++) {
                    Estacion e = simulador.getEstaciones().eliminar();
                    ArrayList<Integer> hD = e.getHistorialDados();
                    ArrayList<Integer> hM = e.getHistorialMovidos();

                    for (int t = 0; t < hD.size() && t < 21; t++) {
                        todosLosDados[t][i] = hD.get(t);
                    }
                    for (int t = 0; t < hM.size() && t < 21; t++) {
                        todosLosMovidos[t][i] = hM.get(t);
                    }

                    simulador.getEstaciones().insertar(e);
                }
                new GraficaActividad(todosLosDados, todosLosMovidos);
                break;
        }
    }
}
