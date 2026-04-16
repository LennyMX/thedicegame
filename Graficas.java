package com.example.thedicegame;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.*;
import java.util.List;

public class Graficas extends JFrame {

    public Graficas(String titulo, List<Integer> datos) {
        setTitle(titulo);
        setSize(850, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final JFXPanel fxPanel = new JFXPanel();
        add(fxPanel);

        Platform.runLater(() -> initFX(fxPanel, titulo, datos));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initFX(JFXPanel fxPanel, String titulo, List<Integer> datos) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        if (titulo.equals("Throughput")) {
            xAxis.setLabel("Turn");
            yAxis.setLabel("Cumulative Out");
            yAxis.setUpperBound(70); 
            yAxis.setAutoRanging(false);
        } else if (titulo.equals("Number in system")) {
            xAxis.setLabel("Turn");
            yAxis.setLabel("WIP (People)");
            yAxis.setAutoRanging(true);
        } else if (titulo.equals("Time in system")) {
            xAxis.setLabel("Order of arrival");
            yAxis.setLabel("Turns to complete");
            yAxis.setLowerBound(8); 
            yAxis.setAutoRanging(true);
        }

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(titulo);
        barChart.setLegendVisible(false);
        barChart.setAnimated(true);
        barChart.setBarGap(0);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        
        for (int i = 0; i < datos.size(); i++) {
            String etiquetaX = (titulo.equals("Time in system")) ? String.valueOf(i + 1) : String.valueOf(i);
            series.getData().add(new XYChart.Data<>(etiquetaX, datos.get(i)));
        }

        barChart.getData().add(series);
        series.nodeProperty().addListener((obs, oldNode, newNode) -> {
            series.getData().forEach(d -> {
                if (d.getNode() != null) d.getNode().setStyle("-fx-bar-fill: #5dade2;");
            });
        });

        Scene scene = new Scene(barChart);
        fxPanel.setScene(scene);
    }
}
