import java.util.Random;

public class Dice {
    private Random random;

    public Dice () {
        random = new Random();
    }

    public int roll() {
        return random.nextInt(6) + 1;
    }

    public int rollTwoDice() {
        int dice1 = roll();
        int dice2 = roll();
        return dice1 + dice2;
    }
}
