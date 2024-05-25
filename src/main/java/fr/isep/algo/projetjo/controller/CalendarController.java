package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.eventDAO;
import fr.isep.algo.projetjo.model.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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

        // Add headers for days of the week
        String[] daysOfWeek = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-size: 16px; -fx-border-color: #d7c378; -fx-border-width: 1px; -fx-alignment: center; -fx-padding: 10px;");
            calendarGrid.add(dayLabel, i, 0);
        }

        // Add the days of the current month to the grid
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, ..., 7=Sunday

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-font-size: 18px; -fx-border-color: #d7c378; -fx-border-width: 1px; -fx-alignment: center; -fx-padding: 10px;");
            dayLabel.setMinSize(80, 80); // Set minimum size for the labels to create a grid effect
            int row = (day + dayOfWeek - 2) / 7 + 1;
            int col = (day + dayOfWeek - 2) % 7;
            calendarGrid.add(dayLabel, col, row);
        }

        // Load and display events
        loadEvents();
    }

    private void loadEvents() {
        eventDAO eventDAO = new eventDAO();
        List<Event> events = eventDAO.getAllEvents();

        for (Event event : events) {
            LocalDate eventDate = event.getDate().toLocalDate();
            if (eventDate.getMonth().equals(currentMonth.getMonth()) && eventDate.getYear() == currentMonth.getYear()) {
                int day = eventDate.getDayOfMonth();

                VBox eventBox = new VBox();
                eventBox.setStyle("-fx-background-color: #d7c378; -fx-padding: 5px; -fx-border-color: black; -fx-border-width: 1px;");

                Label eventNameLabel = new Label(event.getName());
                eventNameLabel.setStyle("-fx-font-size: 14px;");
                Label eventSportLabel = new Label(event.getSportName());
                eventSportLabel.setStyle("-fx-font-size: 12px;");
                Label eventLocationLabel = new Label(event.getLocation());
                eventLocationLabel.setStyle("-fx-font-size: 12px;");

                eventBox.getChildren().addAll(eventNameLabel, eventSportLabel, eventLocationLabel);

                int firstDayOfWeek = LocalDate.of(eventDate.getYear(), eventDate.getMonth(), 1).getDayOfWeek().getValue();
                int row = (day + firstDayOfWeek - 2) / 7 + 1;
                int col = (day + firstDayOfWeek - 2) % 7;
                calendarGrid.add(eventBox, col, row);
            }
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
}
