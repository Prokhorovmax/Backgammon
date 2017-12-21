
public class Cell {

    private PlayerColor color;
    private int number;

    public Cell() {
        this.color = null;
        this.number = 0;
    }

    public boolean isEmpty() {
        return (this.getNumber() == 0);
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public void checkerGone() {
        this.setNumber(this.getNumber() - 1);
        if (this.getNumber() == 0) setColor(null);
    }

    public void checkerCame(PlayerColor color) {
        this.setColor(color);
        this.setNumber(this.getNumber() + 1);
    }
}