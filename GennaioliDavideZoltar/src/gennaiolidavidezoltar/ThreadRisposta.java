package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ThreadRisposta extends Thread {
    private final Socket socket;
    private final Consumer<String> allaRisposta;

    public ThreadRisposta(Socket socket, Consumer<String> allaRisposta) {
        this.socket = socket;
        this.allaRisposta = allaRisposta;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String riga;
            while ((riga = in.readLine()) != null) {
                allaRisposta.accept(riga);
            }
        } catch (IOException e) {
            allaRisposta.accept("Connessione persa.");
        }
    }
}
