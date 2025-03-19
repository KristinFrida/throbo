package vidmot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var scene = new Scene(new Pane());
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        ViewSwitcher.setScene(scene);
        ViewSwitcher.setMainStage(stage);

        ViewSwitcher.switchTo(View.START);

        stage.setScene(scene);
        stage.setTitle("Bókunarsíða");
        stage.show();

        // Optional image check
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
