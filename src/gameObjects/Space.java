
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
import java.awt.Point;

public class Space extends JButton {
    // Instance objects
    private Random rand = new Random();

    // Instance Variables
    private boolean isEmpty = true;
    private boolean isMine = false;
    private int surroundingMines = 0;
    private int r, c;
    private boolean isCleared = false;
    private boolean isFlagged = false;

    // Constructor
    /**
     * Makes a new Space (Basically a JButton with an assigned row and column for an
     * index, as well as several booleans controlling the state of the Space (mine,
     * empty space, flagged, etc.))
     * 
     * @param row         The row the button is assigned to
     * @param col         The column the buttons is assigned to
     * @param buttonWidth The width of the button
     */
    public Space(int row, int col, int buttonWidth) {
        // Sets the local row value to the selected row value
        this.r = row;
        // Sets the local column value to the selected column value
        this.c = col;

        // Sets the boolean that controls whether the space is empty to true
        isEmpty = true;
        // Sets the boolean that controls whether the space is a mine to false
        isMine = false;
        // Sets the number of surrounding mines to 0
        surroundingMines = 0;
        // Sets the boolean that controls whether the space is cleared to false
        isCleared = false;
        // Sets the boolean that controls whether the space is flagged to false
        isFlagged = false;
    }

    // Accessors or "Getters"

    /**
     * If the space is flagged or not
     * 
     * @return The value of the "isFlagged" boolean (for that specific space)
     */
    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * If the space is a mine or not
     * 
     * @return The value of the "isMine" boolean (for that specific space)
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * The X index of the space
     * 
     * @return The value of "r" for that specific space (r -> row)
     */
    public int getLocationX() {
        return r;
    }

    /**
     * The Y index of the space
     * 
     * @return The value of "c" for that specific space (c -> column)
     */
    public int getLocationY() {
        return c;
    }

    /**
     * If the space is empty or not
     * 
     * @return The value of the "isEmpty" boolean (for that specific space)
     */
    public boolean isEmptySpace() {
        return isEmpty;
    }

    /**
     * If the space is cleared or not
     * 
     * @return The value of the "isCleared" boolean (for that specific space)
     */
    public boolean isCleared() {
        return isCleared;
    }

