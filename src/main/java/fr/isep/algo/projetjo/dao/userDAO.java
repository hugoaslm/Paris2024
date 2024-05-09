package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.DatabaseManager;
import fr.isep.algo.projetjo.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {

    // Méthode pour vérifier les informations de connexion de l'utilisateur
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
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            success = resultSet.next(); // Vérifie s'il y a un résultat

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.closeConnection(connection);

        return success;
    }

    // Méthode pour récupérer toutes les informations de l'utilisateur
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
                // Création d'un objet User avec les informations récupérées de la base de données
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
}
