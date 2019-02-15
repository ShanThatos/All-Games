package game.board;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import game.graphics.MainFrame;
import game.graphics.UI.CImage;
import game.input.GraphicsHandler;
import game.player.Player;
import game.tiles.DesertTile;
import game.tiles.LegBetTile;
import game.tiles.LegBetTileStack;
import game.tiles.PyramidTile;
import game.tiles.RaceBetTile;

public class GameBoard {
	
	// Race
	private Race race;
	
	// Players
	private Player[] players;
	
	// Leg Bet Tile Stacks
	private LegBetTileStack[] legBetsStack;
	// Winner and Loser Bet Stacks
	private Queue<RaceBetTile> winnerBets;
	private Queue<RaceBetTile> loserBets;
	// Pyramid Tiles
	private ArrayList<PyramidTile> pyrTiles;
	
	// Pyramid
	private Pyramid pyr;
	
	public static int btnAreaCX, btnAreaCY, btnAreaWidth, btnAreaHeight;
	public static int plyAreaCX, plyAreaCY, plyAreaWidth, plyAreaHeight;
	public static int rbwtAreaCX, rbwtAreaCY, rbtAreaWidth, rbtAreaHeight;
	public static int rbltAreaCX, rbltAreaCY;
	
	public GameBoard(Race race, Player[] players) {
		this.race = race;
		this.players = players;
		
		legBetsStack = new LegBetTileStack[5];
		for (int i = 0; i < 5; i++)
			legBetsStack[i] = new LegBetTileStack(i);
		
		winnerBets = new LinkedList<>();
		loserBets = new LinkedList<>();
		
		pyrTiles = new ArrayList<>();
		for (int i = 0; i < 5; i++)
			pyrTiles.add(new PyramidTile());
		
		pyr = new Pyramid();
		
		btnAreaWidth = (MainFrame.WIDTH - MainFrame.HEIGHT + 50) / 2 - 20;
		btnAreaHeight = MainFrame.HEIGHT / 2 - 100;
		btnAreaCX = MainFrame.HEIGHT - 25 + btnAreaWidth / 2;
		btnAreaCY = MainFrame.HEIGHT / 4;
		
		plyAreaWidth = btnAreaWidth + 100;
		plyAreaHeight = btnAreaHeight + 50;
		plyAreaCX = btnAreaCX + 50;
		plyAreaCY = 3 * MainFrame.HEIGHT / 4 - 8 - 25;
		
		rbwtAreaCX = MainFrame.WIDTH - 280;
		rbwtAreaCY = MainFrame.HEIGHT / 5;
		rbltAreaCX = MainFrame.WIDTH - 140;
		rbltAreaCY = MainFrame.HEIGHT / 5;
		rbtAreaWidth = 120;
		rbtAreaHeight = 200;
	}
	
	public void addToWinnerBets(RaceBetTile raceBet) {
		winnerBets.add(raceBet);
	}
	
	public void addToLoserBet(RaceBetTile raceBet) {
		loserBets.add(raceBet);
	}
	
	public LegBetTileStack[] getLegBetsStacks() {
		return legBetsStack;
	}
	
	public ArrayList<PyramidTile> getPyrTiles() {
		return pyrTiles;
	}
	
	public Pyramid getPyramid() {
		return pyr;
	}
	
	public Queue<RaceBetTile> getWinnerBets() {
		return winnerBets;
	}
	public Queue<RaceBetTile> getLoserBets() {
		return loserBets;
	}
	
	public void displayBoardInfo(Player[] players) {
		// Amount of money for each player
		System.out.println("Player status: ");
		for (int i = 0; i < players.length; i++) {
			System.out.println("Player " + (i + 1) + ": " + players[i].getCapital() + " Pound(s)");
			ArrayList<LegBetTile> legBets = players[i].getLegBets();
			System.out.println("\tCurrently has " + legBets.size() + " Leg Bet Tile(s)");
			for (int j = 0; j < legBets.size(); j++) 
				System.out.println("\t\t" + Camel.getColors()[legBets.get(j).getCamelID()] + " Camel: " + legBets.get(j).getValue() + " Pounds");
		}
		System.out.println();
		
		// Leg Betting Tile Stacks
		System.out.println("Leg Betting Tile Stacks: ");
		for (int i = 0; i < legBetsStack.length; i++) {
			LegBetTileStack legBetStack = legBetsStack[i];
			System.out.println("Top of the " + Camel.getColors()[legBetStack.getCamelID()] + " Camel Bet Stack: " + legBetStack.seeTop().getValue() + " Pounds");
		}
		System.out.println();
		
		// Number of Pyramid Tiles
		System.out.println("Number of Pyramid Tiles Available: " + pyrTiles.size());
		System.out.println();
		
		// Number of Bets placed
		System.out.println("Number of Winner Bets Placed: " + winnerBets.size());
		System.out.println("Number of Loser Bets Placed: " + loserBets.size());
		System.out.println();
	}
	
