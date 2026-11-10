package gennaiolidavidezoltar;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Il Protocollo è la "mente magica" di Zoltar.
// Le frasi non sono più nel codice: le legge dal file JSON tramite il DataManager.
// Comandi accettati:
//   PERSONA_[personalita]  -> imposta lo stile delle risposte
//   DOMANDA_[domanda]      -> risponde alla domanda con lo stile impostato
//   QUIT                   -> chiude la sessione
public class Protocollo {

    private static final String PREFISSO_PERSONA = "PERSONA_";
    private static final String PREFISSO_DOMANDA = "DOMANDA_";

    private final DataManager dataManager;   // da qui arrivano le frasi
    private final Gson gson = new Gson();

    // Frasi già lette dal file (chiave = nome personalità)
    private final Map<String, String[]> cache = new HashMap<>();

    // Personalità attualmente scelta da questo client (null = non ancora scelta)
    private String personaCorrente = null;

    // Il "dado magico": a ogni domanda estrae una risposta nuova
    private final Random random = new Random();

    // Ultima risposta data a questo client, per non ripeterla due volte di fila
    private String ultimaRisposta = null;

    public Protocollo(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // Riceve una riga dal Client e restituisce la risposta del server
    public String elabora(String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return "ERR;Messaggio vuoto";
        }
        msg = msg.trim();

        if (Fine(msg)) {
            return "ARRIVEDERCI";
        }

        if (msg.startsWith(PREFISSO_PERSONA)) {
            return impostaPersona(msg.substring(PREFISSO_PERSONA.length()).trim());
        }

        if (msg.startsWith(PREFISSO_DOMANDA)) {
            return rispondi(msg.substring(PREFISSO_DOMANDA.length()).trim());
        }

        return "ERR;Comando sconosciuto";
    }

    // PERSONA_[parametroPersonalità]
    private String impostaPersona(String persona) {
        if (trovaFrasi(persona) == null) {
            return "ERR;Personalita sconosciuta";
        }
        personaCorrente = persona;
        return "OK;" + persona;
    }

    // DOMANDA_[parametroDomanda]
    private String rispondi(String domanda) {
        if (personaCorrente == null) {
            return "ERR;Scegli prima la persona (PERSONA_...)";
        }
        if (domanda.isEmpty()) {
            return "ERR;Domanda mancante";
        }

        String[] frasi = trovaFrasi(personaCorrente);
        if (frasi == null) {
            return "ERR;Frasi non disponibili";
        }

        String risposta;
        int tentativi = 0;
        do {
            risposta = frasi[random.nextInt(frasi.length)];
            tentativi++;
        } while (risposta.equals(ultimaRisposta) && frasi.length > 1 && tentativi < 10);

        ultimaRisposta = risposta;
        return risposta;
    }

    // Legge dal file JSON l'array di frasi della personalità (null se manca o è vuoto)
    private String[] trovaFrasi(String persona) {
        if (cache.containsKey(persona)) {
            return cache.get(persona);
        }
        String json = dataManager.getData(persona);
        if (json == null) {
            return null;
        }
        try {
            String[] frasi = gson.fromJson(json, String[].class);
            if (frasi == null || frasi.length == 0) {
                return null;
            }
            cache.put(persona, frasi);
            return frasi;
        } catch (RuntimeException e) {
            return null;
        }
    }

    // Controlla se il messaggio inviato è il comando "QUIT" per chiudere la sessione
    public boolean Fine(String msg) {
        return "QUIT".equals(msg == null ? null : msg.trim());
    }
}