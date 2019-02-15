package game.input;

import game.board.GameManager;
import game.tiles.LegBetTile;

public interface InputHandler {
	public void displayPlayerStartTurn(GameManager gm);
	
	public int requestTypeOfTurn(GameManager gm);
	
	public LegBetTile requestLegBettingTile(GameManager gm);
	
	public Object[] requestDesertTilePlacement(GameManager gm);
	
	public Object[] requestRaceBetType(GameManager gm);
	
	public void displayRoundEnd(GameManager gm);
}
