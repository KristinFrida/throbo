module frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens backend to javafx.base, javafx.fxml;
    exports frontend;
    exports backend;
    opens frontend to javafx.base, javafx.fxml;
}