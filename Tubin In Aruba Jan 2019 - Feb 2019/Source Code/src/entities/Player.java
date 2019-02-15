package entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

import game.GamePanel;
import imaging.Animation;
import input.InputManager;
import main.MainFrame;
import main.MainState;

public class Player extends DrawableEntity {
	public static final int NORMAL_ACTION = 0, JUMP_ACTION = 1, DIE_ACTION = 2;
	private static final Rectangle2D.Double colBox = new Rectangle2D.Double(-18, -25, 23, 50);
	
	private Animation[] anims;
	private int currentAction;
	
	private int laneNum;
	private boolean letGoUp, letGoDown;
	
	private static int hScore;
	private int score;
	
	public Player() {
		super("Player", 100, GamePanel.HEIGHT / 2, 0, 0);
		readAnims();
		super.colBox = colBox;
	}
	
	@Override
	public void update(double dt) {
		checkLane();
		moveUpdate(dt);
		anims[currentAction].update();
	}
	
	private void checkLane() {
		boolean up, down;
		boolean[] keys = InputManager.getKeys();
		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		letGoUp = letGoUp || !up;
		letGoDown = letGoDown || !down;
		
		if (up ^ down) {
			if (letGoUp && up && laneNum != 0 && (Math.abs(gm.yLanes[laneNum] - y) < 5 || vy > 0))  {
				laneNum--;
				letGoUp = false;
			}
			else if (letGoDown && down && laneNum != gm.yLanes.length - 1 && (Math.abs(gm.yLanes[laneNum] - y) < 5 || vy < 0)) {
				laneNum++;
				letGoDown = false;
			}
		}
		
		if (Math.abs(gm.yLanes[laneNum] - y) < 5) 
			y = gm.yLanes[laneNum];
		if (gm.yLanes[laneNum] > y)
			vy = 50;
		else if (gm.yLanes[laneNum] < y)
			vy = -50;
		else
			vy = 0;
	}
	
	@Override
	protected void onCollide(Collidable c) {
		if (c.entityName.equals("Coin")) {
			Coin coin = (Coin) c;
			coin.entityDestroyed = true;
			score += coin.getCoinValue();
		} else if (c.entityName.equals("Boulder")) {
			saveHighScore();
			try { Thread.sleep(2000); } catch (Exception e) {}
			MainFrame.changeState(MainState.MENU);
			MainFrame.resetAllPanels();
		}
	}
	
	@Override
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		BufferedImage k = anims[currentAction].getFrame();
		int dx = (int) (2 * Math.cos(System.currentTimeMillis() / 100));
		int dy = (int) (2 * Math.sin(System.currentTimeMillis() / 100));
		g.translate(x + dx, y + dy);
		g.drawImage(k, -k.getWidth() / 2, -k.getHeight() / 2, null);
		super.drawColBox(gr);
	}
	
	private void readAnims() {
		try {
			anims = new Animation[2];
			BufferedImage[] k1 = {ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/mcgee.png"))};
			k1 = new BufferedImage[]{
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/mcgee.png")), 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/mcgee2.png"))
					};
			BufferedImage[] k2 = {ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/mcgee2.png"))};
			anims[0] = new Animation(k1);
			anims[1] = new Animation(k2);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void saveHighScore() {
		String appData = System.getenv("APPDATA");
		File f = new File(appData + "/_tubinInAruba.dat");
		int prevScore = 0;
		try {
			Scanner in = new Scanner(f);
			prevScore = in.nextInt();
		}
		catch (Exception e) {}
		int newHS = Math.max(score, prevScore);
		hScore = newHS;
		try { 
			PrintWriter p = new PrintWriter(f);
			p.println(newHS + "");
			p.close();
		} catch (Exception e) {}
	}
	
	public int getScore() {
		return score;
	}
	
	public static int getHighScore() {
		String appData = System.getenv("APPDATA");
		File f = new File(appData + "/_tubinInAruba.dat");
		int hs = 0;
		try {
			Scanner in = new Scanner(f);
			 hs = in.nextInt();
		}
		catch (Exception e) {}
		return hs;
	}
}
