package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.util.SessionManager;
import fr.isep.algo.projetjo.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

import fr.isep.algo.projetjo.dao.userDAO;

public class connexionController {

    @FXML
    private TextField pseudoField;
    @FXML
    private PasswordField mdpField;

    @FXML
    private void seConnecter(ActionEvent event) throws IOException {


        String pseudo = pseudoField.getText();
        String mdp = mdpField.getText();

        if (userDAO.checkLogin(pseudo, mdp)) {


            User user = userDAO.getUserInfo(pseudo);


            SessionManager.getInstance().setAttribute("pseudo", pseudo);
            SessionManager.getInstance().setAttribute("role", user.getRole());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/dashboard.fxml"));
            Parent root = loader.load();


            Scene currentScene = ((Node) event.getSource()).getScene();


            currentScene.setRoot(root);

        } else {


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tentative de connexion échouée.");
            alert.setHeaderText(null);
            alert.setContentText("Le nom d'utilisateur ou le mot de passe est incorrect.");
            alert.showAndWait();


            pseudoField.setText("");
            mdpField.setText("");
        }
    }

    @FXML
    private void redirectInscription(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/inscription.fxml"));
        Parent root = loader.load();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/homeStart.fxml"));
        Parent root = loader.load();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(root);
    }
}
