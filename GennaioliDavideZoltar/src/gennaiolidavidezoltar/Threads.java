package gennaiolidavidezoltar;
 
import java.io.*;
import java.net.*;
import java.util.function.Consumer;
 
public class Threads {
    private final Socket socket;
    private final PrintWriter out;
    private final Consumer<String> allaRisposta;
 
    public Threads(Socket socket, Consumer<String> allaRisposta) throws IOException {
        this.socket = socket;
        this.allaRisposta = allaRisposta;
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
 
    // Avvia il thread che riceve le risposte dal server
    public void avvia() {
        new ThreadRisposta(socket, allaRisposta).start();
    }
 
    // Manda un messaggio al server
    public void invia(String msg) {
        out.println(msg);
    }
 
    public void chiudi() {
        out.println("QUIT");
    }
}