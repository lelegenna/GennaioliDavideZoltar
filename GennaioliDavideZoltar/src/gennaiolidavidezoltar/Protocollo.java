package gennaiolidavidezoltar;

import java.util.Random;

// Il Protocollo è la "mente magica" di Zoltar.
// Comandi accettati:
//   PERSONA_[personalita]  -> imposta lo stile delle risposte
//   DOMANDA_[domanda]      -> risponde alla domanda con lo stile impostato
//   QUIT                   -> chiude la sessione
public class Protocollo {

    private static final String PREFISSO_PERSONA = "PERSONA_";
    private static final String PREFISSO_DOMANDA = "DOMANDA_";

    // Personalità attualmente scelta da questo client (null = non ancora scelta)
    private String personaCorrente = null;

    // Il "dado magico": a ogni domanda estrae una risposta nuova
    private final Random random = new Random();

    // Ultima risposta data a questo client, per non ripeterla due volte di fila
    private String ultimaRisposta = null;

    private final String[] napoletano = {
        "Sì, certamente! Nun ce sta problema.", "Jamme, jà! Sarrà accussì.",
        "Sì, nun te preoccupà, tutto a posto.", "Sì, sì 'nu fenomeno! Va tutto bbuono.",
        "Che bella jurnata! Sì, assaje.", "Statte buono, 'a risposta è sì.",
        "Nonsignore, scordatillo. Mannaggia 'a miseria!", "Ccà nisciuno è fesso: nun se fa.",
        "Nun fa' 'o fesso cu mme: 'a risposta è no.", "No, chello ca è fatto è fatto.",
        "Uè, che sfaccimma! No, nun se pò.", "No, guagliò, tiene 'a capa tosta ma nun cagne niente.",
        "Chiano chiano, nun se sape ancora.", "Chi tene pacienza vence tutto: aspetta.",
        "Chiano chiano se va lontano: ancora nun se sape.", "Facimmo 'e cunte dopo, guagliò.",
        "Chi dorme nun piglia pesce: movete e vedimme.", "Può essere, può essere... 'o destino nun ha parlato.",
        "Quanno meno t'o aspiette, guagliò.", "Dimane, si 'o Pateterno vo'.",
        "Chiano chiano, quanno è 'o mumento.", "Facimmo 'e cunte dopo, nun è ancora l'ora.",
        "Quanno 'e galline avarranno 'e diente.", "Presto, ma nun te mettere ffretta.",
        "Pecché accussì è 'a vita, guagliò.", "Pecché ogne scarrafone è bello 'a mamma soja.",
        "Pecché chi se fa 'e fatte suoje campa cient'anne.", "Pecché chello ca è fatto è fatto.",
        "Pecché ccà nisciuno è fesso, ecco pecché.", "Pecché 'o destino accussì ha vuluto.",
        "Chiano chiano, cu pacienza e 'a capa tosta.", "Cumme sempe: facenno 'e fatte tuoje.",
        "Cu ammore e senza ffretta.", "Jamme, jà! Rimboccate 'e maniche.",
        "Cumme fanno tutte: un passo 'a vota.", "Cu 'a capa tosta, comme te piace a te."
    };

    private final String[] filosofico = {
        "Sì: il bambù che si piega alla tempesta non si spezza.",
        "Sì: dove c'è la volontà, la montagna diventa pianura.",
        "Sì: il viaggio di mille miglia inizia con un singolo passo.",
        "Sì: il momento migliore per piantare un albero è adesso.",
        "Sì: quando il discepolo è pronto, il maestro appare.",
        "Sì: chi vince se stesso è potente.",
        "No: cadi sette volte, ma questa non è la strada.",
        "No: chi vince se stesso sa anche rinunciare.",
        "No: il saggio non insegue ciò che non gli appartiene.",
        "No: se vuoi cambiare il mondo, comincia da te stesso, non da qui.",
        "No: il silenzio è l'unica risposta che ti serve.",
        "No: anche una scimmia cade dall'albero, e tu stai per cadere.",
        "Il saggio non dice ciò che non sa: il futuro tace.",
        "Il silenzio è un amico: ascolta, e saprai.",
        "La goccia scava la roccia: il tempo darà la risposta.",
        "Chi chiede è stupido per cinque minuti: continua a cercare.",
        "Una volta sola nell'attimo: vivi questo momento e lo scoprirai.",
        "Nulla è scritto: sta a te decidere.",
        "Quando il discepolo è pronto, il momento appare.",
        "Il momento migliore è adesso.",
        "Il momento migliore per piantare un albero era vent'anni fa; il secondo è adesso.",
        "Quando smetterai di aspettarlo.",
        "La goccia scava la roccia: quando sarà il tempo.",
        "Non importa quanto vai piano: arriverà.",
        "Perché la goccia scava la roccia cadendo spesso.",
        "Perché ogni cosa ha la sua stagione.",
        "Perché chi vince se stesso è potente.",
        "Perché il saggio non dice ciò che sa.",
        "Perché il bambù che si piega non si spezza.",
        "Perché ogni caduta insegna a rialzarsi.",
        "Con un singolo passo alla volta.",
        "Piano, ma senza mai fermarsi.",
        "Cadi sette volte, rialzati otto.",
        "Cominciando da te stesso.",
        "Piegandoti come il bambù, senza spezzarti.",
        "Tagliando legna e portando acqua, con pazienza."
    };

