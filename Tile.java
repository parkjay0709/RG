import java.awt.*;
import java.util.Map;

public class Tile {
    protected int x, y, speed;
    protected boolean inPlay = true;
    private final int keyCode;
    protected static Map<String, Integer> keyMappings;

    public static final int TILE_WIDTH = 50;
    private static final int TILE_HEIGHT = 30;
    private static final int SCREEN_HEIGHT = 800;  // 화면 높이 상수화

    public Tile(int x, int speed, int startY) {  // startY를 인자로 받아 타일의 시작 위치를 설정할 수 있게 수정
        this.x = x;
        this.y = startY;  // 시작 위치 설정
        this.speed = speed;
        this.keyCode = keyMappings.get(getKeyFromPosition(x));
    }

    public void update() {
        if (inPlay) {
            y += speed;
        }
    }

    public void draw(Graphics g) {
        if (isInPlay()) {
            g.setColor(Color.RED);
            g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);  // 상수화된 TILE_WIDTH, TILE_HEIGHT 사용
            g.setColor(Color.WHITE);
            String key = getKeyFromPosition(x);  // 키 문자열을 얻어 타일에 그리기
            if (!key.isEmpty()) {
                g.drawString(key, x + 20, y + 20);  // 키 값 그리기
            }
        }
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    public boolean isOffScreen() {
        return y > SCREEN_HEIGHT;  // 상수화된 SCREEN_HEIGHT 사용
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getKeyCode() {
        return keyCode;
    }

    protected String getKeyFromPosition(int x) {
        switch (x) {
            case 50: return "A";
            case 200: return "S";
            case 350: return "D";
            case 500: return "F";
            default: return "";
        }
    }

    public static void setKeyMappings(Map<String, Integer> mappings) {
        keyMappings = mappings;
    }
}