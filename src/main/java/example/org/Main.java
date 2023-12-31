package example.org;

import example.org.gui.GameGui;
import example.org.gui.GuiForm;

import javax.swing.*;
import java.awt.*;

public class Main {

    /**
     * The main entry point for the application.
     * It initializes the GUI form and launches the Swing application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::buildGuiForm);
    }

    /**
     * Builds and displays the main GUI form for the game.
     */
    public static void buildGuiForm() {
        JFrame frame = new JFrame("QuestVerse: An AI Adventure");
        GuiForm gameGui = new GameGui(frame);
        frame.setJMenuBar(gameGui.menuBar);
        frame.setContentPane(gameGui.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setMaximumSize(new Dimension(2400, 1600));
        frame.setVisible(true);
    }
}
