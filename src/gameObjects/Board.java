
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */

// Package Declaration
package gameObjects;

// Package Imports
import display.Window;
import gameFunctions.*;

// Java Extras
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    // Instance Objects

    // 2D array of Spaces to act as a gameboard
    Space[][] buttons;
    // An arraylist of all the points of the empty spaces (isnt a mine)
    ArrayList<Point> emptySpaces = new ArrayList<Point>();
    // Random Object
    Random rand = new Random();
    // A timer that counts every second
    SecondTimer timer = new SecondTimer();

    // Instance variables

    // Flags (not the required amount, the amount placed down)
    public int flags;
    // The width and height of the game board (w * h) and the width of the buttons
    private int boardWidth, boardHeight, buttonWidth;
    // The amount of mines based off of the current difficulty
    private int mineAmount = DifficultySelector.mineCount;
    // How many times the player has clicked
    private int clicks = 0;
    // How long the player has been playing for in seconds
    public static int seconds = 0;
    // If the player has lost or not (true or false)
    public static boolean playerLost;
    // If the player has won or not (true or false)
    public static boolean playerWon;
    // If the player is actively playing the game
    public static boolean playingGame;

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
        // Sets the board dimensions to the width and height specified
        boardWidth = width;
        boardHeight = height;
        // Sets the button width to the specified value
        buttonWidth = buttonwidth;

        // Sets the layout of the selected panel to a grid of the board width/height
        panel.setLayout(new GridLayout(boardHeight, boardWidth));

        // Makes the array of buttons as big as the board width and height
        buttons = new Space[boardHeight][boardWidth];

        // Sets the players status to playing and hasn't won or lost
        playerWon = false;
        playerLost = false;
        playingGame = false;

        // Starts the game
        start(panel);
    }

    /**
     * Adds the gameboard to the panel, as well as all of the functionality of the
     * tiles
     *
     * @param panel Panel the game is to be added on
     */
    private void start(JPanel panel) {
        // Clears the array of empty spaces (their x,y points)
        emptySpaces.clear();

        // Sets the amount of placed flags to 0
        flags = 0;

        // Resets the timer value
        seconds = 000;

        // Formats the seconds to 3 digets
        String formattedNumber = String.format("%03d", Board.seconds);

        // Updates the timer label to the seconds amount
        Window.updateTimer(formattedNumber);

        // Sets up and adds the tiles and mines.
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {

                // Variables for ease of use
                int xIndex = i;
                int yIndex = j;

                // Sets the button at the point of i,j on the gameboard to a new space
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
                                            // Sets the playing Game variable to true so that the timer can update the
                                            // time
                                            playingGame = true;

                                            // Starts the timer
                                            timer.run();
                                            // If the clickedSpace is a mine and its the very first click of the game
                                            if (clickedSpace.isMine()) {
                                                // Random X value from 0 to the width of the gameboard
                                                int randX = rand.nextInt(buttons.length);
                                                // Random Y value from 0 to the height of the gameboard
                                                int randY = rand.nextInt(buttons[0].length);

                                                // While the tile at the random x and y values are a mine, reset the
                                                // random x and y values
                                                do {
                                                    randX = rand.nextInt(buttons.length);
                                                    randY = rand.nextInt(buttons[0].length);
                                                } while (buttons[randX][randY].isMine());

                                                // Set the initially clicked space to an empty space and NOT a mine
                                                clickedSpace.setMine(false);
                                                clickedSpace.setEmptySpace(true);
                                                // Set the new random x and y tile to a mine and NOt an empty space
                                                buttons[randX][randY].setMine(true);
                                                buttons[randX][randY].setEmptySpace(false);
                                            }

                                            // If the clickedSpace is an emptyspace, set it to cleared
                                            if (clickedSpace.isEmptySpace()) {
                                                clickedSpace.setCleared(true);
                                            }

                                            // Increment the clicks by 1
                                            clicks++;
                                            // If the clicks were not 0
                                        } else {
                                            // If the clicked tile was a mine, the player lost boolean is set to true
                                            if (clickedSpace.isMine()) {
                                                playerLost = true;
                                            }
                                        }
                                        // Update the tiles at and around the initially clicked space.
                                        updateTiles(clickedSpace, xIndex, yIndex);
                                    }
                                    break;
                                // If the mouse wheel button is clicked
                                case MouseEvent.BUTTON2:
                                    // As long as the click amount is greater than 0
                                    if (clicks > 0) {

                                        // If the space is cleared
                                        if (clickedSpace.isCleared()) {
                                            // Makes an array of points that contains the coordinates of the surrounding
                                            // mines
                                            Point[] potentialSpaces = clickedSpace.getSurroundingMinePoints(buttons, xIndex, yIndex);

                                            // How many mines have been correctly flagged
                                            int correctlyFlagged = 0;

                                            // Cycles through the array of mines
                                            for (Point p : potentialSpaces) {
                                                // As long as the point is not null
                                                if (p != null) {
                                                    // x coordinate of the mine
                                                    int x = (int) p.getX();
                                                    // y coordinate of the mine
                                                    int y = (int) p.getY();

                                                    // If the mine is flagged, it adds 1 to the correctly flagged amount
                                                    if (buttons[x][y].isFlagged() && buttons[x][y].isMine()) {
                                                        correctlyFlagged++;
                                                    }
                                                }
                                            }

                                            // If the correctly flagged variable is equal to the surrounding flags of
                                            // the tiles
                                            if (correctlyFlagged == clickedSpace.getSurroundingFlags(buttons, xIndex,
                                                    yIndex)) {
                                                // 3x3 area checker
                                                for (int i = -1; i <= 1; i++) {
                                                    for (int j = -1; j <= 1; j++) {
                                                        int newX = xIndex + i;
                                                        int newY = yIndex + j;

                                                        // As long as the newX and newY variables are in-bounds
                                                        if (newX >= 0 && newX < buttons.length && newY >= 0
                                                                && newY < buttons[newX].length) {
                                                            // If the space at newX and newY is empty and is not flagged
                                                            if (buttons[newX][newY].isEmptySpace()
                                                                    && !buttons[newX][newY].isFlagged()) {
                                                                // Clears the space
                                                                buttons[newX][newY].setCleared(true);
                                                                // Sets the color
                                                                buttons[newX][newY].changeColor(newX, newY, buttons);
                                                                // If the button has surrounding mines
                                                                if (buttons[newX][newY].getSurroundingMines(buttons,
                                                                        newX, newY) > 0) {
                                                                    // Sets the text of the space to the amount of mines
                                                                    // and sets the text color to the corresponding
                                                                    // value
                                                                    buttons[newX][newY].setTextColor(buttons[newX][newY]
                                                                            .getSurroundingMines(buttons, newX, newY));
                                                                    // If there isn't any surrounding mines, clear the
                                                                    // surrounding empty tiles
                                                                } else {
                                                                    clearSurroundingEmpty(newX, newY);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                // If the correctly flagged variable doesn't equal the surrounding flags
                                            } else {
                                                // 3x3 area checker
                                                for (int i = -1; i <= 1; i++) {
                                                    for (int j = -1; j <= 1; j++) {
                                                        int newX = xIndex + i;
                                                        int newY = yIndex + j;

                                                        // If the newX and newY are in-bounds
                                                        if (newX >= 0 && newX < buttons.length && newY >= 0
                                                                && newY < buttons[newX].length) {
                                                            // If the space at newX and newY is empty and it's flagged,
                                                            // the player has lost
                                                            if (buttons[newX][newY].isEmptySpace()
                                                                    && buttons[newX][newY].isFlagged()) {
                                                                playerLost = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        // Updates the tiles
                                        updateTiles(clickedSpace, xIndex, yIndex);
                                    }
                                    break;
                                // Right Click
                                case MouseEvent.BUTTON3:
                                    if (!clickedSpace.isCleared()) {
                                        if (clickedSpace.isFlagged()) {
                                            clickedSpace.setFlagged(false);
                                            flags--;
                                        } else {
                                            clickedSpace.setFlagged(true);
                                            flags++;
                                        }
                                    }
                                    break;
                            }

                            // Checks if all of the empty tiles are cleared, all of the empty tiles are
                            // cleared, the placed flags are equal to the mine
                            // amount, and all of the flags are correctly placed, the player won and the
                            // window checks if the win is valid
                            if (checkCleared()) {
                                if (flags != mineAmount || !checkCorrectFlags()) {
                                    playerLost = true;
                                }
                                if (flags == mineAmount && checkCorrectFlags()) {
                                    playerWon = true;
                                    Window.checkWin();
                                }
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

                // Sets the color to the respective color based on the statuses
                buttons[i][j].changeColor(i, j, buttons);

                // Sets the border of the buttons to an empty border
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder());

                // Sets the size of the buttons to the button width variable
                buttons[i][j].setPreferredSize(new Dimension(buttonWidth, buttonWidth));

                // Sets the font of each button for the number of mines
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 10));

                // Sets the spacing inside the buttons to 0
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));

                // Makes the space visible
                buttons[i][j].setVisible(true);

                // Adds the button to the panel
                panel.add(buttons[i][j]);
            }
        }

        // Adds the mines
        addMines();

        // Checks if the space is empty
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                // Variable of the surrounding mine amount of the selected space
                int surroundingMA = buttons[i][j].getSurroundingMines(buttons, i, j);

                // If the button is a mine, it's not an empty space
                if (buttons[i][j].isMine()) {
                    buttons[i][j].setEmptySpace(false);
                    // If the surrounding mine amount is greater than 0, it is an empty space and
                    // adds the point to the array list of points
                } else if (surroundingMA > 0) {
                    buttons[i][j].setEmptySpace(true);
                    emptySpaces.add(new Point(i, j));
                    // If the surrounding mine amount is 0 and the button is not a mine, its an
                    // empty space and it adds the point to the arraylist
                } else {
                    buttons[i][j].setEmptySpace(true);
                    emptySpaces.add(new Point(i, j));
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
        // If the space is flagged, it stops the method
        if (clickedSpace.isFlagged() || clickedSpace.isMine()) {
            return;
        }

        // The surrounding mine number of a selected space.
        int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex, yIndex);

        // Sets the text of the space to correspond with the surrounding Mines
        if (surroundingMines > 0) {
            clickedSpace.setTextColor(surroundingMines);
        }

        // 3x3 area checker
        for (int dx = -1; dx < 1; dx++) {
            for (int dy = -1; dy < 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int x = xIndex + dx;
                int y = yIndex + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space adjacentSpace = buttons[x][y];
                    // Clears the adjacent space
                    // If the adjacent space is an Empty space, update the tiles using the adjacent
                    // space as the clicked space
                    if (adjacentSpace.isEmptySpace() && !adjacentSpace.isFlagged()) {
                        adjacentSpace.setCleared(true);
                        adjacentSpace.changeColor(x, y, buttons);
                        if (adjacentSpace.getSurroundingMines(buttons, x, y) > 0) {
                            adjacentSpace.setTextColor(adjacentSpace.getSurroundingMines(buttons, x, y));
                        }
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
     * Clears the surrounding empty tiles. Stops if a space is flagged or if its a
     * mine
     *
     * @param x The x Index of the tile that the surrounding empty tiles are being
     *          cleared from
     * @param y The y Index of the tiles that the surrounding empty tiles are being
     *          cleared from
     */
    public void clearSurroundingEmpty(int x, int y) {
        // If the button at x and y is either flagged or a mine, it stops the method
        if (buttons[x][y].isMine() || buttons[x][y].isFlagged()) {
            return;
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // A surrounding x value (either to the left or the right)
                int newX = x + i;

                // A surrounding y value (either above or below)
                int newY = y + j;

                // If the values are within the bounds of the gameboard
                if ((newX >= 0 && newX < buttons.length && newY >= 0 && newY < buttons[newX].length)) {
                    // The space at the newX and newY values
                    Space emptySpace = buttons[newX][newY];

                    // If the emptyspace is not cleared
                    if (!emptySpace.isCleared()) {
                        // If the emptySpace is either a mine or flagged, it stops the method
                        if (emptySpace.isMine() || emptySpace.isFlagged()) {
                            continue;
                        } else {
                            // Sets the tile to cleared
                            emptySpace.setCleared(true);
                            // Changes the color
                            emptySpace.changeColor(newX, newY, buttons);
                            // If the space has mines surrounding it, but its not a mine
                            if (emptySpace.getSurroundingMines(buttons, newX, newY) > 0 && !emptySpace.isMine()) {
                                // Sets the text and text color to correspond with the amount of mines
                                // surrounding the tile
                                emptySpace.setTextColor(emptySpace.getSurroundingMines(buttons, newX, newY));
                                // If there are no mines surrounding the space and the space is not a mine
                            } else if (emptySpace.getSurroundingMines(buttons, newX, newY) == 0 && !emptySpace.isMine()) {
                                // Clears the surrounding empty tiles
                                clearSurroundingEmpty(newX, newY);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if all of the empty tiles are cleared
     *
     * @return True if
     */
    public boolean checkCleared() {
        int emptyTiles = 0;
        for (Point p : emptySpaces) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    if (i == x && j == y) {
                        if (buttons[x][y].isCleared()) {
                            emptyTiles++;
                        }
                    }
                }
            }
        }

        if (emptyTiles == emptySpaces.size()) {
            return true;
        } else
            return false;
    }

    // Winning/loosing Logic

    /**
     * Displays a losing message
     *
     * @param frame A JFrame to be used as a parent for a JOptionPane
     */
    public void lose(JFrame frame) {
        // The player has lost, they are no longer playing
        playerLost = true;
        playingGame = false;

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
        playingGame = false;

        // Remove the flagged mines, replace with green
        removeFlags();

        // Display Message
        JOptionPane.showMessageDialog(frame, "You Win!", "MineSweeper", 1);
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
