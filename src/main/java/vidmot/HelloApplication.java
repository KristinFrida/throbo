package vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        var scene = new Scene(new Pane());
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.START);
        stage.setScene(scene);
        stage.setTitle("Bókunarsíða");
        stage.show();

        URL imageUrl = getClass().getResource("/images/sample1.png");
        if (imageUrl == null) {
            System.err.println("Resource not found: /images/sample1.png");
        } else {
            System.out.println("Found resource: " + imageUrl);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}