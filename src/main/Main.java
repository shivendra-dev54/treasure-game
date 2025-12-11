package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use EDT to create GUI
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("My Game");
            GamePanel gp = new GamePanel();

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.add(gp);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            gp.setupGame();
            gp.startGameThread();
        });
    }
}
