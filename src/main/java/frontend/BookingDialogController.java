package frontend;

import backend.BookingManager;
import backend.Tour;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class BookingDialogController {

    @FXML private Label tourNameLabel;
    @FXML private Spinner<Integer> peopleSpinner;
    @FXML private Label priceLabel;
    @FXML private CheckBox hotelPickupCheckBox;
    @FXML private TextField cardHolderName;
    @FXML private TextField cardNumber;
    @FXML private TextField cardExpiry;
    @FXML private TextField ccv;
    @FXML private Label errorLabel;
    @FXML private DatePicker datePicker;

    private Tour selectedTour;
    private boolean submitted = false;

    public void setTour(Tour tour) {
        this.selectedTour = tour;
        tourNameLabel.setText("Booking: " + tour.getName());

        peopleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        peopleSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            updatePrice();
            if (submitted) validatePeopleLimit();
        });

        updatePrice();
        setupDatePicker();
        validatePeopleLimit();
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
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                getStyleClass().removeAll("past-date", "unavailable-date", "available-date");

                boolean isPast = date.isBefore(LocalDate.now());
                boolean isUnavailable = !availableDates.contains(date);
                boolean isFullyBooked = BookingManager.getTotalPeopleForTourOnDate(selectedTour.getName(), date) >= 20;

                if (isPast || isUnavailable || isFullyBooked) {
                    setDisable(true);
                    getStyleClass().add("unavailable-date");

                    if (isFullyBooked) {
                        setTooltip(new Tooltip("Fully booked"));
                    } else if (isUnavailable) {
                        setTooltip(new Tooltip("Not available on this day"));
                    }
                } else {
                    getStyleClass().add("available-date");
                    setTooltip(new Tooltip("Available"));
                }
            }
        });
    }

    @FXML
    private void confirmBooking() {
        submitted = true;

        if (!validatePaymentInfo()) return;

        int people = peopleSpinner.getValue();
        boolean hotelPickup = hotelPickupCheckBox.isSelected();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            showError("Please select a date for the booking.");
            datePicker.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return;
        } else {
            datePicker.setStyle("");
        }

        int alreadyBooked = BookingManager.getTotalPeopleForTourOnDate(selectedTour.getName(), selectedDate);
        int remainingSpots = 20 - alreadyBooked;

        if (people > remainingSpots) {
            showError("Not enough spots available on " + selectedDate + ". Only " + remainingSpots + " left.");
            return;
        }

        if (BookingManager.addBooking(selectedTour, people, selectedDate, hotelPickup)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your booking was successful!");
            alert.showAndWait();

            MyPageController myPageController = (MyPageController) ViewSwitcher.lookup(View.MYPAGE);
            if (myPageController != null) {
                myPageController.refreshPage();
            }

            HelloController helloController = (HelloController) ViewSwitcher.lookup(View.START);
            if (helloController != null) {
                helloController.refreshFilteredTours();
            }
        }
        closeDialog();
    }

    private boolean validatePaymentInfo() {
        boolean valid = true;

        clearInputStyles();

        String name = cardHolderName.getText();
        String cardNum = cardNumber.getText().replaceAll("\\s", "");
        String cvcCode = ccv.getText();

        if (name.isBlank() || name.length() < 2 || !name.matches("[a-zA-ZæöÆÖáéíóúýÁÉÍÓÚÝðÐþÞ\\- '\\s]+")) {
            if (submitted) {
                showError("Cardholder name must be at least 2 letters and contain only letters and spaces");
                cardHolderName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            valid = false;
        }

        if (!cardNum.matches("\\d{16}")) {
            if (submitted) {
                showError("Card number must be 16 digits");
                cardNumber.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            valid = false;
        }

        if (!isValidExpiryAndStyle()) {
            valid = false;
        }

        if (!cvcCode.matches("\\d{3}")) {
            if (submitted) {
                showError("CVC must be exactly 3 digits");
                ccv.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            valid = false;
        }

        return valid;
    }

    private boolean isValidExpiryAndStyle() {
        String expiryText = cardExpiry.getText();
        boolean valid = expiryText.matches("\\d{2}/\\d{2}") && isValidMonth(expiryText);

        if (submitted) {
            if (valid) {
                cardExpiry.setStyle("");
                errorLabel.setVisible(false);
            } else {
                cardExpiry.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                showError("Expiration must be valid MM/YY");
            }
        }

        return valid;
    }

    private boolean isValidMonth(String expiry) {
        try {
            int month = Integer.parseInt(expiry.substring(0, 2));
            return month >= 1 && month <= 12;
        } catch (Exception e) {
            return false;
        }
    }

    private void clearInputStyles() {
        cardHolderName.setStyle("");
        cardNumber.setStyle("");
        cardExpiry.setStyle("");
        ccv.setStyle("");
    }

    private void setupInputListeners() {
        cardHolderName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZæöÆÖáéíóúýÁÉÍÓÚÝðÐþÞ\\- '\\s]*")) {
                cardHolderName.setText(oldVal);
                return;
            }

            if (submitted) {
                if (newVal.isBlank() || newVal.length() < 2) {
                    cardHolderName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    showError("Name must be at least 2 characters");
                } else {
                    cardHolderName.setStyle("");
                    errorLabel.setVisible(false);
                }
            }
        });

        cardNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                cardNumber.setText(oldVal);
                return;
            }
            if (newVal.length() > 16) {
                cardNumber.setText(oldVal);
                return;
            }

            if (submitted) {
                if (newVal.length() == 16) {
                    cardNumber.setStyle("");
                    errorLabel.setVisible(false);
                } else {
                    cardNumber.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    showError("Card number must be 16 digits");
                }
            }
        });

        cardExpiry.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 4) newText = newText.substring(0, 4);

            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < newText.length(); i++) {
                if (i == 2) formatted.append('/');
                formatted.append(newText.charAt(i));
            }

            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());

            if (submitted) isValidExpiryAndStyle();
            return change;
        }));

        cardExpiry.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus && submitted) isValidExpiryAndStyle();
        });

        ccv.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                ccv.setText(oldVal);
                return;
            }

            if (newVal.length() > 3) {
                ccv.setText(oldVal);
                return;
            }

            if (submitted) {
                if (newVal.length() == 3) {
                    ccv.setStyle("");
                    errorLabel.setVisible(false);
                } else {
                    ccv.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    showError("CVC must be exactly 3 digits");
                }
            }
        });

        ccv.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus && submitted) {
                String text = ccv.getText();
                if (!text.matches("\\d{3}")) {
                    ccv.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    showError("CVC must be exactly 3 digits");
                }
            }
        });
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
        tourNameLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setMinWidth(500);
                    stage.setMinHeight(600);
                }
            }
        });
        setupInputListeners();
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                datePicker.setStyle("");
                if (submitted) validatePeopleLimit();
            }
        });
    }

    private void validatePeopleLimit() {
        if (selectedTour == null) return;

        LocalDate selectedDate = datePicker.getValue();
        Integer people = peopleSpinner.getValue();

        if (selectedDate == null || people == null) {
            errorLabel.setVisible(false);
            peopleSpinner.setStyle("");
            return;
        }

        int alreadyBooked = BookingManager.getTotalPeopleForTourOnDate(selectedTour.getName(), selectedDate);
        int remainingSpots = 20 - alreadyBooked;

        if (people > remainingSpots) {
            peopleSpinner.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showError("Only " + remainingSpots + " spots left for " + selectedDate + ".");
        } else {
            peopleSpinner.setStyle("");
            errorLabel.setVisible(false);
        }
    }
}
