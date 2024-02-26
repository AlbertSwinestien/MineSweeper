
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */
// Package Declaration
package gameObjects;

// Java Extras
import java.awt.Color;
import java.util.Random;
import javax.swing.JButton;

public class Space extends JButton {
    // Instance Variables
    private boolean isEmpty = true;
    private boolean isMine = false;
    private int surroundingMines = 0;
    private int r, c;
    private boolean isCleared = false;
    private boolean isFlagged = false;
    private Random rand = new Random();

    public Space(int row, int col, int buttonWidth) {
        this.r = row;
        this.c = col;

        isEmpty = true;
        isMine = false;
        surroundingMines = 0;
        isCleared = false;
        isFlagged = false;
    }

    // Accessors or "Getters"

    public boolean isFlagged() {
        return isFlagged;
    }

    public int getLocationX() {
        return r;
    }

    public int getLocationY() {
        return c;
    }

    public boolean isEmptySpace() {
        return isEmpty;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public int getSurroundingFlags(Space[][] board, int r, int c) {
        int surroundingFlags = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = r + i;
                int y = c + j;
                if (x >= 0 && x < board.length && y >= 0 && y < board[r].length) {
                    Space checkedSpace = board[x][y];
                    if (checkedSpace.isFlagged()) {
                        surroundingFlags++;
                    }
                }
            }
        }

        return surroundingFlags;
    }

    public int getSurroundingMines(Space[][] board, int r, int c) {
        if (surroundingMines > 0) {
            surroundingMines = 0;
        }

        int length = board.length;
        int height = board[r].length;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = r + i;
                int y = c + j;
                if (x >= 0 && x < length && y >= 0 && y < height) {
                    Space checkedSpace = board[x][y];
                    if (checkedSpace.isMine()) {
                        surroundingMines++;
                    }
                }
            }
        }

        return surroundingMines;
    }

    public boolean hasSurroundingMines(Space[][] board, int r, int c) {
        return board[r][c].getSurroundingMines(board, r, c) > 0;
    }

    // Modifiers or "Setters"

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setEmptySpace(boolean empty) {
        this.isEmpty = empty;
    }

    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    public void setCleared(boolean cleared) {
        this.isCleared = cleared;
    }

    public void setLocation(boolean isEmpty, int x, int y) {
        this.isEmpty = isEmpty;
        this.r = x;
        this.c = y;
    }

    // Methods

    public void changeColor(int row, int column, Space[][] buttons) {
        Color[] boardColors = {
                Color.GREEN,
                new Color(0, 205, 70),
                new Color(250, 228, 132),
                new Color(219, 184, 103)
        };

        Space colorSpace = buttons[row][column];

        if (Board.playerLost) {
            // Set the unflagged mines on the board to a random RGB color
            if (colorSpace.isMine() && !colorSpace.isFlagged()) {
                // Random Red value (from 0 - 255)
                int randRed = rand.nextInt(256);
                // Random Green value (from 0 - 255)
                int randGreen = rand.nextInt(256);
                // Random Blue value (from 0 - 255)
                int randBlue = rand.nextInt(256);
                // The color of all the random integer values
                Color mineColor = new Color(randRed, randGreen, randBlue);

                // Runs through the array of colors and checks if each individual red green and
                // blue colors match (or are close to matching). If they do, it generates
                // different color values
                for (Color testColor : boardColors) {

                    while (!checkColorsMatch(testColor.getRed(), testColor.getGreen(), testColor.getBlue(),
                            randRed, randGreen, randBlue)) {
                        randRed = rand.nextInt(256);
                        randGreen = rand.nextInt(256);
                        randBlue = rand.nextInt(256);
                    }
                }
                // Sets the mine background to the new random color.
                colorSpace.setBackground(mineColor);
            }

            // If the space is flagged, but it is not a mine, it sets the flagged state to
            // false, sets the background color to the corresponding green, and sets the
            // text to "X"
            if (colorSpace.isFlagged() && !colorSpace.isMine()) {
                colorSpace.setFlagged(false);
                // If the sum of the row and column indexes are divisible by 2 (meaning it has
                // no remainder), it sets the color to a darker green
                if ((row + column) % 2 == 0) {
                    colorSpace.setBackground(boardColors[1]);
                } else {
                    colorSpace.setBackground(boardColors[0]);
                }
                colorSpace.setForeground(Color.BLACK);
                colorSpace.setText("X");
            }
        } else {
            if (!colorSpace.isCleared() && colorSpace.isFlagged()) {
                colorSpace.setBackground(Color.RED);
            }

            if (!colorSpace.isCleared() && !colorSpace.isFlagged()) {
                if ((row + column) % 2 == 0) {
                    colorSpace.setBackground(boardColors[1]);
                } else {
                    colorSpace.setBackground(boardColors[0]);
                }
            }

            if (colorSpace.isCleared() && !colorSpace.isMine() && colorSpace.isEmptySpace()) {
                if ((row + column) % 2 == 0) {
                    colorSpace.setBackground(boardColors[3]);
                } else {
                    colorSpace.setBackground(boardColors[2]);
                }
            }
        }
    }

    public void setTextColor(int mineNum) {
        Color color_1 = new Color(0, 148, 201);
        Color color_2 = new Color(0, 138, 0);
        Color color_3 = new Color(173, 31, 31);
        Color color_4 = new Color(179, 0, 255);
        Color color_5 = new Color(255, 128, 0);

        // Set the text of the button to the number of surrounding mines
        setText(Integer.toString(mineNum));

        switch (surroundingMines) {
            case 1:
                setForeground(color_1);
                break;
            case 2:
                setForeground(color_2);
                break;
            case 3:
                setForeground(color_3);
                break;
            case 4:
                setForeground(color_4);
                break;
            case 5:
                setForeground(color_5);
                break;
            default:
                setForeground(Color.BLACK);
                break;
        }
    }

    private boolean checkColorsMatch(int r1, int g1, int b1, int r2, int g2, int b2) {
        int diffR = Math.abs(r2 - r1);
        int diffG = Math.abs(g2 - g1);
        int diffB = Math.abs(b2 - b1);

        int maxDiff = Math.max(diffR, Math.max(diffG, diffB));
        return maxDiff > 25;

    }

}
