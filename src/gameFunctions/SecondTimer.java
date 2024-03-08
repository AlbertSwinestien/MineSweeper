
/**
 * @author John Kunz
 * @since 1/3/2024
 */

// Package Declaration
package gameFunctions;

// Package Imports
import gameObjects.Board;
import display.Window;

public class SecondTimer {

    // Makes a new Thread that counts each passing second while the game is running
    Thread thread = new Thread(() -> {
        // While the game is playing
        while (Board.playingGame) {
            // If the passed seconds are not equal to 999, run through the code
            if (Board.seconds != 1000) {
                // Formats a string to a number with three digits (i.e. 001, 002, etc.)
                String formattedNumber = String.format("%03d", Board.seconds);
                
                // Updates the game timer
                Window.updateTimer(formattedNumber);

                // Tries to wait 1 second (1000 miliseconds)
                try {
                    Thread.sleep(1000);
                // Catches the Interrupted Exception error
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Increments the seconds variable
                Board.seconds++;
            }
        }
    });

    /**
     * Starts the thread.
     * 
     * If the thread is already alive, it stops the method
     */
    public void run() {
        if (thread.isAlive()) {
            return;
        }
        thread.start();
    }

    /**
     * Checks if the game timer is running
     * 
     * @return The status of the thread. If it is alive, it returns true, otherwise
     *         false.
     */
    public boolean isRunning() {
        return thread.isAlive();
    }
}
