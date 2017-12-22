
public class Move {

    private final int start;
    private int finish;
    private int weight;
    private int amount;
    private final PlayerColor playerColor;

    public Move(PlayerColor color, int start, int finish) {
        this.playerColor = color;
        this.start = start;
        this.finish = finish;
    }

    public Move(int start, int amount, PlayerColor playerColor) {
        this.start = start;
        this.amount = amount;
        this.playerColor = playerColor;
        if (playerColor == PlayerColor.WHITE) {
            if (start - amount <= 0) {
                this.finish = 0;
            } else {
                this.finish = start - amount;
            }
        }
        if (playerColor == PlayerColor.BLACK) {
            if (start + amount >= 25) {
                this.finish = 25;
            } else {
                this.finish = start + amount;
            }
        }
    }

    public boolean isBearoff() {
        return (((this.finish == 0) && (playerColor == PlayerColor.WHITE)) ||
                ((this.finish == 25) && (playerColor == PlayerColor.BLACK)));
    }

    public int getAmount() {
        return this.amount;
    }

    public int getStart() {
        return start;
    }

    public int getFinish() {
        return finish;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public PlayerColor getPlayerColor() {
        return this.playerColor;
    }
}