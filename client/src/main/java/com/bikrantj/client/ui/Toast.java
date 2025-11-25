package com.bikrantj.client.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Toast {

    private static void show(Pane parent, String message, int seconds, Color color) {
        Label label = new Label(message);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        label.setPadding(new Insets(12, 24, 12, 24));

        StackPane toast = new StackPane(label);
        toast.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), null)));
        toast.setOpacity(0);


        if (parent instanceof AnchorPane) {
            AnchorPane.setBottomAnchor(toast, 30.0);
            AnchorPane.setLeftAnchor(toast, 30.0);
        } else {
            // Fallback for other Pane types
            toast.setLayoutX(30); // Fixed left margin
            toast.setLayoutY(parent.getHeight() - 100); // Bottom position
        }

        parent.getChildren().add(toast);

        // Animate in - slide up from bottom
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), toast);
        slideIn.setFromY(100); // Start from below
        slideIn.setToY(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), toast);
        fadeIn.setToValue(0.95);

        ParallelTransition in = new ParallelTransition(slideIn, fadeIn);
        in.play();

        // Auto hide
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), toast);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> parent.getChildren().remove(toast));
            fadeOut.play();
        });
        delay.play();
    }

    // Helpers
    public static void success(Pane parent, String msg) {
        show(parent, "✓ Success: " + msg, 3, Color.web("#2e7d32"));
    }

    public static void error(Pane parent, String msg) {
        show(parent, "✗ Error: " + msg, 5, Color.web("#c62828"));
    }
}