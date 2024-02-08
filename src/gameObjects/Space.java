
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */
// Package Declaration
package gameObjects;

// Java Extras
import java.awt.Point;
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

    public Point getLocation() {
        return new Point(r, c);
    }

    public void setLocation(boolean isEmpty, int x, int y) {
        this.isEmpty = isEmpty;
        this.r = x;
        this.c = y;
    }

}
