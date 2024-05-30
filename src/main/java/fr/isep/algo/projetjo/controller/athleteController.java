package fr.isep.algo.projetjo.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import fr.isep.algo.projetjo.dao.athleteDAO;
import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class athleteController extends navigationController {

    @FXML
    public Button dashboardButton;
    @FXML
    public Button athletesButton;
    @FXML
    public Button disciplinesButton;
    @FXML
    public Button eventsButton;
    @FXML
    public Button resultsButton;
    @FXML
    public Button analyseButton;
    @FXML
    public Button calendarButton;
    @FXML
    public FontAwesomeIconView iconAdd;

    @FXML
    private ComboBox<String> delegationComboBox;
    @FXML
    private TableView<Athlete> athleteTable;

    @FXML
    public TableColumn<Athlete, String> editColumn;
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
    private TableColumn<Athlete, String> sportColumn;

    public TextField searchField;

    @FXML
    private Button addButton;

    Athlete athlete = null;

    @FXML
    public void initialize() {


        int role = (int) SessionManager.getInstance().getAttribute("role");


        if (role != 1) {
            addButton.setVisible(false);
            editColumn.setVisible(false);
            iconAdd.setVisible(false);
        }


        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        paysColumn.setCellValueFactory(new PropertyValueFactory<>("pays"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sport"));

        sportColumn.setCellValueFactory(cellData -> {
            int sportId = cellData.getValue().getSport();
            String sportName = athleteDAO.getSportNameById(sportId);
            return new SimpleStringProperty(sportName);
        });


        List<String> delegations = athleteDAO.getAllDelegations();
        delegationComboBox.getItems().addAll(delegations);


        Callback<TableColumn<Athlete, String>, TableCell<Athlete, String>> cellFactory = (TableColumn<Athlete, String> param) -> {

            final TableCell<Athlete, String> cell = new TableCell<Athlete, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        editIcon.setFill(javafx.scene.paint.Color.GREEN);
                        deleteIcon.setGlyphSize(18);
                        editIcon.setGlyphSize(18);



                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                athlete = getTableView().getItems().get(getIndex());

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation de suppression");
                                alert.setHeaderText(null);
                                alert.setContentText("Êtes-vous sûr de vouloir supprimer cet athlète ?");

                                ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                                ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
                                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == buttonTypeYes) {
                                    athleteDAO.deleteAthlete(athlete);
                                    refreshTable();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            athlete = getTableView().getItems().get(getIndex());


                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/editAthlete.fxml"));
                            try {
                                Parent root = loader.load();
                                editAthleteController controller = loader.getController();
                                String nom_sport = athleteDAO.getSportNameById(athlete.getSport());
                                controller.initAthleteData(athlete.getId(), athlete.getNom(), athlete.getPrenom(), athlete.getPays(),
                                        athlete.getAge(), athlete.getSex(), nom_sport);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });



                        // Position
                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new javafx.geometry.Insets(2, 3, 0, 2));

                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };

        editColumn.setCellFactory(cellFactory);
    }

    @FXML
    private void searchInTableView() {

        String searchQuery = searchField.getText().trim();
        List<Athlete> athletes = athleteDAO.searchAthletes(searchQuery);
        athleteTable.setItems(FXCollections.observableArrayList(athletes));

    }

    @FXML
    private void loadAthletesByDelegation() {
        String selectedDelegation = delegationComboBox.getValue();
        List<Athlete> athletesByDelegation = athleteDAO.getAthletesByDelegation(selectedDelegation);
        athleteTable.setItems(FXCollections.observableArrayList(athletesByDelegation));
    }

    @FXML
    private void refreshTable() {

        String selectedDelegation = delegationComboBox.getValue();
        List<Athlete> athletesByDelegation = athleteDAO.getAthletesByDelegation(selectedDelegation);
        athleteTable.setItems(FXCollections.observableArrayList(athletesByDelegation));


        delegationComboBox.getItems().clear();
        List<String> delegations = athleteDAO.getAllDelegations();
        delegationComboBox.getItems().addAll(delegations);

    }

    @FXML
    private void openAllAthletesWindow(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/allAthletes.fxml"));
            Parent root = loader.load();


            Scene currentScene = ((Node) event.getSource()).getScene();


            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void goBack(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/homeStart.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void newAthlete() {

        System.out.println("Ajout d'un nouvel athlète");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/addAthlete.fxml"));
        try {
            Parent root = loader.load();

            addAthleteController controller = loader.getController();
            controller.initDefaultValues();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
