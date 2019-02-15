 package coltexpress.game;

import static coltexpress.game.PlayerInfo.drawLootBoard;
import static coltexpress.game.PlayerInfo.getRandomPlayers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.TreeMap;

import javax.swing.JButton;

import coltexpress.card.PlayerCard;
import coltexpress.card.RoundCard;
import coltexpress.loot.Loot;
import coltexpress.main.Runner;
import coltexpress.player.Marshall;
import coltexpress.player.Player;
import coltexpress.round.RoundPart;
import coltexpress.train.Train;
import coltexpress.train.TrainCar;
import coltexpress.ui.CharacterButton;
import coltexpress.ui.cButton;

public class GameManager implements Runnable {
	
	private static boolean hotSeatMode = true;
	
	// Ending Phase
	public boolean gameDone = false;
	public int animIDWinner;
	public int lootGained;
	
	// Thread
	private Thread thread;
	private boolean running, ready;
	
	// Train
	public Train tr;
	
	// Players
	public static final int NUM_PLAYERS = 4;
	public Player[] plys;
	
	// Marshall
	public Marshall marshall = new Marshall(this);
	
	// Schemin Phase
	private ArrayList<PlayerCard> plan;
	private boolean buttonClicked;
	private JButton clickedButton;
	public int currentPlayer;
	public int currentSchemeCard;
	public int currentCard; // For the stealing phase
	
	// Round Card
	private RoundCard rc;
	private RoundPart cRP;
	
	// Buttons
	private ArrayList<cButton> playerMoves;
	
	// Loot
	public ArrayList<Loot> allLoot;
	
	// Waiting Button
	private cButton waitingButton = new cButton(340, 250, "NextPlayer");
	private boolean waitingForNextPlayer;
	
	// Current Phase
	public boolean inStealinPhase;
	public boolean inDiscardMode;
	
	public GameManager() throws IOException {
		Player.gm = this;
		
		tr = new Train(this, NUM_PLAYERS + 1);
		plys = getRandomPlayers();//new Player[NUM_PLAYERS];
//		plys[0] = new Belle(NUM_PLAYERS, 0);
//		plys[1] = new Cheyenne(NUM_PLAYERS, 1);
//		plys[2] = new Django(NUM_PLAYERS, 2);
//		plys[3] = new Doc(NUM_PLAYERS, 3);
//		for (int i = 0, x = 770, y = 550; i < plys.length; i++, x += 23) {
//			tr.addPlayerToTrainCar(NUM_PLAYERS, plys[i]);
//		}
		
		running = true;
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
			thread.setName("Game Manager Thread");
		}
		
		playerMoves = new ArrayList<>();
		
		waitingButton.setBounds(300, 200, 100, 30);
		
