package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.dao.medalDAO;
import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.Country;
import fr.isep.algo.projetjo.model.Medal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rankingController extends navigationController {

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
    private TableColumn<Athlete, Integer> totalMedalColumn;


    @FXML
    private TableView<Country> countryTableView;
    @FXML
    private TableColumn<Country, String> countryColumn;
    @FXML
    private TableColumn<Country, Integer> goldMedalCountryColumn;
    @FXML
    private TableColumn<Country, Integer> silverMedalCountryColumn;
    @FXML
    private TableColumn<Country, Integer> bronzeMedalCountryColumn;
    @FXML
    private TableColumn<Country, Integer> totalMedalCountryColumn;

    private ObservableList<Athlete> athleteList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        initializeAthleteTable();
        initializeCountryTable();
    }

    private void initializeCountryTable() {
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        goldMedalCountryColumn.setCellValueFactory(new PropertyValueFactory<>("goldMedals"));
        silverMedalCountryColumn.setCellValueFactory(new PropertyValueFactory<>("silverMedals"));
        bronzeMedalCountryColumn.setCellValueFactory(new PropertyValueFactory<>("bronzeMedals"));
        totalMedalCountryColumn.setCellValueFactory(new PropertyValueFactory<>("totalMedals"));

        List<Athlete> athletes = athleteDAO.getAllAthletes();
        Map<String, Country> countryMedalMap = new HashMap<>();

        for (Athlete athlete : athletes) {
            String countryName = athlete.getPays();
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

            if (!countryMedalMap.containsKey(countryName)) {
                countryMedalMap.put(countryName, new Country(countryName));
            }

            Country country = countryMedalMap.get(countryName);
            country.addGoldMedals(goldCount);
            country.addSilverMedals(silverCount);
            country.addBronzeMedals(bronzeCount);
        }

        ObservableList<Country> countryList = FXCollections.observableArrayList(countryMedalMap.values());

        // Sort the list of countries based on the total medals
        countryList.sort((c1, c2) -> {
            int totalMedals1 = c1.getGoldMedals() + c1.getSilverMedals() + c1.getBronzeMedals();
            int totalMedals2 = c2.getGoldMedals() + c2.getSilverMedals() + c2.getBronzeMedals();
            return Integer.compare(totalMedals2, totalMedals1);
        });

        countryTableView.setItems(countryList);
    }


    private void initializeAthleteTable() {
        athleteColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        goldMedalColumn.setCellValueFactory(new PropertyValueFactory<>("goldMedals"));
        silverMedalColumn.setCellValueFactory(new PropertyValueFactory<>("silverMedals"));
        bronzeMedalColumn.setCellValueFactory(new PropertyValueFactory<>("bronzeMedals"));
        totalMedalColumn.setCellValueFactory(new PropertyValueFactory<>("totalMedals"));

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

    @FXML
    private void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/results.fxml", event);
    }
}
