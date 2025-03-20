package vidmot;

import bakendi.Booking;
import javafx.fxml.FXML;
import bakendi.BookingManager;
import bakendi.UserRepository;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class MyPageController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> tourNameColumn;
    @FXML private TableColumn<Booking, Integer> peopleColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> pickupColumn;
    @FXML private TableColumn<Booking, Button> cancelColumn;

    @FXML
    private void initialize() {
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
        BookingManager.removeBooking(booking.getId());
        loadBookings();  // Refresh table after canceling
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
