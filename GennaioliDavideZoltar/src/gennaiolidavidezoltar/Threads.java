package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

// La classe Threads fa da direttore d'orchestra per la spedizione e la ricezione sul Client
public class Threads {
    private final Socket socket;
    private final PrintWriter out; // Il canale attraverso cui spediamo il testo al Server
    private final Consumer<String> allaRisposta;

    public Threads(Socket socket, Consumer<String> allaRisposta) throws IOException {
        this.socket = socket;
        this.allaRisposta = allaRisposta;
        // Prepara il tubo di trasmissione verso il Server
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    // Avvia il robottino che ascolta le risposte da Zoltar
    public void avvia() {
        new ThreadRisposta(socket, allaRisposta).start();
    }

    // Spedisce una riga di testo sulla rete
    public void invia(String msg) {
        out.println(msg);
    }

    // Spedisce il messaggio di uscita
    public void chiudi() {
        out.println("QUIT");
    }
}
