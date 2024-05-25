package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.eventDAO;
import fr.isep.algo.projetjo.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class eventController extends dashboardController {
    @FXML
    private ListView<Event> eventListView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField sportNameField;

    private eventDAO eventDAO;
    private ObservableList<Event> eventList;

    public void initialize(Connection connection) {
        eventDAO = new eventDAO(); // Assuming the eventDAO methods are static as in athleteDAO
        loadEvents();
    }

    private void loadEvents() {
        try {
            eventList = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventListView.setItems(eventList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to load events from the database.");
        }
    }

    @FXML
    private void handleAddEvent() {
        String dateString = dateField.getText();
        String sportName = sportNameField.getText(); // Assurez-vous de changer sportIdField en sportNameField
        if (isValidDate(dateString)) {
            try {
                int sportId = eventDAO.getSportIdByName(sportName); // Utilisez la méthode pour obtenir l'ID du sport
                if (sportId != -1) { // Vérifiez si l'ID du sport est valide (-1 indique une absence de correspondance)
                    Event event = new Event(
                            0,
                            sportId, // Utilisez l'ID du sport obtenu
                            nameField.getText(),
                            locationField.getText(),
                            Date.valueOf(dateString)
                    );
                    eventDAO.addEvent(event);
                    loadEvents();
                    nameField.clear();
                    locationField.clear();
                    dateField.clear();
                    sportNameField.clear();
                } else {
                    showAlert("Invalid Sport Name", "Please enter a valid sport name.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Database Error", "Unable to add the event to the database.");
            }
        } else {
            showAlert("Invalid Date Format", "Please enter the date in the format yyyy-MM-dd.");
        }
    }


    @FXML
    private void handleUpdateEvent() {
        String dateString = dateField.getText();
        String sportName = sportNameField.getText(); // Assurez-vous de changer sportIdField en sportNameField
        if (isValidDate(dateString)) {
            try {
                Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                    int sportId = eventDAO.getSportIdByName(sportName); // Utilisez la méthode pour obtenir l'ID du sport
                    if (sportId != -1) { // Vérifiez si l'ID du sport est valide (-1 indique une absence de correspondance)
                        selectedEvent.setSport(sportId);
                        selectedEvent.setName(nameField.getText());
                        selectedEvent.setLocation(locationField.getText());
                        selectedEvent.setDate(Date.valueOf(dateString));
                        eventDAO.updateEvent(selectedEvent);
                        loadEvents();
                        showAlert("Evènement ajouté avec succès", "Evènement ajouté avec succès !");
                    } else {
                        showAlert("Invalid Sport Name", "Please enter a valid sport name.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur BDD", "Unable to update the event in the database.");
            }
        }
    }

    @FXML
    private void handleDeleteEvent() {
        try {
            Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                eventDAO.deleteEvent(selectedEvent.getId());
                loadEvents();
            } else {
                showAlert("No Selection", "Please select an event to delete.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to delete the event from the database.");
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

    @FXML
    private void goBack(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/athleteWindow.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
