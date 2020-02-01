import Controller.Game;
import Controller.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {

    private Player player;
    private Socket client;
    private int id;

    public ServerThread( Player p ) {
        super();
        player = p;
        this.client = player.getSocket();
        this.id = player.getId();
    }

    private void checkPlayerNumbers() throws InterruptedException {
        Game game = Server.getGame();

        if( game.getP1() == null ) {
            game.setP1(Server.getPlayerQueue().remove());
        }
        else if(game.getP2() == null ){
            game.setP2(Server.getPlayerQueue().remove());
        }

        // game.isStarted() -> checks if there is on going game.
        // others are if the players are ready
        while( (game.isGameStarted() && game.getP1()!=player ) || game.getP2()==null ) {
            System.out.println(game.getP2());
            Thread.sleep(1000);
        }

        game.startGame();

        Player p1 = game.getP1();
        Player p2 = game.getP2();

        int turn = (player == p1 ? 0 : 1);

        try {
            playGame(turn);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playGame(int turn ) throws IOException {

        char ch = (turn == 0 ? 'X' : 'O');
        System.out.println(ch + " -- " + turn);

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        String s;

        while ( !Server.getGame().hasGameEnded() ) {

            if( Server.getGame().getTurn() == turn ) {
                out.println("Your turn");
                s = in.readLine();
                int k = Integer.parseInt(s);
                Server.getGame().makeMove(k, ch);
            }
            else {
                out.println("Opponents turn");
                while (Server.getGame().getTurn()!= turn) {
                    System.out.println(Server.getGame().getTurn());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            out.println(Server.getGame().getBoard());

        }

        int winner = Server.getGame().checkWin();
        Server.getGame().finishGame();
        String result;
        if( winner == 0 ){
            result = "TIED";
        }
        else if( winner == 1 ){
            if( turn == 0 )
                result = "YOU WON";
            else
                result = "YOU LOST";
        }
        else {
            if( turn == 1 )
                result = "YOU WON";
            else
                result = "YOU LOST";
        }

        out.println(result);
    }

    public String listToString(List<Player> list ){
        String s = "";
        for (Player p : list)
            s = s + p.getId() + " " ;

        return s;
    }

    public void addGamePlayer(Player p) {
        Server.getPlayerQueue().add(p);
        try {
            checkPlayerNumbers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //System.out.println("!!!!!");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            String line;

            while ( true ) {
                line = in.readLine();
                System.out.println("RECEIVED : " + line);

                if( line.equals("LIST") ) {
                    String s = listToString(Server.getPlayers());
                    System.out.println(s);
                    out.println(s);
                }
                else if( line.equals("PLAY") ) {
                    addGamePlayer( player );
                    System.out.println("Looking for an opponent");
                }
                else if( line.equals("LOGOUT") ) {
                    Server.deletePlayer(player);
                    System.out.println("Logged out");
                    break;
                }
                else {
                    System.out.println("Unknown Command!!");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
