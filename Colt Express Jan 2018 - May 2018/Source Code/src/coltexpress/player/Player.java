package coltexpress.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;

import coltexpress.audio.AudioManager;
import coltexpress.card.PlayerCard;
import coltexpress.game.Background;
import coltexpress.game.GameManager;
import coltexpress.loot.Loot;
import coltexpress.main.Runner;
import coltexpress.train.Train;
import coltexpress.ui.CharacterButton;
import coltexpress.ui.cButton;

public class Player {
	// ArrayList of PlayerCards
	// For onHand, Deck, Discard, Bullets
	public ArrayList<PlayerCard> bullets, deck, discard, onHand;
	protected int maxNumCardsOnHand = 6; // Needed bcuz of Doc
	
	// GAMEMANAGER
	public static GameManager gm;
	
	// 
	// Using protected because we want all  
	// Data to be accessible by subclasses
	// But not accessible globally
	// 
	
	// Train Car
	public int currentTrainCar;
	public boolean onTopOfTrain = false;
	
	// ID
	public static String[] names = {"Bell", "Cheyenne", "Django", "Doc", "Ghost", "Tuco"};
	public static Color[] playerColors = {new Color(163, 73, 164), new Color(20, 200, 11), new Color(0, 0, 0), new Color(94, 206, 255), new Color(195, 195, 195), new Color(237, 28, 36)};
	public static Color[] contrastColors = {Color.white, Color.white, Color.white, Color.white, Color.black, Color.white};
	public Color playerColor;
	public Color contrastColor;
	public int playerID, animPlayerID;
	
	// Animations
	public Animator animator;
	
	// Buttons added by planning phase
	public static ArrayList<CharacterButton> chsButtons = new ArrayList<>();
	public static ArrayList<cButton> planButtons = new ArrayList<>();
	
	// Actions 
	public String ActionDoing = "";
	
	// Showing Loot
	public boolean showingLoot;
	public Rectangle lootShowingBox = new Rectangle(900, 700, 100, 100);
	
	// EXTRA DETAILS FOR ACTIONS THAT NEED MULTIPLE INPUTS
	public CharacterButton punchedPerson;
	public cButton dirPunched;
	
	public Player(int ctc, int playerID, int animPlayerID) {
		bullets = new ArrayList<>();
		deck = new ArrayList<>();
		discard = new ArrayList<>();
		onHand = new ArrayList<>();
		
		for (int i = 0; i < 6; i++)
			bullets.add(new PlayerCard(0, playerID));
		int[] numEach = {2, 2, 2, 2, 1, 1};
		for (int i = 0; i < numEach.length; i++) 
			for (int j = 0; j < numEach[i]; j++)
				deck.add(new PlayerCard(i + 1, playerID));
		while (onHand.size() < maxNumCardsOnHand) {
			onHand.add(deck.remove((int)(Math.random() * deck.size())));
		}
		currentTrainCar = ctc;
		
		this.playerID = playerID;
//		System.out.println(playerID);
		
		
		animator = new Animator(animPlayerID);
		playerColor = playerColors[animPlayerID];
		contrastColor = contrastColors[animPlayerID];
		this.animPlayerID = animPlayerID;
		
	}
	
	// Draw 3 cards
	public void draw3Cards() {
		int numAddedToHand = 0;
		while (numAddedToHand < 3 && !deck.isEmpty()) {
			onHand.add(deck.remove((int)(Math.random() * deck.size())));
			numAddedToHand++;
		}
		if (numAddedToHand != 3) {
			deck.addAll(discard);
			discard.clear();
			Collections.shuffle(deck);
		}
		while (numAddedToHand < 3 && !deck.isEmpty()) {
			onHand.add(deck.remove((int)(Math.random() * deck.size())));
			numAddedToHand++;
		}
	}
	
	// Reseting OnHand
	public void resetHand() {
		while (onHand.size() < maxNumCardsOnHand) {
			if (deck.size() == 0) {
				for (PlayerCard pc : discard) {
					deck.add(pc);
				}
				discard.clear();
//				System.out.println(deck.size());
			}
			onHand.add(deck.remove((int)(Math.random() * deck.size())));
		}
	}
	
