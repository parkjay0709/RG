package main.java;

import java.io.*;
import javazoom.jl.player.Player;
import java.io.File;

public class Music extends Thread {
    private Player player;
    private boolean isLoop;
    private File file;
    private FileInputStream fis;
    private BufferedInputStream bis;

    public Music( String name, boolean isLoop ) {
        try {
            this.isLoop = isLoop;
            file = new File( getClass().getResource("../Resources/music/" + name ).toURI());
            fis = new FileInputStream( file );
            bis = new BufferedInputStream( fis );
            this.player = new Player( bis );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }

    public int getTime() {
        if ( player == null )
            return 0;
        return player.getPosition();
    }

    public void close() {
        isLoop = false;
        player.close();
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            do {
                player.play();
                fis = new FileInputStream( file );
                bis = new BufferedInputStream( fis );
                player = new Player( bis );
            } while( isLoop );
        } catch ( NullPointerException e ) {
            e.printStackTrace();
            System.out.println( e.getMessage() );
        } catch ( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }
}