package frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var scene = new Scene(new Pane());
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        ViewSwitcher.setScene(scene);
        ViewSwitcher.setMainStage(stage);

        ViewSwitcher.switchTo(View.START);

        stage.setScene(scene);
        stage.setTitle("Day Tours");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
