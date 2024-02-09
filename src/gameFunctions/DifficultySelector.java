
/**
 * @author John M Kunz
 * @since 9/2/2024, Febuary 9 2024
 */

package gameFunctions;

import javax.swing.*;
import java.awt.BorderLayout;

import display.*;
import gameObjects.*;

public class DifficultySelector {

    private JPanel dsPanel;
    public JSlider diffSlider = new JSlider(1, 3, 2);

    public DifficultySelector(JFrame frame, JPanel panel) {
        this.dsPanel = panel;

        diffSlider.setPaintTicks(true);
        diffSlider.setPaintLabels(true);
        diffSlider.setSnapToTicks(true);
        dsPanel.add(diffSlider, BorderLayout.NORTH);
        dsPanel.repaint();
    }

}
