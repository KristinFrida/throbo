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


    //From Login-view
    @FXML
    private TextField fxUsername;
    @FXML
    private TextField fxPassword;

    //From Sign-in-view
    @FXML
    private TextField newUsername;
    @FXML
    private TextField newPassword;
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

    public void clearLoginFields() {
        if (fxUsername != null) fxUsername.clear();
        if (fxPassword != null) fxPassword.clear();
    }


    // verið að ná í upplýsingar frá nýskráðum og setja það yfir í töflu fyrir gagnagrunninn
    public void newSignUp(ActionEvent event) {
        String username = newUsername.getText();
        String password = newPassword.getText();
        String email = newEmail.getText();

        if (newUsername.getText().isBlank() || newPassword.getText().isBlank() || newEmail.getText().isBlank()){
            MissingInputDataForNewUser.setText("Missing input data");
        }

        boolean sucess = UserRepository.addUser(username,email,password);
        if(sucess){
            showAlert("Success", "Account created!");
        }else {
            showAlert("Sign up failed", "Input missing || Username or email already exists");
        }
    }

    //Skipta yfir í fxml skrá þar sem hægt er að búa til aðgang
    public void goToSignUp(ActionEvent event) {
        ViewSwitcher.switchTo(View.SIGNUP);
    }

    //Takki til að fara til baka án þess að skrá sig endilega inn, alveg eins method í helloController
    public void goToLoginFromSignUP(ActionEvent event) {
        ViewSwitcher.switchTo(View.LOGIN);
    }
}
