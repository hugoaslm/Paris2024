package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.Event;
import fr.isep.algo.projetjo.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class eventDAO {

    public static List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, s.nom_sport FROM events e JOIN sports s ON e.sport_id = s.sport_id";
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("event_id"),
                        resultSet.getInt("sport_id"),
                        resultSet.getString("event_name"),
                        resultSet.getString("event_location"),
                        resultSet.getDate("event_date")
                );
                event.setSportName(resultSet.getString("nom_sport"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void addEvent(int sportId, String nom, String lieu, Date date) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO events (sport_id, event_name, event_location, event_date) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportId);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, lieu);
            preparedStatement.setDate(4, date);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEvent(Event event) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "UPDATE events SET sport_id = ?, event_name = ?, event_location = ?, event_date = ? WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, event.getSport());
            preparedStatement.setString(2, event.getName());
            preparedStatement.setString(3, event.getLocation());
            preparedStatement.setDate(4, event.getDate());
            preparedStatement.setInt(5, event.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEvent(int eventId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "DELETE FROM events WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getEventById(int eventId) {
        String eventName = null;
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT event_name FROM events WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                eventName = resultSet.getString("event_name");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventName;
    }

    public static String getSportNameById(int sportId) throws SQLException {
        String sportName = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseManager.getConnection();

            String query = "SELECT nom_sport FROM sports WHERE sport_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                sportName = resultSet.getString("nom_sport");
            }
        } finally {
            // Fermez les ressources (ResultSet, PreparedStatement, Connection)
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return sportName;
    }


    public static int getSportIdByName(String sportName) {
        int sportId = -1; // Par défaut, si aucun ID n'est trouvé, retournez -1
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT sport_id FROM sports WHERE nom_sport = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sportName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                sportId = resultSet.getInt("sport_id");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sportId;
    }

    public static int getEventIdBySportName(int sportId, String eventName) {
        int eventId = 0;
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT event_id FROM events WHERE sport_id = ? AND event_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportId);
            preparedStatement.setString(2, eventName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                eventId = resultSet.getInt("event_id");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventId;
    }

    public static int countEvents() {
        int count = 0;
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM events");

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Event> getEventsFromResults() {
        List<Event> events = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT events.event_id, events.event_name, events.event_location, events.event_date, events.sport_id, results.result_data, results.vainqueur " +
                    "FROM results " +
                    "INNER JOIN events ON results.event_id = events.event_id";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String eventName = rs.getString("event_name");
                        String eventLocation = rs.getString("event_location");
                        java.util.Date eventDate = rs.getDate("event_date");
                        int sport = rs.getInt("sport_id");

                        String sportName = eventDAO.getSportNameById(sport);

                        Event event = new Event(eventId, sport, eventName, eventLocation, eventDate);
                        event.setSportName(sportName);

                        String resultData = rs.getString("result_data");
                        String vainqueur = rs.getString("vainqueur");

                        List<Result> results = new ArrayList<>();
                        results.add(new Result(0, eventId, resultData, vainqueur));

                        event.setResults(results);
                        events.add(event);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    public static List<Event> getEventFromResult(int eventId) {
        List<Event> events = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT events.event_name, events.event_location, events.event_date, events.sport_id, results.result_data, results.vainqueur " +
                    "FROM results " +
                    "INNER JOIN events ON results.event_id = events.event_id WHERE events.event_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, eventId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String eventName = rs.getString("event_name");
                        String eventLocation = rs.getString("event_location");
                        java.util.Date eventDate = rs.getDate("event_date");
                        int sport = rs.getInt("sport_id");

                        String sportName = eventDAO.getSportNameById(sport);

                        Event event = new Event(eventId, sport, eventName, eventLocation, eventDate);
                        event.setSportName(sportName);

                        String resultData = rs.getString("result_data");
                        String vainqueur = rs.getString("vainqueur");

                        List<Result> results = new ArrayList<>();
                        results.add(new Result(0, eventId, resultData, vainqueur));

                        event.setResults(results);
                        events.add(event);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

}
