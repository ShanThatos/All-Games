package coltexpress.loot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Loot {
	
	// Row 0 contains Loot Values for the LootBoxes
	// Row 1: Gems
	// Row 2: Bags
	private static int[][] allLootValues;
	public static BufferedImage[] allLootImages;
	// Boolean array to check if loot has already been placed in a car
	public static boolean[][] lootBeingUsed;
	
	// Identification 
	private int rowNum;
	private int colNum;
	// Also named as Purse
	private boolean isMoneyBag;
	
	// Location
	public int trainCarID, animPlayerID;
	public boolean inTrainCar, onTopOfTrain;
	
	public Loot(int row, int col, int trainCarIndex) {
		rowNum = row;
		colNum = col;
		isMoneyBag = (row == 2);
		lootBeingUsed[row][col] = true;
		this.trainCarID = trainCarIndex;
		inTrainCar = true;
//		inTrainCar = false;
//		playerID = 0;
	}
	
	public void draw(Graphics g, int x, int y, int width, int height, boolean showValue) {
		g.drawImage(allLootImages[rowNum], x, y, x + width, y + height, 0, 0, allLootImages[rowNum].getWidth(), allLootImages[rowNum].getHeight(), null);
		if (showValue) {
			g.setFont(new Font("Arial", Font.BOLD, 8));
			Color color = rowNum == 0 ? Color.black : rowNum == 1 ? Color.white : Color.black;
			g.setColor(color);
			int centerX = rowNum == 0 ? x + 3 : rowNum == 1 ? x + 5 : x + 3;
			centerX = x + 10;
			int centerY = rowNum == 0 ? y + 13 : rowNum == 1 ? y + 10: y + 13;
			int stringWidth = g.getFontMetrics().stringWidth(allLootValues[rowNum][colNum] + "");
			g.drawString(allLootValues[rowNum][colNum] + "", centerX - stringWidth / 2, centerY + 4);
		}
	}
	
	public static void readAllLootValues() throws IOException {
		allLootValues = new int[3][];
		allLootImages = new BufferedImage[3];
		Scanner in = new Scanner(new File("Files/Loot/allLoot.dat"));
		lootBeingUsed = new boolean[allLootValues.length][];
		for (int i = 0; i < 3; i++) {
			allLootValues[i] = new int[in.nextInt()];
			lootBeingUsed[i] = new boolean[allLootValues[i].length];
			for (int j = 0; j < allLootValues[i].length; j++)
				allLootValues[i][j] = in.nextInt();
			in.nextLine();
		}
		for (int i = 0; i < 3; i++) {
			String fileName = in.nextLine().trim();
			allLootImages[i] = ImageIO.read(new File("Files/Loot/" + fileName));
		}
		in.close();
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public String getType() {
		return rowNum == 0 ? "LootBox" : rowNum == 1 ? "Gem" : "Bag";
	}
	public int getLootValue() {
		return allLootValues[rowNum][colNum];
	}
}
