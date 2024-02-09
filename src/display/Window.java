
/**
 * @author John Kunz
 * @since 31/1/2024
 */

// Package Declarations
package display;

// Package Imports
import gameObjects.Board;
import java.awt.GridLayout;

// Java Extras
import javax.swing.*;

import gameFunctions.DifficultySelector;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;

public class Window extends Canvas {
    static JFrame frame = new JFrame();
    JPanel[] panels = new JPanel[15];
    Dimension windowDimension;
    Color green1 = new Color(0, 255, 0);
    Color green2 = new Color(0, 225, 50);
    Color brown1 = new Color(155, 125, 75);
    Color brown2 = new Color(155, 150, 75);
    static Board game;

    public Window(int width, int height, String title) {
        frame.setTitle(title);
        windowDimension = new Dimension(width, height);
        frame.setPreferredSize(windowDimension);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.add(panels[0] = new JPanel(), BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panels[1] = new JPanel();

        new DifficultySelector(frame, panels[0]);
    }

    public void makeBoard(int width, int height) {
        panels[1] = new JPanel();

        panels[1].setLayout(new GridLayout(height, width));

        game = new Board(width, height, frame.getHeight() / width, panels[1]);
        panels[0].add(panels[1]);

        panels[0].revalidate();
        panels[0].repaint();

        panels[1].revalidate();
        panels[1].repaint();
    }

    public static void checkLose() {
        if (!Board.areWinning) {
            game.loose(frame);
        }
    }
}