package fr.isep.algo.projetjo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.YearMonth;

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

    @FXML
    private void redirectToDashboard() {
        // Logic to redirect to Dashboard
    }

    @FXML
    private void redirectToAthletes() {
        // Logic to redirect to Athletes
    }

    @FXML
    private void redirectToDisciplines() {
        // Logic to redirect to Disciplines
    }

    @FXML
    private void redirectToEvents() {
        // Logic to redirect to Events
    }

    @FXML
    private void redirectToResults() {
        // Logic to redirect to Results
    }

    @FXML
    private void redirectToAnalyses() {
        // Logic to redirect to Analyses
    }

    @FXML
    private void redirectToCalendar() {
        // Logic to redirect to Calendar (if needed)
    }
}
