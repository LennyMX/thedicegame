package com.example.thedicegame;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.*;

public class GraficaActividad extends JFrame {

    private int[][] datosDados;
    private int[][] datosMovidos;
    private int jugadorSeleccionado = 0;
    private boolean modoMoved = true;

    private BarChart<String, Number> barChart;
    private Label lblAverageValor;

    public GraficaActividad(int[][] dados, int[][] movidos) {
        this.datosDados = dados;
        this.datosMovidos = movidos;

        setTitle("Activity - Station Analysis");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final JFXPanel fxPanel = new JFXPanel();
        add(fxPanel);

        Platform.runLater(() -> initFX(fxPanel));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initFX(JFXPanel fxPanel) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Turn");
        NumberAxis yAxis = new NumberAxis(0, 6, 1);
        yAxis.setLabel("Amount");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setAnimated(true);
        barChart.setBarGap(0);
        root.setCenter(barChart);

        //Selector de Jugadores
        HBox pnlJugadores = new HBox(10);
        pnlJugadores.setAlignment(Pos.CENTER);
        pnlJugadores.setPadding(new Insets(20, 0, 10, 0));

        Label lblPlayer = new Label("Station: ");
        lblPlayer.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        pnlJugadores.getChildren().add(lblPlayer);

        ToggleGroup groupJugadores = new ToggleGroup();
        for (int i = 0; i < 10; i++) {
            ToggleButton btn = new ToggleButton(String.valueOf(i + 1));
            btn.setToggleGroup(groupJugadores);
            btn.setPrefSize(45, 45);
            btn.setStyle("-fx-background-radius: 25;");

            final int index = i;
            btn.setOnAction(e -> {
                jugadorSeleccionado = index;
                actualizarDatos();
            });
            if (i == 0) btn.setSelected(true);
            pnlJugadores.getChildren().add(btn);
        }
        root.setBottom(pnlJugadores);
        VBox pnlDerecho = new VBox(25);
        pnlDerecho.setAlignment(Pos.TOP_CENTER);
        pnlDerecho.setPadding(new Insets(40, 0, 0, 20));

        ToggleGroup groupModo = new ToggleGroup();
        ToggleButton btnMoved = new ToggleButton("Moved");
        ToggleButton btnRolled = new ToggleButton("Rolled");
        btnMoved.setToggleGroup(groupModo);
        btnRolled.setToggleGroup(groupModo);
        btnMoved.setPrefSize(90, 35);
        btnRolled.setPrefSize(90, 35);
        btnMoved.setSelected(true);

        btnMoved.setOnAction(e -> { modoMoved = true; actualizarDatos(); });
        btnRolled.setOnAction(e -> { modoMoved = false; actualizarDatos(); });

        VBox pnlAvg = new VBox(5);
        pnlAvg.setAlignment(Pos.CENTER);
        pnlAvg.setStyle("-fx-border-color: #cccccc; -fx-padding: 10; -fx-border-radius: 5;");
        Label lblAvgTitle = new Label("Average");
        lblAverageValor = new Label("0.0");
        lblAverageValor.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #2e86c1;");
        pnlAvg.getChildren().addAll(lblAvgTitle, lblAverageValor);

        pnlDerecho.getChildren().addAll(btnMoved, btnRolled, pnlAvg);
        root.setRight(pnlDerecho);

        actualizarDatos();
        Scene scene = new Scene(root);
        fxPanel.setScene(scene);
    }

    private void actualizarDatos() {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int[][] matriz = modoMoved ? datosMovidos : datosDados;
        double suma = 0;
        int count = 0;

        for (int t = 1; t <= 20; t++) {
            int valor = matriz[t][jugadorSeleccionado];
            XYChart.Data<String, Number> data = new XYChart.Data<>(String.valueOf(t), valor);
            series.getData().add(data);

            if (valor > 0) {
                suma += valor;
                count++;
            }
            data.nodeProperty().addListener((ov, oldN, newN) -> {
                if (newN != null) newN.setStyle("-fx-bar-fill: #5dade2;");
            });
        }

        double avg = (count > 0) ? suma / count : 0.0;
        lblAverageValor.setText(String.format("%.1f", avg));
        barChart.getData().add(series);
    }
}
