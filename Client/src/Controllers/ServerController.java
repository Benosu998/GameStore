package Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerController {
    private final static String SERVER_ADDRESS = "127.0.0.1";
    private final static int PORT = 8100;

    public static String sendRequestToServer(String request) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            return in.readLine();
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
            return "Error";
        } catch (IOException e) {
            System.out.println("IO Exception");
            return "Error";
        }

    }
}
