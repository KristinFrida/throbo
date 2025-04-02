package vidmot;

import bakendi.Booking;
import bakendi.BookingManager;
import bakendi.UserRepository;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class MyPageController {

    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;
    @FXML private VBox bookingsContainer;

    @FXML
    private void initialize() {
        userNameLabel.setText(UserRepository.getLoggedInUser());
        userEmailLabel.setText(UserRepository.getLoggedInUserEmail());
        loadBookings();
    }

    @FXML
    private void loadBookings() {
        bookingsContainer.getChildren().clear();
        List<Booking> bookings = BookingManager.getBookingsForUser();

        for (Booking booking : bookings) {
            HBox bookingItem = new HBox();
            bookingItem.getStyleClass().add("booking-item");
            bookingItem.setAlignment(Pos.CENTER_LEFT);
            bookingItem.setSpacing(15);

            // Titill (t.d. "Northern Lights")
            Label titleLabel = new Label(booking.getTourName());
            titleLabel.getStyleClass().add("booking-title");

            // Annað info
            String pickupText = booking.getPickup().equalsIgnoreCase("true") ? "Yes" : "No";
            Label infoLabel = new Label("Date: " + booking.getDate()
                    + "    People: " + booking.getPeople()
                    + "    Pickup: " + pickupText);
            infoLabel.getStyleClass().add("booking-label");

            VBox textBox = new VBox(titleLabel, infoLabel);
            textBox.setSpacing(4);

            // Cancel takki
            Button cancelButton = new Button("Cancel");
            cancelButton.getStyleClass().add("cancel-button");
            cancelButton.setOnAction(e -> cancelBooking(booking));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            bookingItem.getChildren().addAll(textBox, spacer, cancelButton);
            bookingsContainer.getChildren().add(bookingItem);
        }
    }


    private void cancelBooking(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText("Are you sure you want to cancel this booking?");
        alert.setContentText("Tour: " + booking.getTourName() + "\nDate: " + booking.getDate());

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(result -> {
            if (result == yes) {
                BookingManager.removeBooking(booking.getId());
                loadBookings();
            }
        });
    }

    @FXML
    private void goToHome() {
        ViewSwitcher.switchTo(View.START);
    }

    @FXML
    private void handleLogout() {
        UserRepository.logoutUser();
        ViewSwitcher.switchTo(View.START);
        HelloController helloController = (HelloController) ViewSwitcher.lookup(View.START);
        if (helloController != null) helloController.refreshLoginState();
    }

    public void refreshPage() {
        userNameLabel.setText(UserRepository.getLoggedInUser());
        userEmailLabel.setText(UserRepository.getLoggedInUserEmail());
        loadBookings();
    }
}
