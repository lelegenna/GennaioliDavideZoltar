package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

// Il Client è il nostro "postino": gestisce il filo telefonico tra il computer dell'utente e il Server
public class Client {
    private Threads threads; // Il gruppo di robottini che spedisce e riceve messaggi

    // Funzione per collegarsi al Server inserendo l'indirizzo IP e il numero di porta
    public boolean connetti(String ip, int porta, Consumer<String> allaRisposta) {
        try {
            // Socket crea il vero e proprio "cavo virtuale" verso il Server
            Socket socket = new Socket(ip, porta);
            // Crea l'insieme dei robottini che lavoreranno su questo cavo
            threads = new Threads(socket, allaRisposta);
            threads.avvia(); // Accende i robottini
            return true;     // Connessione riuscita con successo!
        } catch (IOException e) {
            return false;    // Se il Server è spento, dice che la connessione è fallita
        }
    }

    // Spedisce un messaggio al Server tramite i robottini
    public void invia(String msg) {
        if (threads != null) threads.invia(msg);
    }

    // Chiude la connessione e dice al Server che abbiamo finito
    public void chiudi() {
        if (threads != null) threads.chiudi();
    }
}
