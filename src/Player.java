import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private String name;
    ArrayList<Card> hand = new ArrayList<>();
    private int score;
    private int tokens;
    ArrayList<Card> discardPile = new ArrayList<>();
    private int daysAgo; // player was daysAgo days ago on a date.
    private boolean protectedByHandmaid = false;
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.tokens = 0;
    }

    /**
     * @param deckOfCards
     * @param deckOfCards
     */
    public Card drawCard(Deck deckOfCards) {
        // Get the card on top of the deck and store it in drewCard
        Card drawnCard = deckOfCards.get(0);
        // remove that drew card from the deck
        deckOfCards.remove(0);
        // add that card to the hand of player
        hand.add(drawnCard);
        // return the drawn Card
        return drawnCard;
    }

    /**
     * Get player's name
     * @return name
     */
    public String getName() {
        return name;
    }

    public int getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(int daysAgo) {
        this.daysAgo = daysAgo;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTokens() {
        return tokens;
    }

    public void incrementToken(){
        this.tokens++ ;
    }

    public String toString() {
        return name;
    }

    /**
     *
     * @return true if player was on a date, else false
     */
    public boolean isOnDate() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Was " + name + " on a date recently? (yes/no):");
            String yes = "yes";
            String no = "no";
            String userInput = scanner.nextLine();
            userInput = userInput.toLowerCase();
            if (yes.equals(userInput)) {
                return true;
            } else if (no.equals(userInput)) {
                setDaysAgo(Integer.MAX_VALUE);
                return false;
            } else {
                System.out.println("Invalid input");
                scanner.next();
            }
        }
    }
    public void showHand(){
        System.out.println(name + "'s hand is: ");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i).getName());
        }
    }
    public void showScore(){
        System.out.println(this.name + "'s score: " + calculateScore(this.hand));
    }

    /**
     * calculate Score of a list of cards
     * @param cards
     * @return
     */
    public int calculateScore(ArrayList<Card> cards){ return 0;
    }

    /**
     * get sum of hand's score and discardPile's score
     * @return
     */
    public int getTotalScore(){
        return calculateScore(hand) + calculateScore(discardPile);
    }


    /**
     * Method to protect a player with the Handmaid
     */
    public void protectWithHandmaid() {
        protectedByHandmaid = true;
    }

    /**
     * Method to clear Handmaid protection
     */

    public void clearHandmaidProtection() {
        protectedByHandmaid = false;
    }

}
