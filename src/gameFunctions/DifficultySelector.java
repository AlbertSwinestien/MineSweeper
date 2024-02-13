
/**
 * @author John M Kunz
 * @since 11/2/2024, Febuary 11 2024
 */

package gameFunctions;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DifficultySelector {

    private JPanel dsPanel;
    public JSlider diffSlider = new JSlider(1, 3, 2);

    public static int boardWidth = 18, boardHeight = 14, mineCount = 40;

    public DifficultySelector(JPanel panel) {
        this.dsPanel = panel;

        diffSlider.setPaintTicks(true);
        diffSlider.setPaintLabels(true);
        diffSlider.setSnapToTicks(true);
        diffSlider.setVisible(true);
        dsPanel.add(diffSlider);
        dsPanel.repaint();
        dsPanel.revalidate();

        diffSlider.addChangeListener(new ChangeListener() {
            

            @Override
            public void stateChanged(ChangeEvent e) {
                setDifficulty();
            }
        });
    }

    public void setDifficulty() {
        int diff = diffSlider.getValue();

        if (diff == 1) {
            boardWidth = 10;
            boardHeight = 8;
            mineCount = 10;
        }
        if (diff == 2) {
            boardWidth = 18;
            boardHeight = 14;
            mineCount = 40;
        }
        if (diff == 3) {
            boardWidth = 24;
            boardHeight = 20;
            mineCount = 99;
        }
    }
}
