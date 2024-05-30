package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class editAthleteController {

    @FXML
    private ChoiceBox<String> editSportField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField paysField;
    @FXML
    private TextField ageField;

    private Stage stage;

    private int athleteId;
    @FXML
    private ChoiceBox<String> sexField;

    @FXML
    private void initialize() {
        ObservableList<String> sexes = FXCollections.observableArrayList("M", "F");
        sexField.setItems(sexes);

        ObservableList<String> sports = FXCollections.observableArrayList(athleteDAO.getAllSports());
        editSportField.setItems(sports);
    }

    @FXML
    private void saveChanges(ActionEvent event) {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String pays = paysField.getText();
        int age = Integer.parseInt(ageField.getText());
        String sex = sexField.getValue();
        int editSportId = getEditSportId();


        athleteDAO.modifierAthlete(athleteId, nom, prenom, pays, age, sex, editSportId);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification réussie");
        alert.setHeaderText(null);
        alert.setContentText("Les modifications de l'athlète ont été enregistrées avec succès.");
        alert.showAndWait();

        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();

    }

    public void initAthleteData(int id, String nom, String prenom, String pays, int age, String sex, String sport_nom) {

        athleteId = id;
        nomField.setText(nom);
        prenomField.setText(prenom);
        paysField.setText(pays);
        ageField.setText(Integer.toString(age));
        sexField.getSelectionModel().select(sex);
        editSportField.getSelectionModel().select(sport_nom);

    }

    @FXML
    private void annuler(ActionEvent event) {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private int getEditSportId() {
        String selectionSport = editSportField.getValue();
        int sportId = 0;
        sportId = athleteDAO.getSportIdByName(selectionSport);

        return sportId;
    }

}

