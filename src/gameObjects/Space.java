
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
import javax.swing.BorderFactory;

public class Space extends JButton {
    public boolean flagAllowed = false;
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

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isEmptySpace() {
        return isEmpty;
    }

    public void setEmptySpace(boolean empty) {
        this.isEmpty = empty;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        this.isCleared = cleared;
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

    public int getSurroundingEmpty(Space startSpace, Space[][] buttons) {
        int surroundingEmpty = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = startSpace.getLocationX() + dx;
                int y = startSpace.getLocationY() + dy;
                if (x >= 0 && x < buttons.length && y >= 0 && y < buttons[x].length) {
                    Space changedSpace = buttons[x][y];
                    if (changedSpace.isEmptySpace()) {
                        surroundingEmpty++;
                    }
                }
            }
        }
        return surroundingEmpty;
    }

    public void clearEmpty(Space startSpace, Space[][] buttons) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = startSpace.getLocationX() + dx;
                int y = startSpace.getLocationY() + dy;
                if (x >= 0 && x < buttons.length && y >= 0 && y < buttons[x].length) {
                    Space changedSpace = buttons[x][y];
                    if (changedSpace.isEmptySpace()) {
                        changedSpace.setCleared(true);
                    }
                }
            }
        }
    }

    public int getLocationX() {
        return r;
    }

    public int getLocationY() {
        return c;
    }

    public void setLocation(boolean isEmpty, int x, int y) {
        this.isEmpty = isEmpty;
        this.r = x;
        this.c = y;
    }

    public void changeColor(int row, int column, Space[][] buttons) {
        Color green1 = Color.GREEN;
        Color green2 = new Color(0, 225, 50);
        Color brown1 = new Color(250, 228, 132);
        Color brown2 = new Color(219, 184, 103);
        Space colorSpace = buttons[row][column];
        colorSpace.setBorder(BorderFactory.createEmptyBorder());

        if (!colorSpace.isCleared() && !colorSpace.isFlagged()) {
            if ((row + column) % 2 == 0) {
                colorSpace.setBackground(green2);
            } else {
                colorSpace.setBackground(green1);
            }
        } 
        
        if (colorSpace.isCleared() && !colorSpace.isMine() && colorSpace.isEmptySpace()) {
            if ((row + column) % 2 == 0) {
                colorSpace.setBackground(brown2);
            } else {
                colorSpace.setBackground(brown1);
            }
        }
        
        if (colorSpace.isCleared() && colorSpace.isMine()) {
            int randRed = rand.nextInt(256);
            int randBlue = rand.nextInt(256);
            colorSpace.setBackground(new Color(randRed, 50, randBlue));
        }
        
        if (!Board.areWinning && colorSpace.isFlagged() && !colorSpace.isMine()) {
            colorSpace.setFlagged(false);
            if ((row + column) % 2 == 0) {
                colorSpace.setBackground(green2);
            } else {
                colorSpace.setBackground(green1);
            }
            colorSpace.setForeground(Color.BLACK);
            colorSpace.setText("X");
        }

        if (colorSpace.isFlagged()) {
            colorSpace.setBackground(Color.RED);
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

}
