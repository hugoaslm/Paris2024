package fr.isep.algo.projetjo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class navigationController {

    protected String selectedCategory;

    public static void redirectToPage(String fxmlFilePath, ActionEvent event) {
        try {
            // Chemin du fichier FXML de destination
            URL destinationFXML = navigationController.class.getResource(fxmlFilePath);

            // Fichier FXML de la page actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Récupérer l'URL du fichier FXML de la scène actuelle
            URL currentFXML = null;
            if (currentScene.getWindow() instanceof Stage) {
                FXMLLoader loader = (FXMLLoader) ((Stage) currentScene.getWindow()).getProperties().get("FXMLLoader");
                if (loader != null) {
                    currentFXML = loader.getLocation();
                }
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
        redirectToPage("/fr/isep/algo/projetjo/view/dashboard.fxml", event);
    }

    @FXML
    protected void redirectToAthletes(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/athleteWindow.fxml", event);
    }

    @FXML
    protected void redirectToDisciplines(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/disciplines.fxml", event);
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
        redirectToPage("/fr/isep/algo/projetjo/view/pdf.fxml", event);
    }

    @FXML
    protected void redirectToCalendar(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/calendar.fxml", event);
    }

    protected void redirectToHome(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }

    public void setSelectedCategory(String category) {
        this.selectedCategory = category;
        System.out.println("Catégorie sélectionnée : " + category);
    }

}
