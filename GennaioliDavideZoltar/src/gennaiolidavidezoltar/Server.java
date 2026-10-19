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
                new Threads(socket).avvia();   // blocca finché il client non si disconnette
                socket.close();
                System.out.println("Client disconnesso.");
            }
        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage());
        }
    
    }    
}
