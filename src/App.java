
/**
 * @author John Kunz
 * @since 29/1/2024, January 29 2024
 */
import display.*;

public class App {
    public static void main(String[] args) throws Exception {
        Window window = new Window(800, 600, "MineSweeper");
        window.makeBoard(10, 8);
    }
}
