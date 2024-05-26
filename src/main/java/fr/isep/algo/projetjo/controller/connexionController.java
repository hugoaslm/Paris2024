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

        // Définition des champs de texte
        String pseudo = pseudoField.getText();
        String mdp = mdpField.getText();

        if (userDAO.checkLogin(pseudo, mdp)) {

            // Accès aux infos sur l'utilisateur
            User user = userDAO.getUserInfo(pseudo);

            // Ajout de la session
            SessionManager.getInstance().setAttribute("pseudo", pseudo);
            SessionManager.getInstance().setAttribute("role", user.getRole());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/dashboard.fxml"));
            Parent root = loader.load();
            athleteController controller = loader.getController();

            // Obtenir la scène actuelle à partir de n'importe quel nœud de la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Remplacer le contenu de la scène actuelle par la nouvelle racine chargée à partir du FXML
            currentScene.setRoot(root);

        } else {

            // Alerte pour signaler l'échec de la connexion
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tentative de connexion échouée.");
            alert.setHeaderText(null);
            alert.setContentText("Le nom d'utilisateur ou le mot de passe est incorrect.");
            alert.showAndWait();

            // Effacer les champs de texte
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

}
