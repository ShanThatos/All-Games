package game.player;

import java.util.ArrayList;

import game.tiles.DesertTile;
import game.tiles.LegBetTile;
import game.tiles.PyramidTile;
import game.tiles.RaceBetTile;

public class Player {
	
	private static ArrayList<DesertTile> dts;
	
	// Desert Tile
	private DesertTile dt;
	
	// Leg Betting Tiles
	private ArrayList<LegBetTile> legBets;
	
	// Pyramid Tiles
	private ArrayList<PyramidTile> pyrTiles;
	
	// Race Betting Tiles
	private ArrayList<RaceBetTile> raceBets;
	
	// Money, Capital
	private int capital;
	
	// Identification
	private static int IDBuilder = 0;
	private int id;
	
	public Player() {
		if (dts == null)
			dts = new ArrayList<>();
		id = IDBuilder++;
		dt = new DesertTile(id);
		dts.add(dt);
		legBets = new ArrayList<>();
		pyrTiles = new ArrayList<>();
		raceBets = new ArrayList<>();
		for (int i = 0; i < 5; i++) 
			raceBets.add(new RaceBetTile(id, i));
		capital = 3;
	}
	
	public void addLegBetTile(LegBetTile legBet) {
		legBets.add(legBet);
	}
	
	public void addPyrTile(PyramidTile pyrTile) {
		pyrTiles.add(pyrTile);
	}
	
	public ArrayList<RaceBetTile> getRaceBets() {
		return raceBets;
	}
	
	public ArrayList<LegBetTile> getLegBets() {
		return legBets;
	}
	
	public ArrayList<PyramidTile> getPyrTiles() {
		return pyrTiles;
	}
	
	public int getID() {
		return id;
	}
	
	public void changeCapital(int amountChange) {
		capital = Math.max(capital + amountChange, 0);
	}
	
	public int getCapital() {
		return capital;
	}
	
	public DesertTile getDesertTile() {
		return dt;
	}
	
	public void reset() {
		dt.setProperties(false, false, 0);
		legBets.clear();
		pyrTiles.clear();
	}
	
	public static ArrayList<DesertTile> getAllDesertTiles() {
		return dts;
	}
}
