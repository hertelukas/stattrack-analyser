module de.lukas.stattrackanalyser {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.lukas.stattrackanalyser to javafx.fxml;
    exports de.lukas.stattrackanalyser;
}