package example.org;

import javax.swing.*;

public class GuiForm {
    private final AiResponse aiResponse = new AiResponse();
    private JPanel panelMain;
    private JTextField actionTextField;
    private JButton sendActionButton;
    private JTextArea StoryTextArea;
    private JTable items;

    private void initUiComponents() {
        sendActionButton.setText("Send");
        StoryTextArea.setEditable(false);
        StoryTextArea.setLineWrap(true);
        StoryTextArea.setWrapStyleWord(true);
    }

    public GuiForm() {
        initUiComponents();

        sendActionButton.addActionListener(e -> {
            String action = actionTextField.getText();
            StoryTextArea.append(action + "\n");
            actionTextField.setText("");
            String response = aiResponse.getResponse(action);
            StoryTextArea.append(response + "\n");
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