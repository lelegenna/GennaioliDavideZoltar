package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;

// Questo robottino vive sul Server: ascolta le domande del Client e risponde usando il Protocollo
public class ThreadRicezione extends Thread {

    private final Socket socket;
    private final Protocollo protocollo = new Protocollo(); // Il proprio libro delle risposte

    public ThreadRicezione(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Prepara il tubo per LEGGERE i dati dal Client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Prepara il tubo per SCRIVERE le risposte al Client
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String riga;
            // Legge ogni frase inviata dal Client finché la connessione resta aperta
            while ((riga = in.readLine()) != null) {
                System.out.println("Ricevuto: " + riga);
                // Chiede al protocollo la risposta giusta e la spedisce al Client
                out.println(protocollo.elabora(riga));
                if (protocollo.Fine(riga)) break; // Se il client invia "QUIT", esce dal ciclo
            }
        } catch (IOException e) {
            System.out.println("Errore ricezione: " + e.getMessage());
        } finally {
            // Quando la comunicazione finisce, chiude la presa e saluta
            try { socket.close(); } catch (IOException e) { }
            System.out.println("Client disconnesso.");
        }
    }
}
