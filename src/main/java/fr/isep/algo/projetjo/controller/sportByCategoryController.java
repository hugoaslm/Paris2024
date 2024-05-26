package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.sportDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.List;

public class sportByCategoryController extends dashboardController {

    @FXML
    private Label categoryForSports;
    @FXML
    private FlowPane spContainer;

    private String selectedCategory;

    public void setSelectedCategory(String category) {
        selectedCategory = category;
        loadingPage();
    }

    public void loadingPage() {

        // Récupérer la liste des sports pour la catégorie sélectionnée depuis la base de données
        List<String> sports = sportDAO.getSportsByCategory(selectedCategory);

        // Afficher la catégorie sélectionnée en titre de la page
        categoryForSports.setText(selectedCategory);

        // Créer un bouton pour chaque sport et les ajouter au conteneur
        for (String sportByCategory : sports) {
            Button button = new Button(sportByCategory);
            button.setOnAction(e -> sportButtonClick(sportByCategory));
            spContainer.getChildren().add(button);
        }

    }

    private void sportButtonClick(String sport) {
        System.out.println("Sport sélectionné : " + sport);
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/disciplines.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
