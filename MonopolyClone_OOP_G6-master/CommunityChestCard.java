import java.util.Random;

public class CommunityChestCard extends Card {
    public CommunityChestCard() {

        super("Community Chest Card");
    }

    public String executeAction(Player player) {

        Random random = new Random();
        int actionType = random.nextInt(2);

        return switch (actionType) {
            case 0 -> collectMoney(player);
            case 1 -> payFine(player);
            // Add more cases for other actions
            default -> player.getName() + " got nothing from Community Chest Card";
        };

    }

    private String collectMoney(Player player) {
        int amount = generateRandomAmount();
        player.receiveMoney(amount);
        return player.getName() + " collected $" + amount + " from Community Chest.";
    }

    private String payFine(Player player) {
        int amount = generateRandomAmount();
        player.pay(amount);
        return player.getName() + " paid a fine of $" + amount + " to Community Chest.";
    }

    private int generateRandomAmount() {
        Random random = new Random();
        return random.nextInt(151) + 50;
    }
}
