package vidmot;

/**
 * Geymir valið fyrir mismunandi fxml skrár, þannig það er hægt að velja á milli view
 */
public enum View {
    START("/vidmot/hello-view.fxml"),
    GRID1("grid1-view.fxml"),
    GRID2("grid2-view.fxml"),
    GRID3("grid3-view.fxml"),
    GRID4("grid4-view.fxml"),
    GRID5("grid5-view.fxml"),
    GRID6("grid6-view.fxml"),
    GRID7("grid7-view.fxml"),
    GRID8("grid8-view.fxml"),
    GRID9("grid9-view.fxml"),
    GRID10("grid10-view.fxml"),
    GRID11("grid11-view.fxml"),
    GRID12("grid12-view.fxml"),
    LOGIN("login-view.fxml"),
    TOUR_DETAILS("/vidmot/tour-details-view.fxml");

    private final String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
