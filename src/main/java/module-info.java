module vidmot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens bakendi to javafx.base, javafx.fxml;
    opens vidmot to javafx.fxml;
    exports vidmot;
    exports bakendi;
}