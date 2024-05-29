package fr.isep.algo.projetjo.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import fr.isep.algo.projetjo.dao.*;
import fr.isep.algo.projetjo.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class pdfController extends navigationController {

    private static final String LOGO_PATH = "src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png";

    @FXML
    private ChoiceBox<Athlete> athleteChoiceBox;
    @FXML
    private ChoiceBox<Event> eventChoiceBox;
    @FXML
    private ChoiceBox medalChoiceBox;

    public void initialize() {
        List<Athlete> athletes;
        athletes = athleteDAO.getAllAthletes();
        ObservableList<Athlete> athleteObservableList = FXCollections.observableArrayList(athletes);
        athleteChoiceBox.setItems(athleteObservableList);

        List<Event> events;
        events = eventDAO.getAllEvents();
        ObservableList<Event> eventObservableList = FXCollections.observableArrayList(events);
        eventChoiceBox.setItems(eventObservableList);

        medalChoiceBox.setItems(FXCollections.observableArrayList("Par pays", "Par athlètes"));
    }

    @FXML
    public void generatePDFAllEvents(ActionEvent actionEvent) {
        String pdfFilePath = "Resultats_toutes_epreuves_paris2024.pdf";

        // Créer un document PDF
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Ouvrir le document
            document.open();

            // Ajouter un pied de page
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Page " + writer.getPageNumber()), (document.right() + document.left()) / 2, document.bottom() - 20, 0);

            document.add(Chunk.NEWLINE);

            // Titre du document avec couleur #d7c378 et police en gras
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0xD7, 0xC3, 0x78));
            Paragraph title = new Paragraph("Rapport sur les résultats des Jeux Olympiques 2024 de Paris", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Parcourir tous les événements récupérés
            List<Event> eventResults = eventDAO.getEventsFromResults();
            for (Event evt : eventResults) {

                Font eventFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

                // Titre de l'événement
                Font eventTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
                Paragraph eventTitle = new Paragraph(evt.getName(), eventTitleFont);
                eventTitle.setAlignment(Element.ALIGN_CENTER);
                document.add(eventTitle);
                document.add(Chunk.NEWLINE);

                // Informations sur l'événement
                Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
                Paragraph subtitle = new Paragraph("Sport : " + evt.getSportName(), subtitleFont);
                document.add(subtitle);
                document.add(Chunk.NEWLINE);
                Font eventInfoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
                document.add(createParagraph("Lieu : " + evt.getLocation(), eventInfoFont));
                document.add(createParagraph("Date : " + evt.getDate().toString(), eventInfoFont));
                document.add(Chunk.NEWLINE);

                try {
                    Image eventImage = Image.getInstance("src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png");
                    eventImage.scaleAbsolute(350, 350);
                    eventImage.setAlignment(Element.ALIGN_CENTER);
                    document.add(eventImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                document.add(Chunk.NEWLINE);

                // Texte après le titre de l'événement
                String eventDescription = "Les résultats de la " + evt.getName() + " de " + evt.getSportName() + " sont les suivants :";
                document.add(createParagraph(eventDescription, eventInfoFont));
                document.add(Chunk.NEWLINE);

                List<Result> results = evt.getResults();

                // Créer un tableau pour les résultats
                PdfPTable table = new PdfPTable(2); // 2 colonnes : Données de résultat et Vainqueur
                table.setWidthPercentage(80); // La largeur du tableau est définie sur 100% de la largeur de la page
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Ajouter des cellules pour les titres
                PdfPCell cellTitle1 = new PdfPCell(new Phrase("Résultat", eventFont));
                cellTitle1.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
                PdfPCell cellTitle2 = new PdfPCell(new Phrase("Vainqueur", eventFont));
                cellTitle2.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));

                table.addCell(cellTitle1);
                table.addCell(cellTitle2);

                // Ajouter les données de résultat dans le tableau
                for (Result result : results) {
                    PdfPCell cellData = new PdfPCell(new Phrase(result.getResultData(), eventFont));
                    PdfPCell cellWinner = new PdfPCell(new Phrase(result.getVainqueur(), eventFont));
                    table.addCell(cellData);
                    table.addCell(cellWinner);
                }

                // Ajouter le tableau au document
                document.add(table);

                // Ajouter une nouvelle page pour chaque événement
                document.newPage();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Génération du PDF");
            alert.setContentText("Le fichier PDF a été généré avec succès !");
            alert.showAndWait();
        }
    }

    @FXML
    public void generatePDFOneEvent(ActionEvent actionEvent) {

        Event evt = eventChoiceBox.getValue();
        String pdfFilePath = "Resultats_" + evt.getName() + "_paris2024.pdf";

        // Créer un document PDF
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Ouvrir le document
            document.open();

            // Ajouter un pied de page
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Page " + writer.getPageNumber()), (document.right() + document.left()) / 2, document.bottom() - 20, 0);

            document.add(Chunk.NEWLINE);

            // Titre du document avec couleur #d7c378 et police en gras
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0xD7, 0xC3, 0x78));
            Paragraph title = new Paragraph("Rapport sur les résultats des Jeux Olympiques 2024 de Paris", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            Font eventFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            // Titre de l'événement
            Font eventTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph eventTitle = new Paragraph(evt.getName(), eventTitleFont);
            eventTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(eventTitle);
            document.add(Chunk.NEWLINE);

            // Informations sur l'événement
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph subtitle = new Paragraph("Sport : " + evt.getSportName(), subtitleFont);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);
            Font eventInfoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            document.add(createParagraph("Lieu : " + evt.getLocation(), eventInfoFont));
            document.add(createParagraph("Date : " + evt.getDate().toString(), eventInfoFont));
            document.add(Chunk.NEWLINE);

            try {
                Image eventImage = Image.getInstance("src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png");
                eventImage.scaleAbsolute(350, 350);
                eventImage.setAlignment(Element.ALIGN_CENTER);
                document.add(eventImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.add(Chunk.NEWLINE);

            // Texte après le titre de l'événement
            String eventDescription = "Le résultat de la " + evt.getName() + " de " + evt.getSportName() + " est le suivant :";
            document.add(createParagraph(eventDescription, eventInfoFont));
            document.add(Chunk.NEWLINE);

            List<Result> results = resultsDAO.getResultsByEventId(evt.getId());

            // Créer un tableau pour les résultats
            PdfPTable table = new PdfPTable(2); // 2 colonnes : Données de résultat et Vainqueur
            table.setWidthPercentage(80); // La largeur du tableau est définie sur 100% de la largeur de la page
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Ajouter des cellules pour les titres
            PdfPCell cellTitle1 = new PdfPCell(new Phrase("Résultat", eventFont));
            cellTitle1.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle2 = new PdfPCell(new Phrase("Vainqueur", eventFont));
            cellTitle2.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));

            table.addCell(cellTitle1);
            table.addCell(cellTitle2);

            // Ajouter les données de résultat dans le tableau
            for (Result result : results) {
                PdfPCell cellData = new PdfPCell(new Phrase(result.getResultData(), eventFont));
                PdfPCell cellWinner = new PdfPCell(new Phrase(result.getVainqueur(), eventFont));
                table.addCell(cellData);
                table.addCell(cellWinner);
            }

            // Ajouter le tableau au document
            document.add(table);

            // Ajouter une nouvelle page pour chaque événement
            document.newPage();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Génération du PDF");
            alert.setContentText("Le fichier PDF a été généré avec succès !");
            alert.showAndWait();
        }
    }

    @FXML
    public void generatePDFAthlete(ActionEvent actionEvent) {

        Athlete athlete = athleteChoiceBox.getValue();
        String pdfFilePath = "Resultats_" + athlete.getNom() + "_paris2024.pdf";

        // Créer un document PDF
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Ouvrir le document
            document.open();

            // Ajouter un pied de page
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Page " + writer.getPageNumber()), (document.right() + document.left()) / 2, document.bottom() - 20, 0);

            document.add(Chunk.NEWLINE);

            // Titre du document avec couleur #d7c378 et police en gras
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0xD7, 0xC3, 0x78));
            Paragraph title = new Paragraph("Rapport sur les résultats des Jeux Olympiques 2024 de Paris", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            Font eventFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            // Titre de l'événement
            Font eventTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            String nom = athlete.getPrenom() + " " + athlete.getNom();
            Paragraph eventTitle = new Paragraph(nom, eventTitleFont);
            eventTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(eventTitle);
            document.add(Chunk.NEWLINE);

            // Informations sur l'événement

            String sportName = sportDAO.getSportNameFromId(athlete.getSport());

            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph subtitle = new Paragraph("Sport : " + sportName, subtitleFont);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);
            Font eventInfoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            //document.add(createParagraph("Lieu : " + evt.getLocation(), eventInfoFont));
            //document.add(createParagraph("Date : " + evt.getDate().toString(), eventInfoFont));
            document.add(Chunk.NEWLINE);

            try {
                Image eventImage = Image.getInstance("src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png");
                eventImage.scaleAbsolute(300, 350);
                eventImage.setAlignment(Element.ALIGN_CENTER);
                document.add(eventImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.add(Chunk.NEWLINE);

            // Texte après le titre de l'événement
            String eventDescription = "Les résultats pour " + athlete.getNom() + " " + athlete.getPrenom() + " sont les suivants :";
            document.add(createParagraph(eventDescription, eventInfoFont));
            document.add(Chunk.NEWLINE);

            List<Result> results = resultsDAO.getAllResultsAthlete(athlete.getId());

            // Créer un tableau pour les résultats
            PdfPTable table = new PdfPTable(3); // 2 colonnes : Données de résultat et Vainqueur
            table.setWidthPercentage(80); // La largeur du tableau est définie sur 100% de la largeur de la page
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Ajouter des cellules pour les titres
            PdfPCell cellTitle1 = new PdfPCell(new Phrase("Nom de l'épreuve", eventFont));
            cellTitle1.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle2 = new PdfPCell(new Phrase("Résultat", eventFont));
            cellTitle2.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle3 = new PdfPCell(new Phrase("Vainqueur", eventFont));
            cellTitle3.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));

            table.addCell(cellTitle1);
            table.addCell(cellTitle2);
            table.addCell(cellTitle3);



            // Ajouter les données de résultat dans le tableau
            for (Result result : results) {
                String eventName = eventDAO.getEventById(result.getEventId());
                PdfPCell cellName = new PdfPCell(new Phrase(eventName, eventFont));
                PdfPCell cellData = new PdfPCell(new Phrase(result.getResultData(), eventFont));
                PdfPCell cellWinner = new PdfPCell(new Phrase(result.getVainqueur(), eventFont));
                table.addCell(cellName);
                table.addCell(cellData);
                table.addCell(cellWinner);
            }

            // Ajouter le tableau au document
            document.add(table);

            // Ajouter une nouvelle page pour chaque événement
            document.newPage();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Génération du PDF");
            alert.setContentText("Le fichier PDF a été généré avec succès !");
            alert.showAndWait();
        }
    }

    @FXML
    public void generatePDFMedal(ActionEvent actionEvent) {

        String medalSort = (String) medalChoiceBox.getValue();

        if (medalSort.equals("Par pays")) {
            generatePDFMedalByCountry(actionEvent);
        } else {
            generatePDFMedalByAthlete(actionEvent);
        }
    }

    @FXML
    public void generatePDFMedalByCountry(ActionEvent actionEvent) {

        String pdfFilePath = "Tableau_medaille_par_pays_paris2024.pdf";

        // Créer un document PDF
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Ouvrir le document
            document.open();

            // Ajouter un pied de page
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Page " + writer.getPageNumber()), (document.right() + document.left()) / 2, document.bottom() - 20, 0);

            document.add(Chunk.NEWLINE);

            // Titre du document avec couleur #d7c378 et police en gras
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0xD7, 0xC3, 0x78));
            Paragraph title = new Paragraph("Rapport sur les résultats des Jeux Olympiques 2024 de Paris", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            Font eventFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            // Titre de l'événement
            Font eventTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph eventTitle = new Paragraph("Tableau des médailles par pays", eventTitleFont);
            eventTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(eventTitle);
            document.add(Chunk.NEWLINE);

            try {
                Image eventImage = Image.getInstance("src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png");
                eventImage.scaleAbsolute(300, 350);
                eventImage.setAlignment(Element.ALIGN_CENTER);
                document.add(eventImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.add(Chunk.NEWLINE);

            // Obtenir les médailles par pays
            List<Athlete> athletes = athleteDAO.getAllAthletes();
            Map<String, Country> countryMedalMap = new HashMap<>();

            for (Athlete athlete : athletes) {
                String countryName = athlete.getPays();
                List<Medal> medals = medalDAO.getAthleteMedals(athlete.getId());
                int goldCount = 0;
                int silverCount = 0;
                int bronzeCount = 0;
                for (Medal medal : medals) {
                    switch (medal.getMedalType()) {
                        case "Or":
                            goldCount++;
                            break;
                        case "Argent":
                            silverCount++;
                            break;
                        case "Bronze":
                            bronzeCount++;
                            break;
                    }
                }

                if (!countryMedalMap.containsKey(countryName)) {
                    countryMedalMap.put(countryName, new Country(countryName));
                }

                Country country = countryMedalMap.get(countryName);
                country.addGoldMedals(goldCount);
                country.addSilverMedals(silverCount);
                country.addBronzeMedals(bronzeCount);
            }

            ObservableList<Country> countryList = FXCollections.observableArrayList(countryMedalMap.values());

            // Créer un tableau pour les médailles par pays
            PdfPTable table = new PdfPTable(5); // 5 colonnes : Pays, Or, Argent, Bronze, Total
            table.setWidthPercentage(80); // La largeur du tableau est définie sur 100% de la largeur de la page
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Ajouter des cellules pour les titres
            PdfPCell cellTitle1 = new PdfPCell(new Phrase("Pays", eventFont));
            cellTitle1.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle2 = new PdfPCell(new Phrase("Or", eventFont));
            cellTitle2.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle3 = new PdfPCell(new Phrase("Argent", eventFont));
            cellTitle3.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle4 = new PdfPCell(new Phrase("Bronze", eventFont));
            cellTitle4.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle5 = new PdfPCell(new Phrase("Total", eventFont));
            cellTitle5.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));

            table.addCell(cellTitle1);
            table.addCell(cellTitle2);
            table.addCell(cellTitle3);
            table.addCell(cellTitle4);
            table.addCell(cellTitle5);

            // Ajouter les données de médailles par pays dans le tableau
            for (Country country : countryList) {
                PdfPCell cellCountry = new PdfPCell(new Phrase(country.getName(), eventFont));
                PdfPCell cellGold = new PdfPCell(new Phrase(String.valueOf(country.getGoldMedals()), eventFont));
                PdfPCell cellSilver = new PdfPCell(new Phrase(String.valueOf(country.getSilverMedals()), eventFont));
                PdfPCell cellBronze = new PdfPCell(new Phrase(String.valueOf(country.getBronzeMedals()), eventFont));
                int totalMedals = country.getGoldMedals() + country.getSilverMedals() + country.getBronzeMedals();
                PdfPCell cellTotal = new PdfPCell(new Phrase(String.valueOf(totalMedals), eventFont));

                table.addCell(cellCountry);
                table.addCell(cellGold);
                table.addCell(cellSilver);
                table.addCell(cellBronze);
                table.addCell(cellTotal);
            }

            // Ajouter le tableau au document
            document.add(table);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Génération du PDF");
            alert.setContentText("Le fichier PDF a été généré avec succès !");
            alert.showAndWait();
        }
    }


    @FXML
    public void generatePDFMedalByAthlete(ActionEvent actionEvent) {

        String pdfFilePath = "Tableau_medaille_par_athlète_paris2024.pdf";

        // Créer un document PDF
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Ouvrir le document
            document.open();

            // Ajouter un pied de page
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Page " + writer.getPageNumber()), (document.right() + document.left()) / 2, document.bottom() - 20, 0);

            document.add(Chunk.NEWLINE);

            // Titre du document avec couleur #d7c378 et police en gras
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0xD7, 0xC3, 0x78));
            Paragraph title = new Paragraph("Rapport sur les résultats des Jeux Olympiques 2024 de Paris", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            Font eventFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            // Titre de l'événement
            Font eventTitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph eventTitle = new Paragraph("Tableau des médailles par pays", eventTitleFont);
            eventTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(eventTitle);
            document.add(Chunk.NEWLINE);

            try {
                Image eventImage = Image.getInstance("src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png");
                eventImage.scaleAbsolute(300, 350);
                eventImage.setAlignment(Element.ALIGN_CENTER);
                document.add(eventImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.add(Chunk.NEWLINE);

            List<Athlete> athletes = athleteDAO.getAllAthletes();
            ObservableList<Athlete> athleteList = FXCollections.observableArrayList();

            for (Athlete athlete : athletes) {
                List<Medal> medals = medalDAO.getAthleteMedals(athlete.getId());
                int goldCount = 0;
                int silverCount = 0;
                int bronzeCount = 0;
                for (Medal medal : medals) {
                    switch (medal.getMedalType()) {
                        case "Or":
                            goldCount++;
                            break;
                        case "Argent":
                            silverCount++;
                            break;
                        case "Bronze":
                            bronzeCount++;
                            break;
                    }
                }
                athlete.setGoldMedals(goldCount);
                athlete.setSilverMedals(silverCount);
                athlete.setBronzeMedals(bronzeCount);
                athleteList.add(athlete);
            }

            // Créer un tableau pour les médailles par pays
            PdfPTable table = new PdfPTable(5); // 5 colonnes : Pays, Or, Argent, Bronze, Total
            table.setWidthPercentage(80); // La largeur du tableau est définie sur 100% de la largeur de la page
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Ajouter des cellules pour les titres
            PdfPCell cellTitle1 = new PdfPCell(new Phrase("Athlète", eventFont));
            cellTitle1.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle2 = new PdfPCell(new Phrase("Or", eventFont));
            cellTitle2.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle3 = new PdfPCell(new Phrase("Argent", eventFont));
            cellTitle3.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle4 = new PdfPCell(new Phrase("Bronze", eventFont));
            cellTitle4.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));
            PdfPCell cellTitle5 = new PdfPCell(new Phrase("Total", eventFont));
            cellTitle5.setBackgroundColor(new BaseColor(0xD7, 0xC3, 0x78));

            table.addCell(cellTitle1);
            table.addCell(cellTitle2);
            table.addCell(cellTitle3);
            table.addCell(cellTitle4);
            table.addCell(cellTitle5);

            // Ajouter les données de médailles par pays dans le tableau
            for (Athlete athlete : athleteList) {
                PdfPCell cellCountry = new PdfPCell(new Phrase(athlete.getNom(), eventFont));
                PdfPCell cellGold = new PdfPCell(new Phrase(String.valueOf(athlete.getGoldMedals()), eventFont));
                PdfPCell cellSilver = new PdfPCell(new Phrase(String.valueOf(athlete.getSilverMedals()), eventFont));
                PdfPCell cellBronze = new PdfPCell(new Phrase(String.valueOf(athlete.getBronzeMedals()), eventFont));
                int totalMedals = athlete.getGoldMedals() + athlete.getSilverMedals() + athlete.getBronzeMedals();
                PdfPCell cellTotal = new PdfPCell(new Phrase(String.valueOf(totalMedals), eventFont));

                table.addCell(cellCountry);
                table.addCell(cellGold);
                table.addCell(cellSilver);
                table.addCell(cellBronze);
                table.addCell(cellTotal);
            }

            // Ajouter le tableau au document
            document.add(table);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Génération du PDF");
            alert.setContentText("Le fichier PDF a été généré avec succès !");
            alert.showAndWait();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document
            document.close();
        }
    }

    private Paragraph createParagraph(String content, Font font) {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setIndentationLeft(20);
        return paragraph;
    }

    @FXML
    private void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }
}
