public class Test2 {
    public static void main(String[] args) {
        LoveLetterGame game = new LoveLetterGame();
        game.getPlayersNames(game.getNumberOfPlayers());
        game.dealInitialCards();
        int[] lastOnDate = game.lastOnDateIndices(game.players);
        for (int index : lastOnDate) {
            System.out.println("player at index " + index + " was most recently on a date ");
        }

    }
}
