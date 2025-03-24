package bakendi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vidmot.HelloController;
import vidmot.View;
import vidmot.ViewSwitcher;

public class UserController {


    //From Login-view
    @FXML
    private TextField fxUsername;
    @FXML
    private PasswordField fxPassword;

    //From Sign-in-view
    @FXML
    private TextField newUsername;
    @FXML
    private PasswordField newPassword;
    @FXML
    private TextField newEmail;
    @FXML
    private Label MissingInputDataForNewUser;


    @FXML
    private void onLogin(ActionEvent event){

        String username = fxUsername.getText();
        String password = fxPassword.getText();

        if(UserRepository.validateLogin(username, password)){  // Now correctly calls the fixed method
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

    //Skipta yfir í fxml skrá þar sem hægt er að búa til aðgang
    public void goToSignUp(ActionEvent event) {
        ViewSwitcher.switchTo(View.SIGNUP);
    }

    //Takki til að fara til baka án þess að skrá sig endilega inn, alveg eins method í helloController
    public void goToLoginFromSignUP(ActionEvent event) {
        ViewSwitcher.switchTo(View.LOGIN);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    //Er að senda username yfir á forsíðuna svo hægt sé að segja velkominn username
    @FXML
    private void sendUsernameToHelloController(){
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


    // verið að ná í upplýsingar frá nýskráðum og setja það yfir í töflu fyrir gagnagrunninn
    public void newSignUp(ActionEvent event) {
        String username = newUsername.getText();
        String password = newPassword.getText();
        String email = newEmail.getText();

        if(!validateInput(username,email,password)){
            return;
        }

        boolean sucess = UserRepository.addUser(username,email,password);
        if(sucess){
            showAlert("Success", "Account created!");

        }else {
            showAlert("Sign up failed", "Input missing || Username or email already exists");
        }
    }

    private boolean validateInput(String username, String email, String password){
        boolean isValid = true;
        String errorMessage ="";
        int errorCount = 0;

        if (username.isBlank()){
            if(errorMessage.isEmpty()) errorMessage = "Missing username input";
            setFieldInvalid(newUsername);
            errorCount++;
            isValid =false;
        }else {
            setFieldValid(newUsername);
        }

        if(password.isBlank()){
            setFieldInvalid(newPassword);
            errorCount++;
            if(errorMessage.isEmpty()) errorMessage = "Missing password input";
            isValid =false;
        } else if (password.length() < 4) {
            setFieldInvalid(newPassword);
            errorCount++;
            if(errorMessage.isEmpty()) errorMessage = "Your password must be longer then 3 characters";
            isValid = false;
        } else{
            setFieldValid(newPassword);
        }

        if(email.isBlank()){
            setFieldInvalid(newEmail);
            errorCount++;
            if(errorMessage.isEmpty()) errorMessage = "Missing email input";
            isValid=false;
        }else if (!email.contains("@")){
            setFieldInvalid(newEmail);
            errorCount++;
            if(errorMessage.isEmpty()) errorMessage ="Email must contain @";

            isValid =false;
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            setFieldInvalid(newEmail);
            errorCount++;
            if(errorMessage.isEmpty()) errorMessage ="Email must be in the format 'name@example.com'";
            isValid = false;
        }else{
            setFieldValid(newEmail);
        }
        MissingInputDataForNewUser.setText(errorMessage + " +" + errorCount + " more errors");
        return isValid;
    }

    private void setFieldInvalid(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void setFieldValid(TextField field) {
        field.setStyle("-fx-border-color: gray; ");
    }
}

