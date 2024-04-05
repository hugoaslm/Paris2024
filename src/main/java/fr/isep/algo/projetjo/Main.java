package fr.isep.algo.projetjo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de la vue depuis le fichier FXML
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fr/isep/algo/projetjo/testons.fxml")));

        // Création de la scène avec la vue chargée
        Scene scene = new Scene(root);

        // Configuration de la scène sur la fenêtre principale
        primaryStage.setScene(scene);
        primaryStage.setTitle("Votre application JavaFX");

        // Affichage de la fenêtre principale
        primaryStage.show();

        // Insertion de données dans la base de données
        insertData();
    }

    public static void main(String[] args) {
        // Lancement de l'application JavaFX
        launch(args);
    }

    private static void insertData() {
        Athlete athlete = new Athlete("Teddy Riner", "France", 33, "M");

        //athleteController.save(athlete.getName(), athlete.getCountry(),
                //athlete.getAge(), athlete.getSex());
        athleteController.getAllAthletes();
    }
}

