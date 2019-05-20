import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Application {
    private final static String SERVER_ADDRESS = "127.0.0.1";
    private final static int PORT = 8100;
    private Scene loginScene;
    private Scene mainScene;
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initLogin();
        initMain();
        window = primaryStage;
        window.setTitle("GameStore");
        window.setScene(loginScene);
        window.show();

    }

    private void initLogin() {
        Button button;
        TextField username;
        PasswordField password;
        Label errorLabel = new Label();
        username = new TextField();
        username.setMaxSize(150,20);
        password = new PasswordField();
        password.setMaxSize(150,20);
        button = new Button("Submit");
        button.setOnAction(event -> {
            String respone = "";
            if (!(username.getText().equals("") && password.getText().equals("")))
                respone = sendRequestToServer("find " + username.getText() + ' ' + password.getText());
            if (respone.equals("true")) {
                window.setScene(mainScene);
            } else
                errorLabel.setText("Invalid User or Password.");
        });
        VBox loginLayout = new VBox(20);
        loginLayout.getChildren().add(new Label("Username"));
        loginLayout.getChildren().add(username);
        loginLayout.getChildren().add(new Label("Password"));
        loginLayout.getChildren().add(password);
        loginLayout.getChildren().add(button);
        loginLayout.getChildren().add(errorLabel);
        loginLayout.setAlignment(Pos.CENTER);
        loginScene = new Scene(loginLayout, 400, 360);
    }

    private void initMain() {
        Button button = new Button("Log Out");
        button.setOnAction(event -> {
            window.setScene(loginScene);
        });
        VBox mainLayout = new VBox();
        mainLayout.getChildren().add(button);
        mainLayout.setAlignment(Pos.CENTER);
        mainScene = new Scene(mainLayout, 800, 600);
    }




    private String sendRequestToServer(String request) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            String response = in.readLine();
            return response;
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
            return "Error";
        } catch (IOException e) {
            System.out.println("IO Exception");
            return "Error";
        }

    }
}