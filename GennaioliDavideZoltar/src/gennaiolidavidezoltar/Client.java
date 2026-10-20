package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class Client {
    private Threads threads;

    // Si collega al server. allaRisposta viene chiamato quando arriva una risposta.
    public boolean connetti(String ip, int porta, Consumer<String> allaRisposta) {
        try {
            Socket socket = new Socket(ip, porta);
            threads = new Threads(socket, allaRisposta);
            threads.avvia();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void invia(String msg) {
        threads.invia(msg);
    }

    public void chiudi() {
        if (threads != null) threads.chiudi();
    }
}
