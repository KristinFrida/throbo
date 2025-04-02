package vidmot;

import bakendi.BookingManager;
import bakendi.Tour;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class BookingDialogController {

    @FXML
    private Label tourNameLabel;
    @FXML
    private Spinner<Integer> peopleSpinner;
    @FXML
    private Label priceLabel;
    @FXML
    private CheckBox hotelPickupCheckBox;
    @FXML
    private TextField cardHolderName;
    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cardExpiry;
    @FXML
    private TextField ccv;
    @FXML
    private Label errorLabel;
    @FXML
    private DatePicker datePicker;

    private Tour selectedTour;

    public void setTour(Tour tour) {
        this.selectedTour = tour;
        tourNameLabel.setText("Booking: " + tour.getName());

        peopleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        peopleSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updatePrice());

        updatePrice();
        setupDatePicker();
    }

    @FXML
    private void updatePrice() {
        if (selectedTour == null) return;
        int people = peopleSpinner.getValue();
        double totalPrice = selectedTour.getPrice() * people;
        priceLabel.setText("Total Price: " + totalPrice + " ISK");
    }

    private void setupDatePicker() {
        List<LocalDate> availableDates = selectedTour.getAvailableDates();

        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(!availableDates.contains(date) || date.isBefore(LocalDate.now()));
            }
        });
    }

    @FXML
    private void validateCardNumber() {
        String cardNum = cardNumber.getText().replaceAll("\\s", "");

        if (!cardNum.matches("\\d{16}")) {
            showError("Card number must be 16 digits");
        } else {
            errorLabel.setVisible(false);
        }
    }

    @FXML
    private void confirmBooking() {
        if (!validatePaymentInfo()) {
            return;
        }

        int people = peopleSpinner.getValue();
        boolean hotelPickup = hotelPickupCheckBox.isSelected();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            showError("Please select a date for the booking.");
            return;
        }

        System.out.println("Booking confirmed for: " + selectedTour.getName());
        System.out.println("People: " + people);
        System.out.println("Date: " + selectedDate);
        System.out.println("Hotel Pickup: " + (hotelPickup ? "Yes" : "No"));

        if (BookingManager.addBooking(selectedTour, people, selectedDate, hotelPickup)) {
            System.out.println("Booking successful for: " + selectedTour.getName());

            // ✅ Show alert here
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your booking was successful!");
            alert.showAndWait();

            MyPageController myPageController = (MyPageController) ViewSwitcher.lookup(View.MYPAGE);
            if (myPageController != null) {
                myPageController.refreshPage();
                System.out.println("My Pages updated after new booking");
            } else {
                System.out.println("My Pages not found, Page will update when reopened");
            }
        }


        closeDialog();
    }

    private boolean validatePaymentInfo() {
        String cardNum = cardNumber.getText().replaceAll("\\s", "");
        String expiry = cardExpiry.getText();
        String cvcCode = ccv.getText();

        if (cardHolderName.getText().isBlank()) {
            showError("Cardholder name cannot be empty");
            return false;
        }

        if (!cardNum.matches("\\d{16}")) {
            showError("Card number must be 16 digits");
            return false;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showError("Expiration date must be in MM/YY format");
            return false;
        }

        if (!cvcCode.matches("\\d{3}")) {
            showError("CVC must be 3 digits");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void closeDialog() {
        ((Stage) tourNameLabel.getScene().getWindow()).close();
    }

    @FXML
    private void initialize() {
        // Gáir hvort það sé hægt að fá aðgang að window áður en við breytum stærð
        tourNameLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setMinWidth(500);
                    stage.setMinHeight(600);
                }
            }
        });
    }

}