		ready = true;
	}
	
	public void draw(Graphics g) throws ConcurrentModificationException {
		drawButtons(g);
		
		drawPlayers(g);
		drawCardIcon(g);
		drawLastCard(g);
		drawRoundRules(g);
		drawLootBoard(g, allLoot);
	}
	
	private void drawRoundRules(Graphics g) {
		if (inStealinPhase || inDiscardMode)
			return;
		g.setColor(Background.inATunnel ? Color.white : Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		
		if (cRP == null)
			return;
		
		int x = 500, y = 700;
		if (cRP.counterClockwise) {
			String s = "Switching!";
			int xx = x - g.getFontMetrics().stringWidth(s) / 2;
			int yy = y + g.getFontMetrics().getHeight() / 2;
			g.drawString(s, xx, yy);
			y += 30;
		}
		if (cRP.speedUp) {
			String s = "Speeding Up!";
			int xx = x - g.getFontMetrics().stringWidth(s) / 2;
			int yy = y + g.getFontMetrics().getHeight() / 2;
			g.drawString(s, xx, yy);
		}
	}
	private void drawLastCard(Graphics g) {
		if (inStealinPhase || inDiscardMode) return;
		if (currentSchemeCard == 0) return;
		if (plan.get(currentSchemeCard - 1).type == 0) return;
		
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Background.inATunnel ? Color.white : Color.black);
		g.drawString("   The Last", 20, 680);
		g.drawString("Card Placed", 20, 700);
		g.drawLine(15, 705, 138, 705);
		if (plan.get(currentSchemeCard - 1).faceUp) {
			BufferedImage img = PlayerCard.cardIcons[plan.get(currentSchemeCard - 1).type];
			g.drawImage(img, 80 - img.getWidth() / 2, 740 - img.getHeight() / 2, null);
		}
	}
	private void drawCardIcon(Graphics g) {
		if (!inStealinPhase || inDiscardMode) return;
		if (plan.get(currentCard).type == 0) return;
		g.drawImage(PlayerCard.cardIcons[plan.get(currentCard).type], 920, 620, null);
	}
	private void drawButtons(Graphics g) {
		if (!inStealinPhase && !waitingForNextPlayer && !inDiscardMode) {
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.setColor(Background.inATunnel ? Color.white : Color.black);
			g.drawString("Your Cards:", 100, 120);
			g.fillRect(100, 125, 175, 3);
		} else if (inDiscardMode && !waitingForNextPlayer) {
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.setColor(Background.inATunnel ? Color.white : Color.black);
			g.drawString("Discard Mode:", 100, 120);
			g.fillRect(100, 125, 210, 3);
		}
		boolean gotAllButtons = false;
		while (!gotAllButtons) {
			try {
				for (cButton cb : playerMoves)
					cb.paintComponent(g);
				gotAllButtons = true;
			} catch (ConcurrentModificationException e) {}
		}
		
		gotAllButtons = false;
		while (!gotAllButtons) {
			try {
				for (cButton cb : Player.planButtons)
					cb.paintComponent(g);
				gotAllButtons = true;
			} catch (ConcurrentModificationException e) {}
		}
		gotAllButtons = false;
		while (!gotAllButtons) {
			try {
				for (CharacterButton cb : Player.chsButtons)
					cb.paintComponent(g);
				gotAllButtons = true;
			} catch (ConcurrentModificationException e) {}
		}
		
		if (waitingForNextPlayer) {
			waitingButton.paintComponent(g);
		}
	}
	private void drawPlayers(Graphics g) {
		int[][] xForEachTrain = new int[Train.NUM_TRAIN_CARS][2];
		for (int i = 0; i < plys.length; i++) {
			int x = tr.train[plys[i].currentTrainCar].pos.x + 70 + xForEachTrain[plys[i].currentTrainCar][plys[i].onTopOfTrain ? 0 : 1] + (plys[i].currentTrainCar == 0 ? 50 : 0);
			xForEachTrain[plys[i].currentTrainCar][plys[i].onTopOfTrain ? 0 : 1] += (plys[i].currentTrainCar == 0 ? 10 : 23);
			int y = plys[i].onTopOfTrain ? 480 : 548 + (plys[i].currentTrainCar == 0 ? 20 : 0);
			plys[i].draw(g, x, y);
		}
		
		marshall.draw(g, tr.train[marshall.currentTrainCar].pos.x + 70 + xForEachTrain[0][1] + (marshall.currentTrainCar == 0 ? 50 : 0), 548 + (marshall.currentTrainCar == 0 ? 20 : 0));
	}
	
	private void initNewRound() {
		ArrayList<Integer> randOrder = new ArrayList<>();
		for (int i = 0; i < RoundCard.cardUsed.length; i++)
			if (!RoundCard.cardUsed[i])
				randOrder.add(i);
		Collections.shuffle(randOrder);
		rc = new RoundCard(randOrder.get(0));
		RoundCard.cardUsed[rc.cardNum] = true;
		cRP = rc.getFirstRoundPart();
		
		if (plan != null) {
			returnPlayerCards();
		}
		plan = new ArrayList<PlayerCard>();
		
		for (int i = 0; i < plys.length; i++) {
			plys[i].resetHand();
		}
	}
	private void playerDiscard(int pI) {
		boolean inDiscardMode = true;
		
		while (inDiscardMode) {
			for (int i = 0, x = -60, y = 150; i < plys[pI].getAvailableActions().size(); i++, y += 55) {
				if (i % 3 == 0) {
					x += 160;
					y = 150;
				}
				String cardName = plys[pI].getAvailableActions().get(i).getCardName();
				playerMoves.add(0, new cButton(x, y, cardName));
				playerMoves.get(0).setDimensions(150, 50);
				playerMoves.get(0).isBulletCard = (cardName.contains("bullet"));
			}
			cButton done = new cButton(320, 90, "Done");
			playerMoves.add(done);

			int timeToClick = 1000000;
			while (!buttonClicked) {
				timeToClick--;
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}
			buttonClicked = false;
			
			if (clickedButton == done) {
				inDiscardMode = false;
			} else {
				plys[pI].discard.add(plys[pI].removeCardFromOnHand(playerMoves.size() - 2 - playerMoves.indexOf(clickedButton)));
			}
			playerMoves.clear();
		}
		waitingForNextPlayer = hotSeatMode;
	}
	
	public void addMouseListenerToPanel(GamePanel gp) {
		gp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (waitingForNextPlayer && waitingButton.contains(e.getPoint()))
					waitingButton.pressed = true;
				for (cButton cb : playerMoves)
					if (cb.contains(e.getPoint()))
						cb.pressed = true;
				for (cButton cb : Player.planButtons)
					if (cb.contains(e.getPoint()))
						cb.pressed = true;
				for (CharacterButton cb : Player.chsButtons)
					if (cb.contains(e.getPoint()))
						cb.pressed = true;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (waitingForNextPlayer && waitingButton.contains(e.getPoint())) {
					waitingButton.pressed = false;
					waitingForNextPlayer = false;
				}
				for (cButton cb : playerMoves) {
					if (cb.isBulletCard && !inDiscardMode) {
						continue;
					}
					if (cb.contains(e.getPoint())) {
						cb.pressed = false;
						buttonClicked = true;
						clickedButton = cb;
					}
				}
				for (cButton cb : Player.planButtons)
					if (cb.contains(e.getPoint())) {
						cb.pressed = false;
						buttonClicked = true;
						clickedButton = cb;
					}
				for (CharacterButton cb : Player.chsButtons)
					if (cb.contains(e.getPoint())) {
						cb.pressed = false;
						buttonClicked = true;
						clickedButton = cb;
					}
			}
		});
		
		gp.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (waitingForNextPlayer && !waitingButton.contains(e.getPoint()))
					waitingButton.pressed = false;
				for (cButton cb : playerMoves)
					if (!cb.contains(e.getPoint()))
						cb.pressed = false;
				for (cButton cb : Player.planButtons)
					if (!cb.contains(e.getPoint()))
						cb.pressed = false;
				for (CharacterButton cb : Player.chsButtons)
					if (!cb.contains(e.getPoint()))
						cb.pressed = false;
				
				for (TrainCar tc : tr.train) {
					tc.checkShowLoot(e.getPoint());
				}
				
				plys[currentPlayer].showingLoot = plys[currentPlayer].lootShowingBox.contains(e.getPoint());
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if (waitingForNextPlayer && !waitingButton.contains(e.getPoint()))
					waitingButton.pressed = false;
				for (cButton cb : playerMoves)
					if (!cb.contains(e.getPoint()))
						cb.pressed = false;
				for (cButton cb : Player.planButtons)
					if (!cb.contains(e.getPoint()))
						cb.pressed = false;
				for (CharacterButton cb : Player.chsButtons)
					if (!cb.contains(e.getPoint()))
						cb.pressed = false;
				
				for (TrainCar tc : tr.train) {
					tc.checkShowLoot(e.getPoint());
				}
				
				plys[currentPlayer].showingLoot = plys[currentPlayer].lootShowingBox.contains(e.getPoint());
			}
		});
	}
	
	@Override
	public void run() {
		
		while (!ready) {
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		
		int numRoundsPlayed = 0;
		int startingPlayer = 0;
		
		while (numRoundsPlayed < 5) {
			// This Loop used for Game stuff, Animation loop is in the GamePanel
			
			// Picking a new Round Card
			initNewRound();
			
			// Schemin Phase!
			
			while (cRP != null) {
				int inc = cRP.counterClockwise ? -1 : 1;
				int i = 0;
				for (currentPlayer = startingPlayer; i < NUM_PLAYERS; currentPlayer+=inc, i++) {
					if (currentPlayer == -1)
						currentPlayer = NUM_PLAYERS - 1;
					if (currentPlayer == NUM_PLAYERS)
						currentPlayer = 0;
					runPlayerTurn(currentPlayer);
					if (cRP.speedUp) {
						waitingForNextPlayer = false;
						runPlayerTurn(currentPlayer);
					}
					while (waitingForNextPlayer) {
						try { Thread.sleep(10); } catch (Exception e) {}
					}
				}
				currentPlayer = 0;
				i = 0;
				// This is set to null when reached end of round
				cRP = rc.nextRoundPart();
			}
			
			
			// Stealin Phase
			
			runStealinPhase();
//			System.out.println("Round: " + numRoundsPlayed);
//			System.out.println(plan);
			
			returnPlayerCards();
			
			inDiscardMode = true;
			if (numRoundsPlayed != 4) {
				for (currentPlayer = 0; currentPlayer < plys.length; currentPlayer++) {
					playerDiscard(currentPlayer);
					while (waitingForNextPlayer) {
						try { Thread.sleep(10); } catch (Exception e) {}
					}
				}
			}
			inDiscardMode = false;
			
			numRoundsPlayed++;
			startingPlayer = (startingPlayer + 1) % NUM_PLAYERS;
			
//			if (gameDone)
//				break;
		}
		
		
		
		// End phase
		TreeMap<Integer, Integer> animToID = new TreeMap<>();
		for (int i = 0; i < plys.length; i++) {
			animToID.put(plys[i].animPlayerID, i);
		}
		int[] plysLoot = new int[4];
		for (Loot l : allLoot) {
			if (l.inTrainCar)
				continue;
			plysLoot[animToID.get(l.animPlayerID)] += l.getLootValue();
		}
		int maxLootIndex = 0;
		for (int i = 0; i < plysLoot.length; i++)
			if (plysLoot[i] > plysLoot[maxLootIndex]) {
				maxLootIndex = i;
			}
		
		try { Thread.sleep(1000); } catch (Exception e) {}
		animIDWinner = plys[maxLootIndex].animPlayerID;
		lootGained = plysLoot[maxLootIndex];
		gameDone = true;
	}
	
	
	private void runPlayerTurn(int pI) {
		for (int i = 0, x = -60, y = 150; i < plys[pI].getAvailableActions().size(); i++, y += 55) {
			if (i % 3 == 0) {
				x += 160;
				y = 150;
			}
			String cardName = plys[pI].getAvailableActions().get(i).getCardName();
			playerMoves.add(0, new cButton(x, y, cardName));
			playerMoves.get(0).setDimensions(150, 50);
			playerMoves.get(0).isBulletCard = (cardName.contains("bullet"));
		}
		cButton draw = new cButton(280, 90, "Draw");
		playerMoves.add(draw);
		
		int timeToClick = 1000000;
		while (!buttonClicked) {
			timeToClick--;
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		buttonClicked = false;
		
		if (clickedButton == draw) {
			plys[pI].draw3Cards();
		} else {
			PlayerCard temp = plys[pI].removeCardFromOnHand(playerMoves.size() - 2 - playerMoves.indexOf(clickedButton)); 
			temp.playerID = pI;
			temp.faceUp = cRP.faceUp;
			// Ghost!!!!!
			if (plys[pI].animPlayerID == 4 && rc.currentRoundPart == 0) {
				temp.faceUp = false;
			}
			if (Runner.debugMode)
				System.out.println(pI + " Chose " + temp.getCardName());
			plan.add(temp);
			currentSchemeCard = plan.size();
		}
		playerMoves.clear();
		waitingForNextPlayer = hotSeatMode;
	}
	
	private void returnPlayerCards() {
		for (int i = 0; i < plan.size(); i++) {
			int plyID = plan.get(i).playerID;
			plys[plyID].discard.add(plan.get(i));
		}
		plan.clear();
	}
	
	private void runStealinPhase() {
		currentSchemeCard = 0;
		
		inStealinPhase = true;
		
		boolean goToNextCard = true;
		for (int i = 0; i < plan.size(); ) {
			currentPlayer = plan.get(i).playerID;
			if (goToNextCard) {
				currentCard = i;
				plys[plan.get(i).playerID].runPlayerCard(plan.get(i), this);
			}
			
			
			
			// Very stupid fix for small problem ;)
			boolean goThrough = false;
			boolean skipButtonClick = false;
			if (plys[currentPlayer].ActionDoing.equals("punch3") && Player.planButtons.size() == 0) {
				goThrough = true;
				skipButtonClick = true;
			}
			
			
			if (!Player.chsButtons.isEmpty() || !Player.planButtons.isEmpty() || goThrough) {
				int timeToClick = 1000000;
				while (!buttonClicked && !skipButtonClick) {
//					System.out.println("WAITING FOR A BUTTON TO BE CLICKED");
					timeToClick--;
					try { Thread.sleep(10); } catch (Exception e) {}
				}
				buttonClicked = false;
				
				
				Player.planButtons.clear();
				Player.chsButtons.clear();
				
				goToNextCard = plys[plan.get(i).playerID].thisIsWhereTheShitHappens(clickedButton, this);
			}
			
			for (int j = 0; j < plys.length; j++) {
				plys[j].checkSheriff();
				plys[j].showingLoot = false;
			}
			i+=goToNextCard?1:0;
			
			if (goToNextCard) {
				waitingForNextPlayer = hotSeatMode;
				while (waitingForNextPlayer) {
					try { Thread.sleep(10); } catch (Exception e) {}
				}
			}
		}
		
		inStealinPhase = false;
	}
	
	
	public ArrayList<Integer> getAllPlayersInTrainCar(int trainCarID) {
		ArrayList<Integer> ret = new ArrayList<>();
		for (Player p : plys) {
			if (p.currentTrainCar == trainCarID) {
				ret.add(p.playerID);
			}
		}
		return ret;
	}
	
	public ArrayList<Integer> getAllPlayerOnTrainForShoot(int trainCarID) {
		ArrayList<Integer> ret = new ArrayList<>();
		
		for (int i = trainCarID + 1; i < Train.NUM_TRAIN_CARS; i++) {
			boolean hasPlayers = false;
			for (Player p : plys) {
				if (p.currentTrainCar == i) {
					ret.add(p.playerID);
					hasPlayers = true;
				}
			}
			if (hasPlayers)
				break;
		}
		for (int i = trainCarID - 1; i >= 0; i--) {
			boolean hasPlayers = false;
			for (Player p : plys) {
				if (p.currentTrainCar == i) {
					ret.add(p.playerID);
					hasPlayers = true;
				}
			}
			if (hasPlayers)
				break;
		}
		if (Runner.debugMode) {
			for (int i : ret)
				System.out.print(plys[i]);
			System.out.println();
		}
		return ret;
	}
	
	public void dispose() {
		thread.interrupt();
	}
}
