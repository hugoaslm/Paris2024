package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.Medal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class medalDAO {

    public static void addMedal(int athleteId, String medal) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO medals (athlete_id, medaille) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, athleteId);
            preparedStatement.setString(2, medal);
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

}
