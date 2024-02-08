
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */
// Package Declaration
package gameObjects;

// Java Extras
import java.awt.Point;
import java.awt.Color;
import javax.swing.JButton;

public class Space extends JButton {

    private boolean isEmpty = true;
    private boolean isMine = false;
    private int surroundingMines = 0;
    private int r, c;
    private boolean isCleared = false;

    public Space(int row, int col) {
        this.r = row;
        this.c = col;

        isEmpty = true;
        isMine = false;
        surroundingMines = 0;
        isCleared = false;
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

        // Right
        if ((r + 1) < length) {
            if (board[r + 1][c].isMine()) {
                surroundingMines++;
            }
        }

        // Left
        if ((r - 1) >= 0) {
            if (board[r - 1][c].isMine()) {
                surroundingMines++;
            }
        }

        // Bottom
        if ((c + 1) < height) {
            if (board[r][c + 1].isMine()) {
                surroundingMines++;
            }
        }

        // Top
        if ((c - 1) >= 0) {
            if (board[r][c - 1].isMine()) {
                surroundingMines++;
            }
        }

        // Bottom Right
        if ((r + 1) < length && (c + 1) < height) {
            if (board[r + 1][c + 1].isMine) {
                surroundingMines++;
            }
        }

        // Bottom Left
        if ((r - 1) >= 0 && (c + 1) < height) {
            if (board[r - 1][c + 1].isMine()) {
                surroundingMines++;
            }
        }

        // Top Right
        if ((r + 1) < length && (c - 1) >= 0) {
            if (board[r + 1][c - 1].isMine()) {
                surroundingMines++;
            }
        }

        // Top Left
        if ((r - 1) >= 0 && (c - 1) >= 0) {
            if (board[r - 1][c - 1].isMine()) {
                surroundingMines++;
            }
        }

        return surroundingMines;
    }

    public Point[] getMinePoints(Point clickedSpace, Space[][] board) {
        Point[] mines = new Point[8];
        int r = (int) clickedSpace.getX();
        int c = (int) clickedSpace.getY();
        
        int length = board.length;
        int height = board[r].length;

        int index = 0;

        // Right
        if ((r + 1) < length) {
            if (board[r + 1][c].isMine()) {
                mines[index] = board[r + 1][c].getLocation();
                index++;
            }
        }

        // Left
        if ((r - 1) >= 0) {
            if (board[r - 1][c].isMine()) {
                mines[index] = board[r - 1][c].getLocation();
                index++;
            }
        }

        // Bottom
        if ((c + 1) < height) {
            if (board[r][c + 1].isMine()) {
                mines[index] = board[r][c + 1].getLocation();
                index++;
            }
        }

        // Top
        if ((c - 1) >= 0) {
            if (board[r][c - 1].isMine()) {
                mines[index] = board[r][c - 1].getLocation();
                index++;
            }
        }

        // Bottom Right
        if ((r + 1) < length && (c + 1) < height) {
            if (board[r + 1][c + 1].isMine) {
                mines[index] = board[r + 1][c + 1].getLocation();
                index++;
            }
        }

        // Bottom Left
        if ((r - 1) >= 0 && (c + 1) < height) {
            if (board[r - 1][c + 1].isMine()) {
                mines[index] = board[r - 1][c + 1].getLocation();
                index++;
            }
        }

        // Top Right
        if ((r + 1) < length && (c - 1) >= 0) {
            if (board[r + 1][c - 1].isMine()) {
                mines[index] = board[r + 1][c - 1].getLocation();
                index++;
            }
        }

        // Top Left
        if ((r - 1) >= 0 && (c - 1) >= 0) {
            if (board[r - 1][c - 1].isMine()) {
                mines[index] = board[r - 1][c - 1].getLocation();
                index++;
            }
        }

        return mines;
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

        int surrounding = colorSpace.getSurroundingMines(buttons, row, column);

        int x = colorSpace.getLocationX();
        int y = colorSpace.getLocationY();

        if (colorSpace.isEmptySpace()) {
            if ((x + y) % 2 == 0) {
                colorSpace.setBackground(green1);
            } else {
                colorSpace.setBackground(green2);
            }
        }

        if (colorSpace.isCleared()) {
            if ((x + y) % 2 == 0) {
                colorSpace.setBackground(brown2);
            } else {
                colorSpace.setBackground(brown1);
            }
        }

        if (colorSpace.isMine()) {
            colorSpace.setBackground(Color.BLACK);
        }
    }

}
