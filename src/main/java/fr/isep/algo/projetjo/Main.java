package fr.isep.algo.projetjo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fr/isep/algo/projetjo/img/logo_gold_paris2024.png")));

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fr/isep/algo/projetjo/view/homeStart.fxml")));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Application des Jeux Olympiques");
        primaryStage.getIcons().add(icon);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

