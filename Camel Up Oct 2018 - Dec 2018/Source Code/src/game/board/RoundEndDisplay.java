package game.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.graphics.MainFrame;
import game.player.Player;
import game.tiles.LegBetTile;

public class RoundEndDisplay {
	
	private static boolean isDisplaying;
	private static BufferedImage display;
	
	public static void drawDisplay(Graphics2D g) {
		g.drawImage(display, MainFrame.WIDTH / 2 - display.getWidth() / 2, MainFrame.HEIGHT / 2 - display.getHeight() / 2, null);
	}
	
	public static boolean isDisplaying() {
		return isDisplaying;
	}
	
	public static void stopDisplay() {
		isDisplaying = false;
	}
	
	
	// Shitty code ;)
	public static void createDisplayImage(Player[] players, Race race, GameBoard board) {
		isDisplaying = true;
		
		int numPlayers = players.length;
		
		int[] legBetChange = new int[numPlayers];
		int[] pyrTiles = new int[numPlayers];
		
		// Leg Bets
		ArrayList<Camel> order = race.getCamelsInOrder();
		
		for (int i = 0; i < numPlayers; i++) {
			ArrayList<LegBetTile> legBets = players[i].getLegBets();
			for (int j = 0; j < legBets.size(); j++) {
				int index = order.indexOf(race.getCamelByID(legBets.get(j).getCamelID()));
				if (index == order.size() - 1) 
					legBetChange[i] += legBets.get(j).getValue();
				else if (index == order.size() - 2)
					legBetChange[i]++;
				else
					legBetChange[i]--;
			}
		}
		
		// Pyramid Tiles
		for (int i = 0; i < numPlayers; i++) 
			pyrTiles[i] += players[i].getPyrTiles().size();
		
		int width = MainFrame.WIDTH * 3 / 4, height = MainFrame.HEIGHT * 3 / 4;
		display = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = display.createGraphics();
		g.setColor(new Color(200, 180, 0));
		g.fillRect(0, 0, width, height);
		String[] labels = {"Player", "Leg Bets", "Pyramid Tiles", "Total"};
		for (int i = -1, yy = 0; i < players.length; i++, yy += height / (players.length + 1)) {
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(4));
			g.drawRect(0, yy, width, height / (players.length + 1));
			g.setStroke(new BasicStroke(1));
			
			
			if (i == -1) {
				g.setFont(new Font("Arial", Font.BOLD, 30));
				FontMetrics met = g.getFontMetrics();
				for (int j = 0, xxx = 0; j < 4; j++, xxx += width / 4) 
					g.drawString(labels[j], xxx + 10, (height / players.length + 1) / 2 + met.getHeight() / 2);
				
				continue;
			}
			
			g.setFont(new Font("Arial", Font.BOLD, 30));
			FontMetrics met = g.getFontMetrics();
			
			for (int j = 0, xxx = 0; j < 4; j++, xxx += width / 4) {
				if (j == 0) {
					g.drawString("Player " + i, xxx + 10, yy + (height / players.length + 1) / 2 + met.getHeight() / 2);
				} else if (j == 1) {
					g.drawString(String.format("%+d", legBetChange[i]), xxx + 10, yy + (height / players.length + 1) / 2 + met.getHeight() / 2);
				} else if (j == 2) {
					g.drawString(String.format("%+d", pyrTiles[i]), xxx + 10, yy + (height / players.length + 1) / 2 + met.getHeight() / 2);
				} else if (j == 3) {
					g.drawString(players[i].getCapital() + "", xxx + 10, yy + (height / players.length + 1) / 2 + met.getHeight() / 2);
				}
			}
		}
		g.dispose();
	}
}
