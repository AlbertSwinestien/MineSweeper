
/**
 * @author John Kunz
 * @since 2/4/2024
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class Minesweeper {

    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    JFrame frame = new JFrame("MineSweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int tileSize = 70;
    int xCount = 10;
    int yCount = 8;
    int mineAmount;

    MineTile[][] board = new MineTile[yCount][xCount];
    ArrayList<MineTile> mineList;

    Minesweeper() {
        frame.setSize(1200, 900);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Mines: " + mineAmount);
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(yCount, xCount));
        boardPanel.setPreferredSize(new Dimension(xCount * tileSize, yCount * tileSize));
        frame.add(boardPanel);

        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                MineTile tile = new MineTile(j, i);
                board[j][i] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                // tile.setText("💣");
                tile.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        
                    }
                });
                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);
        setMines();
    }

    void setMines() {
        mineList = new ArrayList<MineTile>();

        mineList.add(board[2][2]);
        mineList.add(board[2][4]);
        mineList.add(board[2][6]);
        mineList.add(board[2][5]);
        mineList.add(board[7][2]);

    }

}
