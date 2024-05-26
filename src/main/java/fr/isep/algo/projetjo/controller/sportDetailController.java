package fr.isep.algo.projetjo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class sportDetailController extends dashboardController {

    @FXML
    private Label sportTitle;
    @FXML
    private ImageView sportImageView;
    @FXML
    private Label sportDescription;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Map des descriptions pour chaque sport
    private static final Map<String, String> sportDescriptions = new HashMap<>();
    static {
        sportDescriptions.put("natation", " La natation est un sport aquatique pratiqué individuellement ou en équipe, qui consiste à parcourir une certaine distance à la nage le plus rapidement possible.");
        sportDescriptions.put("basketball", "Le basket-ball est un sport collectif opposant deux équipes de cinq joueurs sur un terrain rectangulaire. Le but est de marquer des points en lançant un ballon dans le panier de l'équipe adverse.");
    }

    public void setSportDetails(String sport) {
        sportTitle.setText(sport);

        String imagePath = "/fr/isep/algo/projetjo/img/" + sport.toLowerCase() + ".png";
        Image sportImage = new Image(getClass().getResourceAsStream(imagePath));
        sportImageView.setImage(sportImage);

        // Définir la description du sport à partir de la map
        String description = sportDescriptions.getOrDefault(sport.toLowerCase(), "Description non disponible.");
        sportDescription.setText(description);
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/projetjo/view/disciplines.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