    /**
     * Gets the integer amount of the surrounding spaces that are flagged
     * 
     * @param board The array of spaces that will be checked
     * @param r     X index of the space (board[X][...])
     * @param c     Y Index of the space (board[...][Y])
     * @return The number of flags that the space is being surrounded by
     */
    public int getSurroundingFlags(Space[][] board, int r, int c) {
        // Sets the surrounding flags to 0
        int surroundingFlags = 0;

        // 3x3 area checker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = r + i;
                int y = c + j;
                // As long as the x and y values are within the bounds of the array
                if (x >= 0 && x < board.length && y >= 0 && y < board[r].length) {
                    // Makes a space with the x and y values
                    Space checkedSpace = board[x][y];

                    // If the space is flagged
                    if (checkedSpace.isFlagged()) {
                        // Increment the surrounding flags variable
                        surroundingFlags++;
                    }
                }
            }
        }

        // Return how many flags were counted
        return surroundingFlags;
    }

    /**
     * Gets the amount of mines that are surrounding the selected space
     * 
     * @param board The array of Spaces being checked
     * @param r     The X index of the selected space (board[X][...])
     * @param c     The Y index of the selected space (board[...][Y])
     * @return The Integer amount of the mines surrounding the selected Space
     */
    public int getSurroundingMines(Space[][] board, int r, int c) {
        // Sets the surrounding mines to 0 to be counted again
        surroundingMines = 0;

        // Length of the 2-D array "board"
        int length = board.length;
        // Height of the 2-D array "board"
        int height = board[r].length;

        // 3x3 area checker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = r + i;
                int y = c + j;
                // As long as the x and y values are within the bounds of the array
                if (x >= 0 && x < length && y >= 0 && y < height) {
                    // Makes a space with the x and y values
                    Space checkedSpace = board[x][y];
                    // If the space is a mine
                    if (checkedSpace.isMine()) {
                        // Increment the surrounding mines
                        surroundingMines++;
                    }
                }
            }
        }

        // Return how many mines were counted
        return surroundingMines;
    }

    /**
     * Gets the points of the surrounding mines arround a selected space
     * 
     * @param board  The array of tiles being checked
     * @param xIndex The X index of the selected Space (board[xIndex][...])
     * @param yIndex The Y index of the selected Space (board[...][yIndex])
     * @return An array of points with the mine points being the values of each
     *         point
     */
    public Point[] getSurroundingMinePoints(Space[][] board, int xIndex, int yIndex) {
        // An array of potential mine points that has a maximum capacity of 8
        Point[] minePoints = new Point[8];

        // The width of the 2-D array "board"
        int length = board.length;
        // The height of the 2-D array "board"
        int height = board[xIndex].length;

        // Index value for the points array
        int index = 0;

        // 3x3 area checker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = xIndex + i;
                int y = yIndex + j;
                // As long as the x and y values are inside the bounds of the array
                if (x >= 0 && x < length && y >= 0 && y < height) {
                    // Makes a space with the x and y values
                    Space checkedSpace = board[x][y];

                    // If the checked space is a mine
                    if (checkedSpace.isMine()) {
                        // At the index of the index variable, it makes a new point that has the values
                        // of x and y (x and y indexes of the space that is a mine)
                        minePoints[index] = new Point(x, y);
                        // Increments the index so that multiple points arent added to 1 spot in the
                        // array
                        index++;
                    }
                }
            }
        }

        // Returns the array of mine points
        return minePoints;
    }

    /**
     * Returns whether or not a space has more than 0 mines surrounding it
     * 
     * @param board The array of Spaces being checked
     * @param r     The X index of the selected space (board[X][...])
     * @param c     The Y index of the selected space (board[...][Y])
     * @return If board[r][c] has more than 0 surrounding mines
     */
    public boolean hasSurroundingMines(Space[][] board, int r, int c) {
        return board[r][c].getSurroundingMines(board, r, c) > 0;
    }

    // Modifiers or "Setters"

    /**
     * Sets the "isFlagged" boolean to the selected value
     * 
     * @param flagged Modified value
     */
    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    /**
     * Sets the "isEmpty" boolean to the selected value
     * 
     * @param empty Modified value
     */
    public void setEmptySpace(boolean empty) {
        this.isEmpty = empty;
    }

    /**
     * Sets the "isMine" boolean to the selected value
     * 
     * @param mine Modified value
     */
    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    /**
     * Sets the "isCleared" boolean to the selected value
     * 
     * @param cleared Modified value
     */
    public void setCleared(boolean cleared) {
        this.isCleared = cleared;
    }

    /**
     * Sets the X and Y location of the Space
     * 
     * @param isEmpty If the space is an empty space or not
     * @param x       X index of the space
     * @param y       Y index of the space
     */
    public void setLocation(boolean isEmpty, int x, int y) {
        this.isEmpty = isEmpty;
        this.r = x;
        this.c = y;
    }

    // Methods

    /**
     * Changes the color of the space depending on the boolean values
     * 
     * @param row     The x index of the space (buttons[row][...])
     * @param column  The y Index of the space (buttons[...][column])
     * @param buttons The 2-D array of spaces
     */
    public void changeColor(int row, int column, Space[][] buttons) {
        // Array of colors that the board has (light green, dark green, light brown,
        // dark brown)
        Color[] boardColors = {
                Color.GREEN,
                new Color(0, 205, 70),
                new Color(250, 228, 132),
                new Color(219, 184, 103)
        };

        // Makes a new space at the index of row and column
        Space colorSpace = buttons[row][column];

        // If the player has NOT lost
        if (!Board.playerLost) {
            // If the space is not cleared and it is flagged
            if (!colorSpace.isCleared() && colorSpace.isFlagged()) {
                // Sets the color to red
                colorSpace.setBackground(Color.RED);
            }

            // If the color space is not cleared or flagged
            if (!colorSpace.isCleared() && !colorSpace.isFlagged()) {
                // If the sum of row and column can be divided by 2 and not have a remainder
                if ((row + column) % 2 == 0) {
                    // Set the color to a dark green
                    colorSpace.setBackground(boardColors[1]);
                } else {
                    // Set the color to a light green
                    colorSpace.setBackground(boardColors[0]);
                }
            }

            // If the space is cleared, it's not a mine, and it's an empty space (not a mine
            // basically)
            if (colorSpace.isCleared() && !colorSpace.isMine() && colorSpace.isEmptySpace()) {
                // If the sum of the row and column can be divided by 2 and not have a remainder
                if ((row + column) % 2 == 0) {
                    // Sets the color to a dark brown
                    colorSpace.setBackground(boardColors[3]);
                } else {
                    // Sets the color to a light brown
                    colorSpace.setBackground(boardColors[2]);
                }

            }
            // If the player HAS lost
        } else {
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
        }
    }

    /**
     * Sets the text and color of the text to a color relating to the amount of
     * surrounding mines
     * 
     * @param mineNum The amount of surrounding mines
     */
    public void setTextColor(int mineNum) {
        // An array of the text colors (light blue, green, dark red, purple, and orange)
        Color[] textColors = {
                new Color(0, 150, 200),
                new Color(0, 138, 0),
                new Color(173, 31, 31),
                new Color(179, 0, 255),
                new Color(255, 128, 0)
        };

        // Set the text of the button to the number of surrounding mines
        setText(Integer.toString(mineNum));

        // Runs through the values of the surrounding mine amount
        switch (surroundingMines) {
            // If the value is 1, it sets the color to a blue
            case 1:
                setForeground(textColors[0]);
                break;
            // If the value is 2, it sets the color to a dark green
            case 2:
                setForeground(textColors[1]);
                break;
            // If the value is 3, it sets the color to a red color
            case 3:
                setForeground(textColors[2]);
                break;
            // If the value is 4, it sets the color to a purple color
            case 4:
                setForeground(textColors[3]);
                break;
            // If the value is 5, it sets the color to an orange color
            case 5:
                setForeground(textColors[4]);
                break;
            // Any other values are black (6+, x, etc.)
            default:
                setForeground(Color.BLACK);
                break;
        }
    }

    /**
     * Takes in the RGB values of two colors and sees if the absolute value of the
     * difference between the two of them are less than 100
     * 
     * @param r1 Red value for the first color
     * @param g1 Green value for the first color
     * @param b1 Blue value for the first color
     * @param r2 Red value for the second color
     * @param g2 Green value for the second color
     * @param b2 Blue value for the second color
     * @return True if the difference each RGB value is greater than 100, otherwise
     *         false
     */
    private boolean checkColorsMatch(int r1, int g1, int b1, int r2, int g2, int b2) {
        // Absolute value of r1 -r2
        int diffR = Math.abs(r1 - r2);

        // Absolute value of g1 - g2
        int diffG = Math.abs(g1 - g2);

        // Absolute value of b1 - b2
        int diffB = Math.abs(b1 - b2);

        // Returns if each difference is greater than or equal to 100
        return diffR >= 125 && diffB >= 125 && diffG >= 125;
    }

}
