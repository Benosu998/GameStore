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


public class shopController {
    //All
    private String name;
    @FXML
    Label username;
    @FXML
    Label funds;
    //Shop
    @FXML
    ScrollPane scrollView;
    @FXML
    ScrollPane gamesPanel;
    @FXML
    TextField searchBox;
    public shopController() {

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

     void initializareShop(String name) {
        this.name = name;
        this.funds.setText("Funds:" + getFunds());
        this.username.setText("User :" + name);
    }

    private String getFunds() {
        return ServerController.sendRequestToServer("funds " + name);

    }
    public void categoriesButton(ActionEvent e){
        String response = ServerController.sendRequestToServer("categories ");
        String[] categories = response.split("/");
        VBox categoriesBox = new VBox();
        this.gamesPanel.setContent(null);
        for (String category : categories)
        {
            if(!category.equals("")){
                Button categoryButton = new Button(category);
                categoryButton.setOnAction(event ->{
                    VBox panel = new VBox();
                    String request = ServerController.sendRequestToServer("gamesByCategory " + category);
                    String[] gamesByCategory = request.split("\\|");
                    for( String game : gamesByCategory){
                        if(game.equals("null")){
                            panel.getChildren().add(new Label("Categoria aceasta nu are jocuri momentan"));
                        }
                        else{
                            HBox line = new HBox();
                            Label gameTitle = new Label(game);
                            gameTitle.setPrefSize(200,50);
                            String price =ServerController.sendRequestToServer("getPrice "+ game);
                            if (price.equals("false"))
                                price = "0";
                            Label gamePrice = new Label("Price:"+price);
                            gamePrice.setPrefSize(200,50);
                            Button gamePurchace = new Button("Purchase");
                            gamePurchace.setPrefSize(200,50);
                            line.getChildren().addAll(gameTitle,gamePrice,gamePurchace);
                            panel.getChildren().add(line);

                        }

                    }
                    this.gamesPanel.setContent(panel);
                });
                categoriesBox.getChildren().add(categoryButton);
            }
        }
        this.scrollView.setContent(categoriesBox);
    }
    public void getGameFranchises(ActionEvent e){
        this.gamesPanel.setContent(null);
        String response = ServerController.sendRequestToServer("gamesFranchises ");
        String[] franchises =response.split("\\|");
        VBox franchisesPanel = new VBox();
        for(String franchise : franchises){
            Button franchiseButton = new Button (franchise);
            franchiseButton.setOnAction(event -> {
                VBox gameBox = new VBox();
                String getGames = ServerController.sendRequestToServer("gamesByName " + franchise);
                String[] gamesList = getGames.split("\\|");

                for(String game : gamesList){
                    HBox linie = new HBox();
                    Label gameName = new Label(ServerController.sendRequestToServer("game_name "+game).split("/")[0]);
                    gameName.setPrefSize(200,50);
                    linie.getChildren().add(gameName);
                    gameBox.getChildren().add(linie);
                }
                this.gamesPanel.setContent(gameBox);

            });
            franchisesPanel.getChildren().add(franchiseButton);
        }
        this.scrollView.setContent(franchisesPanel);
    }
    public void highRated(ActionEvent e){
        this.scrollView.setContent(null);
        String response = ServerController.sendRequestToServer("highRated ");
        String[] highRatedGames = response.split("\\|");
        VBox gamesPanel = new VBox();
        for(String game : highRatedGames){
            Label gameTitle = new Label(game);gameTitle.setPrefSize(300,50);
            gamesPanel.getChildren().add(gameTitle);
        }
        this.gamesPanel.setContent(gamesPanel);
    }
    public void mostBought(ActionEvent e){
        this.scrollView.setContent(null);
        String response = ServerController.sendRequestToServer("getMost ");
        String[] gamesList = response.split("\\|");
        VBox gamesPanel = new VBox();
        for(String game : gamesList){
            Label gameTitle = new Label(game); gameTitle.setPrefSize(300,50);
            gamesPanel.getChildren().add(gameTitle);
        }
        this.gamesPanel.setContent(gamesPanel);
    }
    public void onSale(ActionEvent e){
        this.scrollView.setContent(null);
        String response = ServerController.sendRequestToServer("getSales ");
        String[] gamesList = response.split("\\|");
        VBox gamesPanel = new VBox();
        for(String game : gamesList){
            Label gameTitle = new Label(game); gameTitle.setPrefSize(300,50);
            gamesPanel.getChildren().add(gameTitle);
        }
        this.gamesPanel.setContent(gamesPanel);
    }
    public void search(ActionEvent e){
        this.scrollView.setContent(null);
        String response = ServerController.sendRequestToServer("gamesByName " + this.searchBox.getText());
        String[] gamesList = response.split("\\|");
        VBox gamesPanel = new VBox();
        for(String game : gamesList){
            Label gameTitle = new Label(ServerController.sendRequestToServer("game_name "+game).split("/")[0]);
            gameTitle.setPrefSize(300,50);
            gamesPanel.getChildren().add(gameTitle);
        }
        this.gamesPanel.setContent(gamesPanel);
    }
}
