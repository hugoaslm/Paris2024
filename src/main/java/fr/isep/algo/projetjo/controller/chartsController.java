package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.model.Athlete;
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
    private LineChart<String, Integer> medalEvolutionChart;
    @FXML
    private LineChart<String, Integer> medalCountryChart;

    @FXML
    public void initialize() {

        Map<LocalDate, Map<String, Integer>> medalDataByDate = loadMedalByDate();
        plotMedals(medalDataByDate);

        Map<LocalDate, Map<String, Integer>> medalDataByCountry = loadMedalByCountry();
        updateCumulMedal(medalDataByCountry);
        plotByCountry(medalDataByCountry);
    }

    private Map<LocalDate, Map<String, Integer>> loadMedalByDate() {
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

    private void plotMedals(Map<LocalDate, Map<String, Integer>> medalDataByDate) {
        XYChart.Series<String, Integer> goldSeries = new XYChart.Series<>();
        goldSeries.setName("Or");
        XYChart.Series<String, Integer> silverSeries = new XYChart.Series<>();
        silverSeries.setName("Argent");
        XYChart.Series<String, Integer> bronzeSeries = new XYChart.Series<>();
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

    private Map<LocalDate, Map<String, Integer>> loadMedalByCountry() {
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


                countryMedalMap.putIfAbsent(date, new HashMap<>());


                Map<String, Integer> dailyMedalCount = countryMedalMap.get(date);


                dailyMedalCount.putIfAbsent(countryName, 0);
                int currentCount = dailyMedalCount.get(countryName) + 1;


                dailyMedalCount.put(countryName, currentCount);
            }
        }

        return countryMedalMap;
    }

    private void updateCumulMedal(Map<LocalDate, Map<String, Integer>> CountryMedalMap) {

        Map<String, Integer> cumulativeTotals = new HashMap<>();


        List<LocalDate> dates = new ArrayList<>(CountryMedalMap.keySet());
        Collections.sort(dates);


        for (LocalDate date : dates) {
            Map<String, Integer> medalsOnDate = CountryMedalMap.get(date);


            for (Map.Entry<String, Integer> entry : medalsOnDate.entrySet()) {
                String country = entry.getKey();
                int medals = entry.getValue();


                cumulativeTotals.put(country, cumulativeTotals.getOrDefault(country, 0) + medals);
            }


            for (Map.Entry<String, Integer> entry : cumulativeTotals.entrySet()) {
                String country = entry.getKey();
                int cumulativeMedals = entry.getValue();
                CountryMedalMap.get(date).put(country, cumulativeMedals);
            }
        }
    }

    private void plotByCountry(Map<LocalDate, Map<String, Integer>> countryMedalMap) {
        for (LocalDate date : countryMedalMap.keySet()) {
            Map<String, Integer> dailyMedalCount = countryMedalMap.get(date);

            for (String countryName : dailyMedalCount.keySet()) {
                int medalCount = dailyMedalCount.get(countryName);

                XYChart.Series<String, Integer> series = getOrCreateSeries(countryName, medalCountryChart);
                series.getData().add(new XYChart.Data<>(date.toString(), medalCount));
            }
        }
    }

    private XYChart.Series<String, Integer> getOrCreateSeries(String seriesName, LineChart<String, Integer> chart) {
        for (XYChart.Series<String, Integer> series : chart.getData()) {
            if (series.getName().equals(seriesName)) {
                return series;
            }
        }

        XYChart.Series<String, Integer> newSeries = new XYChart.Series<>();
        newSeries.setName(seriesName);
        chart.getData().add(newSeries);
        return newSeries;
    }



    @FXML
    public void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }
}