    private final String[] altaborghesia = {
        "Indubbiamente, mio caro. Il destino ha già disposto.", "Certamente. Non potrebbe essere altrimenti.",
        "Gli astri concordano. La fortuna ti sorride.", "Naturalmente. Era scritto ancor prima della tua nascita.",
        "Ma certo, mio caro. Con somma eleganza.", "Senza alcun dubbio. Il buon gusto lo approva.",
        "Giammai. Persino gli dèi ne sarebbero indignati.", "Assolutamente no. Il buon gusto impone un rifiuto.",
        "Mio caro, certe pretese sono alquanto plebee.", "Ne dubito fortemente. Il fato non è dalla tua parte.",
        "Non sia mai. La famiglia non lo tollererebbe.", "Temo di no, mio caro. Sarebbe di pessimo gusto.",
        "Il destino tace. Abbi la decenza di attendere.", "Le stelle sono indecise. Torna quando saranno più cortesi.",
        "Chi può dirlo, mio caro? Ne discuteremo a cena.", "Vedremo. Una persona raffinata sa attendere.",
        "Dipende dall'umore degli astri e dal clima.", "Né sì né no: la discrezione impone il silenzio.",
        "Quando sarà opportuno, mio caro, e non un istante prima.", "In tempo utile per il tè delle cinque.",
        "Alla prossima stagione mondana.", "Quando il maggiordomo l'avrà annunciato.",
        "Non appena sarà conveniente.", "Al sorgere della luna, mio caro.",
        "Perché certe cose, mio caro, semplicemente si fanno.", "Perché il buon nome della famiglia lo esige.",
        "Perché così vuole l'etichetta.", "Perché gli astri, mio caro, hanno gusto.",
        "Perché la plebe non può capire.", "Perché era scritto ancor prima della tua nascita.",
        "Con eleganza, naturalmente.", "Con discrezione e un pizzico di sprezzatura.",
        "Con un inchino e un sorriso di circostanza.", "Con il buon gusto che ci contraddistingue.",
        "Senza dare nell'occhio, mio caro.", "Come si è sempre fatto, in società."
    };

    private final String[] kingVon = {
        "Yeah, no cap. That's fire.", "Real talk, you the GOAT.",
        "Props, my guy! It's a yes.", "Yeah, don't even sweat it.",
        "That's fire, bro. Go for it.", "Yeah, I run this and I say yes.",
        "Nah, you trippin', bro.", "Don't play me. Quit capping.",
        "Who you think you are? Nah.", "Nope. That's wild, homie.",
        "Nah, he's a snake, don't do it.", "No way, bro. Chill out.",
        "Who knows, homie. Chill out.", "Catch you on the flip side, we'll see.",
        "You do you, bro. Could go either way.", "I'm chillin', ask me later.",
        "Real talk, I ain't sure yet.", "Don't sweat it, the future's wild.",
        "When I'm done stacking paper.", "Soon, don't even sweat it.",
        "When I'm done chillin', homie.", "Catch you on the flip side.",
        "When the time's right, no cap.", "Sooner than you think, bro.",
        "'Cause that's how I run this.", "'Cause he's a snake, that's why.",
        "'Cause that's just how it is, real talk.", "'Cause you trippin', bro.",
        "'Cause life's wild like that.", "'Cause I'm stacking paper, that's why.",
        "You do you, bro.", "Real smooth, no cap.",
        "Chill out and stack paper.", "Don't play it, just run it.",
        "Keep it real, homie.", "Slow and steady, no cap."
    };

    private final String[] base = {
        "SI", "ASSOLUTAMENTE", "CERTO", "SENZA DUBBIO", "PROBABILMENTE SI", "DI SICURO",
        "NO", "MAI", "ASSOLUTAMENTE NO", "PER NIENTE", "PROBABILMENTE NO", "NEANCHE PER SOGNO",
        "FORSE", "PROBABILMENTE", "NON SO", "CHISSA", "PUO DARSI", "DIPENDE",
        "PRESTO", "NON SO QUANDO", "DOMANI", "TRA POCO", "TRA MOLTO TEMPO",
        "PERCHE SI", "PERCHE NO", "PERCHE COSI E", "CHISSA PERCHE", "PERCHE E DESTINO",
        "COME CAPITA", "PIANO PIANO", "CON CALMA", "COME PUOI", "A MODO TUO"
    };

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
        String risposta;
        int tentativi = 0;
        do {
            risposta = frasi[random.nextInt(frasi.length)];
            tentativi++;
        } while (risposta.equals(ultimaRisposta) && tentativi < 10);

        ultimaRisposta = risposta;
        return risposta;
    }

    // Restituisce l'array di frasi della personalità, o null se non esiste
    private String[] trovaFrasi(String persona) {
        switch (persona) {
            case "base":          return base;
            case "altaborghesia": return altaborghesia;
            case "filosofico":    return filosofico;
            case "kingVon":       return kingVon;
            case "napoletano":    return napoletano;
            default:              return null;
        }
    }

    // Controlla se il messaggio inviato è il comando "QUIT" per chiudere la sessione
    public boolean Fine(String msg) {
        return "QUIT".equals(msg == null ? null : msg.trim());
    }
}