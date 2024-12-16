package main.java;

import javax.swing.*;
import java.awt.*;

public class Note extends Thread {
    private Image noteBasicImage = new ImageIcon(getClass().getResource("../Resources/images/noteBasic.png")).getImage();
    private int x, y = 580 - (1000 / Main.SLEEP_TIME * Main.SLEEP_TIME) * Main.REACH_TIME;
    private String noteType;
    private boolean proceeded = true;
    private Game game;

    public String getNoteType() {
        return noteType;
    }

    public boolean isProceeded() {
        return proceeded;
    }

    public void close() {
        proceeded = false;
    }

    public Note(String noteType) {
        if (noteType.equals("Q")) {
            x = 228;
        } else if (noteType.equals("W")) {
            x = 332;
        } else if (noteType.equals("E")) {
            x = 436;
        }else if (noteType.equals("Space")) {
            x = 540;
        } else if (noteType.equals("I")) {
            x = 744;
        } else if (noteType.equals("O")) {
            x = 848;
        } else if (noteType.equals("P")) {
            x = 952;
        }
        this.noteType = noteType;
    }

    public void screenDraw(Graphics2D g) {
        if(!noteType.equals("Space")) {
            g.drawImage(noteBasicImage, x, y, null);
        } else {
            g.drawImage(noteBasicImage, x, y, null);
            g.drawImage(noteBasicImage, x + 100, y, null);
        }
    }

    public void drop() {
        y += Main.NOTE_SPEED;
        if (y > 620) {
            close();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                drop();
                if (proceeded) {
                    Thread.sleep(Main.SLEEP_TIME);
                } else {
                    interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String judge() {
        if(y >= 613) {
            close();
            return "Good";
        } else if (y >= 600) {
            close();
            return "Great";
        } else if (y >= 587) {
            close();
            return "Perfect";
        } else if (y >= 550) {
            close();
            return "Great";
        } else if (y >= 533) {
            close();
            return "Good";
        }
        return "None";
    }

    public int getY() {
        return y;
    }
}