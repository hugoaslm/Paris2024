package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.Sport;
import fr.isep.algo.projetjo.model.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class sportDAO {

    // Requête qui récupère toutes les catégories uniques dans la table sport
    public static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseManager.getConnection();
            String query = "SELECT DISTINCT categorie FROM sports";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String category = resultSet.getString("categorie");
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.closeConnection(connection);
        }

        return categories;
    }

    // Requête qui récupère tous les sports pour une catégorie donnée
    public static List<String> getSportsByCategory(String category) {
        List<String> sports = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseManager.getConnection();
            String query = "SELECT nom_sport FROM sports WHERE categorie = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String sport = resultSet.getString("nom_sport");
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sports;
    }

    public static String getTypeResultFromSport(int sportId) {
        String type_resultat = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseManager.getConnection();
            String query = "SELECT type_resultat FROM sports WHERE sport_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                type_resultat = resultSet.getString("type_resultat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.closeConnection(connection);
        }

        return type_resultat;
    }

}
