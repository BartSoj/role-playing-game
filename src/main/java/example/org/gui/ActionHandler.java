package example.org.gui;

import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicServiceImpl1;
import example.org.service.gamelogic.GameLogicServiceImpl2;
import example.org.service.gamelogic.GameLogicServiceImpl3;
import example.org.service.gamelogic.GameLogicServiceImpl4;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionHandler {
    private void getNextTurn(GameGui gameGui) {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                gameGui.sendDisabled();
                AtomicBoolean isFirst = new AtomicBoolean(true);
                gameGui.gameStatusService.getFlowableDescription(gameGui.gameStatus)
                        .doOnNext(descriptionChunk -> {
                            if (isFirst.getAndSet(false)) {
                                gameGui.gameStatus.getMessages().add(descriptionChunk);
                                publish(descriptionChunk);
                            } else {
                                String accumulatedDescription = gameGui.gameStatus.getMessages().get(gameGui.gameStatus.getMessages().size() - 1);
                                gameGui.gameStatus.getMessages().set(gameGui.gameStatus.getMessages().size() - 1, accumulatedDescription + descriptionChunk);
                                publish(descriptionChunk);
                            }
                        }).doOnComplete(() -> {
                            publish("\n");
                            gameGui.gameStatus = gameGui.gameStatusService.getNextGameStatus(gameGui.gameStatus);
                        })
                        .blockingSubscribe();
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    gameGui.storyTextArea.append(chunk);
                }
            }

            @Override
            protected void done() {
                gameGui.sendEnabled();
                gameGui.updateUiAfterMessage();
            }
        };

        worker.execute();
    }


    private void showHelpDialog(JFrame parentFrame) {
        JOptionPane.showMessageDialog(parentFrame, """
                Help:
                1. Enter action in text field
                2. Press 'Send' button
                3. Enjoy the game!
                4. If you want to change game mode, press 'Game Mode' button in menu bar
                """);
    }

    private void createMenuListener(GameGui gameGui) {
        gameGui.gameModeImpl1MenuItem.addActionListener(e -> {
            gameGui.gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
            gameGui.gameStatus = gameGui.gameStatusService.getNewGameStatus();
            gameGui.updateUi();
        });
        gameGui.gameModeImpl2MenuItem.addActionListener(e -> {
            gameGui.gameStatusService = new GameStatusService(new GameLogicServiceImpl2());
            gameGui.gameStatus = gameGui.gameStatusService.getNewGameStatus();
            gameGui.updateUi();
        });
        gameGui.gameModeImpl3MenuItem.addActionListener(e -> {
            gameGui.gameStatusService = new GameStatusService(new GameLogicServiceImpl3());
            gameGui.gameStatus = gameGui.gameStatusService.getNewGameStatus();
            gameGui.updateUi();
        });
        gameGui.gameModeImpl4MenuItem.addActionListener(e -> {
            gameGui.gameStatusService = new GameStatusService(new GameLogicServiceImpl4());
            gameGui.gameStatus = gameGui.gameStatusService.getNewGameStatus();
            gameGui.updateUi();
        });

        gameGui.helpMenuItem.addActionListener(e -> showHelpDialog(gameGui.parentFrame));

    }

    private void createActionFieldFocusListener(GameGui gameGui) {
        gameGui.actionTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (gameGui.actionTextField.getText().equals("Enter action here")) {
                    gameGui.actionTextFieldActive();
                }
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (gameGui.actionTextField.getText().isEmpty()) {
                    gameGui.actionTextFieldDefault();
                }
                super.focusLost(e);
            }
        });
    }

    private void sendActionButtonEvent(GameGui gameGui) {
        String action = gameGui.actionTextField.getText();
        if (!"".equals(action) && !"Enter action here".equals(action) && gameGui.sendActionButton.isEnabled()) {
            gameGui.storyTextArea.append(action + "\n");
            gameGui.actionTextFieldActive();
            gameGui.gameStatus.getMessages().add(action);
            getNextTurn(gameGui);
        }
    }

    private void createSendActionButtonListener(GameGui gameGui) {
        gameGui.sendActionButton.addActionListener(e -> sendActionButtonEvent(gameGui));
        gameGui.actionTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendActionButtonEvent(gameGui);
                }
            }
        });
    }

    public void createListeners(GameGui gameGui) {
        createMenuListener(gameGui);
        createActionFieldFocusListener(gameGui);
        createSendActionButtonListener(gameGui);
    }
}
