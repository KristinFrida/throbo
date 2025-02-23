package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class UserController {

    @FXML
    private TextField fxUsername;
    @FXML
    private TextField fxPassword;


    @FXML
    private void goToLogin(ActionEvent event){ViewSwitcher.switchTo(View.LOGIN);
    }

    @FXML
    private void skraSigInn(ActionEvent event){
        //ef notendanafn og lykilorð passa sama id??? þá fara yfir í hello-view.fxml annar gefa villu að þau pössuðu ekki
        //saman svo halda sé enn í

        String username = fxUsername.getText();
        String password = fxPassword.getText();

        if(validateLogin(username,password)){
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



    private boolean validateLogin(String username, String password) {
        return username.equals("admin") && password.equals("1234");
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
            System.err.println("");
        }
    }



}
