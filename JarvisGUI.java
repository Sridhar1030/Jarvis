import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class JarvisGUI extends JFrame {
    private JTextArea textArea;
    private JPasswordField passwordField;

    public JarvisGUI() {
        // Create a login dialog
        String password = "111"; // Set your desired password here
        boolean loggedIn = false;
        for (int attempts = 0; attempts < 3; attempts++) {
            passwordField = new JPasswordField(10);
            int inputResult = JOptionPane.showConfirmDialog(null, passwordField, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
            if (inputResult == JOptionPane.OK_OPTION) {
                String input = new String(passwordField.getPassword());
                if (input != null && input.equals(password)) {
                    loggedIn = true;
                    break;
                }
            } else {
                System.exit(0);
            }
        }

        if (!loggedIn) {
            System.exit(0);
        }

        setTitle("Jarvis GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a JTextField for entering commands
        JTextField commandField = new JTextField(10);
        commandField.setBackground(new Color(156, 245, 253));
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BorderLayout());
        commandPanel.add(commandField, BorderLayout.CENTER);
        add(commandPanel, BorderLayout.SOUTH);

        // Create a JTextArea for displaying output
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        Color lightBlue = new Color(156, 245, 253);
        textArea.setBackground(lightBlue);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Create a JButton to submit user input
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitCommand(commandField);
            }
        });

        // Create a custom font for the "JARVIS" text
        Font eurostileFont = new Font("Eurostile LT Std Bold Extended 2", Font.BOLD, 12);

        // Add the word "JARVIS" near the Submit button
        JLabel jarvisLabel = new JLabel("JARVIS");
        jarvisLabel.setFont(eurostileFont); // Set the font to Eurostile
        jarvisLabel.setForeground(Color.BLACK); // Set the color to black
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(jarvisLabel, BorderLayout.WEST); // Place "JARVIS" to the left of the button
        buttonPanel.add(submitButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.NORTH);

        commandField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitCommand(commandField);
                }
            }
        });

        Jarvis.setGUIReference(this);
    }

    public void appendText(String text) {
        textArea.append(text);
    }

    private void submitCommand(JTextField commandField) {
        String userInput = commandField.getText();
        String response = Jarvis.processCommand(userInput);
        String fullResponse = "User: " + userInput + "\n" + "Jarvis: " + response + "\n";

        textArea.append(fullResponse);
        if (textArea.getDocument().getLength() > 0) {
            textArea.append("\n");
        }

        commandField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JarvisGUI gui = new JarvisGUI();
                gui.setVisible(true);
            }
        });
    }
}
