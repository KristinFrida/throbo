package vidmot;

/**
 * Geymir valið fyrir mismunandi fxml skrár, þannig það er hægt að velja á milli view
 */
public enum View {
    START("hello-view.fxml"),
    LEIKBORD("gold-rush-view.fxml");

    private final String fileName;

    View(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return fileName;
    }
}