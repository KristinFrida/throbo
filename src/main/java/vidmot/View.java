package vidmot;

/**
 * Geymir valið fyrir mismunandi fxml skrár, þannig það er hægt að velja á milli view
 */
public enum View {
    START("/vidmot/hello-view.fxml"),
    LOGIN("login-view.fxml"),
    SIGNUP("sign-in-view.fxml"),
    TOUR_DETAILS("/vidmot/tour-details-view.fxml"),
    MYPAGE("my-page.fxml");

    private final String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}