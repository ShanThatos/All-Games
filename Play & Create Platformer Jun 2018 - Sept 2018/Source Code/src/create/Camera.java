package create;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;

public class Camera {
	
	private Point2D center;
	private final int WIDTH = 800, HEIGHT = 600;
	private double zoom = 1;
	
	// Movement
	private final Vec2 acc = new Vec2(1, 1), maxVel = new Vec2(10, 10), minVel = new Vec2(-10, -10);
	private Vec2 vel;
	
	// Current Display
	private BufferedImage display;
	
	public Camera() {
		center = new Point2D.Double(0, 0);
		vel = new Vec2(0, 0);
	}
	
	public void startDisplay() {
		display = new BufferedImage((int)(zoom * WIDTH), (int)(zoom * HEIGHT), BufferedImage.TYPE_INT_ARGB);
	}
	public void displayTiles(ArrayList<Tile> tiles, boolean displayMesh) {
		Graphics2D g = display.createGraphics();
		AffineTransform old = g.getTransform();
		
		g.translate((int)(zoom * WIDTH / 2) - (int)(center.getX()), (int)(zoom * HEIGHT / 2) - (int)(center.getY()) );
		for (int i = 0; i < tiles.size(); i++) 
			if ((Math.abs(center.getX() - tiles.get(i).getX())) < zoom * WIDTH / 2 + 100 && (Math.abs(center.getY() - tiles.get(i).getY())) < zoom * HEIGHT / 2 + 100)
				tiles.get(i).display(g, displayMesh);
		
		g.setTransform(old);
	}
	public void displayMouseHoverTiles(int tileNumber, Point2D mouseWorldPress, Point2D mouseWorldPos, boolean mousePressed, boolean deleteTiles) {
		Graphics2D g = display.createGraphics();
		AffineTransform old = g.getTransform();
		g.translate((int)(zoom * WIDTH / 2) - (int)(center.getX()), (int)(zoom * HEIGHT / 2) - (int)(center.getY()) );
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		if (deleteTiles) {
			if (mousePressed) {
				Point2D p = convToTileCoord(mouseWorldPress.getX(), mouseWorldPress.getY());
				Point2D p2 = convToTileCoord(mouseWorldPos.getX(), mouseWorldPos.getY());
				g.setColor(Color.red);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect((int) p.getX(), (int) p.getY(), (int) (p2.getX() - p.getX() + 64), (int) (p2.getY() - p.getY() + 64));
			} else {
				Point2D p = convToTileCoord(mouseWorldPos.getX(), mouseWorldPos.getY());
				g.setColor(Color.red);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect((int) p.getX(), (int) p.getY(), 64, 64);
			}
		}
		else {
			if (mousePressed) {
				Point2D p = convToTileCoord(mouseWorldPress.getX(), mouseWorldPress.getY());
				Point2D p2 = convToTileCoord(mouseWorldPos.getX(), mouseWorldPos.getY());
				for (int xx = (int)p.getX(); xx <= p2.getX(); xx += 64) 
					for (int yy = (int)p.getY(); yy <= p2.getY(); yy += 64) 
						if ((Math.abs(center.getX() - xx)) < zoom * WIDTH / 2 + 100 && (Math.abs(center.getY() - yy)) < zoom * HEIGHT / 2 + 100)
							Tile.displayTileInLoc(g, xx, yy, tileNumber);
			} else {
				Point2D p = convToTileCoord(mouseWorldPos.getX(), mouseWorldPos.getY());
				Tile.displayTileInLoc(g, (int)p.getX(), (int)p.getY(), tileNumber);
			}
		}
		g.setTransform(old);
	}
	public void displayPlayerPos(Point2D playerWorldPos, float alpha) {
		Graphics2D g = display.createGraphics();
		AffineTransform old = g.getTransform();
		g.translate((int)(zoom * WIDTH / 2) - (int)(center.getX()), (int)(zoom * HEIGHT / 2) - (int)(center.getY()) );
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.setColor(Color.black);
		int size = 20;
		g.fillPolygon(new int[]{(int) (playerWorldPos.getX() - size), (int) (playerWorldPos.getX() + size), (int) (playerWorldPos.getX())}, 
				new int[]{(int) (playerWorldPos.getY()), (int) (playerWorldPos.getY()), (int) (playerWorldPos.getY() - size * 1.7)}, 3);
		g.setTransform(old);
	}
	public void endDisplay(Graphics g) {
		g.drawImage(display, 0, 0, (int)WIDTH, (int)HEIGHT, null);
	}
	
	public void update(boolean[] keys) {
		moveCam(keys);
	}
	private void moveCam(boolean[] keys) {
		boolean keysPressed = false;
		boolean right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		boolean left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		boolean up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		boolean down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		if (right ^ left) {
			vel.addLocal(right ? acc.x : -acc.x, 0);
			vel.set(Math.min(Math.max(vel.x, minVel.x), maxVel.x), vel.y);
			keysPressed = true;
		} else {
			float newX = vel.x * .8f;
			vel.set(Math.abs(newX) < .5f ? 0 : newX, vel.y);
		}
		if (up ^ down) {
			vel.addLocal(0, down ? acc.y : -acc.y);
			vel.set(vel.x, Math.min(Math.max(vel.y, minVel.y), maxVel.y));
			keysPressed = true;
		} else {
			float newY = vel.y * .8f;
			vel.set(vel.x, Math.abs(newY) < .5f ? 0 : newY);
		}
		translate(vel.x * zoom, vel.y * zoom);
	}
	
	public void setCenter(Point2D center) {
		this.center = (Point2D) center.clone();
	}
	
	public void zoomScale(double x) {
		zoom = Math.max(Math.min(zoom * x, 5), .1);
	}
	
	public void translate(double x, double y) {
		center.setLocation(center.getX() + x, center.getY() + y);
	}
	
	public Point2D.Float convScreenToWorld(double x, double y) {
		double dx = x - 400, dy = y - 300;
		dx *= zoom;
		dy *= zoom;
		Point2D.Float ret = new Point2D.Float((float)(center.getX() + dx), (float)(center.getY() + dy));
		return ret;
	}
	public Point2D.Float convToTileCoord(double x, double y) {
		x = ((x < 0 ? -1 : 0) + ((int)x) / 64) * 64;
		y = ((y < 0 ? -1 : 0) + ((int)y) / 64) * 64;
		return new Point2D.Float((float) x, (float) y);
	}
}
