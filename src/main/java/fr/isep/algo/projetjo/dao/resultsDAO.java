package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class resultsDAO {

    public static List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM results");

            while (resultSet.next()) {
                Result result = new Result(
                        resultSet.getInt("result_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getString("vainqueur"),
                        resultSet.getString("result_data")
                );
                results.add(result);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void addResult(int eventId, String vainqueur, String resultData) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "INSERT INTO results (event_id, vainqueur, result_data) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            preparedStatement.setString(2, vainqueur);
            preparedStatement.setString(3, resultData);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateResult(Result result) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "UPDATE results SET event_id = ?, athlete_id = ?, result_data = ? WHERE result_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, result.getEventId());
            preparedStatement.setString(2, result.getVainqueur());
            preparedStatement.setString(3, result.getResultData());
            preparedStatement.setInt(4, result.getResultId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteResult(int resultId) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "DELETE FROM results WHERE result_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, resultId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Result getResultById(int resultId) {
        Result result = null;
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM results WHERE result_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, resultId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = new Result(
                        resultSet.getInt("result_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getString("vainqueur"),
                        resultSet.getString("result_data")
                );
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Result> getResultsByEventId(int eventId) {
        List<Result> results = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM results WHERE event_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Result result = new Result(
                        resultSet.getInt("result_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getString("vainqueur"),
                        resultSet.getString("result_data")
                );
                results.add(result);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static List<Result> getResultsByAthleteId(int athleteId) {
        List<Result> results = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM results WHERE athlete_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, athleteId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Result result = new Result(
                        resultSet.getInt("result_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getString("vainqueur"),
                        resultSet.getString("result_data")
                );
                results.add(result);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
