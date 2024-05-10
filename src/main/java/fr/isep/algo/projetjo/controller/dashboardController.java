package fr.isep.algo.projetjo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public abstract class dashboardController {

    public static void redirectToPage(String fxmlFilePath, ActionEvent event) {
        try {
            // Chemin du fichier FXML de destination
            URL destinationFXML = dashboardController.class.getResource(fxmlFilePath);

            // Fichier FXML de la page actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();
            if (currentScene == null) {
                System.err.println("Current scene is null");
                return;
            }
            URL currentFXML = currentScene.getUserData() != null ? dashboardController.class.getResource(currentScene.getUserData().toString()) : null;

            if (Objects.equals(destinationFXML, currentFXML)) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(destinationFXML);
            Parent root = loader.load();

            // Obtenez le contrôleur de la nouvelle vue chargée
            Object controller = loader.getController();

            // Remplacez le contenu de la scène actuelle par la nouvelle racine chargée à partir du FXML
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void redirectToDashboard(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/allAthletes.fxml", event);
    }

    @FXML
    protected void redirectToAthletes(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/athletesWindow.fxml", event);
    }

    @FXML
    protected void redirectToDisciplines(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/discplines.fxml", event);
    }

    @FXML
    protected void redirectToEvents(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/events.fxml", event);
    }

    @FXML
    protected void redirectToResults(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/results.fxml", event);
    }

    @FXML
    protected void redirectToAnalyses(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/analyses.fxml", event);
    }

    @FXML
    protected void redirectToCalendar(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/calendar.fxml", event);
    }
}
