
/**
 * @author John M Kunz
 * @since 11/2/2024, Febuary 11 2024
 */

// Package Declaration
package gameFunctions;

// Java Extras
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DifficultySelector {

    // Instance Objects
    private JPanel dsPanel;
    public JSlider diffSlider = new JSlider(1, 3, 2);

    // Instance Variables
    public static int boardWidth = 18, boardHeight = 14, mineCount = 40;

    /**
     * Makes a slider that determines the difficulty
     * 
     * @param panel The panel that the slider will be added to
     */
    public DifficultySelector(JPanel panel) {

        // Sets the local panel to the panel used with the constructed DifficultySelector
        this.dsPanel = panel;

        // The slider Snaps to the ticks
        diffSlider.setSnapToTicks(true);

        // The Slider is Visible
        diffSlider.setVisible(true);

        // Adds the slider to the panel
        dsPanel.add(diffSlider);

        // Repaints and revalidates the Panel
        dsPanel.repaint();
        dsPanel.revalidate();

        // Adds the change listener to the slider
        diffSlider.addChangeListener(new ChangeListener() {

            // Each time the value of the slider changes, it sets the difficulty
            @Override
            public void stateChanged(ChangeEvent e) {
                setDifficulty();
            }
        });
    }

    /**
     * Checks the value of the slider and sets the board area (width and height in
     * tiles) and mine amount to the corresponding difficulty
     * <ul>
     * <li>Easy: Lowest slider value, 10x8 board grid, 10 mines</li>
     * <li>Medium: Middle slider value, 18x14 board grid, 40 mines</li>
     * <li>Hard: Highest slider value, 24x20 board grid, 99 mines</li>
     * </ul>
     */
    public void setDifficulty() {
        // Gets the value of the difficulty slider
        int diff = diffSlider.getValue();

        // Easy mode
        if (diff == 1) {
            boardWidth = 10;
            boardHeight = 8;
            mineCount = 10;
        }

        // Normal Mode
        if (diff == 2) {
            boardWidth = 18;
            boardHeight = 14;
            mineCount = 40;
        }

        // Hard Mode
        if (diff == 3) {
            boardWidth = 24;
            boardHeight = 20;
            mineCount = 99;
        }
    }
}
