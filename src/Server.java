import Controller.Game;
import Controller.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Server {

    private ServerSocket serverSocket;
    private static List<Player> players;
    private static Queue<Player> playerQueue;
    private static int counter = 0;
    private static Game game = new Game();

    public Server(int portNum){
        players = new ArrayList<>();
        playerQueue = new LinkedList<>();
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenClients() {
        System.out.println("Server started to listen clients...");
        while (true) {
            try {
                Socket client = serverSocket.accept();
                Player p = new Player(client, ++counter);
                System.out.println("Connection received " + p.getId());
                players.add(p);
                (new ServerThread(p) ).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static void deletePlayer( Player p ) {
        players.remove(p);
    }

    public static Game getGame() {
        return game;
    }

    public static Queue<Player> getPlayerQueue() {
        return playerQueue;
    }

    public static void main(String[] args) {

        if( args.length < 1 ) {
            System.out.println("Insufficient number of parameter. Port num is missing");
            return;
        }

        int portNum = Integer.parseInt(args[0]);

        try{
            Server server = new Server(portNum);
            System.out.println("Server started to listen at port: " + server.serverSocket.getLocalPort() );
            server.listenClients();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }

    }

}
