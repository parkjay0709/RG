import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.File;
import java.util.List;
import javax.swing.Timer;

public class RG extends JFrame implements ActionListener {

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager(); // DatabaseManager 초기화
        RG game = new RG(dbManager); // DatabaseManager를 전달하며 RG 생성
        game.setVisible(true); // 게임 화면 표시
    }

    private JButton startButton, signupButton, stopButton; // 회원가입 버튼 추가
    private JLabel statusLabel;
    private Timer gameTimer;
    private List<Tile> tiles;
    private Random random;
    private boolean isPlaying;
    private boolean isPaused; // 게임이 일시 정지 상태인지 확인하는 플래그
    private Clip clip;
    private Map<String, Integer> keyMappings;
    private DatabaseManager dbManager; // DatabaseManager 객체
    private JTextField usernameField;  // usernameField 정의
    private JPasswordField passwordField; // passwordField 정의
    private String currentPlayer;

    private final int TILE_SPEED = 5;
    private final int LINE_Y = 600;
    private final int TILE_HEIGHT = 30;
    private final int MAX_COMBO = 10; // 콤보 최대값
    private int TempCombo = 0;
    private int combo;
    private int multiplier;
    private long startTime;
    private int score;
    private JProgressBar comboGauge; // 콤보 게이지

    public RG(DatabaseManager dbManager) {
        setTitle("TempoX");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        this.dbManager = dbManager;
        tiles = new ArrayList<>();
        startButton = new JButton("Start Game");
        stopButton = new JButton("Stop Game"); // Stop 버튼
        signupButton = new JButton("Sign Up"); // 회원가입 버튼
        statusLabel = new JLabel("로그인 후 게임을 시작하세요!", SwingConstants.CENTER);

        startButton.setBounds(250, 680, 100, 30);
        stopButton.setBounds(250, 720, 100, 30); // Stop 버튼 위치
        signupButton.setBounds(250, 640, 100, 30); // 회원가입 버튼 위치
        statusLabel.setBounds(100, 10, 400, 30);

        add(startButton);
        add(stopButton);
        add(statusLabel);
        add(signupButton); // 회원가입 버튼 추가

        startButton.addActionListener(e -> startGame());
        signupButton.addActionListener(e -> showSignupDialog()); // 회원가입 버튼 클릭 이벤트
        stopButton.addActionListener(e -> stopGame());

        random = new Random();
        score = 0;
        tiles = new ArrayList<>();
        keyMappings = new HashMap<>();
        setupKeyMapping(); // 키 매핑을 설정합니다.
        Tile.setKeyMappings(keyMappings); // Tile 클래스의 keyMappings를 초기화합니다.

        setupKeyMapping();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (isPlaying && !isPaused) {
                        pauseGame(); // ESC를 눌러 게임 일시 정지
                    } else if (isPaused) {
                        resumeGameWithCountdown(); // ESC를 다시 눌러 3초 카운트다운 후 재개
                    }
                } else {
                    handleKeyPress(e); // 다른 키 입력은 기존대로 처리
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    // 메뉴는 게임이 멈춘 상태에서도 열 수 있도록
                    int option = JOptionPane.showOptionDialog(
                            RG.this,
                            "메뉴 옵션을 선택하세요:",
                            "메뉴",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{"랭킹 보기", "게임 나가기", "로그아웃", "타이틀 화면으로"},
                            "타이틀 화면으로"
                    );

                    switch (option) {
                        case 0 -> displayRankings();
                        case 1 -> System.exit(0);
                        case 2 -> logout(); // 로그아웃 구현 메서드 호출
                        case 3 -> showTitleScreen();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });

        setFocusable(true);
        isPlaying = false;
        isPaused = false;

        showLoginDialog(); // 로그인 다이얼로그 호출
        initializeComboGauge(); // 콤보 게이지 초기화
    }

    private void pauseGame() {
        isPaused = true;
        gameTimer.stop(); // 타이머를 멈춤
        stopMusic(); // 음악도 멈춤
        statusLabel.setText("Game Paused. Press ESC to resume."); // 일시 정지 상태 안내
    }

    private void resumeGameWithCountdown() {
        new Thread(() -> { // 별도의 스레드에서 3초 카운트다운
            try {
                for (int i = 3; i > 0; i--) {
                    statusLabel.setText("Resuming in " + i + "...");
                    Thread.sleep(1000); // 1초 대기
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resumeGame(); // 3초 후 게임 재개
        }).start();
    }

    private void resumeGame() {
        isPaused = false;
        if (gameTimer != null) {
            gameTimer.start(); // 게임 타이머 재개
        }
        if (clip != null) {
            clip.start(); // 음악 재생 재개
        }
        statusLabel.setText("Game Resumed! Continue playing.");
    }

    private void showTitleScreen() {
        new TitleScreen(dbManager).setVisible(true); // 타이틀 화면으로 이동
        this.dispose(); // 현재 게임 화면 닫기
    }
    private void logout() {
        currentPlayer = null;
        statusLabel.setText("로그아웃되었습니다. 다시 로그인하세요.");
        showLoginDialog(); // 로그인 창 다시 표시
    }

    private void showLoginDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (dbManager.login(username, password)) {
                currentPlayer = username; // 로그인한 사용자 설정
                statusLabel.setText("게임을 시작하세요, " + currentPlayer + "!");
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패. 아이디와 비밀번호를 확인하세요.");
                showLoginDialog(); // 실패 시 재시도
            }
        }
    }

    // 회원가입 다이얼로그
    private void showSignupDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Sign Up", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            dbManager.addUser(username, password);
            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다. 로그인 해주세요.");
        }
    }

    // 랭킹 표시 메서드
    private void displayRankings() {
        List<String> rankings = dbManager.getTopRankings();
        StringBuilder rankingDisplay = new StringBuilder("랭킹:\n");
        for (String entry : rankings) {
            rankingDisplay.append(entry).append("\n");
        }
        JOptionPane.showMessageDialog(this, rankingDisplay.toString());
    }

    private void initializeComboGauge() {
        comboGauge = new JProgressBar();
        comboGauge.setMaximum(MAX_COMBO); // 최대 콤보 값 설정
        comboGauge.setValue(0); // 초기 값 0으로 설정
        comboGauge.setStringPainted(true); // 텍스트 표시
        comboGauge.setBounds(50, 50, 500, 30); // 위치 및 크기 설정
        add(comboGauge); // 프레임에 추가
    }

    private void updateComboGauge() {
        comboGauge.setValue(TempCombo); // 콤보 값으로 게이지 업데이트

        if (TempCombo >= MAX_COMBO) { // 콤보 게이지가 100%에 도달했는지 확인
            resetTempCombo();
            increaseMultiplier(); // 배수 증가 메서드 호출
            comboGauge.setValue(0); // 콤보 게이지 초기화
        }
    }

    private void setupKeyMapping() {
        keyMappings.put("A", KeyEvent.VK_A);
        keyMappings.put("S", KeyEvent.VK_S);
        keyMappings.put("D", KeyEvent.VK_D);
        keyMappings.put("F", KeyEvent.VK_F);
    }

    private void handleKeyPress(KeyEvent e) {
        if (isPlaying) {
            int keyCode = e.getKeyCode();
            boolean isScored = false;
            Tile toRemove = null;

            for (Tile tile : tiles) {
                if (tile.getKeyCode() == keyCode && tile.isInPlay()) {
                    int tileBottom = tile.getY() + TILE_HEIGHT;

                    // 롱노트인 경우
                    if (tile instanceof LongTile) {
                        LongTile longTile = (LongTile) tile;
                        // 롱노트의 Y 범위가 LINE_Y 근처에서 시작될 때만 startHold() 호출
                        if (!longTile.isHolding() && longTile.getEndY() >= LINE_Y && longTile.getY() <= LINE_Y + TILE_HEIGHT) {
                            longTile.startHold();
                            score += 10;  // 기본 점수
                            combo++;
                            TempCombo++;
                            statusLabel.setText("Holding! Score: " + score + " Combo: " + combo + "x" + multiplier);
                            isScored = true;
                        }
                    }
                    // 일반 타일인 경우
                    else {
                        if (tileBottom >= LINE_Y && tileBottom <= LINE_Y + TILE_HEIGHT) {
                            score += 10 * multiplier;  // 완벽한 타이밍
                            combo++;
                            TempCombo++;
                            statusLabel.setText("Perfect! Score: " + score + " Combo: " + combo + "x" + multiplier);
                            isScored = true;
                            toRemove = tile;
                            break;
                        } else if (tileBottom >= LINE_Y - TILE_HEIGHT && tileBottom <= LINE_Y + TILE_HEIGHT * 2) {
                            score += 1 * multiplier;  // 약간 늦거나 빠른 타이밍
                            combo++;
                            TempCombo++;
                            statusLabel.setText("Good! Score: " + score + " Combo: " + combo + "x" + multiplier);
                            isScored = true;
                            toRemove = tile;
                            break;
                        }
                    }
                }
            }

            // 점수를 얻지 못한 경우 콤보 초기화
            if (!isScored) {
                statusLabel.setText("Miss! Score: " + score);
                resetCombo();
            } else {
                updateComboGauge();  // 콤보 게이지 업데이트
            }

            // 타일을 제거
            if (toRemove != null) {
                toRemove.setInPlay(false);
                tiles.remove(toRemove);
            }
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        int keyCode = e.getKeyCode();
        updateComboGauge();  // 콤보 게이지 업데이트

        for (Tile tile : tiles) {
            if (tile instanceof LongTile && tile.getKeyCode() == keyCode && ((LongTile) tile).isHolding()) {
                LongTile longTile = (LongTile) tile;
                longTile.stopHold();  // 롱노트를 놓았을 때
                score += 10 * multiplier;  // 롱노트를 놓았을 때 추가 점수
                combo++;
                TempCombo++;
                statusLabel.setText("Released! Score: " + score + " Combo: " + combo + "x" + multiplier);
                break;  // 한번의 키 해제에 대해서만 처리
            }
        }
    }

    private void resetCombo() {
        combo = 0;
        multiplier = 1; // 배수 초기화
    }
    private void resetTempCombo() {
        TempCombo = 0;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (!isPlaying) {
                startGame();
            } else {
                stopGame();
            }
        }
    }

    private void startGame() {
        comboGauge.setValue(0); // 콤보 게이지 초기화

        statusLabel.setText("A, S, D, F 키를 눌러 내려오는 타일을 맞추세요");
        isPlaying = true;
        score = 0;
        combo = 0;
        multiplier = 1;
        tiles.clear();
        startTime = System.currentTimeMillis();

        gameTimer = new Timer(30, e -> updateGame());
        gameTimer.start();

        playMusic("path_to_music_file.wav"); // 실제 음악 파일 경로로 변경
        setFocusable(true);
        requestFocusInWindow();
    }

    private void increaseMultiplier() {
        multiplier++; // 배수 증가
        statusLabel.setText("Multiplier Increased! Now: " + multiplier + "x"); // 상태 메시지 업데이트
    }

    private void stopGame() {
        if (isPlaying) {
            startButton.setText("Start Game");
            statusLabel.setText("Game Over! Final Score: " + score);
            gameTimer.stop(); // 타이머를 멈춤
            stopMusic(); // 음악을 멈춤
            isPlaying = false; // isPlaying 플래그를 false로 설정
            dbManager.addScore(currentPlayer, score); // 점수 저장
        }
    }



    private void updateGame() {
        if (isPlaying) {
            if (random.nextInt(100) < 4) {
                int xPosition = random.nextInt(4) * 150 + 50;
                int yPosition = LINE_Y - random.nextInt(200);  // Y 위치를 랜덤으로 설정

                if (random.nextBoolean()) {
                    addTile(new LongTile(xPosition, TILE_SPEED, 200, 0, LINE_Y));  // 롱노트 추가
                } else {
                    addTile(new Tile(xPosition, TILE_SPEED, 0));  // 일반 타일 추가
                }
            }

            for (Tile tile : tiles) {
                tile.update();
                // 롱노트가 끝에 도달했는지 확인
                if (tile instanceof LongTile) {
                    LongTile longTile = (LongTile) tile;
                    if (longTile.isHolding() && longTile.getEndY() >= LINE_Y) {
                        score += 10 * multiplier;  // 롱노트가 끝에 도달했을 때 점수 추가
                        combo++;  // 콤보 증가
                        TempCombo++;
                        statusLabel.setText("Long Note Perfect! Score: " + score + " Combo: " + combo + "x" + multiplier);
                        longTile.setInPlay(false);  // 롱노트 제거
                    }
                }
            }

            tiles.removeIf(tile -> tile.isOffScreen());  // 화면 밖으로 나간 타일 제거
            tiles.removeIf(tile -> !tile.isInPlay());  // 플레이 중이지 않은 타일 제거

            repaint();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (isPlaying) {
            g.setColor(Color.BLACK);
            g.fillRect(0, LINE_Y, 600, TILE_HEIGHT);
            for (Tile tile : tiles) {
                tile.draw(g);
            }
        }
    }
    private boolean isOverlapping(Tile newTile) {
        for (Tile existingTile : tiles) {
            if (existingTile.getX() == newTile.getX() && existingTile.isInPlay()) {
                return true; // 겹치는 경우
            }
        }
        return false; // 겹치지 않는 경우
    }

    private void addTile(Tile newTile) {
        if (!isOverlapping(newTile)) {
            tiles.add(newTile); // 겹치지 않으면 타일 추가
        }
    }

    private void playMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}