import java.awt.*;

public class LongTile extends Tile {
    private int length;
    private boolean isHolding;
    private int lineY;
    private final int holdSpeed = 2;  // 길이를 감소시키는 속도

    public LongTile(int x, int speed, int length, int y, int lineY) {
        super(x, speed, y);
        this.length = length;
        this.lineY = lineY;
        this.isHolding = false;
    }

    public void update() {
        super.update(); // 기본 업데이트 호출

        // 롱노트를 누르고 있을 때의 사라지는 효과
        if (isHolding) {
            if (y + length > lineY) {
                length -= 1; // 롱노트 길이를 감소시켜 사라지는 효과
                if (length < 0) {
                    length = 0; // 길이가 0 이하로 내려가지 않도록
                    setInPlay(false); // 롱노트를 제거
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (isInPlay()) {
            g.setColor(Color.BLUE);  // 롱노트는 파란색으로 설정
            g.fillRect(x, y, TILE_WIDTH, length);  // 길이에 맞게 그리기
            g.setColor(Color.WHITE);
            g.drawString("" + (char) getKeyCode(), x + 20, y + 20);
        }
    }

    public int getEndY() {
        return y + length; // 롱노트의 끝 Y 좌표
    }

    public void startHold() {
        isHolding = true; // 키가 눌린 상태
    }

    public void stopHold() {
        isHolding = false; // 키를 놓은 상태
        setInPlay(false);  // 롱노트를 놓으면 타일을 더 이상 플레이 중이지 않게 설정
    }

    public boolean isHolding() {
        return isHolding;
    }
}
