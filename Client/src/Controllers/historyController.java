package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class historyController {
    //All
    private String name;
    @FXML
    Label username;
    @FXML
    Label funds;
    //history
    @FXML
    ScrollPane historyPanel;

    public historyController() {

    }

    public void logoutButton(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/login.fxml"))));
    }

    public void shopButton(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/shop.fxml"));
        Parent root = loader.load();
        shopController shopController = loader.getController();
        shopController.initializareShop(name);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void libraryButton(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/library.fxml"));
        Parent root = loader.load();
        libraryController libraryController = loader.getController();
        libraryController.initializareLibrary(name);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void historyButton(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/history.fxml"));
        Parent root = loader.load();
        historyController historyController = loader.getController();
        historyController.initializareHistory(name);
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }

    public void addFundsButton(ActionEvent e) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/addFunds.fxml"));
        Parent root = loader.load();
        AddFundsController addFundsController = loader.getController();
        addFundsController.initializareAddFunds();
        addFundsController.name = this.name;
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event ->
                this.funds.setText("Funds:" + getFunds())
        );
        stage.show();

    }


    void initializareHistory(String name) {
        this.name = name;
        this.funds.setText("Funds:" + getFunds());
        this.username.setText("User :" + name);
        String request = ServerController.sendRequestToServer( "showHistory "+this.name);
        String[] info = request.split("\\|");
        VBox panel = new VBox();
        HBox head = new HBox();
        Label numeHead = new Label("Nume Joc"); numeHead.setPrefSize(150,50);
        Label dataHead = new Label("Data Cumpararii Jocului"); dataHead.setPrefSize(200,50);
        head.getChildren().addAll(numeHead,dataHead);
        panel.getChildren().add(head);
        for(int i=0;i<info.length;i+=2){
            HBox line = new HBox();
            Label nume = new Label(info[i]); nume.setPrefSize(150,50);
            Label data = new Label(info[i+1]);data.setPrefSize(150,50);
            line.getChildren().addAll(nume,data);
            panel.getChildren().add(line);
        }
        this.historyPanel.setContent(panel);
    }



    private String getFunds() {
        return ServerController.sendRequestToServer("funds " + name);

    }

}
