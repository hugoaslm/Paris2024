package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.sportDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

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


}
