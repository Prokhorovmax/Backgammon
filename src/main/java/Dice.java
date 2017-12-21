import java.util.Random;

public class Dice {

    private int a;
    private int b;
    private Random random = new Random();

    public Dice() {
        this.a = random.nextInt(6) + 1;
        this.b = random.nextInt(6) + 1;
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

    public void throwDices() {
        this.a = random.nextInt(6) + 1;
        this.b = random.nextInt(6) + 1;
    }
}
