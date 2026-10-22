package gennaiolidavidezoltar;

import java.util.Random;

// Il Protocollo è la "mente magica" di Zoltar: contiene tutti i dizionari di frasi e decide cosa rispondere
public class Protocollo {

    // Dizionario delle frasi filosofiche
    private final String[] filosofico = {
        "Il viaggio di mille miglia inizia con un singolo passo. ",
        "Cadi sette volte, rialzati otto. ",
        "Chi chiede è stupido per cinque minuti, chi non chiede resta stupido per sempre. ",
        "Il momento migliore per piantare un albero era vent'anni fa. Il secondo momento migliore è adesso. ",
        "Anche una scimmia cade dall'albero. ",
        "Non puoi impedire agli uccelli della tristezza di volare sopra la tua testa, ma puoi impedire che facciano il nido fra i tuoi capelli. ",
        "La goccia scava la roccia, non con la forza, ma cadendo spesso. ",
        "Quando il discepolo è pronto, il maestro appare. ",
        "Chi vince gli altri è forte, chi vince se stesso è potente. ",
        "Se vuoi cambiare il mondo, comincia da te stesso. ",
        "Una volta sola nella vita, una volta sola nell'attimo. Vivi questo momento. ",
        "Il bambù che si piega alla tempesta non si spezza. ",
        "Prima dell'illuminazione: tagliare legna, portare acqua. Dopo l'illuminazione: tagliare legna, portare acqua. ",
        "Il saggio non dice ciò che sa, lo stolto non sa ciò che dice. ",
        "Sette cadute, otto rialzate: la sconfitta è solo una pausa. ",
        "Non importa quanto vai piano, l'importante è non fermarsi. ",
        "Dove c'è la volontà, la montagna diventa pianura. ",
        "Il silenzio è un amico che non tradisce mai. "
    };

    // Dizionario delle risposte base (Sì/No)
    private final String[] base = {
        "SI", "NO", "FORSE", "NON SO", "PROBABILMENTE", "ASSOLUTAMENTE", "MAI"
    };

    // Dizionario delle frasi aristocratiche dell'alta borghesia
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

    // Dizionario dello slang rap americano King Von
    private final String[] kingVon = {
        "You trippin', bro.", "No cap.", "Real talk.", "That's fire.",
        "I'm chillin'.", "Don't even sweat it.", "Who you think you are?",
        "I'm broke.", "He's a snake.", "Catch you on the flip side.",
        "You do you.", "Don't play me.", "Props, my guy!", "Chill out, homie.",
        "You the GOAT.", "I'm stacking paper.", "Quit capping.", "I run this.",
        "I'm dead tired.", "That's wild!"
    };

    // Dizionario delle frasi e proverbi napoletani
    private final String[] napoletano = {
        "Uè, guagliò, comme staje? ", "Jamme, jà! ", "Chiano chiano se va lontano. ",
        "Chi dorme nun piglia pesce. ", "Ogne scarrafone è bello 'a mamma soja. ",
        "Chi se fa 'e fatte suoje, campa cient'anne. ", "Nun ce sta problema. ",
        "Statte buono. ", "Mannaggia 'a miseria! ", "Tiene 'a capa tosta. ",
        "Nun te preoccupà, tutto a posto. ", "Chi tene pacienza vence tutto. ",
        "Ccà nisciuno è fesso. ", "Che bella jurnata! ", "Nun fa' 'o fesso cu mme. ",
        "Sì 'nu fenomeno! ", "Facimmo 'e cunte dopo. ", "Ammore mio, nun me lassà. ",
        "Chello ca è fatto è fatto. ", "Uè, che sfaccimma! "
    };

    // Il "dado magico" Random per estrarre una frase casuale
    private final Random random = new Random();

    // Questa funzione riceve il messaggio dal Client, lo analizza e sceglie la risposta
    public String elabora(String msg) {
        if (msg == null || msg.isEmpty()) {
            return "ERR;Messaggio vuoto";
        }
        if (Fine(msg)) {
            return "ARRIVEDERCI";
        }

        // Taglia la frase in due pezzi dove c'è il punto e virgola ';'
        // Esempio: "napoletano;Come andrà la verifica?" -> prende "napoletano"
        String personalita = msg.split(";", 2)[0].trim();

        // In base alla personalità scelta, tira il dado ed estrae una frase dall'insieme corrispondente
        switch (personalita) {
            case "base":          return base[random.nextInt(base.length)];
            case "altaborghesia": return altaborghesia[random.nextInt(altaborghesia.length)];
            case "filosofico":    return filosofico[random.nextInt(filosofico.length)];
            case "kingVon":       return kingVon[random.nextInt(kingVon.length)];
            case "napoletano":    return napoletano[random.nextInt(napoletano.length)];
            default:              return "ERR;Comando sconosciuto";
        }
    }

    // Controlla se il messaggio inviato è il comando "QUIT" per chiudere la sessione
    public boolean Fine(String msg) {
        return "QUIT".equals(msg);
    }
}
