/*
    CS335 Graphics and Multimedia
    Author: Chelina Ortiz Montanez
    Title: Program 0
    Description: Java program that sets up a memory game using JFrame, JPanel, Timers
        and action listeners. The game will have one "joker" card that is showed whenever
        it is selected and remains static afterwards. The game will allow the user to
        select a card, wait until the selection of the next card, compare both of them,
        if they match the cards will remain displayed, and if they don't match, both
        selected cards will be hidden again. There will be a start and a reset cardButton.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MemoryGame extends JFrame implements ActionListener
{
    // Core game play objects
    private Board gameBoard;
    private FlippableCard prevCard1, prevCard2;

    // Labels to display game info
    private JLabel errorLabel, timerLabel, correctLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    // Record keeping counts and times
    private int clickCount = 0, gameTime = 0, errorCount = 0, pairsFound = 0;

    // Game timer: will be configured to trigger an event every second
    private Timer gameTimer;

    public MemoryGame() {
        // Call the base class constructor
        super("Ocean Memory Game");

        // Allocate the interface elements
        JButton restart = new JButton("Start or Restart");
        timerLabel = new JLabel("Timer: 0");
        errorLabel = new JLabel("Errors: 0");
        correctLabel = new JLabel("Pairs found: 0");
        /*
         * To-Do: Setup the interface objects (timer, error counter, buttons)
         * and any event handling they need to perform
         */
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // Restart button implementation
                restartGame();
                gameTimer.start();
            }
        });
        gameTimer = new Timer(1000, new ActionListener() { // Game timer
            public void actionPerformed(ActionEvent e) {
                timerLabel.setText("Timer: " + gameTime++);
            }
        });

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        gameBoard = new Board(25, this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(5, 5, 2, 0));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 5, 2, 2));
        labelView.add(restart);
        labelView.add(timerLabel);
        labelView.add(errorLabel);
        labelView.add(correctLabel);

        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(800, 820);
        setVisible(true);
        // Start timer
        //gameTimer.start();
    }

    /* Handle anything that gets clicked and that uses MemoryGame as an
     * ActionListener */
    public void actionPerformed(ActionEvent e) {
        // Get the currently clicked card from a click event
        FlippableCard currCard = (FlippableCard) e.getSource();
        // Show the front of the card and disable its action listener.
        currCard.showFront();
        currCard.removeActionListener(this);

        if (currCard.id() == 13) { // Check if the selected card is the wild card
            currCard.setEnabled(false);
            currCard.showFront();
            currCard = null;
        }
        if (prevCard1 == null) { // Assign a pointer to our current card (either prevCard1 or prevCard2)
            prevCard1 = currCard;
        } else if (prevCard2 == null || prevCard2.id() != currCard.id()) { // Check if the current card was previously pointed to by the pointer
            prevCard2 = currCard;
        }

        if (prevCard1 != null && prevCard2 != null) { // Check if we have two pointers to different cards
            System.out.println("PrevCard1: " + prevCard1.id() + " PrevCard2: " + prevCard2.id());
            compare(prevCard1, prevCard2);
            prevCard1.addActionListener(this); // Reactivate the action listener for the cards
            prevCard2.addActionListener(this);
            prevCard1 = null; // Set the pointers to null, to allow them to be reused
            prevCard2 = null;
        }
        System.out.println(clickCount++); // Increase the click count
    }

    private void restartGame() {
        // Reset variables, the board and start over
        pairsFound = 0;
        gameTime = 0;
        clickCount = 0;
        errorCount = 0;
        prevCard1 = null;
        prevCard2 = null;
        timerLabel.setText("Timer: " + gameTime);
        errorLabel.setText("Errors: " + errorCount);

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
    }

    public void compare(FlippableCard prevCard1, FlippableCard prevCard2) { // Compare two cards to determine if they are the same
        if (prevCard1.id() != prevCard2.id()) { // check for different IDs
            System.out.println("Wrong");
            prevCard1.hideFront();
            prevCard2.hideFront();
            errorCount++; // Increase the error count
            errorLabel.setText("Errors: " + errorCount);
        } else {
            pairsFound++; // Increase the pair count
            correctLabel.setText("Pairs found: " + pairsFound);
            prevCard1.setEnabled(false);
            prevCard2.setEnabled(false);
            if (pairsFound == 12) { // If all pairs are found
                correctLabel.setText("GAME OVER");
                gameTimer.stop(); // Stop timer, wait for user to reset game
            }
        }
    }

    public static void main(String args[])
    {
        MemoryGame M = new MemoryGame();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
