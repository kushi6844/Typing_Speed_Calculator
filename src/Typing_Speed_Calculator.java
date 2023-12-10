import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Typing_Speed_Calculator extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private JButton startButton;
    private JLabel resultLabel;

    private String[] words;
    private int currentWordIndex;
    private long startTime;
    private String playerName;

    private static final String[] TEXT_CHALLENGES1 = {// printing different texts according to the difficulty level the user has chosen.
            "The sun is shining brightly in the clear blue sky",
            "Birds are chirping, and a gentle breeze is rustling the leaves on the trees",
            "It's a perfect day for a leisurely walk in the park",
            "Take your time and enjoy the simple pleasures of nature"
    };

    private static final String[] TEXT_CHALLENGES2 = {
            "The journey of a thousand miles begins with a single step",
            "Success is not final, failure is not fatal: It is the courage to continue that counts",
            "In the midst of chaos, there is also opportunity. The only way to do great work is to love what you do",
            "Life is what happens when you're busy making other plans. Believe you can and you're halfway there",
            "The best way to predict the future is to create it. Be yourself; everyone else is already taken. The only limit to our realization of tomorrow will be our doubts of today"
    };

    private static final String[] TEXT_CHALLENGES3 = {
            "The enigmatic labyrinth sprawled before the intrepid adventurer, a serpentine maze of convoluted passages and arcane symbols",
            "Shadows danced on the walls as cryptic whispers echoed through the stone corridors, testing the resolve of those daring enough to decipher the enigma within"
    };

    private static final int[] DIFFICULTY_LEVELS = {1, 2, 3};
    private static final int MAX_SCORE = 1000;

    public Typing_Speed_Calculator() { // this is the code for the layout print of the GUI which u see on the screen, like how you'll see the layout and start button on the screen and where u should type the text.
        setTitle("Typing Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        startButton = new JButton("Start");
        add(startButton, BorderLayout.NORTH);

        resultLabel = new JLabel("");
        add(resultLabel, BorderLayout.EAST);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPlayerName();
                startGame();
            }
        });

        setVisible(true);
    }

    private void getPlayerName() {// this code is for taking the username as input
        do {
            playerName = JOptionPane.showInputDialog("Enter your name:");
            if (playerName == null) {
                System.exit(0); // Exit if the user cancels the input dialog
            }
        } while (!isValidPlayerName(playerName)); // if the name is not given, print invalid input.
    }

    private boolean isValidPlayerName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private void startGame() {
        startButton.setEnabled(false);
        resultLabel.setText("");

        int difficulty = getDifficultyLevel();

        // Select a random challenge set based on difficulty
        switch (difficulty) {
            case 1:
                words = TEXT_CHALLENGES1;   // based on the difficulty level, different text challenges are chosen randomly.
                break;
            case 2:
                words = TEXT_CHALLENGES2;
                break;
            case 3:
                words = TEXT_CHALLENGES3;
                break;
            default:
                words = TEXT_CHALLENGES1; // Default to easy if the difficulty is not recognized
                break;
        }

        // Select a random word from the chosen challenge set
        currentWordIndex = new Random().nextInt(words.length);
        textArea.setText(words[currentWordIndex]);

        // Set up the start time once you click the start button.
        startTime = System.currentTimeMillis();

        inputField.setEditable(true);
        inputField.setText("");
        inputField.requestFocus();

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endGame();
            }
        });
    }

    // after typing, the end time is calculated, and the total time taken is measured.
    private void endGame() {
        long endTime = System.currentTimeMillis();
        inputField.setEditable(false);

        // Calculate CPM and WPM
        int typedCharacters = inputField.getText().length();
        long elapsedTime = endTime - startTime;
        double cpm = (double) (typedCharacters) / elapsedTime * 60000;
        double wpm = cpm / 5; // Assuming an average word length of 5 characters

        // Calculate accuracy
        int correctCharacters = 0;
        String typedText = inputField.getText();
        String targetWord = words[currentWordIndex];
        int minLen = Math.min(typedText.length(), targetWord.length());

        for (int i = 0; i < minLen; i++) {
            if (typedText.charAt(i) == targetWord.charAt(i)) {
                correctCharacters++;
            }
        }

        double accuracy = (double) correctCharacters / targetWord.length() * 100;

        // Display results along with the player's name
        resultLabel.setText(String.format("%s's Results - CPM: %.2f | WPM: %.2f | Accuracy: %.2f%%", playerName, cpm, wpm, accuracy));

        // Provide suggestions based on the performance of the user.
        if (wpm < 20) {
            JOptionPane.showMessageDialog(this, "Your typing speed is slow. Try to improve!");
        } else if (accuracy < 90) {
            JOptionPane.showMessageDialog(this, "Your accuracy is low. Practice more!");
        } else {
            JOptionPane.showMessageDialog(this, "Great job! You're doing well.");
        }

        // Reset the game if the user wants to play again.
        startButton.setEnabled(true);
        textArea.setText("");
    }

    private int getDifficultyLevel() {
        int difficulty;

        do { // taking input from the user for choosing the difficulty level.
            String input = JOptionPane.showInputDialog("Choose a difficulty level (1, 2, 3):");
            try {
                difficulty = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                difficulty = -1;
            }

            if (!isValidDifficulty(difficulty)) {// printing an invalid difficulty level if the given level is invalid.
                JOptionPane.showMessageDialog(this, "Invalid difficulty level. Please choose 1, 2, or 3.");
            }

        } while (!isValidDifficulty(difficulty));

        return difficulty;
    }

    private boolean isValidDifficulty(int difficulty) {
        for (int level : DIFFICULTY_LEVELS) {
            if (level == difficulty) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Typing_Speed_Calculator ();
            }
        });
    }
}
