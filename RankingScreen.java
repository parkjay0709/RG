import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankingScreen extends JFrame {
    private DatabaseManager dbManager;

    public RankingScreen(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        setTitle("게임 랭킹");
        setSize(400, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("게임 랭킹", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 랭킹 리스트 표시
        JTextArea rankingArea = new JTextArea();
        rankingArea.setEditable(false);
        List<String> rankings = dbManager.getTopRankings();
        for (String entry : rankings) {
            rankingArea.append(entry + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(rankingArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> dispose());
        add(backButton, BorderLayout.SOUTH);
    }
}
