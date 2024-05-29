package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.Country;
import fr.isep.algo.projetjo.model.Medal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

import fr.isep.algo.projetjo.dao.medalDAO;

public class chartsController extends navigationController {

    @FXML
    private LineChart<String, Number> medalEvolutionChart;
    @FXML
    private LineChart<String, Number> medalCountryChart;

    @FXML
    public void initialize() {

        // Charger et traiter les données
        Map<LocalDate, Map<String, Integer>> medalDataByDate = loadMedalDataByDate();
        populateChart(medalDataByDate);

        Map<LocalDate, Map<String, Integer>> medalDataByCountry = loadMedalDataByCountry();
        populateChartByCountry(medalDataByCountry);
    }

    private Map<LocalDate, Map<String, Integer>> loadMedalDataByDate() {
        List<Medal> medals = medalDAO.getAllMedals();

        Map<LocalDate, Map<String, Integer>> medalDataByDate = new HashMap<>();
        for (Medal medal : medals) {
            int eventId = medalDAO.getEventIdForMedal(medal.getMedalId());
            Date dateSql = medalDAO.getDateForMedal(eventId);
            LocalDate date = dateSql.toLocalDate();
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

    private Map<LocalDate, Map<String, Integer>> loadMedalDataByCountry() {
        List<Medal> medals = medalDAO.getAllMedals();
        List<Athlete> athletes = athleteDAO.getAllAthletes();
        Map<LocalDate, Map<String, Integer>> countryMedalMap = new TreeMap<>();

        for (Medal medal : medals) {
            Athlete athlete = athletes.stream()
                    .filter(a -> a.getId() == medal.getAthleteId())
                    .findFirst()
                    .orElse(null);

            if (athlete != null) {
                int eventId = medalDAO.getEventIdForMedal(medal.getMedalId());
                Date dateSql = medalDAO.getDateForMedal(eventId);
                LocalDate date = dateSql.toLocalDate();
                String countryName = athlete.getPays();

                // Initialize the map for this date if it doesn't exist
                countryMedalMap.putIfAbsent(date, new HashMap<>());

                // Get the map of medal counts for this date
                Map<String, Integer> dailyMedalCount = countryMedalMap.get(date);

                // Get the current total medal count for this country
                int currentCount = dailyMedalCount.getOrDefault(countryName, 0);

                // Increment the medal count for this country by 1
                dailyMedalCount.put(countryName, currentCount + 1);

                // Update the total medal count for this country for subsequent dates
                countryMedalMap.forEach((d, map) -> {
                    if (!d.equals(date)) {
                        map.put(countryName, map.getOrDefault(countryName, 0) + currentCount);
                    }
                });
            }
        }

        return countryMedalMap;
    }




    private void populateChartByCountry(Map<LocalDate, Map<String, Integer>> countryMedalMap) {
        for (LocalDate date : countryMedalMap.keySet()) {
            Map<String, Integer> dailyMedalCount = countryMedalMap.get(date);

            for (String countryName : dailyMedalCount.keySet()) {
                int medalCount = dailyMedalCount.get(countryName);

                XYChart.Series<String, Number> series = getOrCreateSeries(countryName, medalCountryChart);
                series.getData().add(new XYChart.Data<>(date.toString(), medalCount));
            }
        }
    }

    private XYChart.Series<String, Number> getOrCreateSeries(String seriesName, LineChart<String, Number> chart) {
        for (XYChart.Series<String, Number> series : chart.getData()) {
            if (series.getName().equals(seriesName)) {
                return series;
            }
        }

        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
        newSeries.setName(seriesName);
        chart.getData().add(newSeries);
        return newSeries;
    }



    @FXML
    public void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }
}
