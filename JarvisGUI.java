import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JarvisGUI extends JFrame {
    private JTextArea textArea;
    private JPasswordField passwordField;
    private boolean isUserThere = true;

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
                isUserThere = true; // User responded, reset inactivity timer
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
                    isUserThere = true; // User responded, reset inactivity timer
                    submitCommand(commandField);
                }
            }
        });

        Jarvis.setGUIReference(this);

        // Start the inactivity timer in a separate thread
        Thread inactivityThread = new Thread(() -> inactivityTimer());
        inactivityThread.start();
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

       
    private void inactivityTimer() {
        long inactivityThreshold = 6000; // 1 minute (in milliseconds)
        long shutdownDelay = 15000; // 15 seconds (in milliseconds)
    
        while (true) {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastActivity = currentTime - lastActivityTime;
    
            if (timeSinceLastActivity >= inactivityThreshold) {
                // Check if the user is there
                if (isUserThere) {
                    isUserThere = false; // Reset the flag
                } else {
                    // This part will shut down the program if no response within 15 seconds
                    long shutdownTime = System.currentTimeMillis() + shutdownDelay;
                    while (System.currentTimeMillis() < shutdownTime) {
                        int response = JOptionPane.showConfirmDialog(this, "Are you there?", "Inactivity Alert", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.YES_OPTION) {
                            isUserThere = true;
                            lastActivityTime = System.currentTimeMillis(); // Reset the timer
                            break;
                        }
                        try {
                            Thread.sleep(1000); // Check every 1 second
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
    
                    if (!isUserThere) {
                        System.exit(0); // Shut down the program if no response within 15 seconds
                    }
                }
            }
    
            try {
                Thread.sleep(1000); // Check every 1 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private long lastActivityTime = System.currentTimeMillis();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JarvisGUI gui = new JarvisGUI();
                gui.setVisible(true);
            }
        });
    }
}
