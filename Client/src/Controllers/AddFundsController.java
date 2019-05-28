package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddFundsController {
    String name;
    @FXML
    ComboBox paymentMethod;

    @FXML
    ComboBox value;

    @FXML
    TextField card;

    @FXML
    Label message;
    public void addFundsButton(ActionEvent e){
        if(ServerController.sendRequestToServer("addFunds "+ value.getValue().toString() + " " + name +" "+ paymentMethod.getValue()).equals("true")){
//            ((Stage)((Node) e.getSource()).getScene().getWindow()).close();
            message.setText("Transaction Successful");

        }else
        {
            System.out.println("not good" );
        }
    }
    void initializareAddFunds(){
        this.paymentMethod.getItems().removeAll();
        this.paymentMethod.getItems().addAll("Visa","Paypal","MasterCard");
        this.value.getItems().addAll("5","10","20","50","100");
    }
}
