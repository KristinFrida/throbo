package bakendi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import vidmot.HelloController;
import vidmot.View;
import vidmot.ViewSwitcher;

public class UserController {

    // From Login-view
    @FXML private TextField fxUsername;
    @FXML private PasswordField fxPassword;

    // From Sign-in-view
    @FXML private TextField newUsername;
    @FXML private PasswordField newPassword;
    @FXML private TextField newEmail;
    @FXML private Label MissingInputDataForNewUser;

    // LOGIN
    @FXML
    private void onLogin(ActionEvent event) {
        String username = fxUsername.getText();
        String password = fxPassword.getText();

        if (UserRepository.validateLogin(username, password)) {
            System.out.println("User " + username + " logged in.");
            clearLoginFields();
            ViewSwitcher.switchTo(View.START);
            sendUsernameToHelloController();
        } else {
            showAlert("Login failed", "Incorrect username or password. Try again.");
        }
    }

    @FXML
    private void goToHome(ActionEvent event) { ViewSwitcher.switchTo(View.START); }

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

            if(text != null && !text.isBlank()) {
                String upperCaseText = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
                helloController.updateLabel(upperCaseText);
            }else{
                helloController.clearLabel();
            }

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

        if(!validateInput(username, email, password)){
            return;
        }

        boolean success = UserRepository.addUser(username, email, password);
        if (success) {
            showAlert("Success", "Account created!");
            ViewSwitcher.switchTo(View.LOGIN);
        } else {
            showError("Username or email already exists");
        }
    }

    private boolean validateInput(String username, String email, String password) {
        boolean isValid = true;
        int errorCount = 0;
        String errorMessage = "";

        // === Username ===
        if (username.isBlank()) {
            setFieldInvalid(newUsername);
            errorCount++;
            if (errorMessage.isEmpty()) errorMessage = "Missing username input";
            isValid = false;
        } else {
            setFieldValid(newUsername);
        }

        // === Password ===
        if (password.isBlank()) {
            setFieldInvalid(newPassword);
            errorCount++;
            if (errorMessage.isEmpty()) errorMessage = "Missing password input";
            isValid = false;
        } else if (password.length() < 4) {
            setFieldInvalid(newPassword);
            errorCount++;
            if (errorMessage.isEmpty()) errorMessage = "Password must be longer than 3 characters";
            isValid = false;
        } else {
            setFieldValid(newPassword);
        }

        // === Email ===
        if (email.isBlank()) {
            setFieldInvalid(newEmail);
            errorCount++;
            if (errorMessage.isEmpty()) errorMessage = "Missing email input";
            isValid = false;
        } else if (!email.contains("@")) {
            setFieldInvalid(newEmail);
            errorCount++;
            if (errorMessage.isEmpty()) errorMessage = "Email must contain @";
            isValid = false;
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            setFieldInvalid(newEmail);
            errorCount++;
            if (errorMessage.isEmpty()) errorMessage = "Email must be in format 'name@example.com'";
            isValid = false;
        } else {
            setFieldValid(newEmail);
        }

        if (!isValid) {
            String moreErrors = (errorCount > 1) ? " (+ " + (errorCount - 1) + " more error(s))" : "";
            MissingInputDataForNewUser.setText(errorMessage + moreErrors);
            MissingInputDataForNewUser.setVisible(true);
        } else {
            MissingInputDataForNewUser.setText("");
            MissingInputDataForNewUser.setVisible(false);
        }
        return isValid;
    }

    private void setFieldValid(TextField field) {
        field.setStyle("-fx-border-color: gray;");
    }

    private void setFieldInvalid(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
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
        //hægt er að ýta á enter til að logga sig inn

        if (fxUsername != null) {
            fxUsername.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    onLogin(null);
                }
            });
        }

        if (fxPassword != null) {
            fxPassword.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    onLogin(null);
                }
            });
        }


        //hægt er að ýta á enter til að nýskrá sig
        if (newUsername != null) {
            newUsername.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    newSignUp(null);
                }
            });
        }

        if (newPassword != null) {
            newPassword.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    newSignUp(null);
                }
            });
        }

        if (newEmail != null) {
            newEmail.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    newSignUp(null);
                }
            });
        }
    }
}

