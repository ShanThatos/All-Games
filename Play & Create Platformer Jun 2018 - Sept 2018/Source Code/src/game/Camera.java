package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.jbox2d.common.Vec2;

import game.jbox2d.utils.Convert;
import game.player.Player;

public class Camera {
	
	// Default Settings
	private final int WIDTH = 800, HEIGHT = 600;
	
	// Current Zoom
	private double zoom;
	
	// Current Display
	private BufferedImage display;
	
	// Current Background
	private BufferedImage bg;
	private Vec2 backgroundTopLeft;
	// Current Sprite Layer
	private BufferedImage sl;
	// Current Camera center
	private Vec2 center;
	private Vec2 screenCenter;
	// Camera movement
	private Vec2 vel;
	
	public Camera() {
		zoom = 1.5;
		bg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		center = new Vec2(0, 0);
		screenCenter = new Vec2(0, 0);
		backgroundTopLeft = new Vec2(0, 0);
		vel = new Vec2(0, 0);
	}
	
	public void setFocus(Vec2 focus) {
		center = Convert.W2S(focus.clone());
	}
	public void setBackground(BufferedImage background, Vec2 backgroundTopLeft) {
		bg = background;
		this.backgroundTopLeft = backgroundTopLeft;
	}
	
	public void update(Vec2 focus) {
		moveCamera(focus);
	}
	private void moveCamera(Vec2 focus) {
		vel.set(focus.x - center.x, focus.y - center.y);
		vel.mulLocal(0.1f);
		if (vel.length() > 1) 
			center.set(center.x + vel.x, center.y + vel.y);
	}
	
	public void displayEnv(Graphics g) {
		startDisplay();
		displayBackground();
		endDisplay(g);
	}
	
	private void startDisplay() {
		display = new BufferedImage((int) (zoom * WIDTH), (int) (zoom * HEIGHT), BufferedImage.TYPE_INT_ARGB);
		screenCenter.set(center.x - backgroundTopLeft.x, center.y - backgroundTopLeft.y);
	}
	private void displayBackground() {
		Graphics2D g = display.createGraphics();
		int width = display.getWidth();
		int height = display.getHeight();
		
		Rectangle r1 = new Rectangle((int) (screenCenter.x - width / 2), (int) (screenCenter.y - height / 2), width, height);
		Rectangle r2 = new Rectangle(0, 0, bg.getWidth(), bg.getHeight());
		Rectangle it = r1.intersection(r2);
		
		if (r2.contains(r1))
			g.drawImage(bg.getSubimage((int) (screenCenter.x - width / 2), (int) (screenCenter.y - height / 2), width, height), 0, 0, width, height, null);
		else if (r2.intersects(r1)){
			BufferedImage tempBackground = new BufferedImage((int) r1.getWidth(), (int) r1.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g2 = tempBackground.getGraphics();
			g2.setColor(Color.black);
			g2.fillRect(0, 0, tempBackground.getWidth(), tempBackground.getHeight());
			g2.drawImage(bg.getSubimage(it.x, it.y, it.width, it.height), it.x - r1.x, it.y - r1.y, null);
			g.drawImage(tempBackground, 0, 0, width, height, null);
		} else {
			g.setColor(Color.black);
			g.fillRect(0, 0, width, height);
		}
	}
	private void endDisplay(Graphics g) {
		g.drawImage(display, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		screenCenter.set(center);
	}
	
	public void displaySprites(Graphics2D g, Player p) {
		startSpriteDisplay();
		displayPlayer(p);
		endSpriteDisplay(g);
	}
	
	private void startSpriteDisplay() {
		sl = new BufferedImage((int) (zoom * WIDTH), (int) (zoom * HEIGHT), BufferedImage.TYPE_INT_ARGB);
		screenCenter.set((float) (center.x - sl.getWidth() / 2), (float) (center.y - sl.getHeight() / 2));
	}
	private void displayPlayer(Player p) {
		Graphics2D g = sl.createGraphics();
		g.translate(-screenCenter.x, -screenCenter.y);
		p.display(g);
	}
	private void endSpriteDisplay(Graphics g) {
		g.drawImage(sl, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		screenCenter.set(center);
	}
	public Vec2 getTopLeftCorner() {
		return new Vec2(screenCenter.x - WIDTH / 2, screenCenter.y - HEIGHT / 2);
	}
	
	public double getZoom() {
		return zoom;
	}
}
