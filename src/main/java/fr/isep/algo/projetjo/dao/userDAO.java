package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {


    public static boolean checkLogin(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM users WHERE pseudo = ? AND MotDePasse = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password));
            resultSet = preparedStatement.executeQuery();
            success = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.closeConnection(connection);

        return success;
    }


    public static User getUserInfo(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM users WHERE pseudo = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                user = new User(
                        resultSet.getInt("id_user"),
                        resultSet.getString("pseudo"),
                        resultSet.getString("MotDePasse"),
                        resultSet.getInt("admin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.closeConnection(connection);

        return user;
    }


    public static boolean inscription(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DatabaseManager.getConnection();
            String query = "INSERT INTO users (pseudo, MotDePasse) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password)); // Hash du mot de passe avant insertion
            int rowsAffected = preparedStatement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.closeConnection(connection);
        }

        return success;
    }


    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}