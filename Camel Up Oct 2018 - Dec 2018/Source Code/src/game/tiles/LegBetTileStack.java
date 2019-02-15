package game.tiles;

import java.util.ArrayList;

public class LegBetTileStack {
	
	// Camel ID
	private int camelID;
	
	// 0th position is the bottom of the stack
	private ArrayList<LegBetTile> tiles;
	
	public LegBetTileStack(int camelID) {
		this.camelID = camelID;
		
		int[] values = {2, 3, 5};
		tiles = new ArrayList<>();
		for (int value : values)
			tiles.add(new LegBetTile(camelID, value));
	}
	
	public void reset() {
		int[] values = {2, 3, 5};
		tiles.clear();
		for (int value : values)
			tiles.add(new LegBetTile(camelID, value));
	}
	
	public int getSize() {
		return tiles.size();
	}
	
	public LegBetTile seeTop() {
		if (tiles.isEmpty())
			return null;
		return tiles.get(tiles.size() - 1);
	}
	
	public LegBetTile takeOffTheTop() {
		if (tiles.isEmpty())
			return null;
		return tiles.remove(tiles.size() - 1);
	}
	
	public int getCamelID() {
		return camelID;
	}
}
