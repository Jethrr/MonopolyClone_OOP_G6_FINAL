import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Card {
    private String description;

    public Card(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Define methods or properties common to all types of cards
}

