import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:rankings.db";

    public DatabaseManager() {
        createTable();
        createUsersTable(); // Users 테이블 생성
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Rankings 테이블 생성
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Rankings ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " playerName TEXT NOT NULL,"
                + " score INTEGER NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Users 테이블 생성
    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 새로운 사용자 추가
    public void addUser(String username, String password) {
        String sql = "INSERT INTO Users(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 패스워드를 해싱하지 않고 그대로 저장
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 로그인 확인
    public boolean login(String username, String password) {
        String sql = "SELECT password FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return password.equals(storedPassword); // 패스워드 확인
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 점수 추가
    public void addScore(String playerName, int score) {
        String sql = "INSERT INTO Rankings(playerName, score) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 상위 랭킹 10명 조회
    public List<String> getTopRankings() {
        List<String> rankings = new ArrayList<>();
        String sql = "SELECT playerName, score FROM Rankings ORDER BY score DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String rankingEntry = rs.getString("playerName") + ": " + rs.getInt("score");
                rankings.add(rankingEntry); // 랭킹 리스트에 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankings;
    }

    // 랭킹을 출력하는 메서드
    public void displayRankings() {
        List<String> rankings = getTopRankings();
        if (rankings.isEmpty()) {
            System.out.println("No rankings available.");
        } else {
            System.out.println("Top 10 Rankings:");
            for (int i = 0; i < rankings.size(); i++) {
                System.out.println((i + 1) + ". " + rankings.get(i)); // 순위에 맞게 출력
            }
        }
    }
}
