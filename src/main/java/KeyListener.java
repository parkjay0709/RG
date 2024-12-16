package main.java;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {
    @Override
    public void keyPressed( KeyEvent e ) {
        if ( TempoX.game == null ) return;
        if ( e.getKeyCode() == KeyEvent.VK_Q ) {
            TempoX.game.pressQ();
        }else if ( e.getKeyCode() == KeyEvent.VK_W ) {
            TempoX.game.pressW();
        }else if ( e.getKeyCode() == KeyEvent.VK_E ) {
            TempoX.game.pressE();
        }else if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
            TempoX.game.pressSpace();
        }else if ( e.getKeyCode() == KeyEvent.VK_I ) {
            TempoX.game.pressI();
        }else if ( e.getKeyCode() == KeyEvent.VK_O ) {
            TempoX.game.pressO();
        }else if ( e.getKeyCode() == KeyEvent.VK_P ) {
            TempoX.game.pressP();
        }
    }
    @Override
    public void keyReleased( KeyEvent e ) {
        if ( TempoX.game == null ) return;
        if ( e.getKeyCode() == KeyEvent.VK_Q ) {
            TempoX.game.releaseQ();
        }else if ( e.getKeyCode() == KeyEvent.VK_W ) {
            TempoX.game.releaseW();
        }else if ( e.getKeyCode() == KeyEvent.VK_E ) {
            TempoX.game.releaseE();
        }else if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
            TempoX.game.releaseSpace();
        }else if ( e.getKeyCode() == KeyEvent.VK_I ) {
            TempoX.game.releaseI();
        }else if ( e.getKeyCode() == KeyEvent.VK_O ) {
            TempoX.game.releaseO();
        }else if ( e.getKeyCode() == KeyEvent.VK_P ) {
            TempoX.game.releaseP();
        }
    }
}
