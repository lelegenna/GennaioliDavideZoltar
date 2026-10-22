package gennaiolidavidezoltar;

// Classe principale del progetto (Main)
public class GennaioliDavideZoltar {

    // Punto di partenza di Java
    public static void main(String[] args) {
        // Avvia l'interfaccia grafica Oracolo sulla finestra principale
        java.awt.EventQueue.invokeLater(() -> new Oracolo().setVisible(true));
    }
}
