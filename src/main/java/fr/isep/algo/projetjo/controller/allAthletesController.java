package fr.isep.algo.projetjo.controller;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.model.Athlete;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class allAthletesController {

    @FXML
    private TableView<Athlete> athleteTableView;

    @FXML
    private TableColumn<Athlete, String> nomColumn;

    @FXML
    private TableColumn<Athlete, String> prenomColumn;

    @FXML
    private TableColumn<Athlete, String> paysColumn;

    @FXML
    private TableColumn<Athlete, Integer> ageColumn;

    @FXML
    private TableColumn<Athlete, String> sexColumn;

    @FXML
    public void initialize() {

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        paysColumn.setCellValueFactory(new PropertyValueFactory<>("pays"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));

        loadAllAthletes();

    }

    private void loadAllAthletes() {

        ObservableList<Athlete> athletes = FXCollections.observableArrayList(athleteDAO.getAllAthletes());
        athleteTableView.setItems(athletes);

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
