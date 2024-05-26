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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class sportByCategoryController extends navigationController {

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
            button.setStyle("-fx-background-color: #d7c378; -fx-font-family: 'Paris2024-Variable Regular'; -fx-font-size: 18px;"); // Appliquer le style
            button.setOnAction(e -> {
                try {
                    openSportDetailPage(sportByCategory, e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            spContainer.getChildren().add(button);
        }
    }

    private void openSportDetailPage(String sport, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/sportDetail.fxml"));
        Parent root = loader.load();

        sportDetailController controller = loader.getController();
        controller.setSportDetails(sport);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/disciplines.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
