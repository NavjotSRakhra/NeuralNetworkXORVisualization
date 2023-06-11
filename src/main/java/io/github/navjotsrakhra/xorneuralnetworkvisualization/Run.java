package io.github.navjotsrakhra.xorneuralnetworkvisualization;

import io.github.NavjotSRakhra.neuralNetwork.NeuralNetwork;
import io.github.NavjotSRakhra.progressPrinter.ProgressPrinter;
import io.github.navjotsrakhra.xorneuralnetworkvisualization.trainingData.TrainingData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Run extends Application {
    private static final String BLACK_COLOR_WITHOUT_BRIGHTNESS = "0".repeat(6);
    private final NeuralNetwork neuralNetwork = new NeuralNetwork(2, 1, 5, 7, 3);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        GridPane gridPane = new GridPane();

        Rectangle[][] pixels = new Rectangle[WINDOW_SIDE / PIXEL_SIDE][WINDOW_SIDE / PIXEL_SIDE];
        for (Rectangle[] pixel : pixels) {
            for (int i = 0; i < pixel.length; i++) {
                pixel[i] = new Rectangle(PIXEL_SIDE, PIXEL_SIDE);
                pixel[i].setStrokeWidth(0);
            }
        }
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                setNewColor(pixels, i, j);
                gridPane.add(pixels[i][j], i, j);
            }
        }

        Scene scene = new Scene(gridPane, WINDOW_SIDE, WINDOW_SIDE);
        stage.setTitle("XOR Visualization");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        Thread thread = new Thread(() -> {
            int[] arr = new int[]{0, 1, 2, 3};
            FutureTask<Void> uiUpdate = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ProgressPrinter progressPrinter = new ProgressPrinter();
            neuralNetwork.setLearningRate(0.5);

            for (int p = 0; p < 20_000; p++) {

                for (int i = 0; i < 1; i++) {
                    shuffle(arr);
                    for (int l : arr) {
                        neuralNetwork.train(TrainingData.getInputAt(l), TrainingData.getLabelAt(l));
                    }
                }

                progressPrinter.update(p / (20_000 - 1d));
                if (p % 1000 == 0) neuralNetwork.setLearningRate(Math.pow(neuralNetwork.getLearningRate(), 1.5));
                if (p % 10 != 0) continue;

                if (uiUpdate != null) {
                    try {
                        uiUpdate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
                uiUpdate = new FutureTask<>(() -> {
                    for (int i = 0; i < PIXEL_COUNT; i++) {
                        for (int j = 0; j < PIXEL_COUNT; j++) {
                            setNewColor(pixels, i, j);
                        }
                    }
                    return null;
                });

                Platform.runLater(uiUpdate);
            }
            neuralNetwork.writeTo(".\\src\\main\\resources\\neuralNetworkSaved2.nnn");
        });
        thread.start();
    }

    private void shuffle(int[] arr) {
        Random random = new Random();

        for (int i = 0; i < arr.length; i++) {
            int index = random.nextInt(0, arr.length);
            swap(arr, index, arr.length - 1 - index);
        }
    }

    private void swap(int[] arr, int a, int b) {
        int t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
    }

    private void setNewColor(Rectangle[][] pixels, int i, int j) {
        pixels[i][j].setFill(getColorOfValue(neuralNetwork.feedForward(new double[]{(i + 1d) / PIXEL_COUNT, (j + 1d) / PIXEL_COUNT})[0]));
    }

    private Paint getColorOfValue(double n) {
        return Paint.valueOf(BLACK_COLOR_WITHOUT_BRIGHTNESS + String.format("%02X", (int) (n * 255)));
    }

    private static final int WINDOW_SIDE = 500, PIXEL_SIDE = 10, PIXEL_COUNT = WINDOW_SIDE / PIXEL_SIDE;
}