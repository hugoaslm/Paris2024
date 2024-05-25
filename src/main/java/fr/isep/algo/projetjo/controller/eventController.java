package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.dao.eventDAO;
import fr.isep.algo.projetjo.dao.event_athletesDAO;
import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class eventController extends dashboardController {
    @FXML
    private ListView<String> athleteListView;
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

    @FXML
    private TableView<Event> eventTableView;
    @FXML
    private TableColumn<Event, Integer> eventIdColumn;
    @FXML
    private TableColumn<Event, String> sportNameColumn;
    @FXML
    private TableColumn<Event, String> eventNameColumn;
    @FXML
    private TableColumn<Event, String> eventLocationColumn;
    @FXML
    private TableColumn<Event, LocalDate> eventDateColumn;
    @FXML
    private TableColumn<Event, String> athletesColumn;

    @FXML
    private void initialize() {

        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNameColumn.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        athletesColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            List<Athlete> athletes = event.getAthletes();
            String athleteNames = athletes.stream()
                    .map(Athlete::getNom)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(athleteNames);
        });

        eventDAO = new eventDAO();
        try {
            loadEvents();
            loadAthletes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEvents() throws SQLException {
        List<Event> events = eventDAO.getAllEvents();
        for (Event event : events) {
            // Récupérer le nom du sport à partir de son ID
            String sportName = eventDAO.getSportNameById(event.getSport());
            event.setSportName(sportName);

            // Récupérer les athlètes associés
            List<Athlete> athletes = event_athletesDAO.getAthletesByEventId(event.getId());
            event.setAthletes(athletes); // Assurez-vous d'avoir un attribut `athletes` dans votre classe Event
        }
        eventList = FXCollections.observableArrayList(events);

        // Remplacez eventListView par un TableView
        eventTableView.setItems(eventList);
    }

    private void loadAthletes() {
        List<Athlete> athletes = athleteDAO.getAllAthletes();
        ObservableList<String> athleteNames = FXCollections.observableArrayList();
        for (Athlete athlete : athletes) {
            athleteNames.add(athlete.getNom());
        }
        athleteListView.setItems(athleteNames);

        // Configurez la sélection multiple
        athleteListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void handleAddEvent() {
        String dateString = dateField.getText();
        String sportName = sportNameField.getText();
        if (isValidDate(dateString)) {
            try {
                int sportId = eventDAO.getSportIdByName(sportName);
                eventDAO.addEvent(sportId, nameField.getText(), locationField.getText(), Date.valueOf(dateString)); // Ajouter l'événement à la base de données
                int eventId = eventDAO.getEventIdBySportName(sportId, nameField.getText()); // Récupérer l'ID de l'événement ajouté

                // Ajouter les athlètes sélectionnés à cet événement
                ObservableList<String> selectedAthletes = athleteListView.getSelectionModel().getSelectedItems();
                System.out.println(selectedAthletes);
                for (String selectedAthlete : selectedAthletes) {
                    int athleteId = athleteDAO.getAthleteIdByName(selectedAthlete); // Récupérer l'ID de l'athlète
                    event_athletesDAO.addAthleteToEvent(eventId, athleteId); // Ajouter l'athlète à l'événement
                }

                loadEvents();
                nameField.clear();
                locationField.clear();
                dateField.clear();
                sportNameField.clear();
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
        String sportName = sportNameField.getText();
        if (isValidDate(dateString)) {
            try {
                Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                    int sportId = eventDAO.getSportIdByName(sportName);
                    if (sportId != -1) {
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
            Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
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