import java.util.*;

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

    /**
     * get number of players.
      * @return
     */
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

    /**
     *
     * @param numberOfPlayers
     */
    void getPlayersNames(int numberOfPlayers){

        Scanner scanner = new Scanner(System.in);

        Set<String> usedNames = new HashSet<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            String playerName;
            do {
                System.out.print("Enter the name for Player " + (i) + ": ");
                playerName = scanner.nextLine();
                /*
                while(true){
                if (name gueltig, muss mit einem Buchstaben anfangen)
                store to playerName
                break;
                else print invalid input
                scanner.next
                 */
                if (!beginWithALetter(playerName)){
                    System.out.println("Invalid Input.");
                }
                // Check for duplicate names
                else if (isNameUsed(usedNames, playerName)) {
                    System.out.println("Duplicate player names are not allowed. Please enter a unique name.");
                }
            } while (isNameUsed(usedNames, playerName) || !beginWithALetter(playerName));
            usedNames.add(playerName.toLowerCase());
            players.add(new Player(playerName));
        }
    }

    private boolean beginWithALetter(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            // The string is empty or null, so it does not start with a letter.
            return false;
        }

        char firstChar = playerName.charAt(0);
        // Check whether the first character is a letter.
        return Character.isLetter(firstChar);
    }

    // Check for case-insensitive used name.

    /**
     *
     * @param usedNames
     * @param name
     * @return
     */
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
        int numberOfPlayers = getNumberOfPlayers();
        getPlayersNames(numberOfPlayers);
        setTokenToWin(numberOfPlayers);
        dealInitialCards();
        whoPlaysFirst(players);
    }

    /**
     * decide who plays first, then reference the corresponding current player index with that player
     * @param players
     */
    void whoPlaysFirst(ArrayList<Player> players){
        System.out.println("\nNow it is determined who will play first.");
        int[] lastOnDateIndices = lastOnDateIndices(players);
        if(lastOnDateIndices.length == 1){
            currentPlayerIndex = lastOnDateIndices[0];
            System.out.println("\nSo, " + players.get(currentPlayerIndex).getName() + " will play first.");
        }
        else {
            System.out.println("Because there is either no person, who has been on a date recently or several, who have been on a date on the same day," +
                    " age must be asked to resolve the tie.");
            int [] youngestPlayers = youngestPlayerIndices(players, lastOnDateIndices );

            if (youngestPlayers.length == 1) {
                currentPlayerIndex = youngestPlayers[0];
                System.out.println("\nSo, " + players.get(currentPlayerIndex) + " will play first.");

            } else {
                currentPlayerIndex = youngestPlayers[new Random().nextInt(youngestPlayers.length)];
                System.out.println("\nBecause there are several people, who are the youngest, one will be randomly selected to play first");
                System.out.println("\nSo, " + players.get(currentPlayerIndex) + " will play first.");

            }
        }
    }

    /**
     *
     * @param players
     * @param lastOnDateIndices
     * @return an array of indices of the youngest Players relative to all players( if none of them was on a date)
     * or relative to players, who are most recently on a date.
     */
    int [] youngestPlayerIndices (ArrayList<Player> players, int [] lastOnDateIndices){
        Scanner scanner = new Scanner(System.in);
        int youngestAge = Integer.MAX_VALUE;
        List<Integer> youngestIndices = new ArrayList<>();
        if (lastOnDateIndices.length == 0) {
            for (int i = 0; i < players.size(); i++) {
                int age;
                while (true) {
                    System.out.println("How old are you, " + players.get(i).getName() + "? (years of age): ");
                    if (scanner.hasNextInt()) {
                        age = scanner.nextInt();
                        break;
                    } else {
                        System.out.println("Invalid Input.");
                        scanner.next();
                    }
                }
                if (age < youngestAge) {
                    youngestAge = age;
                    youngestIndices.clear();
                    youngestIndices.add(i);
                } else if (age == youngestAge) {
                    youngestIndices.add(i);
                }
            }
            return youngestIndices.stream().mapToInt(Integer::intValue).toArray();
        } else {
            for (int index : lastOnDateIndices) {
                int age;
                while (true) {
                    System.out.println("How old are you, " + players.get(index).getName() + "? (years of age): ");
                    if (scanner.hasNextInt()) {
                        age = scanner.nextInt();
                        break;
                    } else {
                        System.out.println("Invalid Input.");
                        scanner.next();
                    }
                }
                if (age < youngestAge) {
                    youngestAge = age;
                    youngestIndices.clear();
                    youngestIndices.add(index);
                } else if (age == youngestAge) {
                    youngestIndices.add(index);
                }
            }
            return youngestIndices.stream().mapToInt(Integer::intValue).toArray();

        }
    }


    /**
     *
     * @param players
     * @return an array of indices of the players, who were most recently on a date, if no one was on a date, return empty array
     */
    int[] lastOnDateIndices(ArrayList<Player> players){
        Scanner scanner = new Scanner(System.in);
        int[] lastOnDateIndices = new int[players.size()];
        int minDaysAgo = Integer.MAX_VALUE;
        int lastIndex = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isOnDate()) {
                int daysAgo;
                //ask how long ago
                while(true){
                    System.out.println("How many days ago was " + players.get(i).getName() + " on a date? (0 for today, 1 for yesterday, etc.): ");
                    if (scanner.hasNextInt()){
                        daysAgo = scanner.nextInt();
                        break;
                    } else {
                        System.out.println("Invalid Input.");
                        scanner.next();
                    }
                }
                if (daysAgo < minDaysAgo) {
                    minDaysAgo = daysAgo;
                    lastIndex = 0;
                    lastOnDateIndices[lastIndex] = i;
                } else if (daysAgo == minDaysAgo) {
                    lastIndex++;
                    lastOnDateIndices[lastIndex] = i;
                }
            }
        }
        if (minDaysAgo == Integer.MAX_VALUE){
            return new int[0];
        }
        int[] result = new int[lastIndex + 1];
        System.arraycopy(lastOnDateIndices, 0, result, 0, lastIndex + 1);
        return result;
    }

    /**
     * remove a number of card according to the rule, then give each player a card.
     */
    void dealInitialCards() {
        //If there are 2 players, 4 cards will be removed from deck.
        if (players.size() == 2) {
            for (int i = 0; i < 4; i++) {
                deckOfCards.remove(0);
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
                "Enter your game's commands to play. Here are the existing commands:\n ");
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
