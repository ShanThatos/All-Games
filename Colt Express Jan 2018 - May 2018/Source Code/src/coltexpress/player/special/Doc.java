package coltexpress.player.special;

import coltexpress.player.Player;

public class Doc extends Player {
	
	public Doc(int ctc, int playerID) {
		super(ctc, playerID, 3);
		maxNumCardsOnHand = 7;
	}
}
