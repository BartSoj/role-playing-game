package example.org.gui;

import example.org.service.GameStatusService;
import example.org.dto.GameStatus;
import example.org.service.gamelogic.GameLogicServiceImpl1;
import example.org.service.gamelogic.GameLogicServiceImpl2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuiForm {
    private GameStatusService gameStatusService;
    private GameStatus gameStatus;
    private JPanel panelMain;
    private JTextField actionTextField;
    private JButton sendActionButton;
    private JTextArea storyTextArea;
    private JTable itemsTable;
    private JLabel xpLabel;
    private JTable completedQuestsTable;
    private JPanel tableInfoPanel;
    private JPanel bottomPanel;
    private JPanel middlePanel;
    private JPanel topPanel;
    private JScrollPane questsScrollPane;
    private JScrollPane completedQuestsScrollPane;
    private JScrollPane itemsScrollPane;
    private JLabel gameModeLabel;
    private JTable questsTable;
    private JLabel roundLabel;
    private JLabel timeLabel;
    private JLabel dayLabel;
    private JLabel levelLabel;
    private JLabel hpLabel;
    private JMenuBar menuBar;
    private JMenu gameModeMenu;
    private JMenuItem gameModeImpl1MenuItem;
    private JMenuItem gameModeImpl2MenuItem;
    private JMenuItem helpMenuItem;

    private void init() {
//        gameModeMenu.text
        sendActionButton.setText("Send");
        storyTextArea.setEditable(false);
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);
        DefaultTableModel itemsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemsTableModel.setColumnIdentifiers(new Object[]{"Equipment"});
        itemsTable.setModel(itemsTableModel);
        DefaultTableModel questsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        questsTableModel.setColumnIdentifiers(new Object[]{"Quests"});
        questsTable.setModel(questsTableModel);
        DefaultTableModel completedQuestsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        completedQuestsTableModel.setColumnIdentifiers(new Object[]{"Completed Quests"});
        completedQuestsTable.setModel(completedQuestsTableModel);
        gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        gameStatus = gameStatusService.getNewGameStatus();
        menuBar = new JMenuBar();
        gameModeMenu = new JMenu("Game Mode");
        gameModeImpl1MenuItem = new JMenuItem("Impl1");
        gameModeImpl2MenuItem = new JMenuItem("Impl2");
        gameModeMenu.add(gameModeImpl1MenuItem);
        gameModeMenu.add(gameModeImpl2MenuItem);
        menuBar.add(gameModeMenu);
        menuBar.add(Box.createHorizontalGlue());
        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuBar.add(helpMenuItem);
    }

    private void updateQuestsTable(List<String> quests) {
        DefaultTableModel model = (DefaultTableModel) questsTable.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (String quest : quests) {
            model.addRow(new Object[]{quest});
        }
    }

    private void updateCompletedQuestsTable(List<String> completedQuests) {
        DefaultTableModel model = (DefaultTableModel) completedQuestsTable.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (String completedQuest : completedQuests) {
            model.addRow(new Object[]{completedQuest});
        }
    }

    private void updateItemsTable(List<String> items) {
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (String item : items) {
            model.addRow(new Object[]{item});
        }
    }

    private void updateStatusLabels() {
        roundLabel.setText("Round: " + gameStatus.getRound());
        timeLabel.setText("Time: " + gameStatus.getTime());
        dayLabel.setText("Day: " + gameStatus.getDay());
        xpLabel.setText("XP: " + gameStatus.getXp());
        levelLabel.setText("Level: " + gameStatus.getLevel());
        hpLabel.setText("HP: " + gameStatus.getHp());
    }

    private void updateUi() {
        gameModeLabel.setText(gameStatus.getGameMode());
        storyTextArea.setText(String.join("\n", gameStatus.getMessages()));
        updateQuestsTable(gameStatus.getQuests());
        updateCompletedQuestsTable(gameStatus.getCompletedQuests());
        updateItemsTable(gameStatus.getInventory());
        updateStatusLabels();
    }

    private void updateUiAfterMessage() {
        storyTextArea.append(gameStatus.getMessages().get(gameStatus.getMessages().size() - 1) + "\n");
        updateQuestsTable(gameStatus.getQuests());
        updateCompletedQuestsTable(gameStatus.getCompletedQuests());
        updateItemsTable(gameStatus.getInventory());
        updateStatusLabels();
    }

    private void createMenuListener(JFrame parent) {
        gameModeImpl1MenuItem.addActionListener(e -> {
            gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
            gameStatus = gameStatusService.getNewGameStatus();
            updateUi();
        });
        gameModeImpl2MenuItem.addActionListener(e -> {
            gameStatusService = new GameStatusService(new GameLogicServiceImpl2());
            gameStatus = gameStatusService.getNewGameStatus();
            updateUi();
        });
        helpMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(parent, "Help:\n" +
                    "1. Enter action in text field\n" +
                    "2. Press 'Send' button\n" +
                    "3. Enjoy the game!\n" +
                    "4. If you want to change game mode, press 'Game Mode' button in menu bar\n");
        });
    }

    private void sendActionButtonEvent() {
        String action = actionTextField.getText();
        storyTextArea.append(action + "\n");
        actionTextField.setText("");
        gameStatus = gameStatusService.getNextGameStatus(gameStatus, action);
        updateUiAfterMessage();
    }

    private void createSendActionButtonListener() {
        sendActionButton.addActionListener(e -> sendActionButtonEvent());
    }

    public GuiForm(JFrame parentFrame) {
        init();
        updateUi();
        createMenuListener(parentFrame);
        createSendActionButtonListener();
    }

    public static void buildGuiForm() {
        JFrame frame = new JFrame("GuiForm");
        GuiForm guiForm = new GuiForm(frame);
        frame.setJMenuBar(guiForm.menuBar);
        frame.setContentPane(guiForm.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setMaximumSize(new Dimension(1200, 800));
        frame.setVisible(true);
    }
}