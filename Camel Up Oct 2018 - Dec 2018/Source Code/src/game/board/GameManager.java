package game.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeMap;

import game.input.InputHandler;
import game.player.Player;
import game.tiles.DesertTile;
import game.tiles.LegBetTile;
import game.tiles.LegBetTileStack;
import game.tiles.PyramidTile;
import game.tiles.RaceBetTile;

public class GameManager implements Runnable {
	
	// Players
	private int numPlayers;
	private Player[] players;
	
	// GameBoard
	private GameBoard board;
	// Race
	private Race race;
	
	// Game Info
	private int currentPlayer;
	private boolean gameRunning;
	private boolean displayCurrentPlayersRaceBets;
	
	// Input Handler
	private InputHandler input;
	
	public static boolean usingGraphics;
	
	private TreeMap<String, Object> GraphicsInfo;
	
	public GameManager(int numPlayers, InputHandler input) {
		this.numPlayers = numPlayers;
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++)
			players[i] = new Player();
		
		this.input = input;
		
		race = new Race();
		board = new GameBoard(race, players);
		GraphicsInfo = new TreeMap<String, Object>();
	}
	
	public GameBoard getGameBoard() {
		return board;
	}
	public Race getRace() {
		return race;
	}
	
	public Player getCurrentPlayer() {
		return players[currentPlayer];
	}
	public Player getPlayerByID(int playerID) {
		for (int i = 0; i < players.length; i++)
			if (players[i].getID() == playerID)
				return players[i];
		return null;
	}
	
	public void displayBoardInfo() {
		race.displayRace();
		board.displayBoardInfo(players);
	}
	
	public boolean isGameRunning() {
		return gameRunning;
	}
	
	public void start() {
		gameRunning = true;
		(new Thread(this)).start();
	}
	
	@Override
	public void run() {
		int startingPlayer = 0;
		
		while (gameRunning) {
			boolean reachedEndOfLeg = false;
			// Player turns
			for (int cP = startingPlayer; !reachedEndOfLeg && gameRunning; cP = (cP + 1) % numPlayers) {
				currentPlayer = cP;
				
				if (!usingGraphics)
					displayBoardInfo();
				
				input.displayPlayerStartTurn(this);
				displayCurrentPlayersRaceBets = true;
				int turnType = input.requestTypeOfTurn(this);
				if (turnType == 1) {
					LegBetTile legBetTile = input.requestLegBettingTile(this);
					
					// Take tile off the top of its stack
					LegBetTileStack[] legBetStacks = getGameBoard().getLegBetsStacks();
					for (int stack = 0; stack < legBetStacks.length; stack++) {
						LegBetTile top = legBetStacks[stack].seeTop();
						if (top == null)
							continue;
						if (top == legBetTile) {
							legBetTile = legBetStacks[stack].takeOffTheTop();
							players[cP].addLegBetTile(legBetTile);
							break;
						}
					}
				} else if (turnType == 2) {
					Object[] turnOptions = input.requestDesertTilePlacement(this);
					int tileNumber = (int)(turnOptions[0]);
					boolean oasisUp = (boolean)(turnOptions[1]);
					players[cP].getDesertTile().setProperties(true, oasisUp, tileNumber);
				} else if (turnType == 3) {
					ArrayList<PyramidTile> pyrTiles = board.getPyrTiles();
					if (pyrTiles.size() == 0) 
						continue;
					players[cP].addPyrTile(pyrTiles.remove(pyrTiles.size() - 1));
					PyramidDie die = board.getPyramid().getRandomDie();
					int roll = die.rollDie();
					
					int newPosition = race.moveCamelForward(race.getCamelByID(die.getCamelID()), roll);
					
					// Need to check if any camels are on top of desert tiles
					ArrayList<DesertTile> dst = Player.getAllDesertTiles();
					for (int i = 0; i < dst.size(); i++) {
						DesertTile dt = dst.get(i);
						if (dt.isOnBoard()) 
							if (dt.getTileNumber() == newPosition) {
								if (dt.isOasisUp()) 
									race.moveCamelForward(race.getCamelByID(die.getCamelID()), 1);
								else 
									race.moveCamelBackward(race.getCamelByID(die.getCamelID()), 1);
								getPlayerByID(dt.getPlayerID()).changeCapital(1);
							}
					}
					
					reachedEndOfLeg = board.getPyramid().isInsideEmpty();
					if (race.isRaceDone())
						gameRunning = false;
				} else if (turnType == 4) {
					Object[] turnOptions = input.requestRaceBetType(this);
					RaceBetTile raceBet = (RaceBetTile)(turnOptions[0]);
					boolean placeInWinnerStack = (boolean)(turnOptions[1]); 
					players[cP].getRaceBets().remove(raceBet);
					if (placeInWinnerStack)
						board.addToWinnerBets(raceBet);
					else
						board.addToLoserBet(raceBet);
				}
				
				displayCurrentPlayersRaceBets = false;
			}
			
			if (reachedEndOfLeg || !gameRunning) {
				startingPlayer = (currentPlayer + 1) % numPlayers;
				
				if (gameRunning) {
					input.displayRoundEnd(this);
				} else {
					
				}
				
				int[] amountChange = new int[numPlayers];
				
				// Leg Bets
				ArrayList<Camel> order = race.getCamelsInOrder();
				
				for (int i = 0; i < numPlayers; i++) {
					ArrayList<LegBetTile> legBets = players[i].getLegBets();
					for (int j = 0; j < legBets.size(); j++) {
						int index = order.indexOf(race.getCamelByID(legBets.get(j).getCamelID()));
						if (index == order.size() - 1) 
							amountChange[i] += legBets.get(j).getValue();
						else if (index == order.size() - 2)
							amountChange[i]++;
						else
							amountChange[i]--;
					}
				}
				
				// Pyramid Tiles
				for (int i = 0; i < numPlayers; i++) 
					amountChange[i] += players[i].getPyrTiles().size();
				
				// Applying Changes
				for (int i = 0; i < numPlayers; i++) 
					players[i].changeCapital(amountChange[i]);
				
				// Reset
				board.reset();
				for (int i = 0; i < numPlayers; i++) 
					players[i].reset();
			}
		}
	}
	
	public void drawGame(Graphics2D g) {
		board.drawBoard(g);
		drawPlayerInfo(g);
	}
	public void drawPlayerInfo(Graphics2D gg) {
		Graphics2D g = (Graphics2D) gg.create();
		
		int xx = GameBoard.plyAreaCX - GameBoard.plyAreaWidth / 2;
		int yy = GameBoard.plyAreaCY - GameBoard.plyAreaHeight / 2;
		int cx = GameBoard.plyAreaCX, cy = yy + 25;
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString("Player Info", cx - g.getFontMetrics().stringWidth("Player Info") / 2, cy - g.getFontMetrics().getHeight() / 2 + g.getFontMetrics().getAscent());
		g.setStroke(new BasicStroke(5));
		g.drawLine(xx, yy + 50, xx + GameBoard.plyAreaWidth, yy + 50);
		g.setStroke(new BasicStroke(1));
		
		int width = GameBoard.plyAreaWidth, height = GameBoard.plyAreaHeight - 50;
		int plyHeight = height / players.length;
		
		Rectangle2D[][] raceBetLocations = new Rectangle2D[players.length][];
		
		
		for (int i = 0, aX = xx, aY = yy + 50; i < players.length; i++, aY += plyHeight) {
			g.setFont(new Font("Arial", Font.BOLD, 25));
			FontMetrics met = g.getFontMetrics();
			g.setColor(Color.orange);
			g.fillRect(aX, aY, met.stringWidth("Player 0") + 20, plyHeight / 2);
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(5));
			g.drawRect(aX, aY, met.stringWidth("Player 0") + 20, plyHeight / 2);
			g.drawLine(aX, aY, aX + GameBoard.plyAreaWidth, aY);
			g.setStroke(new BasicStroke(1));
			g.drawString("Player " + (i + 1), aX + 10, aY + plyHeight / 4 - met.getHeight() / 2 + met.getAscent());
			
			String cap = players[i].getCapital() + " £'s";
			g.drawString(cap, aX + met.stringWidth("Player 0") + 30, aY + plyHeight / 4 - met.getHeight() / 2 + met.getAscent());
			
			String raceB = "Race Bets";
			g.setColor(Color.cyan);
			if (i == players.length - 1) {
				g.fillRect(aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY, 40, plyHeight);
				g.fillRect(aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY, 20 + met.stringWidth(raceB) - 2, 40);
				g.fillRoundRect(aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY, 20 + met.stringWidth(raceB) - 2, plyHeight, 42, 42);
			}
			else
				g.fillRect(aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY, 20 + met.stringWidth(raceB) - 2, plyHeight);
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(5));
			g.drawLine(aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY, aX + GameBoard.btnAreaWidth, aY);
			g.drawLine(aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY, aX + GameBoard.plyAreaWidth - 20 - met.stringWidth(raceB), aY + plyHeight);
			g.drawString(raceB, aX + GameBoard.plyAreaWidth - 10 - met.stringWidth(raceB), aY + plyHeight / 4 - met.getHeight() / 2 + met.getAscent());
			g.setStroke(new BasicStroke(1));
			
			ArrayList<RaceBetTile> raceBets = players[i].getRaceBets();
			raceBetLocations[i] = new Rectangle2D[raceBets.size()];
			int raceBetWidth = 20, raceBetHeight = 30;
			for (int j = 0, bX = aX + GameBoard.plyAreaWidth - raceBetWidth - 10, bY = aY + 2 * plyHeight / 3; j < raceBets.size(); j++, bX -= raceBetWidth + 5) { 
				raceBetLocations[i][j] = new Rectangle2D.Double(bX, bY - raceBetHeight / 2, raceBetWidth, raceBetHeight);
				raceBets.get(j).drawRaceBetTile(g, bX, bY - raceBetHeight / 2, raceBetWidth, raceBetHeight, displayCurrentPlayersRaceBets && i == currentPlayer, 1f);
			}
			
			g.setFont(new Font("Arial", Font.BOLD, 15));
			met = g.getFontMetrics();
			String legB = "Leg";
			g.drawString(legB, aX + 25 - met.stringWidth(legB) / 2, aY + 3 * plyHeight / 4 - met.getHeight() / 2 + met.getAscent() - 10);
			legB = "Bets";
			g.drawString(legB, aX + 25 - met.stringWidth(legB) / 2, aY + 3 * plyHeight / 4 - met.getHeight() / 2 + met.getAscent() + 10);
			
			ArrayList<LegBetTile> legBets = players[i].getLegBets();
			int cardWidth = 30, cardHeight = 15;
			for (int j = 0, dX = aX + 20 + met.stringWidth(legB), dY = aY + 3 * plyHeight / 4; j < legBets.size(); j++, dX += cardWidth + 10) {
				legBets.get(j).drawSmallLegBetTile(g, dX, dY - cardHeight, cardWidth, cardHeight, 8);
				j++;
				if (j < legBets.size())
					legBets.get(j).drawSmallLegBetTile(g, dX, dY + 2, cardWidth, cardHeight, 8);
			}
			
			g.setStroke(new BasicStroke(5));
			g.drawLine(aX, aY, aX + GameBoard.plyAreaWidth, aY);
			g.setStroke(new BasicStroke(1));
			
			int pyrTiles = players[i].getPyrTiles().size();
			int kx = aX + GameBoard.plyAreaWidth / 2, ky = aY + 5;
			PyramidTile.drawPyramidTile(g, kx, ky, 20, 30);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString(" = " + pyrTiles, kx + 25, ky + 25);
		}
		
		GraphicsInfo.put("RaceBetLocations", raceBetLocations);
	}
	
	public TreeMap<String, Object> getGraphicsInfo() {
		return GraphicsInfo;
	}
	
	public Player[] getPlayers() {
		return players;
	}
}