	public void reset() {
		pyrTiles.clear();
		for (int i = 0; i < 5; i++)
			pyrTiles.add(new PyramidTile());
		
		pyr.returnOutsideDieToInside();
		
		for (int i = 0; i < legBetsStack.length; i++)
			legBetsStack[i].reset();
	}
	
	public void drawBoard(Graphics2D g) {
		g.setColor(new Color(255, 230, 128));
		g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
		drawButtonArea(g);
		drawPlayerDisplayArea(g);
		drawLegBetTileStacks(g);
		drawWinnerAndLoserBets(g);
		race.drawRace(g, players);
		pyr.drawPyramid(g);
		pyr.drawDice(g);
	}
	private void drawButtonArea(Graphics2D gg) {
		Graphics2D g = (Graphics2D) gg.create();
		
		g.translate(btnAreaCX - 8, btnAreaCY + 8);
		g.setColor(Color.black);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		g.fillRoundRect(-btnAreaWidth / 2, -btnAreaHeight / 2, btnAreaWidth, btnAreaHeight, 40, 40);
		
		g.translate(8, -8);
		g.setColor(new Color(204, 153, 0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.fillRoundRect(-btnAreaWidth / 2, -btnAreaHeight / 2, btnAreaWidth, btnAreaHeight, 40, 40);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(5));
		g.drawRoundRect(-btnAreaWidth / 2, -btnAreaHeight / 2, btnAreaWidth, btnAreaHeight, 40, 40);
	}
	private void drawPlayerDisplayArea(Graphics2D gg) {
		Graphics2D g = (Graphics2D) gg.create();
		
		g.translate(plyAreaCX - 8, plyAreaCY + 8);
		g.setColor(Color.black);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		g.fillRoundRect(-plyAreaWidth / 2, -plyAreaHeight / 2, plyAreaWidth, plyAreaHeight, 40, 40);
		
		g.translate(8, -8);
		g.setColor(new Color(255, 102, 102));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.fillRoundRect(-plyAreaWidth / 2, -plyAreaHeight / 2, plyAreaWidth, plyAreaHeight, 40, 40);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(5));
		g.drawRoundRect(-plyAreaWidth / 2, -plyAreaHeight / 2, plyAreaWidth, plyAreaHeight, 40, 40);
	}
	private void drawLegBetTileStacks(Graphics2D gg) {
		Graphics2D g = (Graphics2D) gg.create();
		
		int cardWidth = 100, cardHeight = 50;
		for (int i = 0, xx = MainFrame.WIDTH - cardWidth - 50, yy = MainFrame.HEIGHT / 2 + 50; i < legBetsStack.length; i++, yy += cardHeight + 20) {
			for (int j = 0; j < legBetsStack[i].getSize(); j++) {
				legBetsStack[i].seeTop().drawLegBetTile(gg, xx + j * 5, yy - j * 5, cardWidth, cardHeight, 10, 20);
			}
		}
	}
	private void drawWinnerAndLoserBets(Graphics2D gg) {
		drawBets(gg, winnerBets, rbwtAreaCX, rbwtAreaCY, rbtAreaWidth, rbtAreaHeight, 0);
		drawBets(gg, loserBets, rbltAreaCX, rbltAreaCY, rbtAreaWidth, rbtAreaHeight, 4);
	}
	private void drawBets(Graphics2D gg, Queue<RaceBetTile> bets, int x, int y, int width, int height, int camelHighlight) {
		Color c = new Color(153, 77, 0);
		Graphics2D g = (Graphics2D) gg.create();
		g.translate(x, y);
		
		g.setColor(c);
		g.fillRoundRect(-width / 2, -height / 2, width, height, 10, 10);
		g.setColor(c.brighter());
		g.setStroke(new BasicStroke(8));
		g.drawLine(-width / 2 + 4, -height / 2 + 30, width / 2 - 4, -height / 2 + 30);
		g.drawLine(-width / 2 + 4, height / 2 - 30, width / 2 - 4, height / 2 - 30);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(3));
		g.drawRoundRect(-width / 2, -height / 2, width, height, 10, 10);
		g.setStroke(new BasicStroke(1));
		
		for (int i = 0, xx = x, yy = y - height / 4 + 5; i < 5; yy += 23, i++) {
			Color c2 = c.brighter();
			if (camelHighlight != i)
				c2 = c2.brighter();
			CImage.drawImage(gg, "camel", xx, yy, 0, 46, c2, false);
		}
		
		int i = 0;
		g.translate(-width / 2, -height / 2);
		
		Point2D mouse = (Point2D) GraphicsHandler.getMouseLoc().clone();
		mouse.setLocation(mouse.getX() - x + width / 2, mouse.getY() - y + height / 2);
		float alpha = 1f;
		if (mouse.getX() < width && mouse.getX() > 0 && mouse.getY() < height && mouse.getY() > 0)
			alpha = .15f;
		for (RaceBetTile rbt : bets) {
			rbt.drawRaceBetTile(g, 0, i * 8 + 10, width, height, false, alpha);
			i++;
		}
	}
}
