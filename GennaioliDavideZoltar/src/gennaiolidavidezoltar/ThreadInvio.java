package gennaiolidavideserverclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ThreadInvio extends Thread {
    private final Socket socket;
    public ThreadInvio(Socket socket) { this.socket = socket; }
    @Override public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner tastiera = new Scanner(System.in);
            while (!socket.isClosed() && tastiera.hasNextLine()) {
                String messaggio = tastiera.nextLine();
                if (socket.isClosed()) break;
                out.println(messaggio);
                if (out.checkError() || messaggio.equalsIgnoreCase("fine")) break;
            }
            socket.close();
        } catch (IOException e) {
            if (!socket.isClosed()) System.out.println("Errore invio: " + e.getMessage());
        }
    }
}
