import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread extends Thread {
    private Socket socket = null;
    private final Server server;
    private BasicController controller = new BasicController();

    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        boolean running = true;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //client -> server stream
            PrintWriter out = new PrintWriter(socket.getOutputStream()); //server -> client stream
            while (running) {
                String request = in.readLine();
                String response = null;
                if (request != null)
                    response = execute(request);
                out.println(response);
                out.flush();

            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private String execute(String request) {
        String[] parts = request.split(" ");
        System.out.println(request);
        Boolean response = false;
        try {
            switch (parts[0]) {
                case "login":
                    response = controller.checkClient(parts[1], parts[2]);
                    break;
                case "register":
                    response = controller.register(parts[1], parts[2], parts[3]);
                    break;
                case "addFunds":
                    response = controller.addFunds(Integer.parseInt(parts[1]), parts[2], parts[3]);
                    break;
                case "funds":
                    return controller.getFunds(parts[1]);
                case "lib_games":
                    return controller.getGames(parts[1]);
                case "game_name":
                    return controller.getGameName(Integer.parseInt(parts[1]));
                case "game_comments":
                    return controller.getGameComments(Integer.parseInt(parts[1]));
                case "gamesByName":
                    String name = parts[1];
                    for(int i=2;i<parts.length;i++)
                    {
                        name = name + ' ' + parts[i];
                    }
                    return controller.getGamesByName(name);
                case "categories":
                    return controller.getCategories();
                case "gamesByCategory":
                    return controller.getGamesByCategory(parts[1]);
                case "gamesFranchises":
                    return controller.getGameFranchises();
                case "highRated":
                    return controller.getHighRated();
                case "showHistory":
                    return controller.showHistory(parts[1]);
                case "getMost":
                    return controller.getMost();
                case "getSales":
                    return controller.getSales();
                case "getPrice":
                    name = parts[1];
                    for(int i=2;i<parts.length;i++)
                    {
                        name = name + ' ' + parts[i];
                    }
                    System.out.println(name);
                    return controller.getPrice(name);
                default:
                    response = false;
                    System.out.println("Comanda indisponibila." + parts[0]);
                    break;

            }
        } catch (SQLException e) {
            System.out.println(e);
           return response.toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            return response.toString();
        }
        return response.toString();
    }
}
