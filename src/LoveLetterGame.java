import java.util.*;

public class LoveLetterGame {
    ArrayList<Player> players = new ArrayList<>();
    private Deck deckOfCards;
    private int tokenToWin = 0;
    private int currentPlayerIndex = 0;
    List<Card> removedCard = new ArrayList<>();

    ArrayList<Player> eliminatedPlayers;

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

                if (NotBeginWithALetter(playerName)){
                    System.out.println("Invalid Input.");
                }
                // Check for duplicate names
                else if (isNameUsed(usedNames, playerName)) {
                    System.out.println("Duplicate player names are not allowed. Please enter a unique name.");
                }
            } while (isNameUsed(usedNames, playerName) || NotBeginWithALetter(playerName));
            usedNames.add(playerName.toLowerCase());
            players.add(new Player(playerName));
        }
    }

    /**
     * Check if player name does not begin with a letter.
     * @param playerName
     * @return true, if player name does not begin with a letter, else false
     */
    private boolean NotBeginWithALetter(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            // The string is empty or null, so it does not start with a letter.
            return true;
        }

        char firstChar = playerName.charAt(0);
        // Check whether the first character is a letter.
        return !Character.isLetter(firstChar);
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

    /**
     * show available game commands
     */
    static void showCommands(){
        System.out.println("\"\\playCard i\"  -> Discard the i-th card in your hand. ");
        System.out.println("\"\\showHand\"    -> Show your hand.");
        System.out.println("\"\\showScore\"   -> Show your score.");
        System.out.println("\"\\help\"        -> Show available commands.");
        System.out.println("\"\\showPlayers\" -> Show players of a round with their status, either out or active.");
    }


    /**
     * start and set up game
     */
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
        System.out.println("\nNow it's going to be determined who will play first.");
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
                        scanner.nextLine();
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
     * Find out, who were most recently on a date.
     * @param players A List of players
     * @return an array of indices of the players, who were most recently on a date, if no one was on a date, return empty array
     */
    int[] lastOnDateIndices(ArrayList<Player> players){
        Scanner scanner = new Scanner(System.in);
        int[] lastOnDateIndices = new int[players.size()];
        int minDaysAgo = Integer.MAX_VALUE;
        int lastIndex = 0;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.isOnDate()) {
                //ask how long ago
                boolean continueAsk = true;
                do {
                    System.out.println("How many days ago was " + players.get(i).getName() + " on a date? (0 for today, 1 for yesterday, etc.): ");
                    if (scanner.hasNextInt()){
                        player.setDaysAgo(scanner.nextInt());
                        continueAsk = false;
                    } else {
                        System.out.println("Invalid Input.");
                        scanner.nextLine();
                    }
                } while(continueAsk);
                if (player.getDaysAgo() < minDaysAgo) {
                    minDaysAgo = player.getDaysAgo();
                    lastIndex = 0;
                    lastOnDateIndices[lastIndex] = i;
                } else if (player.getDaysAgo() == minDaysAgo) {
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
        this.deckOfCards = new Deck();
        deckOfCards.shuffleDeck();
        //If there are 2 players, 4 cards will be removed from deck.
        if (players.size() == 2) {
            for (int i = 0; i < 4; i++) {
                removedCard.add(deckOfCards.remove(0));
            }
        }
        else { // otherwise one card will be removed from the top of deck
            removedCard.add(deckOfCards.remove(0));
        }
        // Each player draw an initial card from the deck
        for (Player player : players){
            player.drawCard(deckOfCards);
        }


    }

    /**
     * Game is being played, as long as no game winner
     */
    void playGame(){
        System.out.println("""

                A deck of Card has been created. Each of you now has a card in your hand.
                Enter your game's commands to play. Here are the existing commands:
                """);
        showCommands();
        // create a list of round players, which initially has the same players as in players of the game, since a player can be eliminated from a round.
        ArrayList<Player> roundPlayers = new ArrayList<>(players);
        // play rounds, if someone has reached the token to win at the end of a round, the game will end.
        boolean continueGame = true;
        while (continueGame){
            // get current player and let him play his turn
            Player currentPlayer = roundPlayers.get(currentPlayerIndex);
            boolean hasPlayedCard = false;
            // the current player draws a card on top of deck, and add it to his hand.
            Card drawnCard = currentPlayer.drawCard(deckOfCards);
            // prompt player, that he/she just drew 1 card x from the deck, and should enter his play command.
            System.out.println("\n" + "It's "+ currentPlayer.getName() + "'s turn." +
                    " You have just drawn a card " + drawnCard + " from the deck." +
                    " Please enter your command, " +
                    "or finish your turn with the command, for example \"\\playCard 1\" or see \"\\help\" :");
            currentPlayer.showHand();
            do {

                // read input from console
                Scanner scanner = new Scanner(System.in);
                String command = scanner.nextLine();


                // analyse the command, and execute the corresponding command.
                if (command.matches("\\\\playCard\\s+\\d+")) {// analyse the number in the command and catch exception if it's not a number 1 or 2, since each player has 2 cards in hand at a time.
                    try {
                        // declare cardIndex variable to store the index of card played by the current player.
                        int cardIndex = Integer.parseInt(command.replaceAll("\\\\playCard\\s+", ""));
                        if (cardIndex == 1 || cardIndex == 2) {

                            // discard that chosen card cardIndex.
                            Card playedCard = currentPlayer.hand.remove(cardIndex - 1);
                            // add that played card to the discard pile of the current player.
                            currentPlayer.discardPile.add(playedCard);
                            // execute that card's effect.
                            applyCardEffect(currentPlayer, playedCard, roundPlayers, deckOfCards);
                            // make sure, that the current player has played a card, thus the turn will be passed to the next player
                            hasPlayedCard = true;
                            // Check for the end of the round, if it's the case, determine the round winner
                            // A round ends, if deck is empty at the end of a player’s turn, or all player but one are out of round.
                            if (deckOfCards.isEmpty() || (roundPlayers.size() == 1)) {
                                // determine the round winners
                                List<Player> roundWinners = determineRoundWinners(roundPlayers);

                                // increment the number of tokens of each round winner
                                for (Player p : roundWinners) {
                                    p.incrementToken();
                                }
                                // display round winners
                                System.out.println("Round winner: ");
                                for (Player p : roundWinners) {
                                    System.out.println(p);
                                }
                                // Check, if one of the round winners has reached the tokens to win, terminate the game, else start a new round
                                for (Player p : roundWinners) {
                                    if (p.getTokens() == getTokensToWin()) {
                                        System.out.println("Game winner: " + p);
                                        System.out.println("Congratulations!");
                                        continueGame = false;
                                    }
                                }
                                // Start a new round
                                {
                                    // reset the list of round players
                                    roundPlayers = new ArrayList<>(players);
                                    // clear the hand and the discard pile of each player,
                                    for (Player p : players) {
                                        p.hand.clear();
                                        p.discardPile.clear();
                                    }
                                    // create new deck and deal initial cards
                                    dealInitialCards();
                                    // determine the currentPlayerIndex for the next round, since the previous round winner will go first
                                    currentPlayerIndex = determineNextCurrentPlayerIndex(roundWinners);
                                }
                            }
                        } else {// if that card index is other number than 1 or 2
                            System.out.println("Invalid Command. Use \"\\playCard 1\" or \"\\playCard 2\" ");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid Command. Use \"\\playCard 1\" or \"\\playCard 2\" ");
                    }
                } else if (command.equals("\\showPlayers")) {
                    showPlayersWithStatus();
                } else if (command.equals("\\showHand")) {
                    currentPlayer.showHand();
                } else if (command.equals("\\showScore")) {
                    currentPlayer.showScore();
                } else if (command.equals("\\help")) {
                    showCommands();
                } else {
                    System.out.println("Invalid command. Use \"\\help\" to see the commands.");
                }
            } while(!hasPlayedCard);
            // Only pass the turn to the next person. If the current player has played a card.
            if (hasPlayedCard) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
    }

    /**
     * Determine the next current player index for the next round after a round ends
     */
    private int determineNextCurrentPlayerIndex(List<Player> roundWinners) {
        // If there was more than one winner from the previous round as a result of a tie
        if (roundWinners.size() > 1){
            //then whoever of the round winners was most recently on a date, goes first
            return players.indexOf(whoMostRecentlyOnDate(roundWinners));
        } else{  // there is only one round winner
            return players.indexOf(roundWinners.get(0));
        }
    }

    /**
     * Find out, who was most recently on a date among a list of players
     * @param roundWinners
     * @return player who was most recently on a date
     */
    private Player whoMostRecentlyOnDate(List<Player> roundWinners) {
        Player result = null;
        int minDaysAgo = Integer.MAX_VALUE;
        for (Player p : roundWinners) {
            if (p.getDaysAgo() < minDaysAgo){
                minDaysAgo = p.getDaysAgo();
                result = p;
            }
        }
        // if no one of them was on a date, just return the first one in the list
        if (minDaysAgo == Integer.MAX_VALUE){
            result = roundWinners.get(0);
        }
        return result;
    }

    /**
     * Determine the round winner
     * @param roundPlayers list of remaining round's players in a round.
     * @return A list of round winners
     */
    public ArrayList<Player> determineRoundWinners(ArrayList<Player> roundPlayers) {
        ArrayList<Player> result = new ArrayList<>();

        // If all players are eliminated but one remaining player, that wins the round.
        if (roundPlayers.size() == 1){
            return roundPlayers;
        } else {// Get players, who have the highest score
            ArrayList<Player> roundWinners = compareHands(roundPlayers);
            // 2 or more tied winners, who have the same hand's score.
            // consider adding discardPile's score
            if (roundWinners.size() > 1) {
                int highestScore = 0;
                for (Player player : roundWinners) {
                    int totalScoreWithDisCardPile = player.getTotalScore();
                    if(totalScoreWithDisCardPile > highestScore){
                        if (result.size() > 0) {result.clear();}
                        result.add(player);
                        highestScore = totalScoreWithDisCardPile;
                    } else if (totalScoreWithDisCardPile == highestScore) {
                        result.add(player);
                    }
                }
                return result;
            } else {
                return roundWinners;
            }
        }
    }

    /**
     * Apply the card effect, which has been discarded.
     * @param currentPlayer
     * @param playedCard
     * @param roundPlayers
     * @param deck
     */
    public void applyCardEffect(Player currentPlayer, Card playedCard, ArrayList<Player> roundPlayers, Deck deck) {
        // Get notProtectedPlayers, who can be chosen as target by a player, since a protected player can not be chosen.
        ArrayList<Player> notProtectedPlayers = getNotProtectedPlayers(roundPlayers);
        Scanner scanner = new Scanner(System.in);
        if (playedCard.getName().equals("Guard")) {
//            System.out.print(currentPlayer.getName() + ", choose a player to guess a card (1-" + players.size() + "): ");
//            int targetPlayerIndex = scanner.nextInt() - 1;
//            Player targetPlayer = players.get(targetPlayerIndex);
//            System.out.print(currentPlayer.getName() + ", guess a card (1-8): ");
//            int guess = scanner.nextInt();
//            if (guess >= 1 && guess <= 8) {
//                if (targetPlayer.hand.get(0).getValue() == guess) {
//                    System.out.println(currentPlayer.getName() + " guessed correctly. " + targetPlayer.getName() + " is out of the round.");
//                    players.remove(targetPlayer);
//                } else {
//                    System.out.println(currentPlayer.getName() + " guessed incorrectly.");
//                }
//            } else {
//                System.out.println("Invalid guess. Choose a number between 1 and 8.");
//            }
            // Get valid target Player
            boolean invalidTargetPlayer = true;
            Player targetPlayer = null;
            do {
                try {
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ",choose only one of these following players to guess a card " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by Handmaid: ");
                        showPlayers(notProtectedPlayers);
                    } else {
                        System.out.println(currentPlayer.getName() + ",choose one of these following players to guess a card " +
                                "(1-" + notProtectedPlayers.size() + "): ");
                        showPlayers(notProtectedPlayers);
                    }
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    // Valid target player is chosen
                    if (targetPlayerIndex < notProtectedPlayers.size()){
                        targetPlayer = notProtectedPlayers.get(targetPlayerIndex);
                        invalidTargetPlayer = false;
                        System.out.println(currentPlayer.getName() + " has chosen " + targetPlayer.getName() + " to guess a card.");
                    } else {
                        System.out.println("Invalid Input. Please enter the correct player (1-" + notProtectedPlayers.size() + ").");
                        scanner.nextLine();
                    }

                } catch(RuntimeException ex) {
                    System.out.println("Invalid Input. Please enter the correct player (1-" + notProtectedPlayers.size() +").");
                    scanner.nextLine();
                }
            } while(invalidTargetPlayer);
            // Get valid guessed card
            boolean invalidCard = true;
            int guessedCard = 0;
            do {
                try{
                    System.out.print(currentPlayer.getName() + ", guess a card (2-8): ");
                    int cardNumber = scanner.nextInt();
                    // If valid card number is chosen.
                    if (cardNumber >= 2 && cardNumber <= 8){
                        guessedCard = cardNumber;
                        invalidCard = false;
                    } else {
                        System.out.println("Invalid guess. Choose a number between 2 and 8.");
                        scanner.nextLine();
                    }
                }catch (RuntimeException ex){
                    System.out.println("Invalid guess. Choose a number between 2 and 8.");
                    scanner.nextLine();
                }

            } while (invalidCard);
            // Check if the target player has the guessed card number, if so, eliminate target player
            try {
                if (targetPlayer.hasCardNumber(guessedCard)) {
                    roundPlayers.remove(targetPlayer);
                    System.out.println(currentPlayer.getName() + " guessed correctly. " + targetPlayer.getName() + " is out of the round.");
                } else {
                    System.out.println(currentPlayer.getName() + " guessed incorrectly. So, nothing happens.");
                }
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        } else if (playedCard.getName().equals("Priest")) {


            boolean invalidInput = true;
            do {
                try {
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", you can only choose one of these following players's hands to look at " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by Handmaid: ");
                        showPlayers(notProtectedPlayers);
                    } else {
                        System.out.println(currentPlayer.getName() + ", you can choose one of these following players's hands to look at " +
                                "(1-" + notProtectedPlayers.size() + "): ");
                        showPlayers(notProtectedPlayers);
                    }
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    // Valid target player is chosen.
                    if (targetPlayerIndex < notProtectedPlayers.size()){
                        Player targetPlayer = notProtectedPlayers.get(targetPlayerIndex);
                        System.out.println("You have chosen " + targetPlayer.getName() + "'s hand to look at.");
                        // if yourself is picked, do nothing.
                        if (currentPlayer.equals(targetPlayer)) System.out.println(" Since you have chosen yourself, nothing happens. ");
                        else { // Display target player's hand
                            targetPlayer.showHand();
                        }
                        invalidInput = false;
                    } else {
                        System.out.println("Invalid Input. Please enter the correct player (1-" + notProtectedPlayers.size() +").");
                        scanner.nextLine();
                    }

                } catch (RuntimeException ex){
                    System.out.println("Invalid Input. Please enter the correct player (1-" + notProtectedPlayers.size() +").");
                    scanner.nextLine();
                }

            } while (invalidInput);

        } else if (playedCard.getName().equals("Baron")) {


            boolean invalidInput = true;
            do {
                try {
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", choose only one of these following players to compare hands with " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by Handmaid: ");
                        showPlayers(notProtectedPlayers);
                    } else {
                        System.out.println(currentPlayer.getName() + ", choose one of these following players to compare hands with " +
                                "(1-" + notProtectedPlayers.size() + "): ");
                        showPlayers(notProtectedPlayers);
                    }
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    // Valid target player is chosen.
                    if (targetPlayerIndex < notProtectedPlayers.size()){
                        Player targetPlayer = roundPlayers.get(targetPlayerIndex);
                        System.out.println("You have chosen " + targetPlayer.getName() + " to compare your hand with.");
                        // if yourself is picked, do nothing.
                        if (currentPlayer.equals(targetPlayer)) System.out.println(" Since you have chosen yourself, nothing happens. ");
                        else { // Compare hands, player with lower score is knocked out.
                            ArrayList<Player> playersToCompare = new ArrayList<>();
                            playersToCompare.add(currentPlayer);
                            playersToCompare.add(targetPlayer);
                            ArrayList<Player> playersWithHigherScore = compareHands(playersToCompare);
                            System.out.println("Comparing hands...");
                            //Player with lower score is knocked out.
                            if (playersWithHigherScore.size() == 1){
                                playersToCompare.remove(playersWithHigherScore.get(0));
                                roundPlayers.remove(playersToCompare.get(0));
                                System.out.println(playersToCompare.get(0).getName() + ", You have been eliminated from the round, because you've lost the score comparison with " + currentPlayer.getName());
                            } else System.out.println("Since you both have the same score, nothing happens.");// If tied, do nothing.
                        }
                        invalidInput = false;
                    } else {
                        System.out.println("Invalid Input. Please enter the correct player (1-" + notProtectedPlayers.size() +").");
                        scanner.nextLine();
                    }

                } catch (Exception ex){
                    System.out.println("Invalid Input. Please enter the correct player (1-" + notProtectedPlayers.size() +").");
                    scanner.nextLine();
                }

            } while (invalidInput);

        } else if (playedCard.getName().equals("Handmaid")) {
            currentPlayer.protectWithHandmaid();
            System.out.println(currentPlayer.getName() + " is protected by the Handmaid until their next turn.");
        } else if (playedCard.getName().equals("Prince")) {
            //Choose a player, he discards his hand and draw a new card
            boolean invalidInput = true;
            do {
                try {
                    System.out.print(currentPlayer.getName() + ", choose a player to make them discard their hand (1-" + roundPlayers.size() + "): ");
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    if (targetPlayerIndex < roundPlayers.size()) {
                        Player targetPlayer = roundPlayers.get(targetPlayerIndex);
                        System.out.println(currentPlayer.getName() + " makes " + targetPlayer.getName() + " discard their hand.");
                        // Discard the hand of the target player
                        targetPlayer.hand.clear();
                        // Draw a new card for the target player from the deck
                        if (!deck.isEmpty()) {
                            targetPlayer.drawCard(deck);
                        } else {
                            // If the deck is empty, draw the previously removed card (if available)
                            if (removedCard != null) {
                                targetPlayer.hand.add(removedCard.get(0));
                            }
                        }
                        System.out.println(targetPlayer + ", after your hand is discarded, you've got now a new hand.");
                        targetPlayer.showHand();
                        invalidInput = false;
                    } else System.out.println("Invalid Input.");
                } catch (Exception ex){
                    System.out.println("Invalid Input.");
                }
            } while(invalidInput);
        } else if (playedCard.getName().equals("King")) {
            //Trade hand with another player
            boolean invalidInput = true;
            do {
                try {
                    System.out.print(currentPlayer.getName() + ", choose a player to trade hands with (1-" + roundPlayers.size() + "): ");
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    if (targetPlayerIndex < roundPlayers.size()){
                        Player targetPlayer = roundPlayers.get(targetPlayerIndex);
                        System.out.println(currentPlayer.getName() + " trades hands with " + targetPlayer.getName());
                        List<Card> tempHand = new ArrayList<>(currentPlayer.hand);
                        currentPlayer.hand.clear();
                        currentPlayer.hand.addAll(targetPlayer.hand);
                        targetPlayer.hand.clear();
                        targetPlayer.hand.addAll(tempHand);
                        invalidInput = false;
                    } else System.out.println("Invalid Input.");
                }
                catch (Exception ex){
                    System.out.println("Invalid Input.");
                }
            } while (invalidInput);
        } else if (playedCard.getName().equals("Countess")) {
            // No specific effect; must be played if the player has a King or Prince.
        } else if (playedCard.getName().equals("Princess")) {
            System.out.println(currentPlayer.getName() + " has been eliminated for discarding the Princess.");
            roundPlayers.remove(currentPlayer);
        }
    }

    /**
     * Get players, who are not protected by Handmaid.
     * @param players
     * @return List of not pretected players.
     */
    private ArrayList<Player> getNotProtectedPlayers(ArrayList<Player> players) {
        ArrayList<Player> result = new ArrayList<>();
        try {
            for (Player p : players) {
                if (!p.isProtectedByHandmaid()) {
                    result.add(p);
                }
            }
            return result;
        }catch (NullPointerException ex){
            System.out.println("IllegalArgumentException");
            return result;
        }
    }

    /**
     * Compare hands of players
     * @param playersToCompare
     * @return Return players, who have the highest score.
     */
    public ArrayList<Player> compareHands(ArrayList<Player> playersToCompare) {
        int highestValue = Integer.MIN_VALUE;
        ArrayList<Player> result = new ArrayList<>();
        for (Player player : playersToCompare) {
            int playerScore = player.calculateScore(player.hand);
            if (playerScore > highestValue) {
                if (result.size() > 0) {
                    result.clear();
                }
                result.add(player);
                highestValue = playerScore;
            } else if (playerScore == highestValue) {
                result.add(player);
            }
        }
        return result;
    }

    /**
     *
     * @return number token to win
     */
    public int getTokensToWin() {
        return tokenToWin;
    }

    /**
     * set Token To Win
     * @param tokenToWin
     */
    public void setTokenToWin(int tokenToWin) {
        this.tokenToWin = tokenToWin;
    }

    /**
     * getCurrentPlayerIndex
     * @return CurrentPlayerIndex
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * setCurrentPlayerIndex
     * @param currentPlayerIndex
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * show players of a round with their status, either out or active.
     */
    public void showPlayersWithStatus(){
        for (Player player : this.players){
            String status = eliminatedPlayers.contains(player) ? "out" : "active";
            System.out.println(player + ": " + status);
        }
    }

    /**
     * Display all players in the list @param players
     * @param players
     */
    public void showPlayers(ArrayList<Player> players){
        for (int i = 0; i < players.size(); i++) {
            System.out.print((i + 1) + ". " + players.get(i).getName() + "  ");
        }
        System.out.println();
    }
}
