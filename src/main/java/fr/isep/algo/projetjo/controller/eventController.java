package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.eventDAO;
import fr.isep.algo.projetjo.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class eventController {
    @FXML
    private ListView<Event> eventListView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField dateField;

    private eventDAO eventDAO;
    private ObservableList<Event> eventList;

    public void initialize(Connection connection) {
        eventDAO = new eventDAO(connection);
        loadEvents();
    }

    private void loadEvents() {
        try {
            eventList = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventListView.setItems(eventList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEvent() {
        String dateString = dateField.getText();
        if (isValidDate(dateString)) {
            try {
                Event event = new Event(
                        0,
                        nameField.getText(),
                        locationField.getText(),
                        Date.valueOf(dateString)
                );
                eventDAO.addEvent(event);
                loadEvents();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Invalid Date Format", "Please enter the date in the format yyyy-MM-dd.");
        }
    }

    @FXML
    private void handleUpdateEvent() {
        String dateString = dateField.getText();
        if (isValidDate(dateString)) {
            try {
                Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                    selectedEvent.setName(nameField.getText());
                    selectedEvent.setLocation(locationField.getText());
                    selectedEvent.setDate(Date.valueOf(dateString));
                    eventDAO.updateEvent(selectedEvent);
                    loadEvents();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Invalid Date Format", "Please enter the date in the format yyyy-MM-dd.");
        }
    }

    @FXML
    private void handleDeleteEvent() {
        try {
            Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                eventDAO.deleteEvent(selectedEvent.getId());
                loadEvents();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
