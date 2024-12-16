package main.java;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

public class Game extends Thread {

    private Image gameInfoImage = new ImageIcon(getClass().getResource("../Resources/images/gameInfo.png")).getImage();
    private Image judgementLine = new ImageIcon(getClass().getResource("../Resources/images/judgementLine.png")).getImage();
    private Image noteRouteLineImage = new ImageIcon(getClass().getResource("../Resources/images/noteRouteLine.png")).getImage();
    private Image noteRouteQImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRouteWImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRouteEImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRouteSpace1Image = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRouteSpace2Image = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRouteIImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRouteOImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image noteRoutePImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    private Image blueFlareImage;
    private Image judgeImage;

    public String titleName;
    private String difficulty;
    private String musicTitle;
    private Music gameMusic;

    private String RabbitHoleEasy = "Rabbit Hole Easy.txt";
    private String RabbitHoleHard = "Rabbit Hole Hard.txt";
    private String ボッカデラベリタEasy = "ボッカデラベリタ Easy.txt";
    private String ボッカデラベリタHard = "ボッカデラベリタ Hard.txt";

    private int combo = 0;
    private int multiple = 1;
    private int score = 0;

    ArrayList<Note> noteList = new ArrayList<Note>();

    public Game(String titleName, String difficulty, String musicTitle) {
        this.titleName = titleName;
        this.difficulty = difficulty;
        this.musicTitle = musicTitle;
        gameMusic = new Music(this.musicTitle, false);
    }

