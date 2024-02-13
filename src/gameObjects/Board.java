
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
import java.awt.Color;

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
        areWinning = true;
        panel.revalidate();
        panel.repaint();
    }

    public void addMines() {
        for (int m = 0; m < mineAmount; m++) {
            int randX = rand.nextInt(buttons.length);
            int randY = rand.nextInt(buttons[0].length);

            // Check if the selected space is already a mine
            while (buttons[randX][randY].isMine()) {
                randX = rand.nextInt(buttons.length);
                randY = rand.nextInt(buttons[0].length);
            }
            // Set the new space as a mine
            buttons[randX][randY].setMine(true);
            buttons[randX][randY].setEmptySpace(false);
            buttons[randX][randY].setCleared(false);
        }
    }

    private void start(JPanel panel) {
        flags = 0;
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
                                    if (!clickedSpace.isFlagged()) {
                                        if (clicks > 0) {
                                            revealSurrounding(clickedSpace, xIndex, yIndex);
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
                        Color green1 = Color.GREEN;
                        Color green2 = new Color(0, 225, 50);
                        int button = e.getButton();

                        if (areWinning && arePlaying) {
                            if (button == MouseEvent.BUTTON2) {
                                Space clickedSpace = buttons[xIndex][yIndex];
                                for (int i = -1; i <= 1; i++) {
                                    for (int j = -1; j <= 1; j++) {
                                        int x = clickedSpace.getLocationX() + i;
                                        int y = clickedSpace.getLocationY() + j;
                                        if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                                            Space one = buttons[x][y];
                                            if (!one.isFlagged() && !one.isCleared() && !one.isMine()) {
                                                int x1 = one.getLocationX();
                                                int y1 = one.getLocationY();
                                                if ((x1 + y1) % 2 == 0) {
                                                    one.setBackground(green2);
                                                } else {
                                                    one.setBackground(green1);
                                                }
                                            }
                                        }
                                    }
                                }
                                updateTiles(clickedSpace, xIndex, yIndex);
                            }
                        }
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
        addMines();
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

    private void updateTiles(Space clickedSpace, int xIndex, int yIndex) {
        int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex, yIndex);

        if (clickedSpace.isMine()) {
            return;
        } else {
            if (surroundingMines > 0 && !clickedSpace.isMine()) {
                clickedSpace.setTextColor(surroundingMines);
            }

            if (clickedSpace.getSurroundingEmpty(clickedSpace, buttons) > 0) {
                clickedSpace.changeColor(xIndex, yIndex, buttons);
                for (int dx = -1; dx < 1; dx++) {
                    for (int dy = -1; dy < 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue;
                        }
                        int x = xIndex + dx;
                        int y = yIndex + dy;
                        if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                            Space adjacentSpace = buttons[x][y];
                            if (adjacentSpace.isEmptySpace() || adjacentSpace.getSurroundingMines(buttons, x, y) > 0
                                    && !adjacentSpace.isMine()) {
                                adjacentSpace.setCleared(true);
                                if (adjacentSpace.getSurroundingEmpty(adjacentSpace, buttons) > 0) {
                                    updateTiles(adjacentSpace, x, y);
                                }
                            }
                        }
                    }
                }
            }
            clickedSpace.setCleared(true);
        }
        clickedSpace.changeColor(xIndex, yIndex, buttons);
        clickedSpace.setBorder(null);
    }

    private void spreadTiles(int spreadAmount, Space startSpace) {
        if (startSpace == null || startSpace.isCleared()) {
            System.out.println("Something's wrong...");
            return;
        }

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

        for (int dx = spreadAmount * -1; dx <= spreadAmount; dx++) {
            for (int dy = spreadAmount * -1; dy <= spreadAmount; dy++) {
                int x = startSpace.getLocationX() + dx;
                int y = startSpace.getLocationY() + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space changedSpace = buttons[x][y];
                    int surroundingEmpty = changedSpace.getSurroundingEmpty(changedSpace, buttons);
                    if (!changedSpace.isEmptySpace() && !changedSpace.isMine()) {
                        changedSpace.setCleared(true);
                        int mineNum = changedSpace.getSurroundingMines(buttons, x, y);
                        if (mineNum > 0) {
                            changedSpace.setTextColor(mineNum);
                        }
                        if (surroundingEmpty > 0) {
                            changedSpace.clearEmpty(changedSpace, buttons);
                        }
                        changedSpace.changeColor(x, y, buttons);
                    }
                }
            }
        }
        clicks++;
    }

    private void revealSurrounding(Space clickedSpace, int xIndex, int yIndex) {
        int surroundingFlags = clickedSpace.getSurroundingFlags(buttons, xIndex, yIndex);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = xIndex + dx;
                int y = yIndex + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space adjacentSpace = buttons[x][y];
                    if (!adjacentSpace.isFlagged() && !adjacentSpace.isCleared()) {
                        updateTiles(adjacentSpace, x, y);
                    }
                    if (surroundingFlags != clickedSpace.getSurroundingMines(buttons, xIndex, yIndex)) {
                        areWinning = false;
                        updateTiles(clickedSpace, x, y);
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
