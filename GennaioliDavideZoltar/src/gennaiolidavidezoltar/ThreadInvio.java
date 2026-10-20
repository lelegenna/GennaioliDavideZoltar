package gennaiolidavidezoltar;

import java.io.PrintWriter;

public class ThreadInvio extends Thread {
    private final PrintWriter out;
    private final String msg;

    public ThreadInvio(PrintWriter out, String msg) {
        this.out = out;
        this.msg = msg;
    }

    @Override
    public void run() {
        out.println(msg);
    }
}
