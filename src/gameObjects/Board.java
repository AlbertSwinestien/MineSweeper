
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */

// Package Declaration
package gameObjects;

// Package Imports
import display.Window;
import gameFunctions.DifficultySelector;
import gameFunctions.SpreadTiles;

// Java Extras
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Random;

public class Board {
    // Instance Objects
    Space[][] buttons;
    SpreadTiles tileSpreader;
    Random rand = new Random();

    // Instance variables
    public int flags;
    private int boardWidth, boardHeight, buttonWidth;
    private int mineAmount = DifficultySelector.mineCount;
    private int clicks = 0;

    public static boolean playerLost;
    public static boolean playerWon;

    /**
     * Creates the gameboard to hold all of the spaces
     * 
     * @param width       The width in tiles of the game board
     * @param height      The height in tiles of the game board
     * @param buttonwidth The width (and height, since all of the buttons are
     *                    square) of each button
     * @param panel       The panel the board is being added to
     */
    public Board(int width, int height, int buttonwidth, JPanel panel) {
        boardWidth = width;
        boardHeight = height;
        buttonWidth = buttonwidth;

        panel.setLayout(new GridLayout(boardHeight, boardWidth));
        buttons = new Space[boardHeight][boardWidth];

        playerWon = false;
        playerLost = false;

        start(panel);

        tileSpreader = new SpreadTiles(buttons);
    }

    /**
     * Adds the mines to the game board, as long as the space is not already a mine
     */
    public void addMines() {
        for (int m = 0; m < mineAmount; m++) {
            int randX = rand.nextInt(buttons.length);
            int randY = rand.nextInt(buttons[0].length);

            while (buttons[randX][randY].isMine()) {
                randX = rand.nextInt(buttons.length);
                randY = rand.nextInt(buttons[0].length);
            }
            buttons[randX][randY].setMine(true);
        }
    }

