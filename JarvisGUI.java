import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class JarvisGUI extends JFrame {
    private JTextArea textArea;
    private JTextField commandField;

    public JarvisGUI() {
     // j frame bana raha hai 
        setTitle("Jarvis GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
         
         // Create a JTextField for entering commands
        commandField = new JTextField(10);
        add(commandField, BorderLayout.NORTH);

        // Create a JTextArea for displaying output
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

       
        // Create a JButton to submit user input
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitCommand();
            }
        });
        add(submitButton, BorderLayout.SOUTH);

       //enter key ka function
        commandField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitCommand();
                }
            }
        });

        //gui ko reference in java 
           Jarvis.setGUIReference(this);
    }

    public void appendText(String text) {
        textArea.append(text);
    }

    private void submitCommand() {
        String userInput = commandField.getText();
        //yaha apna userinput backend me jaega 
        String response = Jarvis.processCommand(userInput);
    
        // Construct the complete response message
        String fullResponse = "User: " + userInput + "\n" + "Jarvis: " + response + "\n";
    
        //ye mouse pointer ka location pata karta hai jisme agar koi bich ke text ko nikalna hoga toh kar sakte hai 
        int caretPosition = textArea.getCaretPosition();
    
        // If the caret position is at the beginning, simply append the response
        if (caretPosition == 0) {
            textArea.append(fullResponse);
        } else {
            // Otherwise, insert the new response at the beginning of the text area
            textArea.insert(fullResponse, 0);
        }
    
        // Clear the command field after submission
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
   