	public void runPlayerCard(PlayerCard pc, GameManager gm) {
		if (Runner.debugMode) 
			System.out.println(playerID + " Playing " + pc.getCardName());
		if (pc.getCardName().contains("fire")) {
			shoot(gm);
		} else if (pc.getCardName().contains("punch")) {
			punch(gm);
		} else if (pc.getCardName().contains("move")) {
			run(gm);
		} else if (pc.getCardName().contains("floor")) {
			climb(gm);
		} else if (pc.getCardName().contains("robbery")) {
			loot(gm);
		} else if (pc.getCardName().contains("marshal")) {
			sheriff(gm);
		}
	}
	
	// Actions
	protected void shoot(GameManager gm) {
		ArrayList<Integer> availablePlayers = null;
		if (onTopOfTrain) {
			availablePlayers = gm.getAllPlayerOnTrainForShoot(currentTrainCar);
		} else {
			availablePlayers = gm.getAllPlayersInTrainCar(currentTrainCar - 1);
			availablePlayers.addAll(gm.getAllPlayersInTrainCar(currentTrainCar + 1));
		}
		
		for (int i = availablePlayers.size() - 1; i >= 0; i--) {
			if (gm.plys[availablePlayers.get(i)].onTopOfTrain != onTopOfTrain) {
				availablePlayers.remove(i);
			}
		}
		
		// Belle
		if (availablePlayers.size() > 1) {
			for (int i = availablePlayers.size() - 1; i >= 0; i--) {
				if (gm.plys[availablePlayers.get(i)].animPlayerID == 0) {
					availablePlayers.remove(i);
				}
			}
		}
		
		if (!availablePlayers.isEmpty()) {
			int xx = 300, yy = 150;
			int width = 100, height = 100;
			for (int id : availablePlayers) {
				if (id == playerID)
					continue;
				int animID = gm.plys[id].animPlayerID;
				chsButtons.add(new CharacterButton(xx, yy, animID));
				chsButtons.get(chsButtons.size() - 1).setDimensions(width, height);
				xx += width + 10;
			}
		}
		ActionDoing = "shoot";
	}
	
	protected void punch(GameManager gm) {
		ArrayList<Integer> availablePlayers = gm.getAllPlayersInTrainCar(currentTrainCar);
		
		for (int i = availablePlayers.size() - 1; i >= 0; i--) {
			if (gm.plys[availablePlayers.get(i)].onTopOfTrain != onTopOfTrain) {
				availablePlayers.remove(i);
			}
		}
		
		// Belle
		if (availablePlayers.size() > 1) {
			for (int i = availablePlayers.size() - 1; i >= 0; i--) {
				if (gm.plys[availablePlayers.get(i)].animPlayerID == 0) {
					availablePlayers.remove(i);
					break;
				}
			}
		}
		
		if (!availablePlayers.isEmpty()) {
			int xx = 300, yy = 150;
			int width = 100, height = 100;
			for (int id : availablePlayers) {
				if (id == playerID)
					continue;
				int animID = gm.plys[id].animPlayerID;
				chsButtons.add(new CharacterButton(xx, yy, animID));
				chsButtons.get(chsButtons.size() - 1).setDimensions(width, height);
				xx += width + 10;
			}
		}
		ActionDoing = "punch";
	}
	
	protected void run(GameManager gm) {
		int minTrainCar = Math.max(currentTrainCar - (onTopOfTrain ? 3 : 1), 0);
		int maxTrainCar = Math.min(currentTrainCar + (onTopOfTrain ? 3 : 1), Train.NUM_TRAIN_CARS - 1);
		for (int trID = minTrainCar; trID <= maxTrainCar; trID++) {
			if (trID == currentTrainCar)
				continue;
			planButtons.add(new cButton(gm.tr.train[trID].pos.x + 75, gm.tr.train[currentTrainCar].pos.y - 150, "DownArrow"));
			planButtons.get(planButtons.size() - 1).setDimensions(50, 100);
			planButtons.get(planButtons.size() - 1).trainPointingTo = trID;
		}
		ActionDoing = "run";
	}
	
