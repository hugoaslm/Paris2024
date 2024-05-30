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

public class CalendarController extends navigationController {

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


        String[] daysOfWeek = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-size: 16px; -fx-border-color: #d7c378; -fx-border-width: 1px; -fx-alignment: center; -fx-padding: 10px;");
            calendarGrid.add(dayLabel, i, 0);
        }


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
