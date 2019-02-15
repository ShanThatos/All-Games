package game.board;

import java.awt.Color;
import java.awt.Graphics2D;

import game.graphics.CamelAnimator;
import game.graphics.UI.CImage;

public class Camel {
	
	// Identification
	private static String[] colors = {"Blue", "Yellow", "Green", "Red", "White"};
	private static Color[] gColors = {Color.blue, Color.yellow, Color.green, Color.red, Color.white};
	private static int IDBuilder = 0;
	private int id;
	
	// Last Non-animated position
	private int x, y;
	
	// Animator
	private CamelAnimator canim;
	
	public Camel() {
		id = IDBuilder++;
		canim = new CamelAnimator();
	}
	
	public String getCamelColorString() {
		return colors[id];
	}
	
	public int getID() {
		return id;
	}
	
	public static String[] getColors() {
		return colors;
	}
	
	public static Color[] getGColors() {
		return gColors;
	}
	
	public String toString() {
		return colors[id] + " Camel";
	}
	
	public void drawCamel(Graphics2D gg, int x, int y, double rot, int size, boolean flipped) {
		if (canim.isAnimating()) {
			gg.setColor(Color.black);
			gg.fillOval(canim.getRealPosition()[0] - 2, canim.getRealPosition()[1] - 2, 4, 4);
			CImage.drawImage(gg, "camel", canim.getPosition()[0], canim.getPosition()[1], rot, size, gColors[id], flipped);
		}
		else {
			CImage.drawImage(gg, "camel", x, y, rot, size, gColors[id], flipped);
			this.x = x;
			this.y = y;
		}
	}
	
	public CamelAnimator getAnimator() {
		return canim;
	}
	
	public int[] getLastPosition() {
		return new int[] {x, y};
	}
	public void saveLastPosition() {
		if (canim.isAnimating()) {
			x = canim.getRealPosition()[0];
			y = canim.getRealPosition()[1];
		}
	}
}
