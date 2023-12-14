public class Buyer extends Player {
    public Buyer(String name) {
        super(name);
    }

    public boolean buyProperty(Property property) {
        int propertyPrice = property.getPrice();
        if (canAfford(propertyPrice)) {
            pay(propertyPrice);
            property.setOwner(this);
            System.out.println(getName() + " bought " + property.getName() + " for $" + propertyPrice);
            return true;
        } else {
            System.out.println(getName() + " cannot afford " + property.getName());
            return false;
        }
    }
}
