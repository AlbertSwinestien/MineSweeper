
/**
 * @author John Kunz
 * @since 31/1/2024
 */

// Package Declarations
package display;

// Package Imports
import gameObjects.Board;
import gameFunctions.DifficultySelector;

// Java Extras
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;

public class Window extends Canvas {
    // Instance Objects
    static JFrame frame = new JFrame();
    JPanel[] panels = new JPanel[15];
    Dimension windowDimension;
    JButton startGame = new JButton("Start Game");
    static Board game;
    DifficultySelector diff;
    public static JLabel timerLabel = new JLabel("Time: ");
    
    // Constructor
    /**
     * This makes a new GUI that the game functions with. Just a simple window that
     * holds the gameboard
     * 
     * @param width  The width of the window in pixels
     * @param height The height of the window in pixels
     * @param title  The title of the window
     */
    public Window(int width, int height, String title) {

        // Sets the window title to the new selected title
        frame.setTitle(title);

        // Sets the size of the frame to the width and height of the parameters in the
        // constructor
        windowDimension = new Dimension(width, height);
        frame.setPreferredSize(windowDimension);

        // Sets the layout of the frame so that objects and components can be added to
        // specific spots on the frame.
        frame.setLayout(new BorderLayout());

        // Adds the parent panel to the frame
        frame.add(panels[0] = new JPanel(), BorderLayout.CENTER);

        // Makes the frame non-resizable
        frame.setResizable(false);

        // Makes the frame pop up in a random spot
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Board Panel
        panels[1] = new JPanel();

        // "Header" panel
        panels[2] = new JPanel();

        // "Info" panel
        panels[3] = new JPanel();

        // Header "Formatting"
        JLabel titleLabel = new JLabel("MineSweeper");
        titleLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));

        panels[2].add(titleLabel);

        diff = new DifficultySelector(panels[2]);
        panels[2].add(startGame);
        startGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panels[1].removeAll();
                makeBoard(DifficultySelector.boardWidth, DifficultySelector.boardHeight);
            }

        });

        frame.add(panels[2], BorderLayout.NORTH);
        frame.setVisible(true);

        // Info Panel formatting
        panels[3].add(timerLabel);

        frame.add(panels[3], BorderLayout.WEST);
    }

    public void makeBoard(int width, int height) {
        panels[1].setLayout(new GridLayout(height, width));

        game = new Board(width, height, frame.getHeight() / width, panels[1]);
        panels[0].add(panels[1]);

        panels[0].revalidate();
        panels[0].repaint();

        panels[1].revalidate();
        panels[1].repaint();
    }

    /**
     * Checks to see if the player lost
     */
    public static void checkLose() {
        if (Board.playerLost) {
            game.lose(frame);
        }
    }

    /**
     * Checks to see if the player won
     */
    public static void checkWin() {
        if (Board.playerWon) {
            game.win(frame);
        }
    }

    public static void updateTimer(String secondAMT) {
        timerLabel.setText("Time: " + secondAMT);
    }
}