public class StreetProperty extends Property {
    private String colorGroup;

    public StreetProperty(String name, int price, int rent, String colorGroup) {
        super(name, price, rent);
        this.colorGroup = colorGroup;
    }
    public StreetProperty(String name, int price, String colorGroup) {
        super(name, price);
        this.colorGroup = colorGroup;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public class BrownStreet extends StreetProperty {
        public BrownStreet(String name, int price, int rent) {
            super(name, price, rent, "Brown");
        }
    }

    public class LightBlueStreet extends StreetProperty {
        public LightBlueStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "LightBlue");
        }
    }

    public class PinkStreet extends StreetProperty {
        public PinkStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Pink");
        }
    }

    public class OrangeStreet extends StreetProperty {
        public OrangeStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Orange");
        }
    }

    public class RedStreet extends StreetProperty {
        public RedStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Red");
        }
    }

    public class YellowStreet extends StreetProperty {
        public YellowStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Yellow");
        }
    }

    public class GreenStreet extends StreetProperty {
        public GreenStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Green");
        }
    }

    public class BlueStreet extends StreetProperty {
        public BlueStreet(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Blue");
        }
    }

    public class NoColor extends StreetProperty {
        public NoColor(String name, int price, int rent, String colorGroup) {
            super(name, price, rent, "Blue");
        }
    }

}
