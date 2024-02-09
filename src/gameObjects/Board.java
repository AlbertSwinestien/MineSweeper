
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */

// Package Declaration
package gameObjects;

// Java Extras
import javax.swing.*;

import display.Window;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;
import java.awt.Color;

public class Board {
    // Instance variables
    Space[][] buttons;
    Random rand = new Random();
    private int boardWidth, boardHeight, buttonWidth;
    private int mineAmount = 10;
    private int clicks = 0;
    public static boolean areWinning = true;

    public Board(int width, int height, int buttonwidth, JPanel panel) {
        boardWidth = width;
        boardHeight = height;
        buttonWidth = buttonwidth;

        panel.setLayout(new GridLayout(boardHeight, boardWidth));
        buttons = new Space[boardHeight][boardWidth];

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
                        if (areWinning) {
                            switch (button) {
                                case MouseEvent.BUTTON1:
                                    if (clicks == 0) {
                                        int randomSpreadNum = rand.nextInt(3) + 1;
                                        spreadTiles(randomSpreadNum, clickedSpace);
                                    } else {
                                        if (clickedSpace.isMine()) {
                                            System.out.println("You clicked a Mine!");
                                            areWinning = false;
                                        }
                                        updateTiles(clickedSpace, xIndex, yIndex);
                                    }
                                    break;
                                case MouseEvent.BUTTON2:
                                    // Middle-click logic
                                    if (clicks > 0) {
                                        int surroundingFlags = clickedSpace.getSurroundingFlags(buttons, xIndex,
                                                yIndex);
                                        if (surroundingFlags > 0) {
                                            // Check if surrounding flags are equal to surrounding mines
                                            int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex,
                                                    yIndex);
                                            if (surroundingFlags == surroundingMines) {
                                                revealSurrounding(clickedSpace, xIndex, yIndex);
                                            }
                                        }
                                    }
                                    break;
                                case MouseEvent.BUTTON3:
                                    if (!clickedSpace.isCleared()) {
                                        if (clickedSpace.isFlagged()) {
                                            clickedSpace.setFlagged(false);
                                        } else {
                                            clickedSpace.setFlagged(true);
                                        }
                                    }
                                    clickedSpace.changeColor(xIndex, yIndex, buttons);
                                    break;
                                default:
                                    System.out.println("Don't know what that was...");
                                    break;
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Color green1 = Color.GREEN;
                        Color green2 = new Color(0, 225, 50);
                        int button = e.getButton();

                        if (areWinning) {
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
                buttons[i][j].setBorderPainted(false);
                buttons[i][j].setPreferredSize(new Dimension(buttonWidth, buttonWidth));
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
                    buttons[i][j].setEmptySpace(false);
                } else {
                    buttons[i][j].setEmptySpace(true);
                }
            }
        }

    }

    private void updateTiles(Space clickedSpace, int xIndex, int yIndex) {
        int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex, yIndex);

        if (surroundingMines > 0 && !clickedSpace.isMine()) {
            clickedSpace.setEmptySpace(false);
            clickedSpace.setTextColor(surroundingMines);
        }

        clickedSpace.setCleared(true);

        if (clickedSpace.getSurroundingEmpty(clickedSpace, buttons) > 0) {
            // Mark the clicked space as cleared
            clickedSpace.changeColor(xIndex, yIndex, buttons);

            // Get the surrounding spaces
            for (int dx = -1; dx < 1; dx++) {
                for (int dy = -1; dy < 1; dy++) {
                    // Skip the current clicked space
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    int x = xIndex + dx;
                    int y = yIndex + dy;
                    if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                        Space adjacentSpace = buttons[x][y];
                        if (adjacentSpace.isEmptySpace() || adjacentSpace.getSurroundingMines(buttons, x, y) > 0) {
                            adjacentSpace.setCleared(true);
                            if (adjacentSpace.getSurroundingEmpty(adjacentSpace, buttons) > 0) {
                                updateTiles(adjacentSpace, x, y);
                            }
                        }
                    }
                }
            }
        }

        Window.checkLose();

        clickedSpace.changeColor(xIndex, yIndex, buttons);
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
            buttons[randX][randY].setMine(true);
            buttons[randX][randY].setEmptySpace(false);
        }

        // Spread to adjacent empty spaces
        for (int dx = spreadAmount * -1; dx <= spreadAmount; dx++) {
            for (int dy = spreadAmount * -1; dy <= spreadAmount; dy++) {
                int x = startSpace.getLocationX() + dx;
                int y = startSpace.getLocationY() + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space changedSpace = buttons[x][y];
                    int surroundingEmpty = changedSpace.getSurroundingEmpty(changedSpace, buttons);
                    if (!changedSpace.isMine()) {
                        changedSpace.setCleared(true);
                        changedSpace.changeColor(x, y, buttons);
                        int mineNum = changedSpace.getSurroundingMines(buttons, x, y);
                        if (mineNum > 0) {
                            changedSpace.setTextColor(mineNum);
                        }
                        if (surroundingEmpty > 0) {
                            changedSpace.clearEmpty(changedSpace, buttons);
                        }
                    }
                }
            }
        }
        clicks++;
    }

    private void revealSurrounding(Space clickedSpace, int xIndex, int yIndex) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = xIndex + dx;
                int y = yIndex + dy;
                if (x >= 0 && x < boardHeight && y >= 0 && y < boardWidth) {
                    Space adjacentSpace = buttons[x][y];
                    if (!adjacentSpace.isFlagged() && !adjacentSpace.isCleared()) {
                        updateTiles(adjacentSpace, x, y);
                    }
                    if (adjacentSpace.isMine() && !adjacentSpace.isFlagged()) {
                        adjacentSpace.changeColor(x, y, buttons);
                    }
                }
            }
        }
    }

    public void loose(JFrame frame) {
        showMines();
        JOptionPane.showMessageDialog(frame, "You Loose!", "MineSweeper", 1);
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
