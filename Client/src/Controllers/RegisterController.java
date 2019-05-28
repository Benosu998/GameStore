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


    public void login(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/login.fxml"))));
    }

    public void register(ActionEvent e) throws Exception {
        if (!password.getText().equals(password2.getText())) {
            errorMessage.setText("Passwords doesn't match");
            return;
        }
        if (!email.getText().equals(email.getText())) {
            errorMessage.setText("Emails doesn't match");
            return;
        }
        if (!checkMail(email.getText())) {
            errorMessage.setText("Email Wrong Format!");
            return;
        }
        if (ServerController.sendRequestToServer("register " + username.getText() + " " + password.getText() + " " + email.getText()).equals("true")) {
            login(e);
        }

        System.out.println("date necorecte");
    }

    private boolean checkMail(String mail) {
        String[] tokens = mail.split("@");
        if (tokens.length != 2)
            return false;
        String[] tokens2 = tokens[1].split("\\.");
        if (tokens2.length < 2)
            return false;

        return true;

    }
}
