import java.util.ArrayList;

public class Player {
    private String name;
    ArrayList<Card> hand = new ArrayList<>();
    private int score;
    private int token;
    ArrayList<Card> discardPile = new ArrayList<>();

    public Player(String name){
        this.name = name;
        this.score = 0;
        this.token = 0;
    }

    public void drawCard(Card card, ArrayList<Card> deckOfCards){
        hand.add(card);
        deckOfCards.remove(card);
    }

    public void playCard(){}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
