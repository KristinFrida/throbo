package vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.Stage;

public class ViewSwitcher {
    private static final Map<View, Parent> cache = new HashMap<>();
    private static final Map<View, Object> controllers = new HashMap<>();
    private static Scene scene;
    private static View lastView;
    private static View currentView;

    private static final Map<View, FXMLLoader> loaders = new HashMap<>();
    private static Stage mainStage;

    public static void setScene(Scene scene){
        ViewSwitcher.scene = scene;
    }
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }
    /**
     * Færir notandann á milli mismunandi valmynda/fxml skráa
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


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sækir controllerinn sem er tengdur við gefið view
     * @param view view sem ákveðinn controller þarf
     * @return controller instance fyrir tiltekið view
     */
    public static Object getController(View view) {
        FXMLLoader loader = loaders.get(view);
        return (loader != null) ? loader.getController() : null;
    }
    /**
     * Flettir upp hvaða fxml skrá notandinn er á
     */
    public static Object lookup(View v) {
        return controllers.get(v);
    }
    /**
     * Flettir upp hvaða fxml skrá notandinn var síðast á
     * @return síðasta fxml skráin
     */
    public static View getLastView() {
        return lastView;
    }



}