package main.java;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

public class Game extends Thread {

    private Image gameInfoImage = new ImageIcon( getClass().getResource("../Resources/images/gameInfo.png" ) ).getImage();
    private Image judgementLine = new ImageIcon( getClass().getResource("../Resources/images/judgementLine.png " ) ).getImage();
    private Image noteRouteLineImage = new ImageIcon( getClass().getResource("../Resources/images/noteRouteLine.png" ) ).getImage();
    private Image[] noteRouteImage = new Image[ 8 ];
    private Image blueFlareImage;
    private Image judgeImage;

    private Music gameMusic;

    public String titleName;
    private String difficulty;
    private String musicTitle;
    private String[] KeyCode = new String[] { "Q", "W", "E", "Space Bar", "7", "8", "9" };
    private String RabbitHoleEasy = "Rabbit Hole Easy.txt";
    private String RabbitHoleHard = "Rabbit Hole Hard.txt";
    private String BoccaDellaVeritaEasy = "Bocca Della Verita Easy.txt";
    private String BoccaDellaVeritaHard = "Bocca Della Verita Hard.txt";
    private String KakeraEasy = "Kakera Easy.txt";
    private String KakeraHard = "Kakera Hard.txt";

    private int combo = 0;
    private int multiple = 1;
    private int score = 0;

    private int[] NRI_X = new int[] { 228, 332, 436, 540, 640, 744, 848, 952 };
    private int[] NRLI_X = new int[] { 224, 328, 432, 536, 740, 844, 948, 1052 };
    private int[] KC_X = new int[] { 270, 374, 478, 580, 784, 889, 993 };

    ArrayList<Note> noteList = new ArrayList<Note>();

    public Game( String titleName, String difficulty, String musicTitle ) {
        noteRouteImageSetting();
        this.titleName = titleName;
        this.difficulty = difficulty;
        this.musicTitle = musicTitle;
        gameMusic = new Music( this.musicTitle, false );
    }

    public void close() {
        gameMusic.stop();
        this.interrupt();
    }

    public void dropNotes( String titleName ) {
        int startTime;
        int gap;
        Beat[] beats = new Beat[ 1 ];
        if ( titleName.equals( "DECO27 - Rabbit Hole" ) && difficulty.equals( "Easy" ) ) {
            startTime = 3800;
            gap = 50;
            beats = loadBeatsFromFile( RabbitHoleEasy, startTime, gap );
        } else if ( titleName.equals( "DECO*27 - Rabbit Hole" ) && difficulty.equals( "Hard" ) ) {
            startTime = 3800;
            gap = 50;
            beats = loadBeatsFromFile( RabbitHoleHard, startTime, gap );
        } else if (titleName.equals("Ado - Bocca Della Verita") && difficulty.equals("Easy")) {
            startTime = 2717;
            gap = 100;
            beats = loadBeatsFromFile(BoccaDellaVeritaEasy, startTime, gap);
        } else if (titleName.equals("Ado - Bocca Della Verita") && difficulty.equals("Hard")) {
            startTime = 2717;
            gap = 50;
            beats = loadBeatsFromFile(BoccaDellaVeritaHard, startTime, gap);
        } else if (titleName.equals( "CLTH - Kakera" ) && difficulty.equals("Easy")) {
            startTime = 430;
            gap = 50;
            beats = loadBeatsFromFile(KakeraEasy, startTime, gap);
        } else if (titleName.equals("CLTH - Kakera") && difficulty.equals("Hard")) {
            startTime = 430;
            gap = 50;
            beats = loadBeatsFromFile(KakeraHard, startTime, gap);
        }
        int i = 0;
        gameMusic.start();
        while ( i < beats.length && !isInterrupted() ) {
            boolean dropped = false;
            if ( beats[ i ].getTime() <= gameMusic.getTime() ) {
                Note note = new Note( beats[ i ].getNoteName() );
                note.start();
                noteList.add( note );
                i++;
                dropped = true;
            }
            if ( !dropped ) {
                try {
                    Thread.sleep(5 );
                } catch ( InterruptedException e ) {
                    System.out.println( "노트가 모두 내려오지 않았습니다." );
                }
            }
        }
    }

