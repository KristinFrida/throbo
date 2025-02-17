package vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewSwitcher {
    private static final Map<View, Parent> cache = new HashMap<>();
    private static final Map<View, Object> controllers = new HashMap<>();
    private static Scene scene;
    private static View lastView;
    private static View currentView;

    public static void setScene(Scene scene){
        ViewSwitcher.scene = scene;
    }

    /**
     * Færir notandann á milli mismunandi valmynda/fxml skráa
     */
    public static void switchTo(View view){
        if (scene == null){
            System.out.println("No scene set");
            return;
        }

        try{
            Parent root;
            if (cache.containsKey(view)){
                System.out.println("Loading from cache");
                root = cache.get(view);
            } else {
                System.out.println("Loading from FXML");
                FXMLLoader loader = new FXMLLoader(ViewSwitcher.class.getResource(view.getFileName()));
                root = loader.load();
                cache.put(view, root);
                controllers.put(view, loader.getController());
                System.out.println(view);
            }
            lastView = currentView;
            currentView = view;
            scene.setRoot(root);
        }catch (IOException e){
            e.printStackTrace();
        }
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