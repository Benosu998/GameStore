package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class LoginController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    @FXML
    private Label errorMessage;

    public void login(ActionEvent e) throws Exception {
        if (username.getText().equals("") || password.getText().equals("")) {
            errorMessage.setText("Complete Each Row !");
        } else if (ServerController.sendRequestToServer("login " + username.getText() + " " + password.getText()).equals("true")) {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/library.fxml"));
            Parent root = loader.load();
            libraryController myController = loader.getController();
            myController.initializareLibrary(username.getText());
            Scene scene =new Scene(root);
            stage.setScene(scene);

        } else {
            errorMessage.setText("Wrong Username/Password");
        }
    }
    public void register(ActionEvent e) throws  Exception {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/register.fxml"))));
    }
}
