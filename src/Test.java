public class Test {
    public static void main(String[] args) {
        Deck deck = new Deck();
        for (int i = 0; i < deck.size(); i++) {
            System.out.println(deck.get(i) );
        }
        System.out.println("------------------");
        deck.shuffleDeck();
        for (int i = 0; i < deck.size(); i++) {
            System.out.println(deck.get(i));
        }
    }
}
