package coltexpress.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;

import coltexpress.loot.Loot;
import coltexpress.main.Runner;
import coltexpress.player.Player;
import coltexpress.player.special.Belle;
import coltexpress.player.special.Cheyenne;
import coltexpress.player.special.Django;
import coltexpress.player.special.Doc;
import coltexpress.player.special.Ghost;
import coltexpress.player.special.Tuco;

public class PlayerInfo {
	
	// Images
	public static BufferedImage board;
	public static BufferedImage[] faces;
	// Players chosen in order
	public static int[] players;
	
	public static int[] animIDs;
	
	public static void drawLootBoard(Graphics g, ArrayList<Loot> loot) {
		g.drawImage(board, 785, 10, 200, 353, null);
		int[] xxFaceMid = new int[4];
		Arrays.fill(xxFaceMid, 820);
		int[] yyFaceBot = {83, 158, 233, 307};
		int fWidth = 66, fHeight = 70;
		
		for (int i = 0; i < animIDs.length; i++) {
			g.drawImage(faces[animIDs[i]], xxFaceMid[i] - fWidth / 2, yyFaceBot[i] - fHeight, fWidth, fHeight, null);
		}
		
		int[] xxLoot = new int[4];
		Arrays.fill(xxLoot, 860);
		int[] yyLoot = {14, 90, 164, 238};
		for (int i = 0; i < players.length; i++) {
			drawLootForSinglePlayer(g, loot, xxLoot[i], yyLoot[i], i);
		}
		
		
	}
	
	private static void drawLootForSinglePlayer(Graphics g, ArrayList<Loot> loot, int x, int y, int pI) {
		int i = 0;
		int xx = x, yy = y;
		for (Loot l : loot) {			
			if (l.inTrainCar)
				continue;
			int playerLootBelongsTo = -1;
			for (int j = 0; j < GameFrame.gp.gm.plys.length; j++) {
				if (GameFrame.gp.gm.plys[j].animPlayerID == l.animPlayerID)
					playerLootBelongsTo = j;
			}
			if (playerLootBelongsTo != pI)
				continue;
			
			
			l.draw(g, xx, yy, 20, 20, false);
			xx += 24;
			i++;
			if (i % 5 == 0) {
				xx = x;
				yy += 24;
			}
		}
	}
	
	public static Player[] getRandomPlayers() {
		Player[] ret = new Player[GameManager.NUM_PLAYERS];
		players = new int[GameManager.NUM_PLAYERS];
		animIDs = new int[GameManager.NUM_PLAYERS];
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < 6; i++)
			list.add(i);
		
		Collections.shuffle(list);
		for (int i = 0; i < players.length; i++)
			players[i] = list.get(i);
		
		for (int i = 0; i < players.length; i++) {
			switch (players[i]) {
			case 0:
				ret[i] = new Belle(GameManager.NUM_PLAYERS, i);
				break;
			case 1:
				ret[i] = new Cheyenne(GameManager.NUM_PLAYERS, i);
				break;
			case 2:
				ret[i] = new Django(GameManager.NUM_PLAYERS, i);
				break;
			case 3:
				ret[i] = new Doc(GameManager.NUM_PLAYERS, i);
				break;
			case 4:
				ret[i] = new Ghost(GameManager.NUM_PLAYERS, i);
				break;
			case 5:
				ret[i] = new Tuco(GameManager.NUM_PLAYERS, i);
				break;
			}
			animIDs[i] = ret[i].animPlayerID;
		}
		return ret;
	}
	
	public static void readAllInfo() throws IOException {
		board = ImageIO.read(new File("Files/UI/PlayerInfo/lootboard.png"));
		faces = new BufferedImage[6];
		String[] fileNames = {"belleFace.png", "cheyenneFace.png", "djangoFace.png", "docFace.png", "ghostFace.png", "tucoFace.png"};
		
		for (int i = 0; i < 6; i++) {
			faces[i] = ImageIO.read(new File("Files/UI/PlayerInfo/Heads/" + fileNames[i]));
		}
	}
}
