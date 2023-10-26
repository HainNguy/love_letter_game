import java.util.ArrayList;
import java.util.Scanner;

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
    void getNumberOfPlayers(){
        Scanner scanner = new Scanner(System.in);
        int playerCount = 0;
        while(playerCount < 2 || playerCount > 4){
            System.out.print("Please enter the number of players (from 2 to 4) : ");
            playerCount = scanner.nextInt();
        }
        System.out.println("\nNow you are going to enter your names. " +
                "Please use different player names. If some of you have" +
                "the same names, please indicate with a number after your name," +
                " for example: Alice1 and Alice2. ");

        for (int i = 0; i < playerCount; i++) {
            System.out.print("Name for player " + (i+1) + ":");
            String name = scanner.next();
            players.add(new Player(name));
        }
    }
    void welcome(){
        System.out.println(
                "Wellcome to Love Letter." +
                        "Make sure that you have already read the game rules before playing the game."+
        "To see the game's commands with a short explanation, enter \\help" +
                "or enter \\start to start the game");
    }
    void showCommands(){
        System.out.println("\nAvailable commands:");
        System.out.println("\\playCard - Discard a card ");
        System.out.println("\\showHand - show your hand");
        System.out.println("\\showScore - show your score");
        System.out.println("\\help - show available commands");
    }

    void showHand(Player currentPlayer){}
    void showScore(){}
    void playCard(Player currentPlayer){}
    void startGame(){
        System.out.println("Welcome to Love Letter. Please make sure that you have " +
                "already read the game rules before playing the game" );
        getNumberOfPlayers();

        dealInitialCards();
        whoPlaysFirst();
    }

    void whoPlaysFirst(){
        System.out.println("...");
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
            for (int i = 0; i < 1; i++) {
                player.drawCard(deckOfCards.get(0), deckOfCards);
            }
        }


    }
    void playGame(){
        System.out.println("Enter \\help to see the game's commands.");
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
