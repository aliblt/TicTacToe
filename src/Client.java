import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void printMap(String s) {

        for( int i=0 ; i<9 ; i++ ){
            if( s.charAt(i) == '\u0000' )
                System.out.print( "| |");
            else
                System.out.print("|" + s.charAt(i) + "|");
            if(i%3 == 2)
                System.out.println();
        }
    }

    private static void playGame(Socket socket) {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s;
        Scanner keyboard = new Scanner(System.in);

        while ( true ) {
            try {
                s = in.readLine();
                System.out.println(s);

                if(s.equals("TIED") || s.equals("YOU WON") || s.equals("YOU LOST")  )
                    break;

                if( s.equals("Your turn")) {
                    System.out.println("Please enter number 0-9");
                    int k = keyboard.nextInt();
                    out.println(k);
                }

                s = in.readLine();
                printMap(s);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String args[]) {

        if( args.length < 2 ) {
            System.out.println("Insufficient number of parameter. Port num is missing");
            return;
        }

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host");
            System.exit(-1);
        }
        catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(-1);
        }

        Scanner keyboard = new Scanner(System.in);
        while (true ) {

            String userInput = keyboard.nextLine();
            String response;


            if( userInput.equals("LIST") ) {
                System.out.println("LIST Request sent");
                out.println(userInput);
                try {
                    response = in.readLine();
                    System.out.println("USERS: " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if( userInput.equals("PLAY") ) {
                System.out.println("PLAY request sent");
                out.println(userInput);
                playGame(socket);
            }
            else if( userInput.equals("LOGOUT") ) {
                System.out.println("LOGOUT request sent");
                out.println(userInput);
                break;
            }
        }

        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("Cannot close the socket");
            System.exit(-1);
        }

    }

}
