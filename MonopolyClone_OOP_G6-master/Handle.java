import java.util.Scanner;

public interface Handle {
    Scanner scanner = new Scanner(System.in);

    static String handleStreetProperty(Player currentPlayer, StreetProperty streetProperty) {
        Player owner = streetProperty.getOwner();

        if (!streetProperty.isOwnedBy(currentPlayer)) {

            // The street property is not owned by the current player
            return "Do you want to buy " + streetProperty.getName() + " for $" + streetProperty.getPrice() + "?";

        } else {
            // The street property is owned by the current player
            if (owner != null && owner != currentPlayer) {
                currentPlayer.pay(streetProperty.getRent());
                owner.receiveRent(streetProperty.getRent());
                return currentPlayer.getName() + " paid rent of $" + streetProperty.getRent() + " to " + owner.getName();
            } else {
                return "You already own " + streetProperty.getName();
            }
        }
    }

    static String handleUtilityProperty(Player currentPlayer, UtilityProperty utilityProperty) {
        Player owner = utilityProperty.getOwner();

        if (!utilityProperty.isOwnedBy(currentPlayer)) {
            // The utility property is not owned by the current player
            return "Do you want to buy " + utilityProperty.getName() + " for $" + utilityProperty.getPrice() + "?";
        } else {
            // The utility property is owned by the current player
            if (owner != null && owner != currentPlayer) {
                currentPlayer.pay(utilityProperty.getRent());
                owner.receiveRent(utilityProperty.getRent());
                return currentPlayer.getName() + " paid rent of $" + utilityProperty.getRent() + " to " + owner.getName();
            } else {
                return "You already own " + utilityProperty.getName();
            }
        }
    }

//    static String handleRailroadProperty(Player currentPlayer, RailroadProperty railroadProperty) {
//        Player owner = railroadProperty.getOwner();
//
//        if (owner == null) {
//            // The railroad property is not owned by any player
//            return "Do you want to buy " + railroadProperty.getName() + " for $" + railroadProperty.getPrice() + "?";
//
//        } else if (!owner.equals(currentPlayer)) {
//            // The property is owned by another player, collect rent
//            currentPlayer.pay(railroadProperty.getRent());
//            owner.receiveRent(railroadProperty.getRent());
//            return currentPlayer.getName() + " paid rent of $" + railroadProperty.getRent() + " to " + owner.getName();
//        } else {
//            // The property is already owned by the current player
//            return "You already own " + railroadProperty.getName();
//        }
//    }
}
