package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;

public class ThreadRicezione extends Thread {

    private final Socket socket;
    private final Protocollo protocollo = new Protocollo(); // uno per client

   

    public ThreadRicezione(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String riga;
            while ((riga = in.readLine()) != null) {
                System.out.println("Ricevuto: " + riga);
                out.println(protocollo.elabora(riga));
                if (protocollo.èFine(riga)) break;
            }
        } catch (IOException e) {
            System.out.println("Errore ricezione: " + e.getMessage());
        }
    }
}