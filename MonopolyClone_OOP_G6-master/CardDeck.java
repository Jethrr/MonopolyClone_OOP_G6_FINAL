import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
    private List<Card> cards;

    public CardDeck() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            // Handle the case when the deck is empty (optional)
            return null;
        }
        return cards.remove(0);
    }
}
