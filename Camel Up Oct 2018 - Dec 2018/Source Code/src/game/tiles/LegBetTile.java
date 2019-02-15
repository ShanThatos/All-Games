package game.tiles;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.board.Camel;
import game.graphics.UI.CImage;

public class LegBetTile {
	
	// Camel ID
	private int camelID;
	// Value
	private int value;
	
	public LegBetTile(int camelID, int value) {
		this.camelID = camelID;
		this.value = value;
	}
	
	public int getCamelID() {
		return camelID;
	}
	
	public int getValue() {
		return value;
	}
	
	public void drawLegBetTile(Graphics2D gg, int x, int y, int width, int height, int borderRadius, int camelSize) {
		Color c = new Color(153, 77, 0);
		Graphics2D g = (Graphics2D) gg.create();
		g.translate(x + width / 2, y + height / 2);
		g.translate(-1, 1);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		g.setColor(Color.black);
		g.fillRoundRect(-width / 2, -height / 2, width, height, borderRadius, borderRadius);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.translate(1, -1);
		g.setColor(c);
		g.fillRoundRect(-width / 2, -height / 2, width, height, borderRadius, borderRadius);
		g.setColor(Color.black);
		g.drawRoundRect(-width / 2, -height / 2, width, height, borderRadius, borderRadius);
		g.setColor(c.brighter());
		g.setStroke(new BasicStroke(3));
		g.drawRoundRect(-width / 2 + 5, -height / 2 + 5, width - 10, height - 10, 8, 8);
		g.setStroke(new BasicStroke(1));
		g.setFont(new Font("Arial", Font.BOLD, 20));
		
		g.fillOval(-15, -12, 30, 24);
		g.setColor(Color.black);
		g.drawOval(-15, -12, 30, 24);
		String k = value + "";
		FontMetrics met = g.getFontMetrics();
		g.drawString(k, -met.stringWidth(k) / 2, -met.getHeight() / 2 + met.getAscent());
		
		CImage.drawImage(gg, "camel", x + width / 4, y + height / 2, 0, camelSize, Camel.getGColors()[camelID], true);
		CImage.drawImage(gg, "camel", x + 3 * width / 4, y + height / 2, 0, camelSize, Camel.getGColors()[camelID], false);
	}
	
	public void drawSmallLegBetTile(Graphics2D gg, int x, int y, int width, int height, int camelSize) {
		Color c = new Color(153, 77, 0);
		Graphics2D g = (Graphics2D) gg.create();
		g.translate(x + width / 2, y + height / 2);
		g.fillRoundRect(-width / 2, -height / 2, width, height, 2, 2);
		g.setColor(c);
		g.fillRoundRect(-width / 2, -height / 2, width, height, 2, 2);
		g.setColor(Color.black);
		g.drawRoundRect(-width / 2, -height / 2, width, height, 2, 2);
		g.setColor(c.brighter());
		g.drawRoundRect(-width / 2 + 2, -height / 2 + 2, width - 4, height - 4, 8, 8);
		g.setFont(new Font("Arial", Font.BOLD, height * 4 / 5));
		
		g.fillOval(-8, -3, 16, 6);
		g.setColor(Color.black);
		g.drawOval(-8, -3, 16, 6);
		String k = value + "";
		FontMetrics met = g.getFontMetrics();
		g.drawString(k, -met.stringWidth(k) / 2, -met.getHeight() / 2 + met.getAscent());
		
		CImage.drawImage(gg, "camel", x + width / 4, y + height / 2, 0, camelSize, Camel.getGColors()[camelID], true);
		CImage.drawImage(gg, "camel", x + 3 * width / 4, y + height / 2, 0, camelSize, Camel.getGColors()[camelID], false);
	}
}
