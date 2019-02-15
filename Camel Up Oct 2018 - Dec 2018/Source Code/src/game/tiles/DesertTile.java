package game.tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.graphics.UI.CImage;

public class DesertTile {
	
	// Player ID
	private int playerID;
	// Positioning
	private boolean isOnBoard;
	private boolean isOasisUp;
	private int tileNumber;
	
	public DesertTile(int playerID) {
		this.playerID = playerID;
		isOnBoard = false;
	}
	
	public void setProperties(boolean isOnBoard, boolean isOasisUp, int tileNumber) {
		this.isOnBoard = isOnBoard;
		this.isOasisUp = isOasisUp;
		this.tileNumber = tileNumber;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public boolean isOnBoard() {
		return isOnBoard;
	}
	
	public boolean isOasisUp() {
		return isOasisUp;
	}
	
	public int getTileNumber() {
		return tileNumber;
	}
	
	public void drawDesertTile(Graphics2D gg, int x, int y, int width, int height) {
		if (isOnBoard) {
			Color c = new Color(153, 77, 0);
			Graphics2D g = (Graphics2D) gg.create();
			if (isOasisUp)
				g.setColor(c.brighter().brighter());
			else
				g.setColor(Color.red.brighter().brighter());
			g.fillRoundRect(x - width / 2, y - height / 2, width, height, 10, 10);
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(5));
			g.drawRoundRect(x - width / 2, y - height / 2, width, height, 10, 10);
			g.setStroke(new BasicStroke(1));
			
			if (isOasisUp) {
				CImage.drawImage(gg, "oasis", x, y, 0, 100, Color.green, false);
			} else {
				CImage.drawImage(gg, "mirage", x, y, 0, 100, Color.cyan, false);
			}
		}
		
	}
}
