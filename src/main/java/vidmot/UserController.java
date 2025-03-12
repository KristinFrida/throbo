package vidmot;

import bakendi.UserRespository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    private void skraSigInn(ActionEvent event){

        String username = fxUsername.getText();
        String password = fxPassword.getText();

        if(UserRespository.validateLogin(username,password)){
            ViewSwitcher.switchTo(View.START);
            sendUsernameToHelloController();
        }else{
            showAlert("Login failed", "Incorrect username or password try again");
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

        if(helloController!=null){
            String text = fxUsername.getText();
            //Ekki viss hvort á að gera lowercase restinn því sumir heit t.d. McDonald
            String upperCaseText = text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
            helloController.updateLabel(upperCaseText);
        }else {
            System.err.println(" ");
        }
    }


    // verið að ná í upplýsingar frá nýskráðum og setja það yfir í töflu fyrir gagnagrunninn
    public void newSignUp(ActionEvent event) {
        String username = newUsername.getText();
        String password = newPassword.getText();
        String email = newEmail.getText();

        if (newUsername.getText().isBlank() || newPassword.getText().isBlank() || newEmail.getText().isBlank()){
            MissingInputDataForNewUser.setText("Missing input data");
        }

        boolean sucess = UserRespository.addUser(username,email,password);
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
