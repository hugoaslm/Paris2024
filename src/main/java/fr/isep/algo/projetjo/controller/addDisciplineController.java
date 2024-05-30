package fr.isep.algo.projetjo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

public class addDisciplineController {
    @FXML
    private TextField disciplineNameField;

    private sportController parentController;

    @FXML
    private TextField categorieField;

    @FXML
    private ChoiceBox<String> type_resultChoiceBox;

    public void setParentController(sportController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void initialize() {
        type_resultChoiceBox.getItems().addAll("temps", "Distance", "score", "combat", "poids");
    }

    @FXML
    private void handleAdd() {
        String disciplineName = disciplineNameField.getText();
        String categorie = categorieField.getText();
        String type_result = type_resultChoiceBox.getValue();
        if (disciplineName != null && !disciplineName.isEmpty()) {
            parentController.addDiscipline(disciplineName, categorie, type_result);
            ((Stage) disciplineNameField.getScene().getWindow()).close();
        }
    }
}