	protected void climb(GameManager gm) {
		animator.animating = true;
		animator.nextPoint.y += onTopOfTrain ? 50 : -50;
		animator.vector2 = new Point(0, onTopOfTrain ? 1 : -1);
		animator.currentAnimation = 5;
		animator.flipped = false;
		
		while (animator.animating) {
//			System.out.println(animator.origPoint + " " + animator.nextPoint);
			if (Math.abs(animator.origPoint.y - animator.nextPoint.y) < 5) {
				animator.animating = false;
			}
//			
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		onTopOfTrain = !onTopOfTrain;
	}
	
	protected void loot(GameManager gm) {
		String[] cButtonNames = {"lootbox.png", "ruby.png", "LootBag.png"};
		int[] numEachInCar = new int[3];
		for (Loot l : gm.allLoot) {
			if ((l.trainCarID == currentTrainCar) && l.inTrainCar && (l.onTopOfTrain == onTopOfTrain)) {
				numEachInCar[l.getRowNum()]++;
			}
		}
		
		int xx = 340;
		int yy = 100;
		for (int type = 0; type < 3; type++) {
			for (int i = 0; i < numEachInCar[type]; i++) {
				planButtons.add(new cButton(xx, yy, cButtonNames[type]));
				planButtons.get(planButtons.size() - 1).setDimensions(50, 50);
				planButtons.get(planButtons.size() - 1).lootType = type;
				xx += 55;
			}
		}
		ActionDoing = "loot";
	}
	
	protected void sheriff(GameManager gm) {
		if (gm.marshall.currentTrainCar != 0) {
			planButtons.add(new cButton(gm.tr.train[gm.marshall.currentTrainCar - 1].pos.x + 75, gm.tr.train[currentTrainCar].pos.y - 150, "DownArrow"));
			planButtons.get(planButtons.size() - 1).setDimensions(50, 100);
			planButtons.get(planButtons.size() - 1).trainPointingTo = gm.marshall.currentTrainCar - 1;
		}
		if (gm.marshall.currentTrainCar < Train.NUM_TRAIN_CARS - 1) {
			planButtons.add(new cButton(gm.tr.train[gm.marshall.currentTrainCar + 1].pos.x + 75, gm.tr.train[currentTrainCar].pos.y - 150, "DownArrow"));
			planButtons.get(planButtons.size() - 1).setDimensions(50, 100);
			planButtons.get(planButtons.size() - 1).trainPointingTo = gm.marshall.currentTrainCar + 1;
		}
		ActionDoing = "sheriff";
	}
	
	// Do stuff with the Button Clicked
	public boolean thisIsWhereTheShitHappens(JButton pickedButton, GameManager gm) {
//		System.out.println(ActionDoing);
		if (ActionDoing.equals("shoot")) {
			shoot2((CharacterButton)pickedButton, gm);
		} else if (ActionDoing.equals("punch")) {
			punch2((CharacterButton)pickedButton, gm);
			return false;
		} else if (ActionDoing.equals("punch2")) {
			punch3((cButton)pickedButton, gm);
			return false;
		} else if (ActionDoing.equals("punch3")) {
			punch4((cButton)pickedButton, gm);
		} else if (ActionDoing.equals("run")) {
			run2((cButton)pickedButton, gm);
		} else if (ActionDoing.equals("loot")) {
			loot2((cButton)pickedButton, gm);
		} else if (ActionDoing.equals("sheriff")) {
			sheriff2((cButton)pickedButton, gm);
		}
		return true;
	}
	
	// Extra Actions:
	protected void shoot2(CharacterButton cb, GameManager gm) {
		int playerShot = -1;
		animator.animating = true;
		animator.currentAnimation = 4;
		animator.vector2 = new Point(0, 0);
		Animator.allAnimations[animator.playerAnimID][animator.currentAnimation].currentTime = 1;
		for (int i = 0; i < gm.plys.length; i++) {
			if (gm.plys[i].animPlayerID == cb.animButtonID) {
				playerShot = i;
			}
		}
		
		animator.flipped = (gm.plys[playerShot].animator.origPoint.x < animator.origPoint.x);
		
		AudioManager.playAudioNTimes("fire", 0.5, 4, 200);
		
		while (animator.animating) {
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		gm.plys[playerShot].deck.add(0, bullets.remove(0));
	}
	protected void punch2(CharacterButton cb, GameManager gm) {
		punchedPerson = cb;
		
		int tr = currentTrainCar;
		
		if (tr != 0) {
			planButtons.add(new cButton(gm.tr.train[tr].pos.x + 30, gm.tr.train[tr].pos.y - 50, "LeftArrow"));
			planButtons.get(planButtons.size() - 1).setDimensions(80, 80);
			planButtons.get(planButtons.size() - 1).trainPointingTo = tr - 1;
		}
		if (tr != Train.NUM_TRAIN_CARS - 1) {
			planButtons.add(new cButton(gm.tr.train[tr].pos.x + 115, gm.tr.train[tr].pos.y - 50, "RightArrow"));
			planButtons.get(planButtons.size() - 1).setDimensions(80, 80);
			planButtons.get(planButtons.size() - 1).trainPointingTo = tr + 1;
		}
		
		ActionDoing = "punch2";
	}
	protected void punch3(cButton cb, GameManager gm) {
		dirPunched = cb;		
		
		String[] cButtonNames = {"lootbox.png", "ruby.png", "LootBag.png"};
		
		int xx = 340, yy = 100;
		for (Loot l : gm.allLoot) {
			if (!l.inTrainCar && l.animPlayerID == punchedPerson.animButtonID) {
				planButtons.add(new cButton(xx, yy, cButtonNames[l.getRowNum()]));
				planButtons.get(planButtons.size() - 1).setDimensions(50, 50);
				planButtons.get(planButtons.size() - 1).lootType = l.getRowNum();
				xx += 55;
			}
		}
//		System.out.println(planButtons.size() + " " + chsButtons.size());
		ActionDoing = "punch3";
	}
	protected void punch4(cButton lootPicked, GameManager gm) {
		animator.animating = true;
		animator.currentAnimation = 2;
		animator.vector2 = new Point(0, 0);
		Animator.allAnimations[animator.playerAnimID][animator.currentAnimation].currentTime = 1;
		int playerPunched = -1;
		for (int i = 0; i < gm.plys.length; i++) {
			if (gm.plys[i].animPlayerID == punchedPerson.animButtonID) {
				playerPunched = i;
			}
		}
		animator.flipped = (gm.plys[playerPunched].animator.origPoint.x < animator.origPoint.x);
		
		AudioManager.playAudioNTimes("punch", 0.5, 4, 200);
		
		while (animator.animating) {
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		gm.plys[playerPunched].currentTrainCar = dirPunched.trainPointingTo;
		
		for (int i = 0; i < gm.allLoot.size(); i++) {
			if (!gm.allLoot.get(i).inTrainCar && gm.allLoot.get(i).animPlayerID == punchedPerson.animButtonID && gm.allLoot.get(i).getRowNum() == lootPicked.lootType) {
				Loot l = gm.allLoot.get(i);
				l.inTrainCar = true;
				l.onTopOfTrain = gm.plys[playerPunched].onTopOfTrain;
				l.trainCarID = currentTrainCar;
				break;
			}
		}
		
		punchedPerson = null;
		dirPunched = null;
		ActionDoing = "punch4";
	}
	protected void run2(cButton cb, GameManager gm) {
		animator.animating = true;
		animator.nextPoint.x = cb.dim.x + (cb.trainPointingTo == 0 ? 50 : 0);
		animator.currentAnimation = 3;
		animator.flipped = cb.trainPointingTo < currentTrainCar;
		animator.vector2 = new Point(cb.trainPointingTo < currentTrainCar ? -1 : 1, 0);
		while (animator.animating) {
//			System.out.println(animator.origPoint + " " + animator.nextPoint);
			if (animator.origPoint.distance(animator.nextPoint) < 10) {
				animator.animating = false;
			}
//			
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		currentTrainCar = cb.trainPointingTo;
	}
	protected void loot2(cButton cb, GameManager gm) {
		animator.animating = true;
		animator.currentAnimation = 1;
		animator.flipped = false;
		
		AudioManager.playAudioNTimes("money", 0.5, 1, 200);
		
		while (animator.animating) {
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		
		ArrayList<Loot> lootPickedUp = new ArrayList<>();
		for (Loot l : gm.allLoot) {
			if (l.inTrainCar && (l.onTopOfTrain == onTopOfTrain) && (l.getRowNum() == cb.lootType) && (l.trainCarID == currentTrainCar)) {
				lootPickedUp.add(l);
			}
		}
		
		int rand = (int)(Math.random() * lootPickedUp.size());
		lootPickedUp.get(rand).animPlayerID = animPlayerID;
		lootPickedUp.get(rand).inTrainCar = false;
//		System.out.println(lootPickedUp.get(rand).getType() + " " + lootPickedUp.get(rand).trainCarID);
	}
	protected void sheriff2(cButton cb, GameManager gm) {
		Animator animator = gm.marshall.animator;
		animator.animating = true;
		animator.nextPoint.x = cb.dim.x + (cb.trainPointingTo == 0 ? 50 : 0);
		animator.currentAnimation = 1;
		animator.flipped = cb.trainPointingTo < gm.marshall.currentTrainCar;
		animator.vector2 = new Point(cb.trainPointingTo < gm.marshall.currentTrainCar ? -1 : 1, 0);
		while (animator.animating) {
//			System.out.println(animator.origPoint + " " + animator.nextPoint);
			if (animator.origPoint.distance(animator.nextPoint) < 10) {
				animator.animating = false;
			}
//			
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		gm.marshall.currentTrainCar = cb.trainPointingTo;
		animator.origPoint = (Point)animator.nextPoint.clone();
	}
	
	public void checkSheriff() {
		if (gm.marshall.currentTrainCar != currentTrainCar || onTopOfTrain) { return; }
		
		Animator animator = gm.marshall.animator;
		
		animator.animating = true;
		animator.currentAnimation = 2;
		animator.vector2 = new Point(0, 0);
		animator.flipped = true;
		
		AudioManager.playAudioNTimes("fire", 0.5, 4, 200);
		
		while (animator.animating) {
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		onTopOfTrain = true;
		deck.add(new PlayerCard(0, 6));
	}
	
	// Getting Available Actions
	public ArrayList<PlayerCard> getAvailableActions() {
		return onHand;
	}
	
	public PlayerCard removeCardFromOnHand(int i) {
		return onHand.remove(i);
	}
	
	// Drawing!
	public void draw(Graphics g, int x, int y) {
//		g.setColor(Color.white);
//		g.fillRect(x, y, 10, 10);
		
		if (!animator.animating) {
			animator.drawPlayer(g, new Point(x, y));
		}
		else {
			animator.drawPlayerAnimation(g);
		}
		
		
		
		if (showingLoot) {
			if (!animator.animating) { 
				boolean hasLoot = false;
				int xx = 700, yy = 730;
				int i = 0;
				for (Loot l : gm.allLoot) {
					if (!l.inTrainCar && (l.animPlayerID == animPlayerID)) {
						hasLoot = true;
						l.draw(g, xx, yy, 20, 20, true);
						xx += 23;
						i++;
						if (i == 5) {
							xx = 700;
							i = 0;
							yy += 23;
						}
					}
				}
				if (!hasLoot) {
					xx = 600;
					g.setColor(Background.inATunnel ? Color.white : Color.black);
					g.setFont(new Font("Arial", Font.BOLD, 20));
					g.drawString("YOU HAVE NO LOOT LULZ", xx, yy);
				}
			}
		}
		
		g.setColor(gm.plys[gm.currentPlayer].playerColor);
		g.fillRect(lootShowingBox.x, lootShowingBox.y, lootShowingBox.width, lootShowingBox.height);
		g.setColor(Color.white);
		g.drawRect(lootShowingBox.x, lootShowingBox.y, lootShowingBox.width, lootShowingBox.height);
		g.setColor(gm.plys[gm.currentPlayer].contrastColor);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Your", (int)lootShowingBox.getCenterX() - 25, (int)lootShowingBox.getCenterY() - 18);
		g.drawString("Loot", (int)lootShowingBox.getCenterX() - 25, (int)lootShowingBox.getCenterY() + 5);
	}
	
	public String toString() {
		return names[animPlayerID];
	}
}
