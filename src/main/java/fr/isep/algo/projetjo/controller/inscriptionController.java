package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.userDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class inscriptionController {

    @FXML
    private TextField pseudoInscField;

    @FXML
    private PasswordField mdpInscField;

    @FXML
    private Button inscButton;

    @FXML
    protected void inscription(ActionEvent event) throws IOException {
        String pseudoInsc = pseudoInscField.getText();
        String mdpInsc = mdpInscField.getText();

        // Inscription de l'utilisateur
        if (userDAO.inscription(pseudoInsc, mdpInsc)) {
            // Alerte pour signaler la réussite de l'inscription
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription réussie.");
            alert.setHeaderText(null);
            alert.setContentText("Votre inscription a été effectuée avec succès.");
            alert.showAndWait();
        } else {
            // Alerte pour signaler l'échec de l'inscription
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription échouée.");
            alert.setHeaderText(null);
            alert.setContentText("L'inscription a échoué. Veuillez réessayer.");
            alert.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/home.fxml"));
        Parent root = loader.load();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(root);

    }



    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/home.fxml"));
        Parent root = loader.load();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(root);
    }
}
