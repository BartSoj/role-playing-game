package example.org.gui;

import example.org.service.GameStatusService;
import example.org.dto.GameStatus;
import example.org.service.gamelogic.GameLogicServiceImpl1;
import example.org.service.gamelogic.GameLogicServiceImpl2;
import example.org.service.gamelogic.GameLogicServiceImpl3;
import example.org.service.gamelogic.GameLogicServiceImpl4;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GuiForm {
    public GameStatusService gameStatusService;
    public GameStatus gameStatus;
    public JFrame parentFrame;
    public JPanel panelMain;
    public JTextField actionTextField;
    public JButton sendActionButton;
    public JTextArea storyTextArea;
    public JTable itemsTable;
    public JLabel xpLabel;
    public JTable completedQuestsTable;
    public JPanel tableInfoPanel;
    public JPanel bottomPanel;
    public JPanel middlePanel;
    public JPanel topPanel;
    public JScrollPane questsScrollPane;
    public JScrollPane completedQuestsScrollPane;
    public JScrollPane itemsScrollPane;
    public JLabel gameModeLabel;
    public JTable questsTable;
    public JLabel roundLabel;
    public JLabel timeLabel;
    public JLabel dayLabel;
    public JLabel levelLabel;
    public JLabel hpLabel;
    public JScrollPane storyTextAreaScrollPane;
    public JPanel statusPanel;
    public JLabel questsLabel;
    public JLabel completedQuestsLabel;
    public JLabel itemsLabel;
    public JPanel actionPanel;
    public JPanel actionTextFieldPanel;
    public JMenuBar menuBar;
    public JMenu gameModeMenu;
    public JMenuItem helpMenuItem;
    public JMenuItem gameModeImpl1MenuItem;
    public JMenuItem gameModeImpl2MenuItem;
    public JMenuItem gameModeImpl3MenuItem;
    public JMenuItem gameModeImpl4MenuItem;

    public GuiForm() {
        menuBar = new JMenuBar();

        // Create the "Game Mode" menu
        gameModeMenu = new JMenu("Game Mode");

        // Create menu items for different game modes
        gameModeImpl1MenuItem = new JMenuItem(GameLogicServiceImpl1.getGameName());
        gameModeImpl2MenuItem = new JMenuItem(GameLogicServiceImpl2.getGameName());
        gameModeImpl3MenuItem = new JMenuItem(GameLogicServiceImpl3.getGameName());
        gameModeImpl4MenuItem = new JMenuItem(GameLogicServiceImpl4.getGameName());

        // Add menu items to the "Game Mode" menu
        gameModeMenu.add(gameModeImpl2MenuItem);
        gameModeMenu.add(gameModeImpl3MenuItem);
        gameModeMenu.add(gameModeImpl1MenuItem);
        gameModeMenu.add(gameModeImpl4MenuItem);

        // Create a menu item for displaying help information
        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // Add the "Game Mode" and "Help" menus to the menu bar
        menuBar.add(gameModeMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenuItem);
    }

    static class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel() {
            super();
            setOpaque(false);
        }

        public void setBackgroundImage(BufferedImage image) {
            this.backgroundImage = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Paint the background image
            if (backgroundImage != null) {
                int width = getWidth();
                int height = getHeight();
                g.drawImage(backgroundImage, 0, 0, width, height, null);
            }
        }
    }

    private void createUIComponents() {
        panelMain = new BackgroundPanel();
    }
}
