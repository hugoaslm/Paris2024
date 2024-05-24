package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.Event;
import fr.isep.algo.projetjo.model.Athlete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class event_athletesDAO {

    public static void addAthleteToEvent(int eventId, int athleteId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO event_athletes (event_id, athlete_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, athleteId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeAthleteFromEvent(int eventId, int athleteId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "DELETE FROM event_athletes WHERE event_id = ? AND athlete_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, athleteId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Athlete> getAthletesByEvent(int eventId) {
        List<Athlete> athletes = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT athletes.* FROM athletes INNER JOIN event_athletes " +
                    "ON athletes.athlete_id = event_athletes.athlete_id " +
                    "WHERE event_athletes.event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Athlete athlete = new Athlete(
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("pays"),
                        resultSet.getInt("age"),
                        resultSet.getString("sex"),
                        resultSet.getInt("athlete_id"),
                        resultSet.getInt("sport_id")
                );
                athletes.add(athlete);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    public static List<Athlete> getAthletesByEventId(int eventId) throws SQLException {
        List<Athlete> athletes = new ArrayList<>();
        String query = "SELECT a.* FROM athletes a JOIN event_athletes ea ON a.athlete_id = ea.athlete_id WHERE ea.event_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Athlete athlete = new Athlete(
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("pays"),
                            resultSet.getInt("age"),
                            resultSet.getString("sex"),
                            resultSet.getInt("athlete_id"),
                            resultSet.getInt("sport_id")
                    );
                    athletes.add(athlete);
                }
            }
        }
        return athletes;
    }

    public static List<Event> getEventsByAthlete(int athleteId) {
        List<Event> events = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT events.* FROM events INNER JOIN event_athletes " +
                    "ON events.event_id = event_athletes.event_id " +
                    "WHERE event_athletes.athlete_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, athleteId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("event_id"),
                        resultSet.getInt("sport_id"),
                        resultSet.getString("event_name"),
                        resultSet.getString("event_location"),
                        resultSet.getDate("event_date")
                );
                events.add(event);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
