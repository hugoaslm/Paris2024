package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.dao.eventDAO;
import fr.isep.algo.projetjo.dao.medalDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class dashboardController extends navigationController {

    @FXML
    private Label nbAthletesLabel;

    @FXML
    private Label nbEventsLabel;

    @FXML
    private Label nbMedalsLabel;

    public void initialize() {
        int nb_athlete = athleteDAO.countAthletes();
        int nb_event = eventDAO.countEvents();
        int nb_medal = medalDAO.countMedals();

        nbAthletesLabel.setText("Athlètes inscrits : " + nb_athlete);
        nbEventsLabel.setText("Nombre d'évènements : " + nb_event);
        nbMedalsLabel.setText("Nombre de médailles décernées : " + nb_medal);
    }

    @FXML
    private void goBack(ActionEvent event) {
        redirectToHome(event);
    }
}
