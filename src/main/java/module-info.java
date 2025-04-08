module frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens backend to javafx.base, javafx.fxml;
    opens frontend to javafx.fxml;
    exports frontend;
    exports backend;
}