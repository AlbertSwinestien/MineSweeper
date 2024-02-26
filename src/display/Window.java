
/**
 * @author John Kunz
 * @since 31/1/2024
 */

// Package Declarations
package display;

// Package Imports
import gameObjects.Board;
import gameFunctions.DifficultySelector;

// Java Extras
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;

public class Window extends Canvas {
    static JFrame frame = new JFrame();
    JPanel[] panels = new JPanel[15];
    Dimension windowDimension;
    JButton startGame = new JButton("Start Game");
    static Board game;
    DifficultySelector diff;

    public Window(int width, int height, String title) {
        frame.setTitle(title);
        windowDimension = new Dimension(width, height);
        frame.setPreferredSize(windowDimension);
        frame.setLayout(new BorderLayout());
        frame.add(panels[0] = new JPanel(), BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panels[1] = new JPanel();
        panels[2] = new JPanel();
        panels[2].setLayout(new BorderLayout());
        panels[3] = new JPanel();

        JLabel titleLabel = new JLabel("MineSweeper");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));

        panels[3].add(titleLabel);

        diff = new DifficultySelector(panels[2]);
        panels[2].add(startGame, BorderLayout.EAST);
        startGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panels[1].removeAll();
                makeBoard(DifficultySelector.boardWidth, DifficultySelector.boardHeight);
            }

        });

        panels[0].add(panels[2], BorderLayout.NORTH);
        panels[2].add(panels[3], BorderLayout.NORTH);
        frame.setVisible(true);
    }

    public void makeBoard(int width, int height) {
        panels[1].setLayout(new GridLayout(height, width));

        game = new Board(width, height, frame.getHeight() / width, panels[1]);
        panels[0].add(panels[1]);

        panels[0].revalidate();
        panels[0].repaint();

        panels[1].revalidate();
        panels[1].repaint();

    }

    public static void checkLose() {
        if (Board.playerLost) {
            game.lose(frame);
        }
    }

    public static void checkWin() {
        if (Board.playerWon) {
            game.win(frame);
        }
    }
}