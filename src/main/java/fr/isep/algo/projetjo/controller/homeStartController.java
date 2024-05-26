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

public class homeStartController {


    @FXML
    private void admin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/home.fxml"));
        Parent root = loader.load();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(root);
    }

    @FXML
    private void visiteur(ActionEvent event) throws IOException {

        SessionManager.getInstance().setAttribute("role", 0);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/dashboard.fxml"));
        Parent root = loader.load();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(root);
    }

}