    /**
     * Adds the gameboard to the panel, as well as all of the functionality of the
     * tiles
     * 
     * @param panel Panel the game is to be added on
     */
    private void start(JPanel panel) {
        flags = 0;

        // Sets up and adds the tiles and mines.
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int xIndex = i;
                int yIndex = j;
                buttons[i][j] = new Space(i, j, this.buttonWidth);

                // Click listener for the mines
                // This makes it a little bit easier than adding an ActionListener
                // to each tile.
                buttons[i][j].addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    // If the button is pressed by the mouse with the button (Same as clicked, just
                    // with the same or a longer duration)
                    @Override
                    public void mousePressed(MouseEvent e) {
                        // The button that was used (Left, right, middle, or any other mouse buttons)
                        int button = e.getButton();

                        // Makes a space using the xIndex and yIndex variables instead of using
                        // "buttons[xIndex][yIndex]" every time
                        Space clickedSpace = buttons[xIndex][yIndex];

                        // If the player is winning and the game is running (if the player hasn't lost
                        // or won yet)
                        if (!playerWon && !playerLost) {
                            // A logic checker for most of the mouse buttons
                            // DISCLAIMER: I say most because the mouse that I am using has 5: Left, right,
                            // and middle click, as well as 2 extra buttons. I only have a logic checker for
                            // the first 3, not the other 2.
                            switch (button) {
                                // If the tile was clicked with the Left mouse button
                                case MouseEvent.BUTTON1:
                                    // As long as the space isn't flagged, it runs through the following:
                                    if (!clickedSpace.isFlagged()) {
                                        // If the player hasn't clicked the board yet
                                        if (clicks == 0) {

                                            if (clickedSpace.isMine()) {
                                                int randX = rand.nextInt(buttons.length);
                                                int randY = rand.nextInt(buttons[0].length);

                                                do {
                                                    randX = rand.nextInt(buttons.length);
                                                    randY = rand.nextInt(buttons[0].length);
                                                } while (buttons[randX][randY].isMine());
                                                clickedSpace.setMine(false);
                                                clickedSpace.setEmptySpace(true);
                                                buttons[randX][randY].setMine(true);
                                                buttons[randX][randY].setEmptySpace(false);
                                            }

                                            if (clickedSpace.isEmptySpace()) {
                                                clickedSpace.setCleared(true);
                                            }

                                            clicks++;
                                        } else {
                                            // If the clicked tile was a mine, the player lost boolean is set to true
                                            if (clickedSpace.isMine()) {
                                                playerLost = true;
                                            }
                                        }
                                        updateTiles(clickedSpace, xIndex, yIndex);
                                    }
                                    break;
                                case MouseEvent.BUTTON2:
                                    if (clicks > 0) {
                                        if (clickedSpace.isCleared()) {
                                            tileSpreader.clearEmptyMiddleClicked(clickedSpace);
                                        }
                                    }
                                    updateTiles(clickedSpace, xIndex, yIndex);
                                    break;
                                case MouseEvent.BUTTON3:
                                    if (!clickedSpace.isCleared()) {
                                        if (!clickedSpace.isFlagged()) {
                                            clickedSpace.setFlagged(true);
                                            flags++;
                                            if (flags == mineAmount && checkCorrectFlags()) {
                                                playerWon = true;
                                                Window.checkWin();
                                            }
                                        } else {
                                            clickedSpace.setFlagged(false);
                                            flags--;
                                        }
                                    }
                                    break;
                            }
                            clickedSpace.changeColor(xIndex, yIndex, buttons);
                            Window.checkLose();
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }

                });
                buttons[i][j].changeColor(i, j, buttons);
                buttons[i][j].setBorder(BorderFactory.createBevelBorder(0));
                buttons[i][j].setPreferredSize(new Dimension(buttonWidth, buttonWidth));
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 10));
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setVisible(true);
                panel.add(buttons[i][j]);
            }
        }
        // Adds the mines
        addMines();

        // Checks if the space is empty
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int surroundingMA = buttons[i][j].getSurroundingMines(buttons, i, j);
                if (buttons[i][j].isMine()) {
                    buttons[i][j].setEmptySpace(false);
                } else if (surroundingMA > 0) {
                    buttons[i][j].setEmptySpace(true);
                } else {
                    buttons[i][j].setEmptySpace(true);
                }
            }
        }

    }

    /**
     * Checks if the flagged Spaces are mines
     * 
     * @return True if all the flagged spaces are mines, otherwise false
     */
    private boolean checkCorrectFlags() {
        int flaggedSpaces = 0;
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].isFlagged() && buttons[i][j].isMine()) {
                    flaggedSpaces++;
                }
            }
        }
        if (flaggedSpaces == mineAmount) {
            return true;
        } else {
            return false;
        }
    }

    public void clearSurroundingEmpty(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = x + i;
                int newY = y + j;
                if ((newX >= 0 && newX < buttons.length && newY >= 0 && newY < buttons[newX].length)) {
                    Space emptySpace = buttons[newX][newY];
                    if (!emptySpace.isCleared()) { // Check if the empty space has already been cleared
                        if (emptySpace.isMine()) {
                            return;
                        }
                        emptySpace.setCleared(true); // Mark the empty space as cleared
                        updateTiles(emptySpace, newX, newY);
                    }
                }
            }
        }
    }

    /**
     * Updates the tiles as cleared and checks if the game is still going
     * 
     * @param clickedSpace The space the player most recently clicked on
     * @param xIndex       X Position of the clicked Space
     * @param yIndex       Y Posistion of the clicked Space
     */
    private void updateTiles(Space clickedSpace, int xIndex, int yIndex) {
        if (clickedSpace.isFlagged())
            return;
        // The surrounding mine number of a selected space.
        int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex, yIndex);

        // Sets the text of the space to correspond with the surrounding Mines
        if (surroundingMines > 0 && !clickedSpace.isMine()) {
            clickedSpace.setTextColor(surroundingMines);
        }

        // Sets the clicked space to cleared
        if (!clickedSpace.isMine()) {
            clickedSpace.setCleared(true);
        }

        // Checks for empty spaces in a 3x3 area around the clicked space
        for (int dx = -1; dx < 1; dx++) {
            for (int dy = -1; dy < 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int x = xIndex + dx;
                int y = yIndex + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space adjacentSpace = buttons[x][y];
                    // If the adjacent space IS NOT a mine

                    // Clears the adjacent space
                    // If the adjacent space is an Empty space, update the tiles using the adjacent
                    // space as the clicked space
                    if (adjacentSpace.isEmptySpace()) {
                        adjacentSpace.setCleared(true);
                        clearSurroundingEmpty(x, y);
                    } else {
                        break;
                    }
                }
            }

        }
        // Changes the color of the clicked space
        clickedSpace.changeColor(xIndex, yIndex, buttons);

    }

    /**
     * Displays a losing message
     * 
     * @param frame A JFrame to be used as a parent for a JOptionPane
     */
    public void lose(JFrame frame) {
        // The player has lost, they are no longer playing
        playerLost = true;

        // Show the existing mines on the gameboard
        showMines();

        // Display the loosing message
        JOptionPane.showMessageDialog(frame, "You Loose!", "MineSweeper", 1);
    }

    /**
     * Displays a winning message
     * 
     * @param frame A frame to be used as a parent for a JOptionPane.
     */
    public void win(JFrame frame) {
        // The player has won, they are no longer playing
        playerWon = true;

        // Remove the flagged mines, replace with green
        removeFlags();

        // Display Message
        JOptionPane.showMessageDialog(frame, "You Win!", "MineSweeper", 1);
    }

    /**
     * Replaces flagged spaces with either green colors
     * 
     * Should only be called when the game is complete
     */
    public void removeFlags() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {

                if (buttons[i][j].isFlagged()) {
                    buttons[i][j].setCleared(false);
                    buttons[i][j].setFlagged(false);
                }
                buttons[i][j].changeColor(i, j, buttons);
            }
        }
    }

    /**
     * Shows all mines on the gameboard
     * 
     * Should only be called when the game is lost
     */
    public void showMines() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].isMine() && !buttons[i][j].isFlagged()) {
                    buttons[i][j].setCleared(true);
                }
                buttons[i][j].changeColor(i, j, buttons);
            }
        }
    }
}
