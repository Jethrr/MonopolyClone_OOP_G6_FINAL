import java.util.ArrayList;

public class Player {
    private String name;
    private int position;
    private int money;
    private boolean inJail;
    private ArrayList<Property> ownedProperties;

    public ArrayList<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public void setOwnedProperties(ArrayList<Property> ownedProperties) {
        this.ownedProperties = ownedProperties;
    }

    private boolean hasGetOutOfJailFreeCard;
    public Player(String name) {
        this.name = name;
        this.position = 1; // Starting position on the board
        this.money = 1500; // Starting money
        this.inJail = false;
        this.hasGetOutOfJailFreeCard = false;
        this.ownedProperties = new ArrayList<>();
    }

    public void addProperty(Property property) {
        ownedProperties.add(property);
    }

    // Remove a property from the player's list of owned properties
    public void removeProperty(Property property) {
        ownedProperties.remove(property);
    }

    public String[] getPropertiesOwned() {
        String[] propertyNames = new String[ownedProperties.size()];
        for (int i = 0; i < ownedProperties.size(); i++) {
            propertyNames[i] = ownedProperties.get(i).getName();
        }
        return propertyNames;
    }
    public String move(int steps) {
        position += steps;
        return name + " moved to position " + position;
    }


    public boolean canAfford(int cost) {
        return money >= cost;
    }

    public void pay(int amount) {
        if (canAfford(amount)) {
            money -= amount;}

    }

    public void receiveRent(int amount) {
        money += amount;
    }

    // Add getters and setters as needed

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void receiveMoney(int amount) {
        money += amount;

    }
    public boolean isInJail() {
        return inJail;
    }
    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }
    public String addToMoney(int amount) {
        money += amount;
        return name + " received $" + amount + ". Current balance: $" + money;
    }

    public void payRent(int amount) {
        this.money -= amount;
    }

    public String sendToJail(Player player) {
        player.setPosition(11);
        player.setInJail(true);
        return player.getName() + " is in jail!";
    }

    public void receiveGetOutOfJailFreeCard() {
        this.hasGetOutOfJailFreeCard = true;
    }

    public boolean hasGetOutOfJailFreeCard() {
        return this.hasGetOutOfJailFreeCard;
    }

}

