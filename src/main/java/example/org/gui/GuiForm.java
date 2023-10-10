package example.org.gui;

import example.org.service.GameStatusService;
import example.org.dto.GameStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GuiForm {
    private final GameStatusService gameStatusService = new GameStatusService();
    private JPanel panelMain;
    private JTextField actionTextField;
    private JButton sendActionButton;
    private JTextArea StoryTextArea;
    private JTable itemsTable;
    private JLabel statusLabel;
    private GameStatus gameStatus;

    private void init() {
        sendActionButton.setText("Send");
        StoryTextArea.setEditable(false);
        StoryTextArea.setLineWrap(true);
        StoryTextArea.setWrapStyleWord(true);
        DefaultTableModel model = new DefaultTableModel();
        itemsTable.setModel(model);
        model.addColumn("items");
        gameStatus = gameStatusService.getNewGameStatus();
    }

    private void updateTable(List<String> items) {
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        for (int i = model.getRowCount(); i < items.size(); i++) {
            model.addRow(new Object[]{items.get(i)});
        }
    }

    private void updateUi() {
        StoryTextArea.append(gameStatus.getMessages().get(gameStatus.getMessages().size() - 1) + "\n");
        updateTable(gameStatus.getInventory());
        statusLabel.setText("     Round: " + gameStatus.getRound() + "     Time: " + gameStatus.getTime() + "     Day: " + gameStatus.getDay() + "     Weather: " + gameStatus.getWeather() + "     XP: " + gameStatus.getXp());
    }

    public GuiForm() {
        init();
        updateUi();

        sendActionButton.addActionListener(e -> {
            String action = actionTextField.getText();
            StoryTextArea.append(action + "\n");
            actionTextField.setText("");
            gameStatus = gameStatusService.getNextGameStatus(gameStatus, action);
            updateUi();
        });
    }

    public static void buildGuiForm() {
        JFrame frame = new JFrame("GuiForm");
        frame.setContentPane(new GuiForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setVisible(true);
    }
}