package frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewSwitcher {
    private static final Map<View, Parent> cache = new HashMap<>();
    private static final Map<View, Object> controllers = new HashMap<>();
    private static final Map<View, FXMLLoader> loaders = new HashMap<>();

    private static Scene scene;
    private static View lastView;
    private static View currentView;
    private static Stage mainStage;

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    /**
     * Switches the application view by loading the appropriate FXML and optionally resizing the window.
     */
    public static void switchTo(View view) {
        if (scene == null) {
            return;
        }

        try {
            Parent root;
            if (cache.containsKey(view)) {
                root = cache.get(view);
            } else {
                var resource = ViewSwitcher.class.getResource(view.getFileName());
                if (resource == null) {
                    return;
                }
                FXMLLoader loader = new FXMLLoader(resource);
                root = loader.load();
                cache.put(view, root);
                controllers.put(view, loader.getController());
                loaders.put(view, loader);
            }

            lastView = currentView;
            currentView = view;
            scene.setRoot(root);

            if (mainStage != null) {
                if (view == View.LOGIN || view == View.SIGNUP) {
                    mainStage.setWidth(450);
                    mainStage.setHeight(450);
                    mainStage.setMinWidth(450);
                    mainStage.setMinHeight(450);
                } else if (view == View.MYPAGE) {
                    mainStage.setWidth(800);
                    mainStage.setHeight(700);
                    mainStage.setMinWidth(900);
                    mainStage.setMinHeight(700);
                } else {
                    mainStage.setWidth(700);
                    mainStage.setHeight(800);
                    mainStage.setMinWidth(900);
                    mainStage.setMinHeight(700);
                }
                mainStage.centerOnScreen();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the controller for the given view.
     */
    public static Object getController(View view) {
        FXMLLoader loader = loaders.get(view);
        return (loader != null) ? loader.getController() : null;
    }

    /**
     * Returns the cached controller for a view if already loaded.
     */
    public static Object lookup(View view) {
        return controllers.get(view);
    }

}
