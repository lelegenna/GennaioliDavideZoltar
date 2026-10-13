package gennaiolidavideserverclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner tastiera = new Scanner(System.in);
        System.out.print("IP del server (Invio = stesso PC): ");
        String ip = tastiera.nextLine().trim();
        if (ip.isEmpty()) ip = "127.0.0.1";
        try (Socket socket = new Socket(ip, 5555)) {
            System.out.println("Collegato al server. Scrivi fine per uscire.");
            new Threads(socket).avvia();
        } catch (IOException e) {
            System.out.println("Errore client: " + e.getMessage());
        }
    }
}
