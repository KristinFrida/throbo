package bakendi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import vidmot.HelloController;
import vidmot.View;
import vidmot.ViewSwitcher;

public class UserController {

    // From Login-view
    @FXML
    private TextField fxUsername;
    @FXML
    private TextField fxPassword;

    // From Sign-in-view
    @FXML
    private TextField newUsername;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField newEmail;
    @FXML
    private Label MissingInputDataForNewUser;

    // LOGIN
    @FXML
    private void onLogin(ActionEvent event) {
        String username = fxUsername.getText();
        String password = fxPassword.getText();

        if (UserRepository.validateLogin(username, password)) {
            System.out.println("User " + username + " logged in.");
            ViewSwitcher.switchTo(View.START);
            sendUsernameToHelloController();
        } else {
            showAlert("Login failed", "Incorrect username or password. Try again.");
        }
    }

    @FXML
    private void goToHome(ActionEvent event) {
        ViewSwitcher.switchTo(View.START);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void sendUsernameToHelloController() {
        HelloController helloController = (HelloController) ViewSwitcher.lookup(View.START);

        if (helloController != null) {
            String text = fxUsername.getText();
            String upperCaseText = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
            helloController.updateLabel(upperCaseText);
            helloController.refreshLoginState();
        } else {
            System.err.println("HelloController not found when trying to send username.");
        }
    }

    public void clearLoginFields() {
        if (fxUsername != null) fxUsername.clear();
        if (fxPassword != null) fxPassword.clear();
    }

    // SIGN-UP
    public void newSignUp(ActionEvent event) {
        String username = newUsername.getText();
        String password = newPassword.getText();
        String email = newEmail.getText();

        // Clear previous error
        MissingInputDataForNewUser.setVisible(false);
        MissingInputDataForNewUser.setText("");

        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            showError("Please fill in all fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email format (e.g., name@example.com)");
            return;
        }

        boolean success = UserRepository.addUser(username, email, password);
        if (success) {
            showAlert("Success", "Account created!");
        } else {
            showError("Username or email already exists");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    @FXML
    private void validateEmail() {
        String email = newEmail.getText().trim();
        if (!isValidEmail(email)) {
            showError("Invalid email format (e.g., name@example.com)");
        } else {
            MissingInputDataForNewUser.setVisible(false);
        }
    }

    private void showError(String message) {
        MissingInputDataForNewUser.setText(message);
        MissingInputDataForNewUser.setVisible(true);
    }

    // NAVIGATION
    public void goToSignUp(ActionEvent event) {
        ViewSwitcher.switchTo(View.SIGNUP);
    }

    public void goToLoginFromSignUP(ActionEvent event) {
        MissingInputDataForNewUser.setText("");
        MissingInputDataForNewUser.setVisible(false);
        if (newUsername != null) newUsername.clear();
        if (newPassword != null) newPassword.clear();
        if (newEmail != null) newEmail.clear();
        ViewSwitcher.switchTo(View.LOGIN);
    }
    @FXML
    private void initialize() {
        if (newEmail != null) {
            newEmail.textProperty().addListener((obs, oldVal, newVal) -> MissingInputDataForNewUser.setVisible(false));
        }
        if (newUsername != null) {
            newUsername.textProperty().addListener((obs, oldVal, newVal) -> MissingInputDataForNewUser.setVisible(false));
        }
        if (newPassword != null) {
            newPassword.textProperty().addListener((obs, oldVal, newVal) -> MissingInputDataForNewUser.setVisible(false));
        }
    }
}
