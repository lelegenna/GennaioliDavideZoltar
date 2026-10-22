package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

// Questo robottino lavora sul Client in background: sta in ascolto finché Zoltar non risponde
public class ThreadRisposta extends Thread {
    private final Socket socket;
    private final Consumer<String> allaRisposta;

    public ThreadRisposta(Socket socket, Consumer<String> allaRisposta) {
        this.socket = socket;
        this.allaRisposta = allaRisposta;
        setDaemon(true); // Robottino "fantasma": se l'interfaccia si chiude, si spegne in automatico senza bloccare il sistema
    }

    @Override
    public void run() {
        try {
            // Tubo per leggere i messaggi inviati dal Server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String riga;
            // Quando arriva una risposta dal Server, la consegna alla funzione della grafica
            while ((riga = in.readLine()) != null) {
                allaRisposta.accept(riga);
            }
        } catch (IOException e) {
            allaRisposta.accept("Connessione persa.");
        }
    }
}
