public class Tenant extends Player {
    public Tenant(String name) {
        super(name);
    }

    public String payRent(Property property) {
        int rentAmount = property.getRent();
        Player owner = property.getOwner();

        if (owner != null) {
            if (canAfford(rentAmount)) {
                pay(rentAmount);
                owner.receiveRent(rentAmount);
                return getName() + " paid rent of $" + rentAmount + " to " + owner.getName();
            } else {
                return getName() + " cannot afford the rent for " + property.getName();
                // Implement additional logic here, e.g., bankrupt the player
            }
        } else {
            return property.getName() + " is not owned. No rent is paid.";
        }
    }


}
