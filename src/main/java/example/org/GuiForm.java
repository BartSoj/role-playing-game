package example.org;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GuiForm {
    private final AiResponse aiResponse = new AiResponse();
    private JPanel panelMain;
    private JTextField actionTextField;
    private JButton sendActionButton;
    private JTextArea StoryTextArea;
    private JTable itemsTable;

    private void initUiComponents() {
        sendActionButton.setText("Send");
        StoryTextArea.setEditable(false);
        StoryTextArea.setLineWrap(true);
        StoryTextArea.setWrapStyleWord(true);
        DefaultTableModel model = new DefaultTableModel();
        itemsTable.setModel(model);
        model.addColumn("items");
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        List<String> items = aiResponse.getItemsList();
        for (int i = model.getRowCount(); i < items.size(); i++) {
            model.addRow(new Object[]{items.get(i)});
        }
    }

    public GuiForm() {
        initUiComponents();

        sendActionButton.addActionListener(e -> {
            String action = actionTextField.getText();
            StoryTextArea.append(action + "\n");
            actionTextField.setText("");
            String response = aiResponse.getResponse(action);
            StoryTextArea.append(response + "\n");
            updateTable();
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