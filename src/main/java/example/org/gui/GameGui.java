package example.org.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GameGui extends GuiForm {
    ActionHandler actionHandler = new ActionHandler();
    ComponentInitializer componentInitializer = new ComponentInitializer();

    public GameGui(JFrame parentFrame) {
        super();
        this.parentFrame = parentFrame;
        componentInitializer.initializeGui(this);
        actionHandler.createListeners(this);
        updateUi();
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

    private void lightLabel(JLabel label, Color color) {
        Color labelColor = label.getForeground();
        label.setForeground(color); // Change label text color
        Timer timer = new Timer(2000, e -> {
            label.setForeground(labelColor); // Change the color back to black after 1 second
        });
        timer.setRepeats(false); // Make the timer run only once
        timer.start();
    }

    private void updateStatusLabels() {
        roundLabel.setText("Round: " + gameStatus.getRound());
        timeLabel.setText("Time: " + gameStatus.getTime());
        dayLabel.setText("Day: " + gameStatus.getDay());
        String xpText = "XP: " + gameStatus.getXp();
        String levelText = "Level: " + gameStatus.getLevel();
        String hpText = "HP: " + gameStatus.getHp();
        if (!xpLabel.getText().equals(xpText)) {
            lightLabel(xpLabel, new Color(0, 150, 255));
            xpLabel.setText(xpText);
        }
        if (!levelLabel.getText().equals(levelText)) {
            lightLabel(levelLabel, new Color(0, 0, 139));

            levelLabel.setText(levelText);
        }
        int prevHp;
        try {
            prevHp = Integer.parseInt(hpLabel.getText().substring(4));
            if (prevHp < gameStatus.getHp()) {
                lightLabel(hpLabel, Color.GREEN);
            } else if (prevHp > gameStatus.getHp()) {
                lightLabel(hpLabel, Color.RED);
            }
        } catch (NumberFormatException e) {
            // Do nothing
        } finally {
            hpLabel.setText(hpText);
        }
    }

    public void sendEnabled() {
        sendActionButton.setEnabled(true);
        sendActionButton.setText("Send");
    }

    public void sendDisabled() {
        sendActionButton.setEnabled(false);
        sendActionButton.setText("Wait...");
    }

    public void updateUi() {
        gameModeLabel.setText("Game Mode: " + gameStatus.getGameMode());
        storyTextArea.setText(String.join("\n", gameStatus.getMessages()) + "\n");
        updateQuestsTable(gameStatus.getQuests());
        updateCompletedQuestsTable(gameStatus.getCompletedQuests());
        updateItemsTable(gameStatus.getInventory());
        updateStatusLabels();
    }

    public void updateUiAfterMessage() {
        updateQuestsTable(gameStatus.getQuests());
        updateCompletedQuestsTable(gameStatus.getCompletedQuests());
        updateItemsTable(gameStatus.getInventory());
        updateStatusLabels();
    }
}
