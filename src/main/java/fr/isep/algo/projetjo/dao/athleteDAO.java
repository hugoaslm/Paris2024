package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class athleteDAO {

    public static List<Athlete> getAllAthletes() {
        List<Athlete> athletes = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM athletes");

            while (resultSet.next()) {
                Athlete athlete = new Athlete(
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("pays"),
                        resultSet.getInt("age"),
                        resultSet.getString("sex"),
                        resultSet.getInt("athlete_id")
                );
                athletes.add(athlete);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    public static List<String> getAllDelegations() {
        List<String> delegations = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT pays FROM athletes")) {

            while (resultSet.next()) {
                String delegation = resultSet.getString("pays");
                delegations.add(delegation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return delegations;
    }

    public static List<Athlete> getAthletesByDelegation(String delegation) {
        List<Athlete> athletes = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM athletes WHERE pays = ?")) {

            statement.setString(1, delegation);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Athlete athlete = new Athlete(
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("pays"),
                            resultSet.getInt("age"),
                            resultSet.getString("sex"),
                            resultSet.getInt("athlete_id")
                    );
                    athletes.add(athlete);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    public static List<Athlete> searchAthletes(String searchTerm) {
        List<Athlete> athletes = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM athletes WHERE nom LIKE ? OR prenom LIKE ?")) {

            statement.setString(1, "%" + searchTerm + "%");
            statement.setString(2, "%" + searchTerm + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Athlete athlete = new Athlete(
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("pays"),
                            resultSet.getInt("age"),
                            resultSet.getString("sex"),
                            resultSet.getInt("athlete_id")
                    );
                    athletes.add(athlete);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    public static void deleteAthlete(Athlete athlete) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM athletes WHERE athlete_id = ?")) {

            statement.setInt(1, athlete.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifierAthlete(int id, String nom, String prenom, String pays, int age, String sex) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "UPDATE athletes SET nom = ?, prenom = ?, pays = ?, age = ?, sex = ? WHERE athlete_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, pays);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, sex);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAthlete(String nom, String prenom, String pays, int age, String sex) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO athletes (nom, prenom, pays, age, sex) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, pays);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, sex);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
