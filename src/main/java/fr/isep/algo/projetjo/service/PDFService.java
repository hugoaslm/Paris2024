package fr.isep.algo.projetjo.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import fr.isep.algo.projetjo.controller.navigationController;
import fr.isep.algo.projetjo.dao.eventDAO;
import fr.isep.algo.projetjo.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDFService extends navigationController {

    private static final String LOGO_PATH = "src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png";

    @FXML
    public void generatePDF(ActionEvent actionEvent) {
        String pdfFilePath = "results.pdf";

        // Créer un document PDF
        Document document = new Document();

        try {
            // Créer un écrivain PDF avec la destination spécifiée
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
            List<Event> eventResults = getEventsFromResults();
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
                    Image eventImage = Image.getInstance("src/main/resources/fr/isep/algo/projetjo/img/cover_ath.png");
                    eventImage.scaleAbsolute(350, 200);
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
        }
    }

    private Paragraph createParagraph(String content, Font font) {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setIndentationLeft(20);
        return paragraph;
    }

    public List<Event> getEventsFromResults() {
        List<Event> events = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT events.event_id, events.event_name, events.event_location, events.event_date, events.sport_id, results.result_data, results.vainqueur " +
                    "FROM results " +
                    "INNER JOIN events ON results.event_id = events.event_id";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String eventName = rs.getString("event_name");
                        String eventLocation = rs.getString("event_location");
                        Date eventDate = rs.getDate("event_date");
                        int sport = rs.getInt("sport_id");

                        String sportName = eventDAO.getSportNameById(sport);

                        Event event = new Event(eventId, sport, eventName, eventLocation, eventDate);
                        event.setSportName(sportName);

                        String resultData = rs.getString("result_data");
                        String vainqueur = rs.getString("vainqueur");

                        List<Result> results = new ArrayList<>();
                        results.add(new Result(0, eventId, resultData, vainqueur));

                        event.setResults(results);
                        events.add(event);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    @FXML
    private void goBack(ActionEvent event) {
        redirectToPage("/fr/isep/algo/projetjo/view/homeStart.fxml", event);
    }
}
