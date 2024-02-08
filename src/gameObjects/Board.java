
/**
 * @author John Kunz
 * @since 6/2/2024, Feburary 6, 2024
 */

// Package Declaration
package gameObjects;

// Java Extras
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;

public class Board {
    // Instance variables
    Space[][] buttons;
    Color green1 = new Color(0, 255, 0);
    Color green2 = new Color(0, 225, 50);
    Color brown1 = new Color(250, 228, 132);
    Color brown2 = new Color(219, 184, 103);
    Random rand = new Random();
    private int boardWidth, boardHeight, buttonWidth;
    private int mineAmount = 10;
    private int clicks = 0;

    public Board(int width, int height, int buttonwidth, JPanel panel) {
        boardWidth = width;
        boardHeight = height;
        buttonWidth = buttonwidth;

        panel.setLayout(new GridLayout(boardHeight, boardWidth));
        buttons = new Space[boardHeight][boardWidth];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int xIndex = i;
                int yIndex = j;
                buttons[i][j] = new Space(i, j);
                buttons[i][j].setPreferredSize(new Dimension(buttonWidth, buttonWidth));
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Space clickedSpace = buttons[xIndex][yIndex];
                        if (clicks == 0) {
                            start(clickedSpace);
                        } else {
                            int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex, yIndex);

                            if (surroundingMines > 0 && !clickedSpace.isMine()) {
                                // Set the text of the button to the number of surrounding mines
                                clickedSpace.setText(Integer.toString(surroundingMines));
                                clickedSpace.setEmptySpace(false);
                            } else {
                                // Handle empty space
                                clickedSpace.setEmptySpace(true);
                            }

                            if (clickedSpace.isEmptySpace()) {
                                // Set background color for empty space
                                if ((xIndex + yIndex) % 2 == 0) {
                                    clickedSpace.setBackground(brown2);
                                } else {
                                    clickedSpace.setBackground(brown1);
                                }
                            }
                            if (clickedSpace.isMine()) {
                                // Set background color for mine
                                clickedSpace.setBackground(Color.BLACK);
                            }

                            clickedSpace.setCleared(true);
                        }

                    }
                });
                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(green1);
                    panel.add(buttons[i][j]);
                    // System.out.println("Green1 Button added");
                } else {
                    buttons[i][j].setBackground(green2);
                    panel.add(buttons[i][j]);
                    // System.out.println("Green2 Button added");
                }
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    public void addMines() {
        for (int m = 0; m < mineAmount; m++) {
            int randX = rand.nextInt(buttons.length);
            int randY = rand.nextInt(buttons[0].length);

            Space newMine = buttons[randX][randY];

            // Check if the selected space is already a mine
            while (newMine.isMine()) {
                randX = rand.nextInt(buttons.length);
                randY = rand.nextInt(buttons[0].length);
                newMine = buttons[randX][randY];
            }

            // Set the new space as a mine
            newMine.setMine(true);
            newMine.setEmptySpace(false);
        }
    }

    private void start(Space clicked) {

        if (clicked.isMine()) {
            int randX = rand.nextInt(buttons.length);
            int randY = rand.nextInt(buttons[0].length);

            while (buttons[randX][randY].isMine()) {
                clicked.setMine(false);
                buttons[randX][randY].setMine(true);
            }
        }

        int randomSpreadNum = rand.nextInt(8) + 1;
        spreadTiles(randomSpreadNum, clicked);
    }

    private void spreadTiles(int spreadAmount, Space startSpace) {
        int xPos = (int) startSpace.getLocation().getX();
        int yPos = (int) startSpace.getLocation().getY();
        startSpace = buttons[xPos][yPos];

        for (int i = 0; i < spreadAmount; i++) {
            int index = i + 1;
            // Right
            if (xPos + 1 < buttons.length) {
                if (buttons[xPos + 1][yPos].isEmptySpace()) {
                    if ((xPos + yPos) % 2 == 0) {
                        buttons[xPos + 1][yPos].setBackground(brown2);
                    } else {
                        buttons[xPos + 1][yPos].setBackground(brown1);
                    }
                }
            }
            // Left
            if (xPos - 1 >= 0) {
                if ((xPos + yPos) % 2 == 0) {
                    buttons[xPos - 1][yPos].setBackground(brown2);
                } else {
                    buttons[xPos - 1][yPos].setBackground(brown1);
                }
            }
            // Top
            if (yPos + 1 < buttons[xPos].length) {
                if ((xPos + yPos) % 2 == 0) {
                    buttons[xPos][yPos + 1].setBackground(brown2);
                } else {
                    buttons[xPos][yPos + 1].setBackground(brown1);
                }
            }
            // Bottom
            if (yPos - 1 >= 0) {
                if ((xPos + yPos) % 2 == 0) {
                    buttons[xPos][yPos - 1].setBackground(brown2);
                } else {
                    buttons[xPos][yPos - 1].setBackground(brown1);
                }
            }
            // Bottom Right
            if (xPos + 1 < buttons.length && yPos - 1 >= 0) {
                if ((xPos + yPos) % 2 == 0) {
                    buttons[xPos + 1][yPos - 1].setBackground(brown2);
                } else {
                    buttons[xPos + 1][yPos - 1].setBackground(brown1);
                }
            }
        }
    }
}
