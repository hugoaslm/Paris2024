package fr.isep.algo.projetjo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class dashboardController extends navigationController {

    @FXML
    private void goBack(ActionEvent event) {
        redirectToHome(event);
    }
}
