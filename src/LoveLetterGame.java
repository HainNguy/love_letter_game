import java.util.ArrayList;
import java.util.Scanner;

public class LoveLetterGame {
    ArrayList<Player> players = new ArrayList<>();
    private int tokenToWin = 0;
    ArrayList<Card> deckOfCards = new ArrayList<>();
    private int currentPlayerIndex = 0;


    public LoveLetterGame(){

    }
    //get number of players
    void getNumberOfPlayers(){
        Scanner scanner = new Scanner(System.in);
        int playerCount = 0;
        while(playerCount < 2 || playerCount > 4){
            System.out.println("Please enter the number of players (from 2 to 4) : ");
            playerCount = scanner.nextInt();
        }
        for (int i = 0; i < playerCount; i++) {
            System.out.print("Name for player " + (i+1) + ":");
            String name = scanner.next();
            players.add(new Player(name));
        }
    }
    void welcome(){}
    void showCommands(){}
    void showHand(){}
    void showScore(){}
    void playCard(){}
    void startGame(){}

    public int getTokenToWin() {
        return tokenToWin;
    }

    public void setTokenToWin(int tokenToWin) {
        this.tokenToWin = tokenToWin;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
}
