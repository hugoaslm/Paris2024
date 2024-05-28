package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.*;
import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.Event;
import fr.isep.algo.projetjo.model.Result;
import fr.isep.algo.projetjo.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class resultController extends navigationController {

    @FXML
    private ListView<Athlete> athleteListView2;
    @FXML
    private TextField resultField;
    @FXML
    private ListView<Event> eventListView2;

    @FXML
    private TableView<Result> resultsTableView;
    @FXML
    private TableColumn<Result, Integer> resultIdColumn;
    @FXML
    private TableColumn<Result, String> eventNameColumn;
    @FXML
    private TableColumn<Result, String> athleteNameColumn;
    @FXML
    private TableColumn<Result, String> resultDataColumn;
    @FXML
    private TableColumn<Result, String> vainqueurColumn;

    @FXML
    private ChoiceBox<Athlete> athleteChoiceBox;

    @FXML
    private ChoiceBox medalBox;

    @FXML
    private Label resultLabel;
    @FXML
    private Label eventLabel;
    @FXML
    private Label resultLabel1;
    @FXML
    private Label athleteLabel;
    @FXML
    private Button addResultButton;
    @FXML
    private Button deleteResultButton;
    @FXML
    private Button addMedalButton;
    @FXML
    private Button deleteMedalButton;
    @FXML
    private Button ranking;
    @FXML
    private Label medalLabel;

    private ObservableList<Result> resultList = FXCollections.observableArrayList();

    private ObservableList<Event> eventList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {

        int role = (int) SessionManager.getInstance().getAttribute("role");
        // Si l'utilisateur n'est pas admin, il ne peut pas ajouter de résultats ou modifier/supprimer
        if (role != 1) {
            resultLabel.setVisible(false);
            eventLabel.setVisible(false);
            resultLabel1.setVisible(false);
            addResultButton.setVisible(false);
            deleteResultButton.setVisible(false);
            addMedalButton.setVisible(false);
            deleteMedalButton.setVisible(false);
            athleteLabel.setVisible(false);
            medalLabel.setVisible(false);
            athleteChoiceBox.setVisible(false);
            medalBox.setVisible(false);
            athleteListView2.setVisible(false);
            eventListView2.setVisible(false);
            resultField.setVisible(false);
        }

        resultIdColumn.setCellValueFactory(new PropertyValueFactory<>("resultId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        vainqueurColumn.setCellValueFactory(new PropertyValueFactory<>("vainqueur"));
        resultDataColumn.setCellValueFactory(new PropertyValueFactory<>("resultData"));

        athleteNameColumn.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            int eventId = result.getEventId();
            List<Athlete> athletes = null;
            try {
                athletes = event_athletesDAO.getAthletesByEventId(eventId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String athleteNames = athletes.stream()
                    .map(Athlete::getNom)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(athleteNames);
        });

        loadResults();
        loadEventsListView();
        loadAthletes();
        resultsTableView.setItems(resultList);

        medalBox.setItems(FXCollections.observableArrayList("Or", "Argent", "Bronze"));

        eventListView2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int sportId = newValue.getSport();
                String typeResult = sportDAO.getTypeResultFromSport(sportId);

                List<Athlete> athletes;
                try {
                    athletes = event_athletesDAO.getAthletesByEventId(newValue.getId());
                    ObservableList<Athlete> athleteObservableList = FXCollections.observableArrayList(athletes);
                    athleteChoiceBox.setItems(athleteObservableList);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                resultLabel.setText("Résultat (" + typeResult + ") :");
                resultField.setPromptText("Entrez le résultat (" + typeResult + ")");
            }
        });
    }

    private void loadResults() throws SQLException {
        List<Result> results = resultsDAO.getAllResults();

        for (Result result : results) {
            String eventName = eventDAO.getEventById(result.getEventId());
            result.setEventName(eventName);

            // Récupérer les athlètes associés
            List<Athlete> athletesName = event_athletesDAO.getAthletesByEventId(result.getEventId());
            result.setAthletesName(athletesName);
        }

        resultList.addAll(results);
        resultsTableView.setItems(resultList);
    }

    private void loadEventsListView() throws SQLException {
        List<Event> events = eventDAO.getAllEvents();

        for (Event event : events) {
            String sportName = eventDAO.getSportNameById(event.getSport());
            event.setSportName(sportName);
        }

        eventList.addAll(events);
        eventListView2.setItems(eventList);

    }

    private void loadAthletes() {

        List<Athlete> athletes = athleteDAO.getAllAthletes();
        ObservableList<Athlete> athleteObservableList = FXCollections.observableArrayList(athletes);
        athleteListView2.setItems(athleteObservableList);

    }

    @FXML
    private void addMedal() {
        Athlete selectedAthlete = athleteListView2.getSelectionModel().getSelectedItem();

        String medal = (String) medalBox.getValue();

        if (selectedAthlete != null) {
            if (medal == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune sélection");
                alert.setHeaderText("Aucune médaille sélectionnée");
                alert.setContentText("Veuillez sélectionner une médaille dans la liste.");
                alert.showAndWait();
            } else {
                medalDAO.addMedal(selectedAthlete.getId(), medal);
                refreshTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Médaille attribuée");
                alert.setContentText("La médaille a bien été décernée.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun athlète sélectionné");
            alert.setContentText("Veuillez sélectionner un athlète dans la liste.");
            alert.showAndWait();
        }

    }

    @FXML
    private void addResult() {
        Event selectedEvent = eventListView2.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            String vainqueur = String.valueOf(athleteChoiceBox.getValue());
            String resultData = resultField.getText();

                if (resultData.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Champ vide");
                    alert.setHeaderText("Le champ résultat est vide");
                    alert.setContentText("Veuillez entrer un résultat.");
                    alert.showAndWait();
                } else {
                    resultsDAO.addResult(selectedEvent.getId(), vainqueur, resultData);
                    refreshTable();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune sélection");
                alert.setHeaderText("Aucun athlète sélectionné");
                alert.setContentText("Veuillez sélectionner un athlète dans la liste.");
                alert.showAndWait();
            }
    }

    @FXML
    private void deleteResult() {

        Result selectedResult = resultsTableView.getSelectionModel().getSelectedItem();
        if (selectedResult != null) {
            resultsDAO.deleteResult(selectedResult.getResultId());
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun résultat sélectionné");
            alert.setContentText("Veuillez sélectionner un résultat dans la table.");
            alert.showAndWait();
        }

    }


    private void refreshTable() {
        resultList.clear();
        try {
            List<Result> results = resultsDAO.getAllResults();

            for (Result result : results) {
                String eventName = eventDAO.getEventById(result.getEventId());
                result.setEventName(eventName);

                // Récupérer les athlètes associés
                List<Athlete> athletesName = event_athletesDAO.getAthletesByEventId(result.getEventId());
                result.setAthletesName(athletesName);
            }

            resultList.addAll(results);
            resultsTableView.setItems(resultList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteMedal() {
        Athlete selectedAthlete = athleteListView2.getSelectionModel().getSelectedItem();

        if (selectedAthlete != null) {
            // Supprimer la médaille de l'athlète sélectionné
            medalDAO.deleteMedal(selectedAthlete.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Médaille retirée");
            alert.setContentText("La médaille a bien été retirée.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun athlète sélectionné");
            alert.setContentText("Veuillez sélectionner un athlète dans la liste.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openRanking(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/ranking.fxml", event);
    }

    @FXML
    private void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }
}
