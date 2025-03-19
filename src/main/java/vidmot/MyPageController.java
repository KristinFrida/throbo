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

    @FXML
    private void loadBookings() {
        if (bookingListView == null) {
            System.err.println("ERROR: bookingListView is null! Check FXML.");
            return;
        }

        bookingListView.getItems().clear();
        List<String> bookings = BookingManager.getBookingsForUser();

        System.out.println("Displaying " + bookings.size() + " bookings in My Pages.");

        if (bookings.isEmpty()) {
            bookingListView.getItems().add("No bookings found.");
        } else {
            for (String booking : bookings) {
                bookingListView.getItems().add(booking);
                System.out.println("Added to ListView: " + booking);
            }
        }
    }


    @FXML
    public void refreshBookings() {
        System.out.println("Refreshing bookings...");
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
