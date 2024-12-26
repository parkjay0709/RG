package main.java;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {
    @Override
    public void keyPressed( KeyEvent e ) {
        if ( TempoX.game == null ) return;
        switch ( e.getKeyCode() ) {
            case KeyEvent.VK_Q : TempoX.game.pressQ(); break;
            case KeyEvent.VK_W : TempoX.game.pressW(); break;
            case KeyEvent.VK_E : TempoX.game.pressE(); break;
            case KeyEvent.VK_SPACE : TempoX.game.pressSpace(); break;
            case KeyEvent.VK_NUMPAD7 : TempoX.game.press7(); break;
            case KeyEvent.VK_NUMPAD8 : TempoX.game.press8(); break;
            case KeyEvent.VK_NUMPAD9 : TempoX.game.press9(); break;
        }
    }
    @Override
    public void keyReleased( KeyEvent e ) {
        if ( TempoX.game == null ) return;
        switch ( e.getKeyCode() ) {
            case KeyEvent.VK_Q : TempoX.game.releaseQ(); break;
            case KeyEvent.VK_W : TempoX.game.releaseW(); break;
            case KeyEvent.VK_E : TempoX.game.releaseE(); break;
            case KeyEvent.VK_SPACE : TempoX.game.releaseSpace(); break;
            case KeyEvent.VK_NUMPAD7 : TempoX.game.release7(); break;
            case KeyEvent.VK_NUMPAD8 : TempoX.game.release8(); break;
            case KeyEvent.VK_NUMPAD9 : TempoX.game.release9(); break;
        }
    }
}
