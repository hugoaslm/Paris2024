module fr.isep.algo.projetjo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires itextpdf;
    requires java.desktop;


    opens fr.isep.algo.projetjo to javafx.fxml;
    exports fr.isep.algo.projetjo;
    exports fr.isep.algo.projetjo.controller;
    opens fr.isep.algo.projetjo.controller to javafx.fxml;
    exports fr.isep.algo.projetjo.model;
    opens fr.isep.algo.projetjo.model to javafx.fxml;
    exports fr.isep.algo.projetjo.util;
    opens fr.isep.algo.projetjo.util to javafx.fxml;

    exports fr.isep.algo.projetjo.service;
    opens fr.isep.algo.projetjo.service to javafx.fxml;
}