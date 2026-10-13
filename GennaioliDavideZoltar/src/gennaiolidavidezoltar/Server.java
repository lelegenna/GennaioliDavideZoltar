package gennaiolidavideserverclient;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    public static void main(String[] args) {
        Map<Integer, Socket> clienti = new ConcurrentHashMap<>();
        AtomicInteger numeratore = new AtomicInteger(1);

        try (ServerSocket server = new ServerSocket(5555)) {
            System.out.println("Server avviato sulla porta 5555.");
            System.out.println("Per rispondere scrivi: numeroClient messaggio (esempio: 1 Ciao)");
            System.out.println("Scrivi fine per spegnere il server.");

            Thread accettazione = new Thread(() -> {
                while (!server.isClosed()) {
                    try {
                        Socket socket = server.accept();
                        int id = numeratore.getAndIncrement();
                        clienti.put(id, socket);
                        System.out.println("\nClient " + id + " collegato: " + socket.getInetAddress());
                        new Thread(() -> {
                            try {
                                new ThreadRicezione(socket, id).run();
                            } finally {
                                clienti.remove(id);
                                try { socket.close(); } catch (IOException ignored) { }
                            }
                        }).start();
                    } catch (IOException e) {
                        if (!server.isClosed()) System.out.println("Errore connessione: " + e.getMessage());
                    }
                }
            });
            accettazione.setDaemon(true);
            accettazione.start();

            Scanner tastiera = new Scanner(System.in);
            while (tastiera.hasNextLine()) {
                String riga = tastiera.nextLine().trim();
                if (riga.equalsIgnoreCase("fine")) break;
                String[] parti = riga.split("\\s+", 2);
                if (parti.length < 2) {
                    System.out.println("Scrivi: numeroClient messaggio (esempio: 1 Ciao)");
                    continue;
                }
                try {
                    int id = Integer.parseInt(parti[0]);
                    Socket socket = clienti.get(id);
                    if (socket == null || socket.isClosed()) {
                        System.out.println("Client " + id + " non collegato.");
                        continue;
                    }
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(parti[1]);
                    if (out.checkError()) System.out.println("Invio non riuscito.");
                    else System.out.println("Tu -> Client " + id + ": " + parti[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Il primo valore deve essere il numero del client.");
                } catch (IOException e) {
                    System.out.println("Errore invio: " + e.getMessage());
                }
            }
            for (Socket socket : clienti.values()) {
                try { socket.close(); } catch (IOException ignored) { }
            }
        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage());
        }
    }
}
