package gennaiolidavidezoltar;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// Questa classe è come il "telecomando" e lo "schermo" del gioco.
// Estende JFrame, cioè dice a Java: "Ehi, crea una finestra vera e propria sul computer!"
public class Oracolo extends JFrame {

    // Il Client è il nostro "postino speciale": prende il messaggio scritto e lo porta al Server
    private final Client client = new Client();

    // Qui dichiariamo tutti i "pezzi di plastica e vetro" (i componenti) che metteremo sulla finestra
    private JLabel lblTitolo;          // La scritta grande in alto come un'insegna luminosa
    private JLabel lblDomanda;         // L'etichetta con scritto "Fai la tua domanda"
    private JLabel lblRisposta;        // L'etichetta con scritto "La profezia di Zoltar"
    private JLabel lblPersonalita;     // L'etichetta con scritto "Scegli Personalità"
    private JTextArea txtDomanda;      // Il foglio bianco dove il bambino scriverà la domanda
    private JTextArea txtRisposta;     // La lavagna magica dove Zoltar farà apparire la risposta
    private JComboBox<String> cmbPersonalita; // Il menu a tendina per scegliere il carattere di Zoltar
    private JButton btnInvia;          // Il pulsante magico viola per spedire la domanda
    private JButton btnHelp;           // Il pulsante arancione d'aiuto per capire come funziona Zoltar
    private JButton btnPulisci;        // Il pulsante blu della scopa per cancellare tutto

