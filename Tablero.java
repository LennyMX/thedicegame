package com.example.thedicegame;
import javax.swing.*;
import java.awt.*;

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

        casillas[0] = new Point(inicioX, inicioY);
        casillas[1] = new Point(inicioX + espacio, inicioY);
        casillas[2] = new Point(inicioX + 2 * espacio, inicioY);
        casillas[3] = new Point(inicioX + 3 * espacio, inicioY);
        casillas[4] = new Point(inicioX + 3 * espacio, inicioY + espacio);
        casillas[5] = new Point(inicioX + 3 * espacio, inicioY + 2 * espacio);
        casillas[6] = new Point(inicioX + 3 * espacio, inicioY + 3 * espacio);
        casillas[7] = new Point(inicioX + 2 * espacio, inicioY + 3 * espacio);
        casillas[8] = new Point(inicioX + espacio, inicioY + 3 * espacio);
        casillas[9] = new Point(inicioX, inicioY + 3 * espacio);

        setBackground(Color.WHITE);
        try {
            for (int i = 0; i < 6; i++) {
                dados[i] = new ImageIcon(getClass().getResource("/img/dado" + (i + 1) + ".png")).getImage();
            }
            persona = new ImageIcon(getClass().getResource("/img/persona.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error cargando recursos visuales");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // OBTENER LAS ESTACIONES REALES
        Estacion[] estaciones = simulador.getEstaciones();

        for (int i = 0; i < 10; i++) {
            Estacion est = estaciones[i];
            int x = casillas[i].x;
            int y = casillas[i].y;

            // Dibujar el cuadro de la estación
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 60, 60);

            // CANTIDAD DE PERSONAS EN LA COLA DE LA ESTACIÓN
            int cantidadPersonas = est.getColaActual().size();
            dibujarJugadores(g, cantidadPersonas, x, y);

            // VALOR DEL DADO ACTUAL DE ESA ESTACIÓN
            int valorDado = est.getCapacidad();

            if (valorDado > 0) {
                // Dibujamos el dado arriba de la casilla
                g.drawImage(dados[valorDado - 1], x + 15, y - 35, 30, 30, this);
            }

            // Dibujar icono decorativo de persona al lado
            if (persona != null) {
                g.drawImage(persona, x + 65, y + 10, 40, 40, this);
            }

            // Dibujar el número de personas debajo del cuadro
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(String.valueOf(cantidadPersonas), x + 20, y + 80);
        }

        dibujarSalida(g);
    }

    private void dibujarJugadores(Graphics g, int personas, int x, int y) {
        g.setColor(new Color(41, 128, 185));
        for (int j = 0; j < personas; j++) {
            if (j < 16) {
                int px = x + 5 + (j % 4) * 14;
                int py = y + 5 + (j / 4) * 14;
                g.fillOval(px, py, 12, 12);
            }
        }
    }

    private void dibujarSalida(Graphics g) {
        int total = simulador.getTotalProcesados();
        int inicioX = 50;
        int inicioY = 400;
        int columnas = 5;
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
