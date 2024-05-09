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
    }

    @FXML
    private void saveChanges(ActionEvent event) {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String pays = paysField.getText();
        int age = Integer.parseInt(ageField.getText());
        String sex = sexField.getValue();

        // Appel depuis DAO
        athleteDAO.modifierAthlete(athleteId, nom, prenom, pays, age, sex);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification réussie");
        alert.setHeaderText(null);
        alert.setContentText("Les modifications de l'athlète ont été enregistrées avec succès.");
        alert.showAndWait();

        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();

    }

    public void initAthleteData(int id, String nom, String prenom, String pays, int age) {

        athleteId = id;
        nomField.setText(nom);
        prenomField.setText(prenom);
        paysField.setText(pays);
        ageField.setText(Integer.toString(age));
        sexField.getSelectionModel().clearSelection();

    }

    @FXML
    private void annuler(ActionEvent event) {
        stage.close();
    }


}