    // IL COSTRUTTORE: È come il pulsante d'accensione del giocattolo, si attiva per primo
    public Oracolo() {
        // Disegna e incolla tutti i pulsanti e le caselle di testo sulla finestra
        costruisciGrafica();

        // Tenta di telefonare al Server sulla porta 5555 del computer locale (127.0.0.1).
        // "this::mostraRisposta" dice al postino: "Quando torna la risposta, chiama la funzione mostraRisposta!"
        if (!client.connetti("127.0.0.1", 5555, this::mostraRisposta)) {
            // Se il Server è spento, avvisa l'utente nel riquadro della risposta
            txtRisposta.setText("⚠️ Server non raggiungibile. Avvia prima il Server!");
            btnInvia.setEnabled(false); // Spegne il pulsante per evitare di mandare messaggi a vuoto
        }

        // Questo "orecchio" (WindowListener) ascolta quando clicchi sulla "X" in alto per chiudere la finestra
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                client.chiudi(); // Chiude la telefonata con il Server prima di spegnersi
            }
        });
    }

    // Questa funzione è la "fabbrica dei bottoni": decide dimensioni, colori e posizioni di ogni pezzo
    private void costruisciGrafica() {
        setTitle("✨ Zoltar l'Oracolo Digitale ✨"); // Scrive il nome in cima alla finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quando premi X, il programma si chiude del tutto
        setSize(650, 480); // Imposta la larghezza (650 pixel) e l'altezza (480 pixel)
        setLocationRelativeTo(null); // Mette la finestra perfettamente al centro dello schermo
        setLayout(null); // Spegne la griglia automatica: così posizioniamo ogni pezzo al millimetro!
        
        // Colora lo sfondo della finestra di un viola scuro misterioso
        getContentPane().setBackground(new Color(35, 25, 50));

        // 1. IL TITOLO DORATO
        lblTitolo = new JLabel("🔮 ORACOLO ZOLTAR 🔮", SwingConstants.CENTER); // Scritta al centro
        lblTitolo.setFont(new Font("Serif", Font.BOLD, 24)); // Font grande e in grassetto
        lblTitolo.setForeground(new Color(255, 215, 0)); // Colore oro lucente
        lblTitolo.setBounds(20, 15, 595, 35); // Posizione (x=20, y=15) e dimensione (largh=595, alt=35)
        add(lblTitolo); // Incolla il titolo sulla finestra

        // 2. ETICHETTA E CASELLA DOMANDA
        lblDomanda = new JLabel("Fai la tua domanda:");
        lblDomanda.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDomanda.setForeground(Color.WHITE); // Testo bianco
        lblDomanda.setBounds(30, 60, 200, 20);
        add(lblDomanda);

        txtDomanda = new JTextArea();
        txtDomanda.setLineWrap(true);       // Se arrivi in fondo alla riga, va a capo da solo
        txtDomanda.setWrapStyleWord(true);   // Non spezza le parole a metà quando va a capo
        txtDomanda.setBackground(new Color(245, 245, 250)); // Colore del foglio (bianco-grigio)
        JScrollPane scrollDomanda = new JScrollPane(txtDomanda); // Mette le barre di scorrimento lateralmente
        scrollDomanda.setBounds(30, 85, 260, 100);
        scrollDomanda.setBorder(BorderFactory.createLineBorder(new Color(180, 100, 255), 2)); // Bordo viola
        add(scrollDomanda);

        // 3. ETICHETTA E MENU SELEZIONE PERSONALITÀ
        lblPersonalita = new JLabel("Scegli Personalità:");
        lblPersonalita.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPersonalita.setForeground(Color.WHITE);
        lblPersonalita.setBounds(330, 60, 200, 20);
        add(lblPersonalita);

        // Il menu a tendina contiene i 5 modi di parlare di Zoltar
        cmbPersonalita = new JComboBox<>(new String[] { 
            "base", "altaborghesia", "filosofico", "kingVon", "napoletano" 
        });
        cmbPersonalita.setBounds(330, 85, 260, 30);
        cmbPersonalita.setFont(new Font("SansSerif", Font.BOLD, 12));
        cmbPersonalita.setBackground(new Color(230, 230, 250));
        add(cmbPersonalita);

        // 4. ETICHETTA E CASELLA RISPOSTA
        lblRisposta = new JLabel("La profezia di Zoltar:");
        lblRisposta.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblRisposta.setForeground(Color.WHITE);
        lblRisposta.setBounds(330, 130, 200, 20);
        add(lblRisposta);

        txtRisposta = new JTextArea();
        txtRisposta.setEditable(false);      // FONDAMENTALE: L'utente non può scrivere nella casella di Zoltar!
        txtRisposta.setLineWrap(true);
        txtRisposta.setWrapStyleWord(true);
        txtRisposta.setBackground(new Color(235, 235, 240));
        txtRisposta.setFont(new Font("Serif", Font.ITALIC, 14)); // Testo in corsivo
        JScrollPane scrollRisposta = new JScrollPane(txtRisposta);
        scrollRisposta.setBounds(330, 155, 260, 130);
        scrollRisposta.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2)); // Bordo dorato
        add(scrollRisposta);

        // 5. PULSANTE INVIA
        btnInvia = new JButton("🔮 CHIEDI ALL'ORACOLO");
        btnInvia.setBounds(30, 200, 260, 40);
        btnInvia.setBackground(new Color(106, 90, 205)); // Viola brillante
        btnInvia.setForeground(Color.WHITE);
        btnInvia.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnInvia.addActionListener(this::btnInviaActionPerformed); // Dice: "Quando mi clicchi, esegui btnInviaActionPerformed!"
        add(btnInvia);

        // 6. PULSANTE PULISCI
        btnPulisci = new JButton("🧹 PULISCI");
        btnPulisci.setBounds(30, 250, 260, 35);
        btnPulisci.setBackground(new Color(70, 130, 180)); // Blu acciaio
        btnPulisci.setForeground(Color.WHITE);
        btnPulisci.setFont(new Font("SansSerif", Font.BOLD, 12));
        // Quando lo clicchi, svuota entrambe le caselle di testo
        btnPulisci.addActionListener(e -> {
            txtDomanda.setText("");
            txtRisposta.setText("");
        });
        add(btnPulisci);

        // 7. PULSANTE HELP (Spiega come funziona il gioco)
        btnHelp = new JButton("❓ HELP / COME FUNZIONA");
        btnHelp.setBounds(185, 330, 260, 40);
        btnHelp.setBackground(new Color(220, 100, 50)); // Arancione vivace
        btnHelp.setForeground(Color.WHITE);
        btnHelp.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHelp.addActionListener(this::btnHelpActionPerformed); // Dice: "Quando mi clicchi, fai apparire il pop-up!"
        add(btnHelp);
    }

    // Questa funzione fa apparire una finestrella pop-up che spiega il funzionamento di Zoltar
    private void btnHelpActionPerformed(ActionEvent evt) {
        String spiegazione = 
            "✨ BENVENUTO DALL'ORACOLO ZOLTAR! ✨\n\n" +
            "Ecco come funziona l'oracolo:\n" +
            "1️⃣ Scrivi la tua domanda nel box di sinistra.\n" +
            "2️⃣ Scegli la personalità desiderata dal menu a tendina:\n" +
            "   • base: Risposte dirette e immediate.\n" +
            "   • altaborghesia: Linguaggio raffinato e aristocratico.\n" +
            "   • filosofico: Antichi aforismi e perle di saggezza.\n" +
            "   • kingVon: Gergo rap e street slang americano.\n" +
            "   • napoletano: Proverbi ed espressioni tipiche napoletane.\n" +
            "3️⃣ Premi 'CHIEDI ALL'ORACOLO' per inviare la richiesta al Server.\n" +
            "4️⃣ Riceverai la profezia generata nel box di destra!";

        // Mostra il messaggio sullo schermo come un avviso informativo
        JOptionPane.showMessageDialog(this, spiegazione, "Guida Zoltar", JOptionPane.INFORMATION_MESSAGE);
    }

    // Questa funzione prende la risposta arrivata dal Server e la scrive nello schermo.
    // SwingUtilities.invokeLater è il "vigile urbano": fa in modo che la finestra si aggiorni senza bloccarsi!
    private void mostraRisposta(String risposta) {
        SwingUtilities.invokeLater(() -> {
            txtRisposta.setText(risposta); // Scrive la risposta di Zoltar
            btnInvia.setEnabled(true);     // Riattiva il pulsante Invia
        });
    }

    // Questa funzione scatta quando premi il pulsante "CHIEDI ALL'ORACOLO"
    private void btnInviaActionPerformed(ActionEvent evt) {
        // Prende il testo scritto e rimuove gli "a capo" per non rompere il messaggio
        String domanda = txtDomanda.getText().replace("\n", " ").trim();
        
        // Se non hai scritto niente, ti avvisa e si ferma
        if (domanda.isEmpty()) {
            txtRisposta.setText("⚠️ Scrivi prima una domanda!");
            return;
        }

        // Unisce la personalità scelta e la domanda con un ';' (es. "napoletano;Avrò fortuna?")
        String comando = cmbPersonalita.getSelectedItem() + ";" + domanda;
        btnInvia.setEnabled(false); // Spegne il pulsante momentaneamente per evitare clic doppi
        client.invia(comando);      // Manda la busta da lettere al postino Client
    }

    // Il punto da cui parte tutto quando si avvia il programma
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Oracolo().setVisible(true));
    }
}
