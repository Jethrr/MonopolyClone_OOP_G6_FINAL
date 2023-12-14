import java.util.Random;

public class ChanceCard extends Card {
    public ChanceCard() {
        super("Chance Card");
    }
    public String executeAction(Player player) {

        Random random = new Random();
        int actionType = random.nextInt(10);

        switch (actionType) {
            case 0 -> {
                return collectMoney(player);
            }
            case 1 -> {
                return movePlayer(player);
            }
            case 2 -> {
                return payRent(player);
            }
            case 3 -> {
                return goToJail(player);
            }
            case 4 -> {
                return getOutOfJailFreeCard(player);
            }
            default -> {
                return player.getName() + " got nothing from Chance.";
                // Add more cases for other actions
            }

        }
    }

    private String collectMoney(Player player) {
        int amount = generateRandomAmount();
        player.receiveMoney(amount);
        return player.getName() + " collected $" + amount + " from Chance.";
    }

    private String movePlayer(Player player) {
        int steps = generateRandomSteps();
        player.move(steps);
        return player.getName() + " moved " + steps + " steps from Chance.";
    }

    private String payRent(Player player){
        int amount = generateRandomAmount();
        player.payRent(amount);
        return player.getName() + " paid $" + amount + " in rent from Chance.";
    }

    private String goToJail(Player player){
        player.setInJail(player.isInJail());
        player.sendToJail(player);
        return player.getName() + " goes to jail from Chance";
    }
    private String getOutOfJailFreeCard(Player player){
        player.receiveGetOutOfJailFreeCard();
        return player.getName() + " received a 'Get Out of Jail Free' card from Chance.";
    }

    private int generateRandomAmount() {
        Random random = new Random();
        return random.nextInt(151) + 50;
    }

    private int generateRandomSteps() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }


}