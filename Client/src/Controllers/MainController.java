package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;


public class MainController {

    @FXML
    Label username;
    @FXML
    Label funds;
    @FXML
    ToolBar games;
    @FXML
    TableView shopGames;
    public MainController(){

    }
    public void logout(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/login.fxml"))));
    }
    public void shop(ActionEvent e) throws  Exception{
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/shop.fxml"));
        Parent root = loader.load();
        MainController myController = loader.getController();
        Scene scene =new Scene(root);
        stage.setScene(scene);
    }


}
