package fr.isep.algo.projetjo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.YearMonth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


import static fr.isep.algo.projetjo.controller.dashboardController.redirectToPage;

public class CalendarController {

    @FXML
    private Label monthYearLabel;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Button prevMonthButton;
    @FXML
    private Button nextMonthButton;

    private YearMonth currentMonth;

    @FXML
    public void initialize() {
        currentMonth = YearMonth.now();
        updateCalendar();
    }

    @FXML
    private void prevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void nextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        updateCalendar();
    }

    private void updateCalendar() {
        monthYearLabel.setText(currentMonth.getMonth().toString() + " " + currentMonth.getYear());
        calendarGrid.getChildren().clear();
        // Add headers for days of the week (if not already added)
        // Add the days of the current month to the grid
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, ..., 7=Sunday

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            Label dayLabel = new Label(String.valueOf(day));
            int row = (day + dayOfWeek - 2) / 7 + 1;
            int col = (day + dayOfWeek - 2) % 7;
            calendarGrid.add(dayLabel, col, row);
        }
    }


    public static void redirectToPage(String fxmlFilePath, ActionEvent event) {
        try {
            // Chemin du fichier FXML de destination
            URL destinationFXML = dashboardController.class.getResource(fxmlFilePath);

            // Fichier FXML de la page actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Récupérer l'URL du fichier FXML de la scène actuelle
            URL currentFXML = null;
            if (currentScene.getWindow() instanceof Stage) {
                FXMLLoader loader = (FXMLLoader) ((Stage) currentScene.getWindow()).getProperties().get("FXMLLoader");
                if (loader != null) {
                    currentFXML = loader.getLocation();
                }
            }

            FXMLLoader loader = new FXMLLoader(destinationFXML);
            Parent root = loader.load();

            // Obtenez le contrôleur de la nouvelle vue chargée
            Object controller = loader.getController();

            // Remplacez le contenu de la scène actuelle par la nouvelle racine chargée à partir du FXML
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    protected void redirectToDashboard(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/allAthletes.fxml", event);
    }

    @FXML
    protected void redirectToAthletes(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/athleteWindow.fxml", event);
    }

    @FXML
    protected void redirectToDisciplines(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/disciplines.fxml", event);
    }

    @FXML
    protected void redirectToEvents(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/events.fxml", event);
    }

    @FXML
    protected void redirectToResults(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/results.fxml", event);
    }

    @FXML
    protected void redirectToAnalyses(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/analyses.fxml", event);
    }

    @FXML
    protected void redirectToCalendar(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/calendar.fxml", event);
    }





    @FXML
    private void goBack(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/athleteWindow.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
