module fr.isep.algo.projetjo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens fr.isep.algo.projetjo to javafx.fxml;
    exports fr.isep.algo.projetjo;
}