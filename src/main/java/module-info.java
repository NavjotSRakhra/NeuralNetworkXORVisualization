module io.github.navjotsrakhra.xorneuralnetworkvisualization {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.NavjotSRakhra;
    requires io.github.NavjotSRakhra.progressPrinter;

    opens io.github.navjotsrakhra.xorneuralnetworkvisualization to javafx.fxml;
    exports io.github.navjotsrakhra.xorneuralnetworkvisualization;
}