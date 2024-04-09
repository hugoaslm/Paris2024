package fr.isep.algo.projetjo.controller;// PrimaryController.java
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class PrimaryController {

    @FXML
    private void handleOpenNewWindowButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/athleteWindow.fxml"));
        Parent root = loader.load();
        athleteController controller = loader.getController();

        // Obtenez la scène actuelle à partir de n'importe quel nœud de votre scène actuelle
        Scene currentScene = ((Node) event.getSource()).getScene();

        // Remplacez le contenu de la scène actuelle par la nouvelle racine chargée à partir du FXML
        currentScene.setRoot(root);
    }
}
