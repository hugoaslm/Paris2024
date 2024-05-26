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
                        resultSet.getInt("athlete_id"),
                        resultSet.getInt("sport_id")
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
                            resultSet.getInt("athlete_id"),
                            resultSet.getInt("sport_id")
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
                            resultSet.getInt("athlete_id"),
                            resultSet.getInt("sport_id")
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

    public static void modifierAthlete(int id, String nom, String prenom, String pays, int age, String sex, int sport_id) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "UPDATE athletes SET nom = ?, prenom = ?, pays = ?, age = ?, sex = ?, sport_id = ? WHERE athlete_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, pays);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, sex);
            preparedStatement.setInt(6, sport_id);
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAthlete(String nom, String prenom, String pays, int age, String sex, int sportId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO athletes (nom, prenom, pays, age, sex, sport_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, pays);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, sex);
            preparedStatement.setInt(6, sportId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllSports() {
        List<String> sports = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nom_sport FROM sports");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String sport = resultSet.getString("nom_sport");
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sports;
    }

    public static int getSportIdByName(String sportName) {
        int sportId = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT sport_id FROM sports WHERE nom_sport = ?")) {

            statement.setString(1, sportName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    sportId = resultSet.getInt("sport_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sportId;
    }

    public static String getSportNameById(int sportId) {
        String sportName = "";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT nom_sport FROM sports WHERE sport_id = ?")) {

            statement.setInt(1, sportId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    sportName = resultSet.getString("nom_sport");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sportName;
    }

    public static int getAthleteIdByName(String athleteName) {
        int athleteId = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT athlete_id FROM athletes WHERE nom = ?")) {

            statement.setString(1, athleteName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    athleteId = resultSet.getInt("athlete_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athleteId;
    }

    public static String getCountryFromAthleteId(int athleteId) {
        String pays = "";
        String query = "SELECT pays FROM athletes WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, athleteId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                pays = resultSet.getString("pays");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pays;
    }

    public static int countAthletes() {
        int count = 0;
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM athletes");

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
