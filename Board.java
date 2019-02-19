import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class Board
{
    private int size;
    private ActionListener AL;
    // Array to hold board cards
    private ArrayList<FlippableCard> cards;

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL)
    {
        // Allocate and configure the game board: an array of cards
        cards = new ArrayList<FlippableCard>();
        this.size = size;
        this.AL = AL;

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < size; i++) {

            // Load the front image from the resources folder
            String imgPath = "res/sea" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));

            // Setup one card at a time
            FlippableCard c = new FlippableCard(img);

            c.setCustomName(imgPath);
            c.setID(imageIdx);

            // Add them to the array
            cards.add(c);
            c.addActionListener(AL);
            //c.hideFront();

            if(i % 2 != 0){ //We only want two cards to have the same image, so change the index on every odd i
                imageIdx++;  // get ready for the next pair of cards
            }
        }
        // Randomize the card positions
        Collections.shuffle(cards);
    }

    public void fillBoardView(JPanel view)
    {
        for (FlippableCard c : cards) {
            view.add(c);
        }
    }

    public void resetBoard()
    {
        /*
         * To-Do: Reset the flipped cards and randomize the card positions
         */
        cards = new ArrayList<FlippableCard>();

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < size; i++) {

            // Load the front image from the resources folder
            String imgPath = "res/sea" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));

            // Setup one card at a time
            FlippableCard c = new FlippableCard(img);

            c.setCustomName(imgPath);
            c.setID(imageIdx);

            // Add them to the array
            cards.add(c);

            c.hideFront();
            c.addActionListener(AL);

            if(i % 2 != 0){ //We only want two cards to have the same image, so change the index on every odd i
                imageIdx++;  // get ready for the next pair of cards
            }
        }
        Collections.shuffle(cards);
    }
}
