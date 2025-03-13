package vidmot;

import javafx.fxml.FXML;
import bakendi.UserRepository;

public class MyPageController {

    @FXML
    private void goToHome() {
        ViewSwitcher.switchTo(View.START);
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out user...");
        UserRepository.logoutUser();
        ViewSwitcher.switchTo(View.START);
        HelloController helloController = (HelloController) ViewSwitcher.lookup(View.START);
        if (helloController != null) {
            helloController.refreshLoginState();
        }
    }
}
