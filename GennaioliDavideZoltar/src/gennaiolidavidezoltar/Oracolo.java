package gennaiolidavidezoltar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/** Interfaccia grafica di Zoltar. */
public class Oracolo extends JFrame {

    private static final Color INK = new Color(35, 28, 52);
    private static final Color PURPLE = new Color(91, 61, 137);
    private static final Color GOLD = new Color(221, 177, 91);
    private static final Color PAPER = new Color(250, 247, 241);
    private static final int SAMPLE_RATE = 44100;
    private static final ExecutorService AUDIO_QUEUE = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "zoltar-audio");
        thread.setDaemon(true);
        return thread;
    });

    private final Client client = new Client();
    private JTextArea txtDomanda;
    private JTextArea txtRisposta;
    private JComboBox<String> cmbPersonalita;
    private JButton btnInvia;
    private Timer thinkingTimer;
    private int thinkingFrame;
    private volatile boolean audioEnabled = true;

    public Oracolo() {
        super("Zoltar | L'Oracolo Digitale");
        initComponents();

        boolean connesso = client.connetti("127.0.0.1", 5555, this::mostraRisposta);
        if (!connesso) {
            txtRisposta.setText("Il server non è raggiungibile. Avvia il Server e poi riprova.");
            btnInvia.setEnabled(false);
        } else {
            txtRisposta.setText("L'oracolo è pronto. Formula la tua domanda…");
        }

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { client.chiudi(); }
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 560));
        getContentPane().setBackground(PAPER);

        JPanel root = new MagicBackground();
        root.setLayout(new BorderLayout(24, 22));
        root.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        setContentPane(root);

        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setOpaque(false);
        JLabel title = new JLabel("🔮  ZOLTAR", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(PURPLE);
        JLabel subtitle = new JLabel("L'ORACOLO DIGITALE · CHIEDI E ASCOLTA IL DESTINO", JLabel.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(112, 101, 125));
        JToggleButton audioToggle = new JToggleButton("♫ AUDIO: ON", true);
        audioToggle.setFont(new Font("SansSerif", Font.BOLD, 11));
        audioToggle.setForeground(PURPLE);
        audioToggle.setBackground(new Color(255, 247, 226));
        audioToggle.setFocusPainted(false);
        audioToggle.setToolTipText("Attiva o disattiva i suoni di Zoltar");
        audioToggle.addActionListener(e -> {
            audioEnabled = audioToggle.isSelected();
            audioToggle.setText(audioEnabled ? "♫ AUDIO: ON" : "♫ AUDIO: OFF");
        });
        header.add(title, BorderLayout.CENTER);
        header.add(subtitle, BorderLayout.SOUTH);
        header.add(audioToggle, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new java.awt.GridLayout(1, 2, 22, 0));
        content.setOpaque(false);
        JPanel questionCard = card();
        JPanel answerCard = card();
        content.add(questionCard);
        content.add(answerCard);
        root.add(content, BorderLayout.CENTER);

        questionCard.setLayout(new BorderLayout(0, 12));
        questionCard.add(sectionLabel("LA TUA DOMANDA"), BorderLayout.NORTH);
        txtDomanda = new JTextArea(5, 24);
        txtDomanda.setLineWrap(true);
        txtDomanda.setWrapStyleWord(true);
        txtDomanda.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtDomanda.setForeground(INK);
        txtDomanda.setBackground(Color.WHITE);
        txtDomanda.setMargin(new Insets(12, 12, 12, 12));
        JScrollPane questionScroll = new JScrollPane(txtDomanda);
        questionScroll.setBorder(BorderFactory.createLineBorder(new Color(224, 218, 231)));
        questionCard.add(questionScroll, BorderLayout.CENTER);

        JPanel controls = new JPanel(new java.awt.GridLayout(0, 1, 0, 10));
        controls.setOpaque(false);
        JLabel personalityLabel = new JLabel("Scegli la personalità");
        personalityLabel.setForeground(PURPLE);
        personalityLabel.setFont(new Font("Serif", Font.BOLD, 16));
        cmbPersonalita = new JComboBox<>(new String[] {
            "base", "altaborghesia", "filosofico", "kingVon", "napoletano"
        });
        cmbPersonalita.setFont(new Font("Serif", Font.PLAIN, 16));
        btnInvia = new JButton("🔮  CHIEDI ALL'ORACOLO");
        styleButton(btnInvia, PURPLE, Color.WHITE);
        btnInvia.setToolTipText("Invia la domanda all'oracolo");
        btnInvia.addActionListener(e -> inviaDomanda());
        JButton clear = new JButton("PULISCI");
        styleButton(clear, new Color(238, 232, 244), PURPLE);
        clear.setToolTipText("Cancella la domanda e prepara una nuova consultazione");
        clear.addActionListener(e -> {
            txtDomanda.setText("");
            txtRisposta.setText("L'oracolo è pronto per una nuova domanda…");
            txtDomanda.requestFocusInWindow();
        });
        controls.add(personalityLabel);
        controls.add(cmbPersonalita);
        controls.add(btnInvia);
        controls.add(clear);
        questionCard.add(controls, BorderLayout.SOUTH);

        answerCard.setLayout(new BorderLayout(0, 12));
        answerCard.add(sectionLabel("LA PROFEZIA DI ZOLTAR"), BorderLayout.NORTH);
        txtRisposta = new JTextArea();
        txtRisposta.setEditable(false);
        txtRisposta.setLineWrap(true);
        txtRisposta.setWrapStyleWord(true);
        txtRisposta.setFont(new Font("Serif", Font.PLAIN, 18));
        txtRisposta.setForeground(INK);
        txtRisposta.setBackground(new Color(255, 253, 249));
        txtRisposta.setMargin(new Insets(16, 16, 16, 16));
        JScrollPane answerScroll = new JScrollPane(txtRisposta);
        answerScroll.setBorder(BorderFactory.createLineBorder(new Color(224, 218, 231)));
        answerCard.add(answerScroll, BorderLayout.CENTER);
        JButton help = new JButton("❓  HELP · COME FUNZIONA");
        styleButton(help, new Color(255, 247, 226), new Color(122, 86, 28));
        help.setToolTipText("Leggi le istruzioni e scopri le personalità di Zoltar");
        help.addActionListener(e -> mostraAiuto());
        answerCard.add(help, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        // Il titolo cambia dimensione e colore, così il movimento si nota anche a colpo d'occhio.
        final double[] pulse = {0};
        Timer titleAnimation = new Timer(16, e -> {
            pulse[0] += 0.037;
            float wave = (float) (0.5 + 0.5 * Math.sin(pulse[0]));
            float goldMix = 0.58f * wave;
            title.setForeground(new Color(
                (int) (PURPLE.getRed() * (1 - goldMix) + GOLD.getRed() * goldMix),
                (int) (PURPLE.getGreen() * (1 - goldMix) + GOLD.getGreen() * goldMix),
                (int) (PURPLE.getBlue() * (1 - goldMix) + GOLD.getBlue() * goldMix)));
        });
        titleAnimation.setCoalesce(true);
        titleAnimation.start();
    }

    private JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(231, 225, 235)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        return panel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(PURPLE);
        return label;
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(background.equals(PURPLE) ? GOLD : new Color(224, 218, 231)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));
    }

    private void inviaDomanda() {
        String domanda = txtDomanda.getText().replace('\n', ' ').trim();
        if (domanda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Scrivi prima una domanda per Zoltar.",
                "Domanda mancante", JOptionPane.INFORMATION_MESSAGE);
            txtDomanda.requestFocusInWindow();
            return;
        }
        txtRisposta.setText("Le stelle stanno preparando la risposta…");
        btnInvia.setEnabled(false);
        avviaAnimazionePensiero();
        suonaZoltar(false);
        client.invia(cmbPersonalita.getSelectedItem() + ";" + domanda);
    }

    private void avviaAnimazionePensiero() {
        if (thinkingTimer != null) thinkingTimer.stop();
        thinkingFrame = 0;
        thinkingTimer = new Timer(120, e -> {
            String[] fasi = {"✦", "✧", "✨", "✧"};
            btnInvia.setText("" + fasi[thinkingFrame++ % fasi.length] + "  ZOLTAR CONSULTA GLI ASTRI");
        });
        thinkingTimer.start();
    }

    private void mostraRisposta(String risposta) {
        SwingUtilities.invokeLater(() -> {
            if (thinkingTimer != null) thinkingTimer.stop();
            btnInvia.setText("🔮  CHIEDI ALL'ORACOLO");
            txtRisposta.setText(risposta);
            btnInvia.setEnabled(true);
            suonaZoltar(true);
        });
    }

    /** Genera un breve effetto mistico senza dipendere da file audio esterni. */
    private void suonaZoltar(boolean profezia) {
        if (!audioEnabled) return;
        double[] note = profezia
            ? new double[] {523.25, 659.25, 783.99}
            : new double[] {196.00, 293.66};
        AUDIO_QUEUE.execute(() -> {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format, 4096);
                line.start();
                for (double frequency : note) {
                    byte[] sound = creaNota(frequency, profezia ? 0.20 : 0.16);
                    line.write(sound, 0, sound.length);
                }
                line.drain();
            } catch (Exception ignored) {
                // Audio opzionale: se il dispositivo non è disponibile, la GUI continua normalmente.
            }
        });
    }

    private byte[] creaNota(double frequency, double seconds) {
        int samples = (int) (SAMPLE_RATE * seconds);
        byte[] sound = new byte[samples * 2];
        for (int i = 0; i < samples; i++) {
            double time = i / (double) SAMPLE_RATE;
            double fadeIn = Math.min(1.0, time / 0.018);
            double fadeOut = Math.pow(1.0 - i / (double) samples, 1.8);
            double fundamental = Math.sin(2 * Math.PI * frequency * time);
            double overtone = 0.32 * Math.sin(2 * Math.PI * frequency * 2.01 * time);
            short value = (short) (Short.MAX_VALUE * 0.34 * fadeIn * fadeOut * (fundamental + overtone));
            sound[i * 2] = (byte) (value & 0xff);
            sound[i * 2 + 1] = (byte) ((value >>> 8) & 0xff);
        }
        return sound;
    }

    private void mostraAiuto() {
        String messaggio = "1. Scrivi una domanda nel riquadro.\n"
            + "2. Scegli la personalità con cui vuoi ricevere la profezia.\n"
            + "3. Premi CHIEDI ALL'ORACOLO e leggi la risposta.\n\n"
            + "Le personalità disponibili sono: base (risposte brevi), altaborghesia "
            + "(tono aristocratico), filosofico (proverbi), kingVon (slang rap) e "
            + "napoletano (detti e modi di dire).\n\n"
            + "Per usare l'applicazione, avvia prima il Server. PULISCI prepara "
            + "la schermata per una nuova domanda.";
        JOptionPane.showMessageDialog(this, messaggio, "Come funziona Zoltar",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> new Oracolo().setVisible(true));
    }

    /** Sfondo notturno sfumato con piccole stelle che si muovono lentamente. */
    private static final class MagicBackground extends JPanel {
        private static final int STAR_COUNT = 42;
        private final float[] x = new float[STAR_COUNT];
        private final float[] y = new float[STAR_COUNT];
        private final float[] speed = new float[STAR_COUNT];
        private final float[] phase = new float[STAR_COUNT];
        private float shimmer;

        MagicBackground() {
            setOpaque(true);
            Random random = new Random(17);
            for (int i = 0; i < STAR_COUNT; i++) {
                x[i] = random.nextFloat();
                y[i] = random.nextFloat();
                speed[i] = 0.0008f + random.nextFloat() * 0.0014f;
                phase[i] = random.nextFloat() * 6.28f;
            }
            Timer starAnimation = new Timer(16, e -> {
                shimmer += 0.04f;
                for (int i = 0; i < STAR_COUNT; i++) {
                    y[i] -= speed[i];
                    if (y[i] < 0) {
                        y[i] = 1;
                        x[i] = (x[i] + 0.37f) % 1;
                    }
                }
                repaint();
            });
            starAnimation.setCoalesce(true);
            starAnimation.start();
        }

        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            int w = getWidth();
            int h = getHeight();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new java.awt.GradientPaint(0, 0, new Color(247, 241, 252), w, h,
                new Color(255, 249, 232)));
            g.fillRect(0, 0, w, h);
            for (int i = 0; i < STAR_COUNT; i++) {
                float twinkle = (float) (0.5 + 0.5 * Math.sin(shimmer + phase[i]));
                int alpha = 95 + (int) (145 * twinkle);
                int size = 4 + (int) (6 * twinkle);
                int sx = (int) (x[i] * w);
                int sy = (int) (y[i] * h);
                g.setColor(new Color(221, 177, 91, 40 + (int) (45 * twinkle)));
                g.fillOval(sx - size, sy - size, size * 3, size * 3);
                g.setColor(new Color(159, 112, 190, alpha));
                g.fillOval(sx, sy, size, size);
                g.setColor(new Color(255, 255, 255, Math.min(255, alpha)));
                g.drawLine(sx - size, sy + size / 2, sx + size * 2, sy + size / 2);
                g.drawLine(sx + size / 2, sy - size, sx + size / 2, sy + size * 2);
            }
            g.dispose();
        }
    }
}
