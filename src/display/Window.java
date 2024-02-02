
/**
 * @author John Kunz
 * @since 31/1/2024
 */

package display;

import javax.swing.*;

import gameFuctions.ClickListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;

public class Window extends Canvas{
    ClickListener listener = new ClickListener();
    JFrame frame = new JFrame();
    JPanel[] panels = new JPanel[15];
    Dimension windowDimension;
    JButton[][] buttons;
    Color green1 = new Color(0, 255, 0);
    Color green2 = new Color(0, 225, 50);
    Color brown1 = new Color(155, 125, 75);
    Color brown2 = new Color(155, 150, 75);
    
    public Window(int width, int height, String title) {
        frame.setTitle(title);
        windowDimension = new Dimension(width, height);
        frame.setPreferredSize(windowDimension);
        frame.setVisible(true);
        frame.add(panels[0] = new JPanel());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void makeBoard(int width, int height) {
        panels[1] = new JPanel();
        int buttonwidth = frame.getHeight() / width;

        panels[1].setLayout(new GridLayout(height, width));
        buttons = new JButton[height][width];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int xIndex = i;
                int yIndex = j;
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(buttonwidth, buttonwidth));
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if ((xIndex + yIndex) % 2 == 0) {
                            buttons[xIndex][yIndex].setBackground(brown1);
                        } else {
                            buttons[xIndex][yIndex].setBackground(brown2);
                        }
                    }                    
                });
                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(green1);
                    panels[1].add(buttons[i][j]);
                    // System.out.println("Green1 Button added");
                } else {
                    buttons[i][j].setBackground(green2);
                    panels[1].add(buttons[i][j]);
                    // System.out.println("Green2 Button added");
                }
            }
        }
        panels[0].add(panels[1]);

        panels[0].revalidate();
        panels[0].repaint();

        panels[1].revalidate();
        panels[1].repaint();
    }
}
