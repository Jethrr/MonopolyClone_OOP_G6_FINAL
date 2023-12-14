public class UtilityProperty extends Property {

    public UtilityProperty(String name, int price) {
        super(name, price);
    }

    public int calculateRent(int diceRoll, int numOwnedUtilities) {
        // Assuming rent is calculated based on the dice roll and the number of owned utilities
        // You can customize this logic based on the Monopoly rules for utility properties
        int rent;

        if (numOwnedUtilities == 1) {
            // If the player owns one utility, rent is 4 times the dice roll
            rent = 4 * diceRoll;
        } else if (numOwnedUtilities == 2) {
            // If the player owns two utilities, rent is 10 times the dice roll
            rent = 10 * diceRoll;
        } else {
            // Default case or additional rules
            rent = 0;
        }

        return rent;
    }

    public static class ElectricCompany extends UtilityProperty {
        public ElectricCompany() {
            super("Electric Company", 150);
        }

    }

    public static class WaterWorks extends UtilityProperty {
        public WaterWorks() {
            super("Water Works", 200);
        }

    }
}
