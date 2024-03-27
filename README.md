**This is my first project about a board game, Love Letter from the software development internship at the LMU. The game Love Letter was developed in Java using the following game rules. The goal of the project is to apply and deepen the concept of object-oriented programming. The game will be played on the console.**







**Rules: In general, this game is about sending (as many) love letters as possible to a princess.**
The original rule can be found [hier](http://alderac.com/wp-content/uploads/2017/11/Love-Letter-Premium_Rulebook.pdf).

## 1. Set up (2 to 4 players)
- Assemble the following cards into the deck: 
   
   [1] Guard (5 copies)

   [2] Priest(2 copies)

   [3] Baron(2 copies)

   [4] Handmaid(2 copies)

   [5] Prince(2 copies)

   [6] King

   [7] Countess

   [8] Princess
   
   [x]: The higher x is, the closer that person is to the Princess.

- 	Shuffle these 16 cards. Remove the top card of the deck from the game and place it aside
- By 2-player games take 3 more cards and place aside
-	Each player draw a card from a deck, secretly from other players
-	Who was most recently on a date goes first (if tied, the youngest player wins the tie)
## 2.	How to play
-	Series of rounds will be played
-	After winning a round, the winner get a token for affection.
- To win the game, the winner must have:
  - 7 tokens, if 2 players
  - 5 tokens, if 3 players
  - 4 tokens, if 4 players

- When does a round end: 
  - Deck is empty at the end of a player’s turn
  - All player but one are out of round


- Who wins a round:
  - Who has the highest number in their hand after revealing. If tie, players add number of their discard pile. If still tie, all player win.
  - The only one remaining in a round as the other are out.
  - Who wins the round, get a token of affection.

- After a round ends:
  - Shuffle all 16 cards together, and play a new round following all of the setup rules above. 
  - The winner of the previous round goes first. 
  - If there was more than one winner from the previous round as a result of a tie, then whoever of the tied players was most recently on a date goes first


    
-	Description of a turn:
     - Draw the first card from a deck and add it to your hand.
     - Choose one of two card in your hand and discard it.
     - Apply its effect.
     - All discard card remain in front of the player(Skat)
     - After applying pass turn to the next player on your left


## 3. Meaning of cards
[8]**Princess**: Lose if discarded.

[7]**Countess**: Must be played if you have Cards.King or Cards.Prince in hand

[6]**King**: Trade hand with another player	

[5]**Prince**: Choose a player, he discards his hand and draw a new card

[4]**Handmaid**: You cannot be chosen until your next turn

[3]**Baron**: Compare hand with another player, lower number is out

[2]**Priest**: Look at an other player’s hand

[1]**Guard**: Guess a player’s hand, if correct, the player is out

For more details of the rules see [this link.](http://alderac.com/wp-content/uploads/2017/11/Love-Letter-Premium_Rulebook.pdf)