    public void screenDraw(Graphics2D g) {
        g.drawImage(noteRouteQImage, 228, 30, null);
        g.drawImage(noteRouteWImage, 332, 30, null);
        g.drawImage(noteRouteEImage, 436, 30, null);
        g.drawImage(noteRouteSpace1Image, 540, 30, null);
        g.drawImage(noteRouteSpace2Image, 640, 30, null);
        g.drawImage(noteRouteIImage, 744, 30, null);
        g.drawImage(noteRouteOImage, 848, 30, null);
        g.drawImage(noteRoutePImage, 952, 30, null);
        g.drawImage(noteRouteLineImage, 224, 30, null);
        g.drawImage(noteRouteLineImage, 328, 30, null);
        g.drawImage(noteRouteLineImage, 432, 30, null);
        g.drawImage(noteRouteLineImage, 536, 30, null);
        g.drawImage(noteRouteLineImage, 740, 30, null);
        g.drawImage(noteRouteLineImage, 844, 30, null);
        g.drawImage(noteRouteLineImage, 948, 30, null);
        g.drawImage(noteRouteLineImage, 1052, 30, null);
        g.drawImage(gameInfoImage, 0, 660, null);
        g.drawImage(judgementLine, 0, 580, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Elephant", Font.BOLD, 30));
        g.drawString("COMBO : " + combo, 540, 60);
        for (int i = 0 ; i < noteList.size() ; i++) {
            Note note = noteList.get(i);
            if (note.getY() > 620) {
                judgeImage = new ImageIcon(getClass().getResource("../Resources/images/judgeMiss.png")).getImage();
                resetCombo();
            }
            if (!note.isProceeded()) {
                noteList.remove(i);
                i--;
            } else {
                note.screenDraw(g);
            }
        }
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(titleName, 20, 702);
        g.drawString(difficulty, 1190, 702);
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        g.setColor(Color.DARK_GRAY);
        g.drawString("Q", 270, 609);
        g.drawString("W", 374, 609);
        g.drawString("E", 478, 609);
        g.drawString("Space Bar", 580, 609);
        g.drawString("I", 784, 609);
        g.drawString("O", 889, 609);
        g.drawString("P", 993, 609);
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Elephant", Font.BOLD, 30));
        g.drawString("SCORE : " + score + " X " + multiple, 530, 702);
        g.drawImage(blueFlareImage, 300, 300, null);
        g.drawImage(judgeImage, 460, 420, null);
    }

    public void pressQ() {
        judge("Q");
        noteRouteQImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseQ() {
        noteRouteQImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressW() {
        judge("W");
        noteRouteWImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseW() {
        noteRouteWImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressE() {
        judge("E");
        noteRouteEImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseE() {
        noteRouteEImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressSpace() {
        judge("Space");
        noteRouteSpace1Image = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
        noteRouteSpace2Image = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseSpace() {
        noteRouteSpace1Image = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
        noteRouteSpace2Image = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressI() {
        judge("I");
        noteRouteIImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseI() {
        noteRouteIImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressO() {
        judge("O");
        noteRouteOImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseO() {
        noteRouteOImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressP() {
        judge("P");
        noteRoutePImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseP() {
        noteRoutePImage = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }

    @Override
    public void run() {
        dropNotes(this.titleName);
    }

    public void close() {
        gameMusic.stop();
        this.interrupt();
    }

    public static Beat[] loadBeatsFromFile(String filePath, int startTime, int gap) {
        ArrayList<Beat> beatList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int beatGap = Integer.parseInt(parts[0]);
                String key = parts[1];
                beatList.add(new Beat(startTime + gap * beatGap, key));
            }
        } catch (IOException e) {
            System.out.println("파일을 읽어오는데 실패함 (왜?) : " + e.getMessage());
            return new Beat[0];
        }
        return beatList.toArray(new Beat[0]);
    }

    public void dropNotes(String titleName) {
        int startTime;
        int gap;
        Beat[] beats = new Beat[1];
        if (titleName.equals("DECO27 - Rabbit Hole") && difficulty.equals("Easy")) {
            startTime = 3800;
            gap = 50;
            beats = loadBeatsFromFile(RabbitHoleEasy, startTime, gap);
        } else if (titleName.equals("DECO27 - Rabbit Hole") && difficulty.equals("Hard")) {
            startTime = 3800;
            gap = 50;
            beats = loadBeatsFromFile(RabbitHoleHard, startTime, gap);
        } else if (titleName.equals("Ado - Bocca Della Verita") && difficulty.equals("Easy")) {
            startTime = 2717;
            gap = 100;
            beats = loadBeatsFromFile(ボッカデラベリタEasy, startTime, gap);
        } else if (titleName.equals("Ado - Bocca Della Verita") && difficulty.equals("Hard")) {
            startTime = 2717;
            gap = 100;
            beats = loadBeatsFromFile(ボッカデラベリタHard, startTime, gap);
        }
        int i = 0;
        gameMusic.start();
        while (i < beats.length && !isInterrupted()) {
            boolean dropped = false;
            if (beats[i].getTime() <= gameMusic.getTime()) {
                Note note = new Note(beats[i].getNoteName());
                note.start();
                noteList.add(note);
                i++;
                dropped = true;
            }
            if (!dropped) {
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resetCombo() {
        combo = 0;
        multiple = 1;
    }
    public void setCombo() {
        combo+=1;
        if (combo%10 == 0 && combo != 10) {
            multiple++;
        }
    }

    public void judge(String input) {
        for (int i = 0 ; i < noteList.size() ; i++) {
            Note note = noteList.get(i);
            if (input.equals(note.getNoteType())) {
                judgeEvent(note.judge());
                break;
            }
        }
    }

    public void judgeEvent(String judge) {
        if(!judge.equals("None")) {
            blueFlareImage = new ImageIcon(getClass().getResource("../Resources/images/blueFlare.png")).getImage();
            setCombo();
        }
        switch (judge) {
            case "Miss" -> {
                judgeImage = new ImageIcon(getClass().getResource("../Resources/images/judgeMiss.png")).getImage();
            }
            case "Good" -> {
                judgeImage = new ImageIcon(getClass().getResource("../Resources/images/judgeGood.png")).getImage();
                score += 200;
            }
            case "Great" -> {
                judgeImage = new ImageIcon(getClass().getResource("../Resources/images/judgeGreat.png")).getImage();
                score += 500;
            }
            case "Perfect" -> {
                judgeImage = new ImageIcon(getClass().getResource("../Resources/images/judgePerfect.png")).getImage();
                score += 1000;
            }
        }
    }
}