
/**
 * @author John Kunz
 * @since 31/1/2024
 */

// Package Declarations
package display;

// Package Imports
import gameFuctions.ClickListener;
import gameObjects.Board;

// Java Extras
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

public class Window extends Canvas{
    ClickListener listener = new ClickListener();
    JFrame frame = new JFrame();
    JPanel[] panels = new JPanel[15];
    Dimension windowDimension;
    
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
    }

    public void makeBoard(int width, int height) {
        
        Board game = new Board(width, height, frame.getHeight() / width, panels[1]);

        panels[0].add(panels[1]);
        panels[0].revalidate();
        panels[0].repaint();

        game.addMines();
    }
}
