package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.model.Medal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDate;

import fr.isep.algo.projetjo.dao.medalDAO;

public class chartsController extends navigationController {

    @FXML
    private LineChart<String, Number> medalEvolutionChart;

    @FXML
    public void initialize() {

        // Charger et traiter les données
        Map<LocalDate, Map<String, Integer>> medalDataByDate = loadMedalDataByDate();
        populateChart(medalDataByDate);
    }

    private Map<LocalDate, Map<String, Integer>> loadMedalDataByDate() {
        // Suppose that medalDAO.getAllMedals() returns a list of medals with date and type
        List<Medal> medals = medalDAO.getAllMedals();

        Map<LocalDate, Map<String, Integer>> medalDataByDate = new HashMap<>();
        for (Medal medal : medals) {
            System.out.println(medal.getMedalId());
            int eventId = medalDAO.getEventIdForMedal(medal.getMedalId());
            Date dateSql = medalDAO.getDateForMedal(eventId);
            LocalDate date = dateSql.toLocalDate();
            System.out.println(date);
            String type = medal.getMedalType();

            medalDataByDate.putIfAbsent(date, new HashMap<>());
            Map<String, Integer> dailyMedalCount = medalDataByDate.get(date);

            dailyMedalCount.put(type, dailyMedalCount.getOrDefault(type, 0) + 1);
        }

        return medalDataByDate;
    }

    private void populateChart(Map<LocalDate, Map<String, Integer>> medalDataByDate) {
        XYChart.Series<String, Number> goldSeries = new XYChart.Series<>();
        goldSeries.setName("Or");
        XYChart.Series<String, Number> silverSeries = new XYChart.Series<>();
        silverSeries.setName("Argent");
        XYChart.Series<String, Number> bronzeSeries = new XYChart.Series<>();
        bronzeSeries.setName("Bronze");

        Map<String, Integer> cumulativeMedals = new HashMap<>();
        cumulativeMedals.put("Or", 0);
        cumulativeMedals.put("Argent", 0);
        cumulativeMedals.put("Bronze", 0);

        List<LocalDate> sortedDates = medalDataByDate.keySet().stream().sorted().collect(Collectors.toList());
        for (LocalDate date : sortedDates) {
            Map<String, Integer> dailyMedalCount = medalDataByDate.get(date);

            cumulativeMedals.put("Or", cumulativeMedals.get("Or") + dailyMedalCount.getOrDefault("Or", 0));
            cumulativeMedals.put("Argent", cumulativeMedals.get("Argent") + dailyMedalCount.getOrDefault("Argent", 0));
            cumulativeMedals.put("Bronze", cumulativeMedals.get("Bronze") + dailyMedalCount.getOrDefault("Bronze", 0));

            goldSeries.getData().add(new XYChart.Data<>(date.toString(), cumulativeMedals.get("Or")));
            silverSeries.getData().add(new XYChart.Data<>(date.toString(), cumulativeMedals.get("Argent")));
            bronzeSeries.getData().add(new XYChart.Data<>(date.toString(), cumulativeMedals.get("Bronze")));
        }

        medalEvolutionChart.getData().addAll(goldSeries, silverSeries, bronzeSeries);
    }

    @FXML
    public void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }
}
