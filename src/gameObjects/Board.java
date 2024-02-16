
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */

// Package Declaration
package gameObjects;

// Package Imports
import display.Window;
import gameFunctions.DifficultySelector;

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
    // Instance variables
    Space[][] buttons;
    Random rand = new Random();
    private int boardWidth, boardHeight, buttonWidth;
    private int mineAmount = DifficultySelector.mineCount;
    private int clicks = 0;
    public static boolean areWinning;
    public static boolean arePlaying;
    public int flags;

    public Board(int width, int height, int buttonwidth, JPanel panel) {
        boardWidth = width;
        boardHeight = height;
        buttonWidth = buttonwidth;

        panel.setLayout(new GridLayout(boardHeight, boardWidth));
        buttons = new Space[boardHeight][boardWidth];

        arePlaying = true;
        areWinning = true;

        start(panel);
    }

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
     * Adds the game to the panel
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
                buttons[i][j].addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        int button = e.getButton();
                        Space clickedSpace = buttons[xIndex][yIndex];
                        if (arePlaying && areWinning) {
                            switch (button) {
                                case MouseEvent.BUTTON1:
                                    if (!clickedSpace.isFlagged()) {
                                        if (clicks == 0) {
                                            int randomSpreadNum = rand.nextInt(3) + 1;
                                            spreadTiles(randomSpreadNum, clickedSpace);
                                        } else {
                                            if (clickedSpace.isMine()) {
                                                areWinning = false;
                                            }
                                        }
                                    }
                                    updateTiles(clickedSpace, xIndex, yIndex);
                                    break;
                                case MouseEvent.BUTTON2:
                                    if (clicks > 0) {
                                        if (!clickedSpace.isMine() && !clickedSpace.isFlagged()) {
                                            int surroundingFlags = clickedSpace.getSurroundingFlags(buttons, xIndex,
                                                    yIndex);
                                            int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex,
                                                    yIndex);
                                            if (surroundingFlags != surroundingMines) {
                                                revealSurrounding(clickedSpace, xIndex, yIndex);
                                            }
                                        }
                                    }
                                    break;
                                case MouseEvent.BUTTON3:
                                    if (!clickedSpace.isCleared()) {
                                        if (!clickedSpace.isFlagged()) {
                                            clickedSpace.setFlagged(true);
                                            flags++;
                                            if (flags == mineAmount && checkCorrectFlags()) {
                                                arePlaying = false;
                                                Window.checkWin();
                                            }
                                        } else {
                                            clickedSpace.setFlagged(false);
                                            flags--;
                                        }
                                        clickedSpace.changeColor(xIndex, yIndex, buttons);
                                    }
                                    break;
                                default:
                                    System.out.println("Don't know what that was...");
                                    break;
                            }
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
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
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

    /**
     * Updates the tiles as cleared and checks if the game is still going
     * 
     * @param clickedSpace The space the player most recently clicked on
     * @param xIndex       X Position of the clicked Space
     * @param yIndex       Y Posistion of the clicked Space
     */
    private void updateTiles(Space clickedSpace, int xIndex, int yIndex) {
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

        // If there are empty spaces around the clicked space
        if (clickedSpace.getSurroundingEmpty(clickedSpace, buttons) > 0) {
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
                        if (!adjacentSpace.isMine()) {
                            // Clears the adjacent space
                            adjacentSpace.setCleared(true);
                            // If the adjacent space is an Empty space, update the tiles using the adjacent
                            // space as the clicked space
                            if (adjacentSpace.isEmptySpace()) {
                                updateTiles(adjacentSpace, x, y);
                            }
                        }
                    }
                }
            }
        }

        // Changes the color of the clicked space
        clickedSpace.changeColor(xIndex, yIndex, buttons);

    }

    /**
     * Spreads the tiles around the initially clicked space
     * 
     * @param spreadAmount the radius of the spread
     * @param startSpace   The starting space that was clicked on
     */
    private void spreadTiles(int spreadAmount, Space startSpace) {
        // If the starting space is a mine, swap with a random space on the board until
        // the starting space is not a mine
        if (startSpace.isMine()) {
            int randX = rand.nextInt(buttons.length);
            int randY = rand.nextInt(buttons[0].length);

            while (buttons[randX][randY].isMine()) {
                randX = rand.nextInt(buttons.length);
                randY = rand.nextInt(buttons[0].length);
            }
            startSpace.setMine(false);
            startSpace.setEmptySpace(true);
            buttons[randX][randY].setMine(true);
            buttons[randX][randY].setEmptySpace(false);
        }

        // Clears the tiles in a 1-3 space radius
        for (int dx = spreadAmount * -1; dx <= spreadAmount; dx++) {
            for (int dy = spreadAmount * -1; dy <= spreadAmount; dy++) {
                int x = startSpace.getLocationX() + dx;
                int y = startSpace.getLocationY() + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space changedSpace = buttons[x][y];
                    int surroundingEmpty = changedSpace.getSurroundingEmpty(changedSpace, buttons);

                    // If the changed space is not a mine, set the space to cleared
                    if (!changedSpace.isMine()) {
                        changedSpace.setCleared(true);
                        int mineNum = changedSpace.getSurroundingMines(buttons, x, y);
                        // If the changed space has multiple surrounding mines, set the text color to
                        // the coresponding color/number
                        if (mineNum > 0) {
                            changedSpace.setTextColor(mineNum);
                        }
                        // If the changed space has more than 1 empty tile surrounding it, clear the
                        // empty spaces
                        if (surroundingEmpty > 0) {
                            changedSpace.clearEmpty(changedSpace, buttons);
                        }

                        // Change the color of the changed space
                        changedSpace.changeColor(x, y, buttons);
                    }
                }
            }
        }
        // Update the click number
        clicks++;
    }

    /**
     * Reveals the surrounding spaces of a specific space. Only reveals empty
     * spaces, not mines
     * 
     * @param clickedSpace The most recently clicked on space
     * @param xIndex       X Posistion of clicked space
     * @param yIndex       Y posistion of clicked space
     */
    private void revealSurrounding(Space clickedSpace, int xIndex, int yIndex) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = xIndex + dx;
                int y = yIndex + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space adjacentSpace = buttons[x][y];
                    if (adjacentSpace.isFlagged() && !adjacentSpace.isMine()) {
                        areWinning = false;
                        updateTiles(adjacentSpace, x, y);
                    } else {
                        adjacentSpace.setCleared(true);
                        updateTiles(adjacentSpace, x, y);
                    }
                }
            }
        }
    }

    public void lose(JFrame frame) {
        areWinning = false;
        showMines();
        JOptionPane.showMessageDialog(frame, "You Loose!", "MineSweeper", 1);
    }

    public void win(JFrame frame) {
        arePlaying = false;
        removeFlags();
        JOptionPane.showMessageDialog(frame, "You Win!", "MineSweeper", 1);
    }

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
