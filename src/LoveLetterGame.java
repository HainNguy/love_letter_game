import Cards.*;
import java.util.*;
public class LoveLetterGame {
    private ArrayList<Player> players = new ArrayList<>();
    private Deck deckOfCards;
    private int tokenToWin = 0;
    private int currentPlayerIndex = 0;


    /**
     * Removed cards at the start of the round.
     */
    private ArrayList<Card> removedCards = new ArrayList<>();

    /**
     *
     * @return - List of current players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }


    /**
     *
     * @return list of removed cards at the start of a round
     */
    public ArrayList<Card> getRemovedCards() {
        return this.removedCards;
    }

    /**
     *
      * @return The number of players.
     */
    public int getNumberOfPlayers() {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = 0;
        boolean invalidInput = true;
        do {
            try {
                System.out.print("How many players are there? (2-4): ");
                numberOfPlayers = scanner.nextInt();
                if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                    invalidInput = false;
                } else {
                    System.out.println("Invalid number of players. Please enter a number between 2 and 4.");
                    scanner.nextLine();
                }
            } catch (Exception ex){
                System.out.println("Invalid input. Please enter a valid number between 2 and 4.");
                scanner.nextLine();
            }
        } while (invalidInput);
        return numberOfPlayers;
    }

    /**
     *
     * @param numberOfPlayers
     */
    public void getPlayersNames(int numberOfPlayers){

        Scanner scanner = new Scanner(System.in);

        Set<String> usedNames = new HashSet<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            String playerName;
            do {
                System.out.print("Enter name for Player " + (i) + ": ");
                playerName = scanner.nextLine();

                if (NotBeginWithALetter(playerName)){
                    System.out.println("Invalid Input. Your name must start with a letter.");
                }
                // Check for duplicate names
                else if (isNameUsed(usedNames, playerName)) {
                    System.out.println("Duplicate names are not allowed. Please enter an unique name.");
                }
            } while (isNameUsed(usedNames, playerName) || NotBeginWithALetter(playerName));
            usedNames.add(playerName.toLowerCase());
            players.add(new Player(playerName));
        }
    }

    /**
     * Check if player's name does not begin with a letter.
     * @param playerName  name of player
     * @return true, if player name does not begin with a letter, else false.
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
     * @param usedNames a collection of used names
     * @param name name to check for duplicate
     * @return true, if name is already used, otherwise false
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
        System.out.println("\"\\showPlayers\" -> Show players with their status, either out or active.");
    }


    /**
     * start and set up game
     */
    void startGame(){
        System.out.println("Welcome to Love Letter!" );
        int numberOfPlayers = getNumberOfPlayers();
        setTokenToWin(calculateTokensToWin(numberOfPlayers));
        getPlayersNames(numberOfPlayers);
        whoPlaysFirst(players);
        dealInitialCards();

    }

    /**
     * Calculate number of token to win according to the number of players
     * @param numberOfPlayers - number of players.
     * @return Number of tokens to win the game.
     */
    private int calculateTokensToWin(int numberOfPlayers) {
        System.out.println("Calculating number of tokens to win...");
        int tokensToWin = 0;
        switch (numberOfPlayers){
            case 2: tokensToWin = 7;
            case 3: tokensToWin = 5;
            case 4: tokensToWin = 4;
        }
        System.out.println("To win the game you have to collect " + tokensToWin + " tokens.");
        return tokensToWin;
    }

    /**
     * decide who plays first, then reference the corresponding current player index with that player
     * @param players
     */
    private void whoPlaysFirst(ArrayList<Player> players){
        System.out.println("Determining who will play first...");
        int[] lastOnDateIndices = lastOnDateIndices(players);
        if(lastOnDateIndices.length == 1){
            currentPlayerIndex = lastOnDateIndices[0];
            System.out.println("Because " + players.get(currentPlayerIndex).getName() + " was most recently on a date.");
            System.out.println("So, " + players.get(currentPlayerIndex).getName() + " will play first.");
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
                        System.out.println("Invalid Input. Please enter a number.");
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
     * Remove a number of cards according to the number of players, then deal each player a card.
     */
    private void dealInitialCards() {
        this.deckOfCards = new Deck();
        deckOfCards.shuffleDeck();
        //If there are 2 players, 4 cards will be removed from deck.
        if (players.size() == 2) {
            for (int i = 0; i < 4; i++) {
                getRemovedCards().add(deckOfCards.remove(0));
            }
        }
        else { // otherwise one card will be removed from the top of deck
            getRemovedCards().add(deckOfCards.remove(0));
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

                A deck of cards has been created. Each of you now has a card in your hand.
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
            System.out.println("\n" + "It is "+ currentPlayer.getName() + "'s turn." +
                    " You have just drawn a card " + drawnCard.getName() + " from the deck." +
                    " Please enter your command or " +
                    " see \"\\help\" :");
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
                            Card playedCard = currentPlayer.getHand().remove(cardIndex - 1);
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
                                    // clear the hand and the discard pile of each player, set their active status to true
                                    for (Player p : players) {
                                        p.getHand().clear();
                                        p.discardPile.clear();
                                        p.setActive(true);
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
                    showPlayersWithStatus(this.players);
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
            System.out.println(currentPlayer.getName() + " has discarded Guard.");
            boolean invalidTargetPlayer = true;
            Player targetPlayer = null;
            do {
                try {
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", choose only one of these following players to guess a card " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by the Handmaid: ");
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
                    System.out.print(currentPlayer.getName() + ", please guess a card, that " + targetPlayer.getName() + " might have " +
                            " (2-8): ");
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
                    targetPlayer.setActive(false);
                    System.out.println(currentPlayer.getName() + " guessed correctly. " + targetPlayer.getName() + " is out of the round.");
                } else {
                    System.out.println(currentPlayer.getName() + " guessed incorrectly. So, nothing happens.");
                }
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        } else if (playedCard.getName().equals("Priest")) {

            System.out.println(currentPlayer.getName() + " has discarded Priest.");
            boolean invalidInput = true;
            do {
                try {
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", you can only choose one of these following player's hands to look at " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by Handmaid: ");
                        showPlayers(notProtectedPlayers);
                    } else {
                        System.out.println(currentPlayer.getName() + ", you can choose one of these following player's hands to look at " +
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

            System.out.println(currentPlayer.getName() + " has discarded Baron.");
            boolean invalidInput = true;
            do {
                try {
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", choose only one of these following players to compare hands with " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by the Handmaid: ");
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
                                playersToCompare.get(0).setActive(false);
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
            System.out.println(currentPlayer.getName() + " has discarded Handmaid.");
            currentPlayer.protectWithHandmaid();
            System.out.println(currentPlayer.getName() + " is protected by the Handmaid until your next turn.");
        } else if (playedCard.getName().equals("Prince")) {
            System.out.println(currentPlayer.getName() + " has discarded Prince.");
            //Choose a player, he discards his hand and draw a new card
            boolean invalidInput = true;
            do {
                try {
                    // Pick a non-protected player
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", please choose only one of these following players to make them discard their hand " +
                                "(1-" + notProtectedPlayers.size() + "). You might have to choose yourself, if other players are protected by the Handmaid: ");
                        showPlayers(notProtectedPlayers);
                    } else {
                        System.out.println(currentPlayer.getName() + ", please choose one of these following players to discard their hand " +
                                "(1-" + notProtectedPlayers.size() + "): ");
                        showPlayers(notProtectedPlayers);
                    }
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    // Valid player is chosen
                    if (targetPlayerIndex < notProtectedPlayers.size()){
                        Player targetPlayer = notProtectedPlayers.get(targetPlayerIndex);
                        System.out.println(currentPlayer.getName() + " makes " + targetPlayer.getName() + " discard their hand.");
                        if (targetPlayer.hasCardNumber(8)){
                            targetPlayer.getHand().clear();
                            System.out.println(targetPlayer.getName() + ", Your hand is discarded. Because a Princess was in your hand, you has been eliminated from the round.");
                            roundPlayers.remove(targetPlayer);
                            targetPlayer.setActive(false);
                        } else {
                            targetPlayer.getHand().clear();
                            System.out.println(targetPlayer.getName() + ", Your hand is discarded.");
                            if (deckOfCards.isEmpty()){
                                Card newCard = targetPlayer.drawCard(getRemovedCards());
                                System.out.println("Since the deck is empty, you've got a new card" + newCard.getName() +
                                        " from the removed cards at the start of the round.");
                            } else {
                                Card newCard = targetPlayer.drawCard(deckOfCards);
                                System.out.println("You've got a new card " + newCard.getName() + " from the deck.");
                            }
                        }
                        invalidInput = false;
                    } else {
                        System.out.println("Invalid Input. Please enter a correct player (1-" + notProtectedPlayers.size() + ").");
                        scanner.nextLine();
                    }
                } catch (Exception ex){
                    System.out.println("Invalid Input. Please enter a correct player (1-" + notProtectedPlayers.size() +").");
                    scanner.nextLine();
                }
            } while(invalidInput);
        } else if (playedCard.getName().equals("King")) {
            System.out.println(currentPlayer.getName() + " has discarded King.");
            boolean invalidInput = true;
            do {
                try {
                    // Pick a non-protected player
                    if (notProtectedPlayers.size() < roundPlayers.size()) {
                        System.out.println(currentPlayer.getName() + ", choose only one of these following players to trade hands with " +
                                "(1-" + notProtectedPlayers.size() + "), since the other players are protected by the Handmaid: ");
                        showPlayers(notProtectedPlayers);
                    } else {
                        System.out.println(currentPlayer.getName() + ", choose one of these following players to trade hands with " +
                                "(1-" + notProtectedPlayers.size() + "): ");
                        showPlayers(notProtectedPlayers);
                    }
                    int targetPlayerIndex = scanner.nextInt() - 1;
                    // Valid player is chosen
                    if (targetPlayerIndex < notProtectedPlayers.size()){
                        Player targetPlayer = notProtectedPlayers.get(targetPlayerIndex);
                        System.out.println( currentPlayer.getName() + " has chosen " + targetPlayer.getName() + " to trade hand with.");
                        System.out.println(" Trading hands...");
                        tradeHands(currentPlayer, targetPlayer);
                        System.out.println("Hands traded.");
                        System.out.print("Now, "); currentPlayer.showHand(); targetPlayer.showHand();
                        invalidInput = false;
                    } else {
                        System.out.println("Invalid Input. Please enter a correct player (1-" + notProtectedPlayers.size() + ").");
                        scanner.nextLine();
                    }
                } catch (Exception ex){
                    System.out.println(ex.getMessage() + ". Please enter a correct player (1-" + notProtectedPlayers.size() +").");
                    scanner.nextLine();
                }
            } while(invalidInput);
        } else if (playedCard.getName().equals("Countess")) {
            System.out.println(currentPlayer.getName() + " has discarded Countess.");
        } else if (playedCard.getName().equals("Princess")) {
            System.out.println(currentPlayer.getName() + " has been eliminated for discarding the Princess.");
            roundPlayers.remove(currentPlayer);
            currentPlayer.setActive(false);
        }
    }

    private void tradeHands(Player currentPlayer, Player targetPlayer) {
        try {
            List<Card> tempHand = new ArrayList<>(currentPlayer.getHand());
            currentPlayer.getHand().clear();
            currentPlayer.getHand().addAll(targetPlayer.getHand());
            targetPlayer.getHand().clear();
            targetPlayer.getHand().addAll(tempHand);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Get players, who are not protected by Cards.Handmaid.
     * @param players
     * @return List of not protected players.
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
            int playerScore = player.calculateScore(player.getHand());
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
     * @param tokenToWin- number of tokens to win
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
    public void showPlayersWithStatus(ArrayList<Player> players){
        for (Player player : players){
            String status = player.isActive() ? "active" : "out"  ;
            System.out.println(player.getName() + " (" + status + ")");
        }
    }

    /**
     * Display all players in a list @param players
     * @param players a list of players
     */
    public void showPlayers(ArrayList<Player> players){
        for (int i = 0; i < players.size(); i++) {
            System.out.print((i + 1) + ". " + players.get(i).getName() + "  ");
        }
        System.out.println();
    }
}
