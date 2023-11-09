import Cards.Card;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private String name;
    private ArrayList<Card> getHand = new ArrayList<>();
    private int tokens;
    ArrayList<Card> discardPile = new ArrayList<>();
    private int daysAgo; // player was daysAgo days ago on a date.
    private boolean protectedByHandmaid = false;
    /**
     * true if player is still active in a round, false if player is out of the round.
     */
    private boolean active = true;

    /**
     * @return Hand of player.
     */
    public ArrayList<Card> getHand() {
        return this.getHand;
    }

    /**
     * Check if player is protected by Card.Handmaid.
     * @return boolean
     */
    public boolean isProtectedByHandmaid() {
        return protectedByHandmaid;
    }

    public Player(String name) {
        this.name = name;
        this.tokens = 0;
    }

    /**
     * @param deckOfCards An instance from class Card.Deck
     *
     */
    public Card drawCard(ArrayList<Card> deckOfCards) {
        // Get the card on top of the deck and store it in drewCard
        Card drawnCard = deckOfCards.get(0);
        // remove that drew card from the deck
        deckOfCards.remove(0);
        // add that card to the hand of player
        getHand.add(drawnCard);
        // return the drawn Card.Card
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
            try {
                System.out.println("Was " + name + " on a date recently? (yes/no):");
                String yes = "yes";
                String no = "no";
                String userInput = scanner.next();
                userInput = userInput.toLowerCase();
                if (yes.equals(userInput)) {
                    return true;
                } else if (no.equals(userInput)) {
                    setDaysAgo(Integer.MAX_VALUE);
                    return false;
                } else {
                    System.out.println("Invalid input. Please enter \"yes\" or \"no\".");
                    scanner.nextLine();
                }
            } catch (Exception ex){
                System.out.println("Invalid Input. Please enter \"yes\" or \"no\".");
            }
        }
    }
    public void showHand(){
        System.out.println(name + "'s hand is: ");
        for (int i = 0; i < getHand.size(); i++) {
            System.out.println((i + 1) + ". " + getHand.get(i).getName());
        }
    }
    public void showScore(){
        System.out.println(this.name + "'s score: " + calculateScore(this.getHand));
    }

    /**
     * Calculate total score of a list of cards
     * @param cards An ArrayList of cards, which are instances of class Card.Card.
     * @return Total score of a list of cards.
     */
    public int calculateScore(ArrayList<Card> cards){
        try {
            int sum = 0;
            for (Card card : cards) {
                sum = sum + card.getValue();
            }
            return sum;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Get sum of hand's score and discardPile's score
     * @return An integer number, which is the total score
     */
    public int getTotalScore(){
        try {
            return calculateScore(getHand) + calculateScore(discardPile);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return 0;
        }
    }


    /**
     * Protect a player with the Card.Handmaid, set status of protection to true.
     */
    public void protectWithHandmaid() {
        this.protectedByHandmaid = true;
    }

    /**
     * Disable Card.Handmaid's protection, set status of protection to false.
     */
    public void disableHandmaidProtection() {
        this.protectedByHandmaid = false;
    }

    /**
     * Check if a player has a card with a particular card value.
     * @param guessedCard Value of a card
     * @return true or false
     */
    public boolean hasCardNumber(int guessedCard) {
        try {
            for (Card card : getHand) {
                if (card.getValue() == guessedCard) return true;
            }
            return false;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
