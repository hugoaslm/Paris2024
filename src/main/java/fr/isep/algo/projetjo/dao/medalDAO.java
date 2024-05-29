package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.Medal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class medalDAO {

    public static void addMedal(int athleteId, String medal, int eventId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO medals (athlete_id, medaille, event_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, athleteId);
            preparedStatement.setString(2, medal);
            preparedStatement.setInt(3, eventId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMedal(int medalId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "DELETE FROM medals WHERE medal_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, medalId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Medal> getAthleteMedals(int athleteId) {
        List<Medal> medals = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM medals WHERE athlete_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, athleteId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int medalId = resultSet.getInt("medal_id");
                String medal = resultSet.getString("medaille");
                Medal athleteMedal = new Medal(medalId, athleteId, medal);
                medals.add(athleteMedal);
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medals;
    }

    public static int countMedals() {
        int count = 0;
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM medals");

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

    public static List<Medal> getAllMedals() {
        List<Medal> medals = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM medals");
            while (resultSet.next()) {
                int medalId = resultSet.getInt("medal_id");
                int athleteId = resultSet.getInt("athlete_id");
                String medal = resultSet.getString("medaille");
                Medal medalObject = new Medal(medalId, athleteId, medal);
                medals.add(medalObject);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medals;
    }

    public static Date getDateForMedal(int eventId) {
        Date date = null;
        String query = "SELECT event_date FROM events WHERE event_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    date = resultSet.getDate("event_date");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static int getEventIdForMedal(int medalId) {
        int eventId = -1;
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT event_id FROM medals WHERE medal_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, medalId);
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

}
