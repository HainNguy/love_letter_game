import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private String name;
    ArrayList<Card> hand = new ArrayList<>();
    private int score;
    private int token;
    ArrayList<Card> discardPile = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.token = 0;
    }

    /**
     * @param card
     * @param deckOfCards
     */
    public void drawCard(Card card, Deck deckOfCards) {
        hand.add(card);
        deckOfCards.remove(card);
    }

    public String getName() {
        return name;
    }



    public void playCard() {
    }

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

    public String toString() {
        return name;
    }

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
                return false;
            } else {
                System.out.println("Invalid input");
                scanner.next();
            }
        }
    }
}
