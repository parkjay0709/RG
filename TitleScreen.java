import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TitleScreen extends JFrame {

    public class Main {
        public static void main(String[] args) {
            DatabaseManager dbManager = new DatabaseManager();
            TitleScreen titleScreen = new TitleScreen(dbManager);
            titleScreen.setVisible(true);
        }
    }

    private DatabaseManager dbManager;

    // dbManager를 생성자에서 전달받는 방식
    public TitleScreen(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        setTitle("TempoX - Title Screen");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JButton startGameButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");
        JButton rankingButton = new JButton("랭킹 보기");

        // Ranking 버튼 동작 설정
        rankingButton.addActionListener(e -> {
            RankingScreen rankingScreen = new RankingScreen(dbManager);
            rankingScreen.setVisible(true);
        });

        startGameButton.setBounds(250, 300, 100, 30);
        exitButton.setBounds(250, 350, 100, 30);
        rankingButton.setBounds(250, 400, 100, 30);

        add(startGameButton);
        add(exitButton);
        add(rankingButton);

        // 버튼 동작 설정
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void startGame() {
        new RG(dbManager).setVisible(true); // dbManager를 전달하며 RG 화면으로 이동
        this.dispose(); // 타이틀 화면 닫기
    }
}