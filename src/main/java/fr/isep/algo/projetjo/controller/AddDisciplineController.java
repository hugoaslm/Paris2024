package fr.isep.algo.projetjo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddDisciplineController {
    @FXML
    private TextField disciplineNameField;

    private sportController parentController;

    public void setParentController(sportController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleAdd() {
        String disciplineName = disciplineNameField.getText();
        if (disciplineName != null && !disciplineName.isEmpty()) {
            parentController.AddDiscipline(disciplineName);
            ((Stage) disciplineNameField.getScene().getWindow()).close();
        }
    }
}
