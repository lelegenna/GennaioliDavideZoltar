package gennaiolidavidezoltar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// DataManager che tiene i dati in un file JSON.
// Il file ha la forma {"chiave": ...}: se il valore è un testo, getData restituisce il testo;
// se è un array o un oggetto (come le frasi), getData restituisce la sua stringa JSON.
public class JsonDataManager implements DataManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path file;
    private JsonObject dati = new JsonObject();

    public JsonDataManager(String percorso) {
        this.file = Paths.get(percorso);
        carica();
    }

    // Restituisce il valore associato alla chiave (null se non esiste)
    @Override
    public synchronized String getData(String key) {
        JsonElement el = dati.get(key);
        if (el == null || el.isJsonNull()) return null;
        return el.isJsonPrimitive() ? el.getAsString() : el.toString();
    }

    // Salva il valore (come testo) e aggiorna il file
    @Override
    public synchronized void setData(String key, String value) {
        dati.addProperty(key, value);
        salva();
    }

    // Deserializza: file JSON -> JsonObject
    private void carica() {
        if (!Files.exists(file)) {
            System.out.println("File non trovato: " + file.toAbsolutePath());
            return;
        }
        try (Reader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonElement radice = JsonParser.parseReader(r);
            if (radice != null && radice.isJsonObject()) dati = radice.getAsJsonObject();
        } catch (IOException | RuntimeException e) {
            System.out.println("Impossibile leggere " + file + ": " + e.getMessage());
        }
    }

    // Serializza: JsonObject -> file JSON
    private void salva() {
        try (Writer w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(dati, w);
        } catch (IOException e) {
            System.out.println("Impossibile salvare " + file + ": " + e.getMessage());
        }
    }
}