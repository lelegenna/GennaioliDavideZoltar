package gennaiolidavidezoltar;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ThreadInvio extends Thread {
    private final Socket socket;

   

    public ThreadInvio(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner tastiera = new Scanner(System.in);
            while (true) {
                out.println(tastiera.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Errore invio: " + e.getMessage());
        }
    }
}
