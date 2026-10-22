package gennaiolidavidezoltar;

import java.io.PrintWriter;

// Questo robottino ha un solo compito: prende un messaggio e lo spedisce super veloce al Server
public class ThreadInvio extends Thread {
    private final PrintWriter out; // Il tubo di trasmissione
    private final String msg;       // Il messaggio da spedire

    public ThreadInvio(PrintWriter out, String msg) {
        this.out = out;
        this.msg = msg;
    }

    @Override
    public void run() {
        out.println(msg); // Spinge il messaggio nel tubo della rete
    }
}
