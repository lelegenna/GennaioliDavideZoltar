package gennaiolidavidezoltar;

import java.util.Random;

public class Protocollo {

    private final String[] arrabbiato = {
        "Lasciami in pace!",
        "Non ho voglia di parlare con te.",
        "Basta, mi hai stufato!",
        "Che vuoi ancora?",
        "Vai via!"
    };

    private final String[] altaborghesia = {
            "Indubbiamente, mio caro. Il destino ha già disposto.",
            "Giammai. Persino gli dèi ne sarebbero indignati.",
            "Certamente. Non potrebbe essere altrimenti.",
            "Mio caro, certe pretese sono alquanto plebee.",
            "Gli astri concordano. La fortuna ti sorride.",
            "Ne dubito fortemente. Il fato non è dalla tua parte.",
            "Il destino tace. Abbi la decenza di attendere.",
            "Naturalmente. Era scritto ancor prima della tua nascita.",
            "Assolutamente no. Il buon gusto impone un rifiuto.",
            "Le stelle sono indecise. Torna quando saranno più cortesi."
    };

    private final Random random = new Random();

    public String elabora(String msg) {
        if (msg == null || msg.isEmpty()) {
            return "ERR;Messaggio vuoto";
        }

        switch (msg) {
            case "arrabbiato":
                return  arrabbiato[random.nextInt(arrabbiato.length)];

            case "altaborghesia":
                return altaborghesia[random.nextInt(altaborghesia.length)];

            default:
                return "ERR;Comando sconosciuto";
        }
    }

    public boolean èFine(String msg) {
        return "QUIT".equals(msg);
    }
}