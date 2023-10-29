package example.org.gui;

import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicServiceImpl1;

import javax.imageio.ImageIO;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ComponentInitializer {

    private void setIcon(GameGui gameGui) {
        // Set the icon
        try {
            // Use the class's class loader to load the image
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("icon.png");
            BufferedImage iconImage = ImageIO.read(resourceStream);
            gameGui.parentFrame.setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGameStatus(GameGui gameGui) {
        gameGui.gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        gameGui.gameStatus = gameGui.gameStatusService.getNewGameStatus();
    }

    private void setBackground(GameGui gameGui) {
        BufferedImage backgroundImage = null;
        // Load the background image from resources
        try {
            // Use the class's class loader to load the image
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("background.jpg");
            backgroundImage = ImageIO.read(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((GameGui.BackgroundPanel) gameGui.panelMain).setBackgroundImage(backgroundImage);
    }

    private void initInfoPanel(GameGui gameGui) {

    }

    private void initStoryTextArea(GameGui gameGui) {
        gameGui.storyTextAreaScrollPane.setOpaque(false);
        gameGui.storyTextAreaScrollPane.getViewport().setOpaque(false);
        gameGui.storyTextAreaScrollPane.setBorder(null);
        gameGui.storyTextArea.setOpaque(false);
        gameGui.storyTextArea.setEditable(false);
        gameGui.storyTextArea.setLineWrap(true);
        gameGui.storyTextArea.setWrapStyleWord(true);
    }

    private void initTables(GameGui gameGui) {
        gameGui.tableInfoPanel.setOpaque(false);
        gameGui.itemsScrollPane.setOpaque(false);
        gameGui.itemsScrollPane.getViewport().setOpaque(false);
        gameGui.itemsScrollPane.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        gameGui.itemsTable.setBackground(new Color(0, 0, 0, 0));
        gameGui.questsScrollPane.setOpaque(false);
        gameGui.questsScrollPane.getViewport().setOpaque(false);
        gameGui.questsScrollPane.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        gameGui.questsTable.setBackground(new Color(0, 0, 0, 0));
        gameGui.completedQuestsScrollPane.setOpaque(false);
        gameGui.completedQuestsScrollPane.getViewport().setOpaque(false);
        gameGui.completedQuestsScrollPane.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        gameGui.completedQuestsTable.setBackground(new Color(0, 0, 0, 0));

        // Set the table models
        DefaultTableModel itemsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gameGui.itemsLabel.setText("Items");
        itemsTableModel.addColumn("Items");
        gameGui.itemsTable.setModel(itemsTableModel);
        gameGui.itemsTable.setTableHeader(null);
        DefaultTableModel questsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gameGui.questsLabel.setText("Quests");
        questsTableModel.addColumn("Quests");
        gameGui.questsTable.setModel(questsTableModel);
        gameGui.questsTable.setTableHeader(null);
        DefaultTableModel completedQuestsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gameGui.completedQuestsLabel.setText("Completed Quests");
        completedQuestsTableModel.addColumn("Completed Quests");
        gameGui.completedQuestsTable.setModel(completedQuestsTableModel);
        gameGui.completedQuestsTable.setTableHeader(null);
    }

    private void initStatusPanel(GameGui gameGui) {
        gameGui.statusPanel.setOpaque(false);
        gameGui.statusPanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
    }

    private void initActionPanel(GameGui gameGui) {
        gameGui.actionPanel.setOpaque(false);
        gameGui.actionTextFieldPanel.setOpaque(false);
        gameGui.actionTextFieldPanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        gameGui.actionTextField.setOpaque(false);
        gameGui.actionTextField.setBackground(new Color(0, 0, 0, 0));
        gameGui.actionTextField.setBorder(null);
        gameGui.sendActionButton.setText("Send");
        gameGui.sendActionButton.setOpaque(false);
        gameGui.sendActionButton.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        gameGui.sendActionButton.setFocusPainted(false);
        gameGui.sendActionButton.setContentAreaFilled(false);
    }

    private void initTopPanel(GameGui gameGui) {
        gameGui.topPanel.setOpaque(false);
        gameGui.topPanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        initInfoPanel(gameGui);
    }

    private void initMiddlePanel(GameGui gameGui) {
        gameGui.middlePanel.setOpaque(false);
        initStoryTextArea(gameGui);
        initTables(gameGui);
    }

    private void initBottomPanel(GameGui gameGui) {
        gameGui.bottomPanel.setOpaque(false);
        initStatusPanel(gameGui);
        initActionPanel(gameGui);
    }

    public void initializeGui(GameGui gameGui) {
        initTopPanel(gameGui);
        initMiddlePanel(gameGui);
        initBottomPanel(gameGui);
        setBackground(gameGui);
        initGameStatus(gameGui);
        setIcon(gameGui);
    }
}
