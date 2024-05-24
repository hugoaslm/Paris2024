package fr.isep.algo.projetjo.dao;

import fr.isep.algo.projetjo.model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class eventDAO {
    private Connection connection;

    public eventDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDate("date")
                );
                events.add(event);
            }
        }
        return events;
    }

    public void addEvent(Event event) throws SQLException {
        String query = "INSERT INTO events (name, location, date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getLocation());
            pstmt.setDate(3, new java.sql.Date(event.getDate().getTime()));
            pstmt.executeUpdate();
        }
    }

    public void updateEvent(Event event) throws SQLException {
        String query = "UPDATE events SET name = ?, location = ?, date = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getLocation());
            pstmt.setDate(3, new java.sql.Date(event.getDate().getTime()));
            pstmt.setInt(4, event.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteEvent(int id) throws SQLException {
        String query = "DELETE FROM events WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
