package Cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {
    public Deck(){
        for (int i = 0; i < 5; i++) {
            this.add(new Guard());
        }
        for (int i = 0; i < 2; i++) {
            this.add(new Priest());
        }
        for (int i = 0; i < 2; i++) {
            this.add(new Baron());
        }
        for (int i = 0; i < 2; i++) {
            this.add(new Handmaid());
        }
        for (int i = 0; i < 2; i++) {
            this.add(new Prince());
        }
        this.add(new King());
        this.add(new Countess());
        this.add(new Princess());
    }
    public void shuffleDeck(){
        Collections.shuffle(this);
    }


}
