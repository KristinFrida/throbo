package vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import bakendi.BookingManager;
import bakendi.UserRepository;
import java.util.List;

public class MyPageController {

    @FXML
    private ListView<String> bookingListView;

    @FXML
    private void initialize() {
        loadBookings();  // Load bookings when the page opens
    }

    private void loadBookings() {
        bookingListView.getItems().clear();  // Clear previous items
        List<String> bookings = BookingManager.getBookings();

        if (bookings.isEmpty()) {
            bookingListView.getItems().add("No bookings found.");
        } else {
            bookingListView.getItems().setAll(bookings);
        }
    }

    @FXML
    private void refreshBookings() {
        loadBookings();  // Reload bookings dynamically
    }

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
