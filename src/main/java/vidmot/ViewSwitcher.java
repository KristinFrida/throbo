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

    //heðan
    private static final Map<View, FXMLLoader> loaders = new HashMap<>();
    private static Stage mainStage;
    //hingað

    public static void setScene(Scene scene){
        ViewSwitcher.scene = scene;
    }
    public static void setMainStage(Stage stage) {
        mainStage = stage; // Assign the main stage
    }
    /**
     * Færir notandann á milli mismunandi valmynda/fxml skráa
     */
    public static void switchTo(View view) {
        if (scene == null) {
            System.out.println("No scene set");
            return;
        }

        try {
            Parent root;
            if (cache.containsKey(view)) {
                System.out.println("✅ Loading from cache: " + view);
                root = cache.get(view);
            } else {
                System.out.println("✅ Loading from FXML: " + view.getFileName());
                var resource = ViewSwitcher.class.getResource(view.getFileName());
                if (resource == null) {
                    System.err.println("❌ Resource not found: " + view.getFileName());
                    return;
                }
                FXMLLoader loader = new FXMLLoader(resource);
                root = loader.load();
                cache.put(view, root);
                controllers.put(view, loader.getController());
                loaders.put(view, loader); // ✅ FIX: Store loader
                System.out.println("✅ View Loaded: " + view);
            }

            lastView = currentView;
            currentView = view;
            scene.setRoot(root);

            // Debug: Print controller info
            Object controller = getController(view);
            if (controller == null) {
                System.out.println("❌ Error: Controller is NULL for " + view);
            } else {
                System.out.println("✅ Controller found: " + controller.getClass().getSimpleName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //heðan
    public static Object getController(View view) {
        FXMLLoader loader = loaders.get(view);
        if (loader != null) {
            return loader.getController();
        } else {
            System.out.println("❌ Error: No loader found for " + view);
            return null;
        }
    }

    //hingað
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