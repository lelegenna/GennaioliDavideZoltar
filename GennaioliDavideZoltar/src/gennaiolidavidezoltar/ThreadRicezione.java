package gennaiolidavideserverclient;

import java.io.*;
import java.net.*;

public class ThreadRicezione extends Thread {
    private final Socket socket;
    private final int id;
    public ThreadRicezione(Socket socket) { this(socket, 0); }
    public ThreadRicezione(Socket socket, int id) { this.socket = socket; this.id = id; }
    @Override public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String messaggio;
            while ((messaggio = in.readLine()) != null) {
                if (messaggio.equalsIgnoreCase("fine")) break;
                System.out.println(id == 0 ? "Server: " + messaggio : "[Client " + id + "] " + messaggio);
            }
            System.out.println(id == 0 ? "Server disconnesso." : "Client " + id + " disconnesso.");
        } catch (IOException e) {
            if (!socket.isClosed()) System.out.println("Connessione interrotta: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) { }
        }
    }
}
