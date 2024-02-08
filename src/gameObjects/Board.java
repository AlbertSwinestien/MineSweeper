
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
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Random;

public class Board {
    // Instance variables
    Space[][] buttons;
    Random rand = new Random();
    private int boardWidth, boardHeight, buttonWidth;
    private int mineAmount = 10;
    private int clicks = 0;

    Color color_1 = new Color(0, 148, 201);
    Color color_2 = new Color(0, 138, 0);
    Color color_3 = new Color(173, 31, 31);
    Color color_4 = new Color(179, 0, 255);
    Color color_5 = Color.ORANGE;

    public Board(int width, int height, int buttonwidth, JPanel panel) {
        boardWidth = width;
        boardHeight = height;
        buttonWidth = buttonwidth;

        panel.setLayout(new GridLayout(boardHeight, boardWidth));
        buttons = new Space[boardHeight][boardWidth];

        start(panel);

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

    private void start(JPanel panel) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int xIndex = i;
                int yIndex = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        Space clickedSpace = buttons[xIndex][yIndex];

                        if (clicks == 0) {
                            int randomSpreadNum = rand.nextInt(8) + 1;
                            spreadTiles(randomSpreadNum, clickedSpace);
                        }

                        int surroundingMines = clickedSpace.getSurroundingMines(buttons, xIndex, yIndex);

                        if (surroundingMines > 0 && !clickedSpace.isMine()) {
                            // Set the text of the button to the number of surrounding mines
                            clickedSpace.setText(Integer.toString(surroundingMines));
                            clickedSpace.setEmptySpace(false);
                            switch (surroundingMines) {
                                case 1:
                                    clickedSpace.setForeground(color_1);
                                    break;
                                case 2:
                                    clickedSpace.setForeground(color_2);
                                    break;
                                case 3:
                                    clickedSpace.setForeground(color_3);
                                    break;
                                case 4:
                                    clickedSpace.setForeground(color_4);
                                    break;
                                case 5:
                                    clickedSpace.setForeground(color_5);
                                    break;
                                default:
                                    clickedSpace.setForeground(Color.BLACK);
                                    break;
                            }
                        } else {
                            // Handle empty space
                            clickedSpace.setEmptySpace(true);
                        }

                        clickedSpace.setCleared(true);

                        clickedSpace.changeColor(xIndex, yIndex, buttons);
                    }
                });
                panel.add(buttons[i][j]);
            }
        }
    }

    private void spreadTiles(int spreadAmount, Space startSpace) {
        int xPos = startSpace.getLocationX();
        int yPos = startSpace.getLocationY();

        startSpace = buttons[xPos][yPos];

        if (startSpace.isMine())

        {
            int randX = rand.nextInt(buttons.length);
            int randY = rand.nextInt(buttons[0].length);

            while (buttons[randX][randY].isMine()) {
                startSpace.setMine(false);
                buttons[randX][randY].setMine(true);

                if (startSpace.getSurroundingMines(buttons, startSpace.getLocationX(),
                        startSpace.getLocationY()) > 0) {
                    startSpace.setEmptySpace(false);
                } else {
                    startSpace.setEmptySpace(true);
                }
                buttons[randX][randY].setEmptySpace(false);

                randX = rand.nextInt(buttons.length);
                randY = rand.nextInt(buttons[0].length);
            }
        }

        for (int i = 0; i < spreadAmount; i++) {
            int index = i + 1;
            int right = xPos + index;
            int left = xPos - index;
            int up = yPos - index;
            int down = yPos + index;

            if (right < buttons.length) {
                int mineAmountButton = buttons[right][yPos].getSurroundingMines(buttons, right, yPos);
                if (mineAmountButton > 0)
                    buttons[right][yPos].setEmptySpace(false);
                if (buttons[right][yPos].isEmptySpace()) {

                }
            }

            if (left >= 0) {
                int mineAmountButton = buttons[left][yPos].getSurroundingMines(buttons, left, yPos);
                if (mineAmountButton > 0)
                    buttons[left][yPos].setEmptySpace(false);
                if (buttons[left][yPos].isEmptySpace()) {

                }
            }

            if (down >= 0) {
                int mineAmountButton = buttons[xPos][down].getSurroundingMines(buttons, xPos, down);
                if (mineAmountButton > 0)
                    buttons[xPos][down].setEmptySpace(false);
                if (buttons[xPos][down].isEmptySpace()) {

                }
            }

            if (up < buttons[xPos].length) {
                int mineAmountButton = buttons[xPos][down].getSurroundingMines(buttons, xPos, down);
                if (mineAmountButton > 0)
                    buttons[xPos][down].setEmptySpace(false);
                if (buttons[xPos][down].isEmptySpace()) {

                }
            }

            i++;
        }
        clicks++;
    }
}
