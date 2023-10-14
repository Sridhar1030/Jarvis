import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader; // Add this line
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.FileWriter;
class BrightnessControl {
    // Brightness adjust function
    public void inc(Scanner scanner) {
        try {
            System.out.print("Enter the desired brightness level (0-100): ");
            int brightnessLevel = scanner.nextInt();

            if (brightnessLevel < 0 || brightnessLevel > 100) {
                System.out.println("Invalid brightness level. Please enter a value between 0 and 100.");
                return;
            }

            // Execute a command to set the brightness using the Windows command line
            String command = "powershell (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1, " + brightnessLevel + ")";
            Process process = Runtime.getRuntime().exec(command);

            // Wait for the process to finish
            process.waitFor();

            System.out.println("Brightness set to " + brightnessLevel + "%");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Jarvis {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static Scanner scanner = new Scanner(System.in);
    private static JarvisGUI gui; // Reference to the GUI
    private static long lastActivityTime;
     // Include the FileUtils class here
     private static class FileUtils {
        public static void writeToFile(String filePath, String content) {
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void clickPhoto() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start microsoft.windows.camera:");
            Process process = processBuilder.start();

            // Wait for the camera app to open
            TimeUnit.SECONDS.sleep(5);

            // Simulate pressing the Enter key
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            process.destroy();

            System.out.println("Photo taken!");
        } catch (IOException | InterruptedException | AWTException e) {
            e.printStackTrace();
        }
    }

    // Commands calling here
    public static String processCommand(String command) {
        String response = "";

        if (command.contains("hello") || command.contains("hey") || command.contains("hi") || command.contains("hola")) {
            response = "Hello, how can I assist you?";
        } else if (command.contains("time")) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            response = "The current time is " + dateFormat.format(date);
        } else if (command.contains("photo")) {
            Jarvis jarvisInstance = new Jarvis();
            jarvisInstance.clickPhoto();
            response = "Taking a photo for you.";
        } else if (command.contains("brightness")) {
            try {
                BrightnessControl obj = new BrightnessControl();
                obj.inc(scanner); // Pass the scanner to the BrightnessControl function
            } catch (Exception e) {
                response = "An error occurred while adjusting brightness.";
            }
        } else if (command.contains("write")) {
            response = "What would you like to write?";
        } else if (command.contains("hi") || command.contains("hello") || command.contains("hey")) {
            response = "Hello, how are you?";
        } else if (command.contains("search")) {
            String searchQuery = command.replace("search", "").trim();
            response = "Searching for: " + searchQuery;

            search(searchQuery);
        } else if (command.contains("play") || command.contains("watch") || command.contains("watching")) {
            response = "Playing...";
            String searchQuery = command.replace("play", "").replace("watch", "").replace("watching", "").trim();

            Ytsearch(searchQuery);
        } else if (command.contains("songs")) {
            response = "Playing songs...";
        } else if (command.contains("exit") || command.contains("bye") || command.contains("shutdown")) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else if (command.contains("system properties")) {
            Jarvis jarvisInstance = new Jarvis();
            jarvisInstance.displaySystemProperties();
           response = "Displaying system properties.";
        } else if (command.contains("file")) {
            String fileName = command.replace("file", "").trim();
            response = "Searching for file: " + fileName;
            searchFileInExplorer(fileName);
        }
        else if (command.contains("note") || command.contains("write")) {
            response = "Please enter your note. Type 'exit' on a new line to finish.";
            takeNotes();
        }
        else if (command.contains("lock")) {
            response = "Please enter the password to lock the computer:";
            System.out.println(response);

            // Wait for user to input password
            Scanner scanner = new Scanner(System.in);
            String password = scanner.nextLine();

            // Check if the password is correct (you can customize this logic)
            if (isPasswordCorrect(password)) {
                lockComputer();
                response = "Computer locked.";
            } else {
                response = "Incorrect password. Computer not locked.";
            }
        }
        else if (command.contains("exit") || command.contains("bye") || command.contains("shutdown")) {
            System.out.println("Goodbye!");
            System.exit(0);
        } 
         else {
            response = "I'm sorry, I don't understand that command.";
        }

        // Update the last activity time
        lastActivityTime = System.currentTimeMillis();

        return response;
    }

    // Function declarations here
    private void displaySystemProperties() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "systeminfo");
            Process process = processBuilder.start();
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("System Properties:");
    
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void takeNotes() {
        try {
            StringBuilder note = new StringBuilder();
            String line;
    
            // Prompt the user to enter notes
            while (true) {
                System.out.print("Your note: ");
                line = br.readLine();
    
                // Check if the user wants to exit
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
    
                // Append the note
                note.append(line).append("\n");
            }
    
            // Save the note to a file (you can specify the file path)
            String filePath = "notes.txt";
            FileUtils.writeToFile(filePath, note.toString());
    
            System.out.println("Note saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void searchFileInExplorer(String fileName) {
        try {
            // Open File Explorer
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_E);
            robot.keyRelease(KeyEvent.VK_E);
            robot.keyRelease(KeyEvent.VK_WINDOWS);

            // Wait for File Explorer to open
            Thread.sleep(2000);

            // Simulate navigating to the search bar
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_F);
            robot.keyRelease(KeyEvent.VK_F);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            
            // Type the file name
            String[] words = fileName.split("\\s+");
            for (String word : words) {
                for (char c : word.toCharArray()) {
                    int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                }
                robot.keyPress(KeyEvent.VK_SPACE); // Add a space between words
                robot.keyRelease(KeyEvent.VK_SPACE);
            }

            // Simulate hitting Enter to start the search
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Wait for the search to complete
            Thread.sleep(2000);

            System.out.println("Searching for file: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean isPasswordCorrect(String password) {
     return password.equals("Sri");
    }
    private static void lockComputer() {
        try {
            // Lock the computer using the rundll32.exe command
            ProcessBuilder processBuilder = new ProcessBuilder("rundll32.exe", "user32.dll,LockWorkStation");
            processBuilder.start();

            // Wait for a moment to give time for the computer to lock
            TimeUnit.SECONDS.sleep(2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void Ytsearch(String query) {
        String searchUrl = "https://www.youtube.com/search?q=" + query.replace(" ", "+");
        gui.appendText("Searching for: " + query + "\n");

        // Create a new thread to display dots
        Thread dotsThread = new Thread(new DotsRunnable(gui, searchUrl));
        dotsThread.start(); // Start the thread
    }

    private static class DotsRunnable implements Runnable {
        private JarvisGUI gui;
        private String searchUrl;

        public DotsRunnable(JarvisGUI gui, String searchUrl) {
            this.gui = gui;
            this.searchUrl = searchUrl;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 6; i++) {
                    gui.appendText(".");
                    TimeUnit.MILLISECONDS.sleep(500);
                }
                gui.appendText("\n");
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", searchUrl);
                processBuilder.start();
                Thread.sleep(700);
                // Create a Robot instance to move the pointer and perform a right-click
                Robot robot = new Robot();
                robot.mouseMove(440, 665);
                Thread.sleep(5000);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            } catch (InterruptedException | IOException | AWTException e) {
                e.printStackTrace();
            }
        }
    }

    private static void search(String searchQuery) {
        String searchUrl = "https://www.google.com/search?q=" + searchQuery.replace(" ", "+");
        gui.appendText("Searching for: " + searchQuery + "\n");

        // Create a new thread to display dots
        Thread dotsThread = new Thread(new DotsRunnable(gui, searchUrl));
        dotsThread.start(); // Start the thread
    }

    public static void main(String[] args) {
        lastActivityTime = System.currentTimeMillis(); // Initialize last activity time

        // Start the inactivity timer in a separate thread
        Thread inactivityThread = new Thread(() -> inactivityTimer());
        inactivityThread.start();

        try {
            while (true) {
                System.out.print("Enter a command: ");
                String userInput = br.readLine();
                lastActivityTime = System.currentTimeMillis(); // Update last activity time
                String response = processCommand(userInput);
                System.out.println("Response: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inactivity timer
    private static void inactivityTimer() {
        long inactivityThreshold = 60000; // 1 minute (in milliseconds)
        long shutdownDelay = 15000; // 15 seconds (in milliseconds)

        while (true) {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastActivity = currentTime - lastActivityTime;

            if (timeSinceLastActivity >= inactivityThreshold) {
                System.out.println("Are you there?");
                try {
                    TimeUnit.SECONDS.sleep(15); // Wait for 15 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Check inactivity again after 15 seconds
                if ((System.currentTimeMillis() - lastActivityTime) >= inactivityThreshold) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }

            try {
                Thread.sleep(5000); // Check every 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // jarvisGUI se jo reference liya tha wo
    public static void setGUIReference(JarvisGUI gui) {
        Jarvis.gui = gui;
    }
}
