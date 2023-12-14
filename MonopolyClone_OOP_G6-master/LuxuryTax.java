public class LuxuryTax {
    private String name;
    private int taxAmount;

    public LuxuryTax(String name, int taxAmount) {
        this.name = name;
        this.taxAmount = taxAmount;
    }

    public String collectLuxuryTax(Player player) {
        player.pay(taxAmount);
        return player.getName() + " paid $" + taxAmount + " for luxury tax at " + name;
    }
}