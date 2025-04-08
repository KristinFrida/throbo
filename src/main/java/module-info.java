module vidmot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens backend to javafx.base, javafx.fxml;
    opens vidmot to javafx.fxml;
    exports vidmot;
    exports backend;
}