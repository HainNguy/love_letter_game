public class Test {
    public static void main(String[] args) {
        LoveLetterGame game = new LoveLetterGame();
        game.startGame();
        System.out.println("The first Player is: " + game.players.get(game.getCurrentPlayerIndex()));

    }
}
