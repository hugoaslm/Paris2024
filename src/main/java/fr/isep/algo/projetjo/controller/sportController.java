package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.sportDAO;
import fr.isep.algo.projetjo.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class sportController extends navigationController {

    @FXML
    private FlowPane sportButtonsContainer;

    @FXML
    private Button addDisciplineButton;

    public void initialize() {
        // Vérifier le rôle de l'utilisateur
        int role = (int) SessionManager.getInstance().getAttribute("role");
        if (role != 1) { // Supposons que le rôle 1 est l'administrateur
            addDisciplineButton.setVisible(false);
        }

        List<String> categories = sportDAO.getAllCategories();
        for (String categorie : categories) {
            Button button = new Button(categorie);
            button.setStyle("-fx-background-color: #d7c378; -fx-font-family: 'Paris2024-Variable Regular'; -fx-font-size: 18px;");
            button.setOnAction(e -> openSportByCategory(categorie, e));
            sportButtonsContainer.getChildren().add(button);
        }
    }

    @FXML
    private void openSportByCategory(String category, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/sportByCategory.fxml"));
            Parent root = loader.load();
            sportByCategoryController controller = loader.getController();
            controller.setSelectedCategory(category);
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddDiscipline() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/addDiscipline.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            addDisciplineController controller = loader.getController();
            controller.setParentController(this);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDiscipline(String disciplineName, String categorie, String type_result) {
        Button newDisciplineButton = new Button(disciplineName);
        newDisciplineButton.setStyle("-fx-background-color: #d7c378; -fx-font-family: 'Paris2024-Variable Regular'; -fx-font-size: 18px;");
        sportButtonsContainer.getChildren().add(newDisciplineButton);
        sportDAO.addDiscipline(disciplineName, categorie, type_result);
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

