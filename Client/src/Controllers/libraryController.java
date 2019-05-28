package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class libraryController {
    //All
    private String name;
    @FXML
    Label username;
    @FXML
    Label funds;
    //library
    @FXML
    ScrollPane games;
    @FXML
    Label gameName;
    @FXML
    Label gameRating;
    @FXML
    Label gameCategories;
    @FXML
    ScrollPane gameComents;
    @FXML
    ScrollPane gameRecomandations;



    public libraryController() {

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

    void initializareLibrary(String name) {
        this.name = name;
        this.funds.setText("Funds:" + getFunds());
        this.username.setText("User :" + name);
        String game = ServerController.sendRequestToServer("lib_games " + name);
        String[] games = game.split(" ");
        VBox pane = new VBox();
        for (String gm : games) {
            if (!gm.equals("")) {
                String gameName = ServerController.sendRequestToServer("game_name " + gm);
                String[] gameNameParts = gameName.split("/");
                Button gmButton = new Button(gameNameParts[0]);
                gmButton.setOnAction(event -> {
                    this.gameName.setText(gameNameParts[0]);
                    this.gameRating.setText(gameNameParts[3]);
                    String cat = "";
                    for(int i=4;i<gameNameParts.length;i++){
                        cat=cat + gameNameParts[i] ;
                        if(i<gameNameParts.length-1) cat = cat + ',';
                    }
                    this.gameCategories.setText(cat);

                    this.gameComents.setContent(getComments(gm));
                    this.gameRecomandations.setContent(getRecomandations(gameNameParts[0]));
                });
                pane.getChildren().add(gmButton);
            }
        }
        this.games.setContent(pane);
        String gameName = ServerController.sendRequestToServer("game_name "+games[1]);
        String[] gameNameParts = gameName.split("/");
        this.gameName.setText(gameNameParts[0]);
        this.gameRating.setText(gameNameParts[3]);
        String cat = "";
        for(int i=4;i<gameNameParts.length;i++){
            cat=cat + gameNameParts[i] ;
            if(i<gameNameParts.length-1) cat = cat + ',';
        }
        this.gameComents.setContent(getComments(games[1]));
        this.gameRecomandations.setContent(getRecomandations(gameNameParts[0]));
        this.gameCategories.setText(cat);
    }



    private String getFunds() {
        return ServerController.sendRequestToServer("funds " + name);

    }
    private VBox getRecomandations(String gameName){
        String gameRec = ServerController.sendRequestToServer("gamesByName "+gameName);
        String[] gameRecSplit = gameRec.split("\\|");
        VBox rec = new VBox();
        for (String gameSec:gameRecSplit)
        {
            if(!gameSec.equals("")) {
                String gameName2 = ServerController.sendRequestToServer("game_name " + gameSec);
                rec.getChildren().add(new Label(gameName2.split("/")[0]));
            }
        }
        return rec;
    }
    private VBox getComments(String gm){
        String comments = ServerController.sendRequestToServer("game_comments " + gm);
        String[] commentsParts = comments.split("\\|");
        String showComments = "";
        int i=0;
        VBox commentsPane = new VBox();
        while(i<commentsParts.length)
        {
            commentsPane.getChildren().add(new Label("Username: " + commentsParts[i] ));
            i++;
            commentsPane.getChildren().add(new Label("Stars: " + commentsParts[i] ));
            i++;
        }
        return commentsPane;
    }
}
