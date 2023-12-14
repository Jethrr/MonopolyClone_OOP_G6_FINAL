public class FreeParking {
    private String name;

    public FreeParking(String name) {
        this.name = name;
    }

    public FreeParking() {

    }

    public String handleFreeParking(Player player) {
        return player.getName() + " landed on Free Parking";
    }
}