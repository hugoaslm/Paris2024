package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.dao.medalDAO;
import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.Medal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class rankingController extends dashboardController {

    @FXML
    private TableView<Athlete> athleteTableView;
    @FXML
    private TableColumn<Athlete, String> athleteColumn;
    @FXML
    private TableColumn<Athlete, String> goldMedalColumn;
    @FXML
    private TableColumn<Athlete, String> silverMedalColumn;
    @FXML
    private TableColumn<Athlete, String> bronzeMedalColumn;

    @FXML
    private TableView<?> countryTableView;
    @FXML
    private TableColumn<?, String> countryColumn;
    @FXML
    private TableColumn<?, String> totalMedalsColumn;
    @FXML
    private TableColumn<?, String> goldMedalsColumn;
    @FXML
    private TableColumn<?, String> silverMedalsColumn;
    @FXML
    private TableColumn<?, String> bronzeMedalsColumn;

    private ObservableList<Athlete> athleteList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        initializeAthleteTable();
        initializeCountryTable();
    }

    private void initializeAthleteTable() {
        athleteColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        goldMedalColumn.setCellValueFactory(new PropertyValueFactory<>("goldMedals"));
        silverMedalColumn.setCellValueFactory(new PropertyValueFactory<>("silverMedals"));
        bronzeMedalColumn.setCellValueFactory(new PropertyValueFactory<>("bronzeMedals"));

        List<Athlete> athletes = athleteDAO.getAllAthletes();
        ObservableList<Athlete> athleteList = FXCollections.observableArrayList();

        for (Athlete athlete : athletes) {
            List<Medal> medals = medalDAO.getAthleteMedals(athlete.getId());
            int goldCount = 0;
            int silverCount = 0;
            int bronzeCount = 0;
            for (Medal medal : medals) {
                switch (medal.getMedalType()) {
                    case "Or":
                        goldCount++;
                        break;
                    case "Argent":
                        silverCount++;
                        break;
                    case "Bronze":
                        bronzeCount++;
                        break;
                }
            }
            athlete.setGoldMedals(goldCount);
            athlete.setSilverMedals(silverCount);
            athlete.setBronzeMedals(bronzeCount);
            athleteList.add(athlete);
        }

        // Tri de la liste des athlètes en fonction du total des médailles
        athleteList.sort((a1, a2) -> {
            int totalMedals1 = a1.getGoldMedals() + a1.getSilverMedals() + a1.getBronzeMedals();
            int totalMedals2 = a2.getGoldMedals() + a2.getSilverMedals() + a2.getBronzeMedals();
            return Integer.compare(totalMedals2, totalMedals1);
        });

        athleteTableView.setItems(athleteList);
    }


    private void initializeCountryTable() {
        // Initialisation du tableau pour le classement par pays
        // Vous devrez implémenter la logique pour afficher le classement par pays avec les médailles correspondantes
    }

    @FXML
    private void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/results.fxml", event);
    }
}
