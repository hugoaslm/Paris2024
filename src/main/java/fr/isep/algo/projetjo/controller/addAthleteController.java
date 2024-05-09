package fr.isep.algo.projetjo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import fr.isep.algo.projetjo.dao.athleteDAO;

public class addAthleteController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField paysField;
    @FXML
    private TextField ageField;
    @FXML
    private ChoiceBox<String> sexField;

    private Stage stage;

    @FXML
    private void initialize() {
        ObservableList<String> sexes = FXCollections.observableArrayList("M", "F");
        sexField.setItems(sexes);
    }

    @FXML
    private void saveAthlete(ActionEvent event) {

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String pays = paysField.getText();
        int age = Integer.parseInt(ageField.getText());
        String sex = sexField.getValue();

        // Appel depuis DAO
        athleteDAO.addAthlete(nom, prenom, pays, age, sex);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout réussi");
        alert.setHeaderText(null);
        alert.setContentText("Le nouvel athlète a été ajouté avec succès.");
        alert.showAndWait();

        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();

    }

    public void initDefaultValues() {

        nomField.setText("");
        prenomField.setText("");
        paysField.setText("");
        ageField.setText("");
        sexField.getSelectionModel().clearSelection();

    }

    @FXML
    private void cancel(ActionEvent event) {
        stage.close();
    }

}

