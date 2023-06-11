package io.github.navjotsrakhra.xorneuralnetworkvisualization.trainingData;

import java.util.List;

public class TrainingData {
    private static final List<double[]> inputs = List.of(new double[]{1, 1}, new double[]{0, 0}, new double[]{1, 0}, new double[]{0, 1}),
            labels = List.of(new double[]{0}, new double[]{0}, new double[]{1}, new double[]{1});

    public static double[] getInputAt(int i) {
        return inputs.get(i).clone();
    }

    public static double[] getLabelAt(int i) {
        return labels.get(i).clone();
    }

    public static InputAndLabel getRandomInputAndLabel() {
        int i = (int) (Math.random() * 4);
        return new InputAndLabel(inputs.get(i), labels.get(i));
    }

    public static InputAndLabel getContinuousInputData() {
        double a = Math.random(), b = Math.random();
        double label = XOR(Math.rint(a), Math.rint(b));
        return new InputAndLabel(new double[]{a, b}, new double[]{label});
    }

    private static double XOR(double a, double b) {
        return a == b ? 0 : 1;
    }

    public static InputAndLabel getDataAt(int i) {
        return new InputAndLabel(inputs.get(i), labels.get(i));
    }

    public record InputAndLabel(double[] input, double[] label) {
    }
}
