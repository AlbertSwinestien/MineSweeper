
/**
 * @author John Kunz
 * @since 21/2/2024
 */

// Package Declaration
package gameFunctions;

// Package Imports
import gameObjects.*;

// Java Extras
import java.util.Random;

public class SpreadTiles {
    Random rand = new Random();

    int randX, randY;
    private Space[][] tiles;

    public SpreadTiles(Space[][] buttons) {
        this.tiles = buttons;
    }
    
    /**
     * 
     * @param startSpace
     */
    public void clearEmptyMiddleClicked(Space startSpace) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = startSpace.getLocationX() + dx;
                int y = startSpace.getLocationY() + dy;
                if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[x].length) {
                    Space changedSpace = tiles[x][y];

                    int flagged = startSpace.getSurroundingFlags(tiles, startSpace.getLocationX(),
                            startSpace.getLocationY());
                    int mines = startSpace.getSurroundingMines(tiles, startSpace.getLocationX(),
                            startSpace.getLocationY());

                    if (flagged == mines && (changedSpace.isEmptySpace() && changedSpace.isFlagged())) {
                        Board.playerLost = true;
                    } else {
                        int mineAmount = changedSpace.getSurroundingMines(tiles, x, y);
                        if (changedSpace.isEmptySpace()) {
                            changedSpace.setCleared(true);
                            changedSpace.changeColor(x, y, tiles);
                            if (mineAmount > 0)
                                changedSpace.setTextColor(mineAmount);
                        }

                    }
                }
            }
        }
    }
}
