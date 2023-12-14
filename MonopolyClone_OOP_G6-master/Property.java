public abstract class Property {
    private String name;
    private  int price;
    private int rent;
    private Player owner;

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null; // Initially, no owner
    }

    public Property(String name, int price) {
        this.name = name;
        this.price = price;
        this.owner = null; // Initially, no owner
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;

    }

    // Add more methods as needed, such as methods for buying, selling, etc.
    public boolean isOwnedBy(Player player) {
        return owner != null && owner.equals(player);
    }

    public boolean isOwned() {
        return owner != null;
    }

    public boolean buyProperty(Player buyer) {
        if (!isOwnedBy(buyer) && buyer.canAfford(price)) {
            buyer.pay(price);
            setOwner(buyer);
            return true;
        }
        return false;
    }

    public void collectRent(Player tenant) {
        if (isOwnedBy(tenant) && !owner.equals(tenant)) {
            tenant.pay(rent);
            owner.receiveRent(rent);
        }
    }
}
