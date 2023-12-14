public class Bank {
    private int money;

    public Bank(int initialMoney) {
        this.money = initialMoney;
    }

    public void transferMoney(Player sender, Player receiver, int amount) {
        if (sender.canAfford(amount)) {
            sender.pay(amount);
            receiver.receiveMoney(amount);
            System.out.println(sender.getName() + " paid $" + amount + " to " + receiver.getName());
        } else {
            System.out.println(sender.getName() + " does not have enough money to make the transfer.");
            bankruptPlayer(sender);
            // Implement additional logic, e.g., bankrupt the player
        }
    }

    private void bankruptPlayer(Player player) {
        System.out.println(player.getName() + " is bankrupt!");
    }
}