    public void judge( String input ) {
        for ( int i = 0 ; i < noteList.size() ; i++ ) {
            Note note = noteList.get( i );
            if ( input.equals( note.getNoteType() ) ) {
                judgeEvent( note.judge() );
                break;
            }
        }
    }

    public void judgeEvent( String judge ) {
        if( !judge.equals( "None" ) ) {
            blueFlareImage = new ImageIcon( getClass().getResource("../Resources/images/blueFlare.png" ) ).getImage();
            setCombo();
        }
        switch ( judge ) {
            case "Miss" -> {
                judgeImage = new ImageIcon( getClass().getResource("../Resources/images/judgeMiss.png" ) ).getImage();
            }
            case "Good" -> {
                judgeImage = new ImageIcon( getClass().getResource("../Resources/images/judgeGood.png" ) ).getImage();
                score += 200;
            }
            case "Great" -> {
                judgeImage = new ImageIcon( getClass().getResource("../Resources/images/judgeGreat.png" ) ).getImage();
                score += 500;
            }
            case "Perfect" -> {
                judgeImage = new ImageIcon( getClass().getResource("../Resources/images/judgePerfect.png" ) ).getImage();
                score += 1000;
            }
        }
    }

    public void screenDraw( Graphics2D g ) {
        for ( int i = 0; i < NRI_X.length; i++ ) {
            g.drawImage(noteRouteImage[i], NRI_X[i], 30, null);
        }
        for ( int i = 0; i < NRLI_X.length; i++ ) {
            g.drawImage(noteRouteLineImage, NRLI_X[i], 30, null);
        }
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
        for ( int i = 0 ; i < 7 ; i++ ) {
            g.drawString(KeyCode[i], KC_X[i], 609);
        }
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Elephant", Font.BOLD, 30));
        g.drawString("SCORE : " + score + " X " + multiple, 530, 702);
        g.drawImage(blueFlareImage, 300, 300, null);
        g.drawImage(judgeImage, 460, 420, null);
    }

    private void noteRouteImageSetting() {
        for ( int i = 0; i < noteRouteImage.length; i++ ) {
            noteRouteImage[i] = new ImageIcon( getClass().getResource("../Resources/images/noteRoute.png" ) ).getImage();
        }
    }

    public void pressQ() {
        judge("Q");
        noteRouteImage[0] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseQ() {
        noteRouteImage[0] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressW() {
        judge("W");
        noteRouteImage[1] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseW() {
        noteRouteImage[1] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressE() {
        judge("E");
        noteRouteImage[2] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseE() {
        noteRouteImage[2] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void pressSpace() {
        judge("Space");
        noteRouteImage[3] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
        noteRouteImage[4] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void releaseSpace() {
        noteRouteImage[3] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
        noteRouteImage[4] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void press7() {
        judge("7");
        noteRouteImage[5] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void release7() {
        noteRouteImage[5] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void press8() {
        judge("8");
        noteRouteImage[6] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void release8() {
        noteRouteImage[6] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }
    public void press9() {
        judge("9");
        noteRouteImage[7] = new ImageIcon(getClass().getResource("../Resources/images/noteRoutePressed.png")).getImage();
    }
    public void release9() {
        noteRouteImage[7] = new ImageIcon(getClass().getResource("../Resources/images/noteRoute.png")).getImage();
    }

    @Override
    public void run() {
        dropNotes(this.titleName);
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

    public void resetCombo() {
        combo = 0;
        multiple = 1;
    }
    public void setCombo() {
        combo+=1;
        if ( combo%10 == 0 && combo != 10 ) {
            multiple++;
        }
    }
}