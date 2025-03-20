package vidmot;

import bakendi.Booking;
import javafx.fxml.FXML;
import bakendi.BookingManager;
import bakendi.UserRepository;
import java.util.List;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyPageController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> tourNameColumn;
    @FXML private TableColumn<Booking, Integer> peopleColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> pickupColumn;
    @FXML private TableColumn<Booking, Button> cancelColumn;
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;

    @FXML
    private void initialize() {
        String userName = UserRepository.getLoggedInUser();
        String userEmail = UserRepository.getLoggedInUserEmail();

        userNameLabel.setText(userName != null ? userName : "Guest");
        userEmailLabel.setText(userEmail != null ? userEmail : "No Email");
        tourNameColumn.setCellValueFactory(new PropertyValueFactory<>("tourName"));
        peopleColumn.setCellValueFactory(new PropertyValueFactory<>("people"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        pickupColumn.setCellValueFactory(new PropertyValueFactory<>("pickup"));

        cancelColumn.setCellFactory(col -> new TableCell<>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    cancelBooking(booking);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });

        loadBookings();  // Load bookings when the page opens

    }

    @FXML
    private void loadBookings() {
        bookingTable.getItems().clear();
        List<Booking> bookings = BookingManager.getBookingsForUser();

        for (Booking booking : bookings) {
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> cancelBooking(booking));

            bookingTable.getItems().add(booking);
        }
    }

    @FXML
    public void refreshBookings() {
        System.out.println("Refreshing bookings...");
        loadBookings();  // Reload bookings dynamically
    }
    private void cancelBooking(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText("Are you sure you want to cancel this booking?");
        alert.setContentText("Tour: " + booking.getTourName() + "\nDate: " + booking.getDate());

        // Valmöguleikar
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Birtir gluggann og vistar niðurstöðu
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                BookingManager.removeBooking(booking.getId());
                loadBookings(); // Endurnýja töflu
            }
        });
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
