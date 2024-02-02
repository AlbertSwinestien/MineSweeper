package gameFuctions;

import java.awt.event.MouseEvent;

/**
 * @author John Kunz
 * @since 1/2/2024
 */

import javax.swing.event.*;

public class ClickListener implements MouseInputListener {

    private int mouseButtonClicked;
    private boolean[] mouseButtons = new boolean[3];

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseButtonClicked = e.getButton();

        if (mouseButtonClicked == MouseEvent.BUTTON1) {
            System.out.println("Left Clicked?");
            mouseButtons[0] = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseDragged'");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }
    
}
