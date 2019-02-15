package game.tiles;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.board.Camel;
import game.graphics.UI.CImage;

public class RaceBetTile {
	
	// Player ID
	private int playerID;
	// Camel ID
	private int camelID;
	
	public RaceBetTile(int playerID, int camelID) {
		this.playerID = playerID;
		this.camelID = camelID;
	}
	
	public int getCamelID() {
		return camelID;
	}
	
	public void drawRaceBetTile(Graphics2D gg, int x, int y, int width, int height, boolean faceUp, float alpha) {
		Graphics2D g = (Graphics2D) gg.create();
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.translate(x + width / 2, y + height / 2);
		Color c = new Color(153, 77, 0);
		g.setColor(c);
		g.fillRoundRect(-width / 2, -height / 2, width, height, 10, 10);
		g.setColor(Color.black);
		g.drawRoundRect(-width / 2, -height / 2, width, height, 10, 10);
		g.setColor(c.brighter());
		g.setStroke(new BasicStroke(2));
		g.drawRoundRect(-width / 2 + 4, -height / 2 + 4, width - 8, height - 8, 5, 5);
		g.setStroke(new BasicStroke(1));
		
		if (faceUp) {
			CImage.drawImage(g, "camel", 0, 0, 0, 20, Camel.getGColors()[camelID], false);
		} else {
			g.setColor(Color.black);
			String s = "?";
			g.setFont(new Font("Arial", Font.BOLD, height * 4 / 5));
			FontMetrics met = g.getFontMetrics();
			g.drawString(s, -met.stringWidth(s) / 2, -met.getHeight() / 2 + met.getAscent());
		}
	}
}
