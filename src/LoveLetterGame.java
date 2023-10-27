import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LoveLetterGame {
    ArrayList<Player> players = new ArrayList<>();
    private Deck deckOfCards;
    private int tokenToWin = 0;
    private int currentPlayerIndex = 0;


    public LoveLetterGame(){
        this.deckOfCards = new Deck();
        deckOfCards.shuffleDeck();
    }
    //get number of players
     public int getNumberOfPlayers() {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers;
        while (true) {
            System.out.print("Enter the number of players (2-4): ");
            if (scanner.hasNextInt()) {
                numberOfPlayers = scanner.nextInt();
                if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                    return numberOfPlayers;
                } else {
                    System.out.println("Invalid number of players. Please enter a number between 2 and 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number between 2 and 4.");
                scanner.next();
            }
        }
    }
    void getPlayersNames(int numberOfPlayers){

        Scanner scanner = new Scanner(System.in);

        Set<String> usedNames = new HashSet<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            String playerName;
            do {
                System.out.print("Enter the name for Player " + (i) + ": ");
                playerName = scanner.nextLine();
                // Check for duplicate names
                if (isNameUsed(usedNames, playerName)) {
                    System.out.println("Duplicate player names are not allowed. Please enter a unique name.");
                }
            } while (isNameUsed(usedNames, playerName));
            usedNames.add(playerName.toLowerCase());
            players.add(new Player(playerName));
        }
    }
    // Check for case-insensitive used name.
    private boolean isNameUsed(Set<String> usedNames, String name){
        // Convert the provided name to lowercase for case-insensitive comparison
        return usedNames.contains(name.toLowerCase());
    }
    void showCommands(){
        System.out.println("\\playCard - Discard a card ");
        System.out.println("\\showHand - Show your hand");
        System.out.println("\\showScore - Show your score");
        System.out.println("\\help - Show available commands");
    }

    void showHand(Player currentPlayer){}
    void showScore(){}
    void playCard(Player currentPlayer){}
    void startGame(){
        System.out.println("Welcome to Love Letter!" );
        getPlayersNames(getNumberOfPlayers());
        dealInitialCards();
        whoPlaysFirst();
    }

    void whoPlaysFirst(){

    }
    void dealInitialCards() {
        //If there are 2 players, 4 cards will be removed from deck.
        if (players.size() == 2) {
            for (int i = 0; i < 4; i++) {
                deckOfCards.remove(i);
            }}
        else {
            deckOfCards.remove(0);
        }
        for (Player player : players){
            player.drawCard(deckOfCards.get(0), deckOfCards);
        }


    }
    void playGame(){
        System.out.println("\nA deck of Card is created. Each of you now has a card in your hand. " +
                "Enter your game commands to play. Here are the existing commands: ");
        showCommands();


        while(players.size() > 1){
            Player currentPlayer = players.get(currentPlayerIndex);
            boolean hasPlayedCard = false;

            System.out.println("\n" + "It's "+ currentPlayer.getName() + "'s turn. Please enter your command:");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.next();

            if (command.equals("\\playCard")) {
                if (!hasPlayedCard) {
                    playCard(currentPlayer);
                    hasPlayedCard = true;
                } else {
                    System.out.println("You have already played a card this turn. Choose another command.");
                }
            } else if (command.equals("\\showHand")) {
                showHand(currentPlayer);
            } else if (command.equals("\\showScore")) {
                showScore();
            } else if (command.equals("\\help")) {
                showCommands();
            } else {
                System.out.println("Invalid command. Please use \\help to see the commands");
            }

            // Only increment the currentPlayerIndex if the current player has played a card
            if (hasPlayedCard) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
    }


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
