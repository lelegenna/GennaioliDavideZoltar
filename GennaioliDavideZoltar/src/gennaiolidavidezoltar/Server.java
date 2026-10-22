package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;

// Il Server è la centrale operativa che risponde sempre a tutti i client collegati
public class Server {
    public static void main(String[] args) {
        // Apriamo la porta 5555 del computer per ricevere le chiamate dei client
        try (ServerSocket server = new ServerSocket(5555)) {
            System.out.println("Server avviato sulla porta 5555.");
            
            // Ciclo infinito: il Server non dorme mai, resta sempre in ascolto!
            while (true) {
                // Quando un client si collega, il server accetta la chiamata (accept)
                Socket socket = server.accept();
                System.out.println("Client collegato: " + socket.getInetAddress());
                
                // Crea un nuovo robottino (ThreadRicezione) dedicato unicamente a questo client
                new ThreadRicezione(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage());
        }
    }
}
