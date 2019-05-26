package Controllers;


import com.sun.security.ntlm.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    Label errorMessage;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    PasswordField password2;

    @FXML
    TextField email;

    @FXML
    TextField email2;


    public void login(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/login.fxml"))));
    }
    public  void register(ActionEvent e){
        if(!password.getText().equals(password2.getText()))
        {
            errorMessage.setText("Passwords doesn't match");
            return;
        }
        if(!email.getText().equals(email.getText())){
            errorMessage.setText("Emails doesn't match");
            return;
        }
        System.out.println();
        if(ServerController.sendRequestToServer("register " + username.getText() + " " + password.getText() + " " + email.getText()).equals("true")){
            System.out.println("inregistrat");
        }
        else
            System.out.println("date necorecte");
    }
}
