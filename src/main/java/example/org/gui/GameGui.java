package example.org.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    private void updateStatusLabels() {
        roundLabel.setText("Round: " + gameStatus.getRound());
        timeLabel.setText("Time: " + gameStatus.getTime());
        dayLabel.setText("Day: " + gameStatus.getDay());
        xpLabel.setText("XP: " + gameStatus.getXp());
        levelLabel.setText("Level: " + gameStatus.getLevel());
        hpLabel.setText("HP: " + gameStatus.getHp());
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
