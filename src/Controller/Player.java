package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    Socket socket;
    int id;

    public Player(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }

    public BufferedReader getInputStream() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public PrintWriter getOutputStream() throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }
}
