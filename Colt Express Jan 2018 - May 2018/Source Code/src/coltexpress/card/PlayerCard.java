package coltexpress.card;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerCard {
	
	// All Player Card types
	public static String[] types = 
		{"bullet", "move", "floor-change", "fire", "robbery", "marshal", "punch"};
	public static BufferedImage[] cardIcons = new BufferedImage[7];
	
	// FaceUp
	public boolean faceUp;
	
	// Index
	public int type;
	
	private String playerName;
	public int playerID;
	
	public PlayerCard(int type, int playerID) {
		this.type = type;
	}
	
	public void setPlayerName(String pn) {
		playerName = pn;
	}
	
	public String getCardName() {
		return types[type];
	}
	
	public static void readAllIcons() throws IOException {
		for (int i = 1; i < 7; i++) {
			cardIcons[i] = ImageIO.read(new File("Files/Cards/" + types[i] + ".PNG"));
		}
	}
}
