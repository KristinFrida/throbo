package frontend;

/**
 * Store choices for different fxml files so it is possible to switch between views
 */
public enum View {
    START("/frontend/hello-view.fxml"),
    LOGIN("login-view.fxml"),
    SIGNUP("sign-in-view.fxml"),
    TOUR_DETAILS("/frontend/tour-details-view.fxml"),
    MYPAGE("my-page.fxml");

    private final String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}