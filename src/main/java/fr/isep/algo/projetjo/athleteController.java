package fr.isep.algo.projetjo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class athleteController {
    private String name;
    private String country;
    private int age;
    private String sex;

    public athleteController(String name, String country, int age, String sex) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.sex = sex;
    }

    // Getters et setters

    // Méthode pour enregistrer un nouvel athlète dans la base de données
    public static void save(String name, String country, int age, String sex) {
        try {
            Connection connection = DatabaseManager.getConnection();
            String sql = "INSERT INTO athletes (name, country, age, sex) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, country);
            statement.setInt(3, age);
            statement.setString(4, sex);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer tous les athlètes de la base de données
    public static List<Athlete> getAllAthletes() {
        List<Athlete> athletes = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            String sql = "SELECT * FROM athletes";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Athlete athlete = new Athlete(
                        resultSet.getString("name"),
                        resultSet.getString("country"),
                        resultSet.getInt("age"),
                        resultSet.getString("sex")
                );
                athletes.add(athlete);
                System.out.println(athlete.getName());
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return athletes;
    }

    // Méthode pour supprimer cet athlète de la base de données
    public void delete() {
        try {
            Connection connection = DatabaseManager.getConnection();
            String sql = "DELETE FROM athletes WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

