package vidmot;

import bakendi.BookingManager;
import bakendi.Tour;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class BookingDialogController {

    @FXML
    private Label tourNameLabel;
    @FXML
    private Spinner<Integer> peopleSpinner;
    @FXML
    private Label priceLabel;

    private Tour selectedTour;

    public void setTour(Tour tour) {
        this.selectedTour = tour;
        tourNameLabel.setText("Bókun: " + tour.getName());

        peopleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        peopleSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updatePrice());

        updatePrice();
    }

    @FXML
    private void updatePrice() {
        if (selectedTour == null) return;
        int people = peopleSpinner.getValue();
        double totalPrice = selectedTour.getPrice() * people;
        priceLabel.setText("Heildarverð: " + totalPrice + " ISK");
    }

    @FXML
    private void confirmBooking() {
        if (selectedTour == null) return;

        int people = peopleSpinner.getValue();
        // Það þarf að útvega betri "payment" valmöguleika
        System.out.println("Payment processed for " + people + " people.");
        BookingManager.addBooking(selectedTour);
        System.out.println("Booking confirmed for: " + selectedTour.getName() + ".");

        closeDialog();
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) tourNameLabel.getScene().getWindow();
        stage.close();
    }
}
