package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.sportDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import java.io.IOException;
import java.util.List;

public class sportController extends dashboardController {

    @FXML
    private FlowPane sportButtonsContainer; // Container où seront ajoutés les boutons

    public void initialize() {
        // Récupérer la liste des sports depuis la bdd
        List<String> categories = sportDAO.getAllCategories();

        // Créer un bouton pour chaque sport et les ajouter au conteneur
        for (String categorie : categories) {
            Button button = new Button(categorie);
            button.setStyle("-fx-background-color: #d7c378; -fx-font-family: 'Paris2024-Variable Regular'; -fx-font-size: 18px;"); // Appliquer le style
            button.setOnAction(e -> openSportByCategory(categorie, e)); // Définir un gestionnaire d'événements pour le clic sur le bouton
            sportButtonsContainer.getChildren().add(button);
        }
    }

    @FXML
    private void openSportByCategory(String category, ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/sportByCategory.fxml"));
            Parent root = loader.load();

            // Obtenir une référence au contrôleur
            sportByCategoryController controller = loader.getController();

            // Sélectionner la catégorie
            controller.setSelectedCategory(category);

            // Obtenir la scène actuelle
            Scene scene = ((Node) event.getSource()).getScene();

            // Remplacer la racine de la scène par le nouveau contenu chargé
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/athleteWindow.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
