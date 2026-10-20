package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5555)) {
            System.out.println("Server avviato sulla porta 5555.");
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client collegato: " + socket.getInetAddress());
                new ThreadRicezione(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage());
        }
    }
}
