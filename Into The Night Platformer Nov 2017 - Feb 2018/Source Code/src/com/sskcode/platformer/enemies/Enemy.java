package com.sskcode.platformer.enemies;

import com.sskcode.external.imageprocessing.AnimatedGIF;
import com.sskcode.platformer.entities.Player;
import com.sskcode.platformer.physics.Collider;
import com.sskcode.platformer.physics.Point;
import com.sskcode.platformer.physics.Vector2D;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class Enemy {
	
	// States
	public static final int WANDER_STATE = 0, FOLLOW_STATE = 1, JUMP_STATE = 2, ATTACK_STATE = 3;
	public boolean wandering, following, jumping, attacking;
	private TreeSet<Integer> states = new TreeSet<>();
	
	
	// Movement
	public Vector2D vel;
	public Collider c;
	private Point startingPos, nextPoint;
	private final double normSpeed = 1;
	
	
	// Animations
	private static AnimatedGIF[][][] allAnims;
	private static Collider[] allColliders;
	private AnimatedGIF[][] anims;
	public int enemyNum;
	public int currentAnim;
	public boolean facingRight;
	
	// Attacking
	private static ArrayList<HitBox>[] allHbs;
	
	// Arrays for different Animations
	private static final char[] allActionsC = { 'S', 'R', 'W', 'A', 'J', 'D' };
	private static final String[] allActions = { "stand", "run", "walk",
			"attack", "jump", "die" };
	
	// Array for different Enemy Types
	private static final String[] allEnemies = {"Viking1", "Viking2", "Viking3"};
	public static boolean infoRead = false;

	// External
	private Collider platformStandingOn;
	
	// Health Bar
	private HealthBar hb;
	
	
	
	public Enemy(int x, int y, String enemyName) {
		for (int i = 0; i < allEnemies.length; i++)
			if (enemyName.equals(allEnemies[i]))
				enemyNum = i;
		vel = new Vector2D(0, 2);
		
		// allColliders x & y contain offsets
		c = new Collider(x + allColliders[enemyNum].box.x, y
				+ allColliders[enemyNum].box.y,
				allColliders[enemyNum].box.width,
				allColliders[enemyNum].box.height, true);
		anims = allAnims[enemyNum].clone();
		
		startingPos = new Point(x, y);
		hb = new HealthBar();
	}
	
	public void update(Collider[] clds, Player p) {
//		updateMove();
//		updateAnim(attack(p));
		changeState(clds, p);
		decideMovement(p);
		c.update(vel.dx, vel.dy);
		checkCollisions(clds, p);
		updateAnim(attacking);
	}
	
	private void changeState(Collider[] clds, Player p) {
		states.clear();
		if (attack(p) || attacking) {
			attacking = true;
			states.add(ATTACK_STATE);
		} else if (shouldJump(clds) || jumping) {
			states.add(JUMP_STATE);
		}
		if (shouldFollow(p) || following) {
			following = true;
			states.add(FOLLOW_STATE);
			wandering = false;
		} else {
			wandering = true;
			states.add(WANDER_STATE);
			following = false;
		}
	}
	
	private boolean shouldJump(Collider[] clds) {
		int checkingDist = 5;
//		Point check = new Point(c.pos.x + c.box.width / 2 + checkingDist * (facingRight ? 1 : -1), c.pos.y + c.box.height / 2);
		Collider temp = new Collider(c.box.x + c.box.width / 2 + checkingDist * (facingRight ? 1 : -1) + (facingRight ? 0 : -10), c.box.y, 10, 10, true);
		for (Collider cld : clds) {
			if (nextPoint != null && cld.box.contains(nextPoint.x, nextPoint.y)) {
				nextPoint.x = c.box.x + c.box.width / 2;
			}
				
			if (cld.collide && cld.collidesWith(temp)) {
				if (Math.abs(c.pos.y - cld.pos.y) < 30) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean attack(Player p) {
		Point centEm = new Point(c.pos.x + c.box.width / 2, c.pos.y + c.box.height / 2);
		if (p.getMiddlePosition().distance(centEm) < 20) {
			return true;
		}
		return false;
	}
	
	private boolean shouldFollow(Player p) {
		Point centEm = new Point(c.pos.x + c.box.width / 2, c.pos.y + c.box.height / 2);
		if (Math.abs(p.getMiddlePosition().x - centEm.x) < 200 && Math.abs(p.getMiddlePosition().y - centEm.y) < 50) {
			facingRight = p.c.pos.x > c.pos.x;
			return true;
		}
		following = false;
		return false;
	}
	
	private void decideMovement(Player p) {
		vel.dy += 2;
		vel.dy = Math.min(vel.dy, 7);
		if (!jumping && states.contains(JUMP_STATE)) {
//			System.out.println("YAY");
			jumping = true;
			vel.dy -= 20;
		}
		if (following) {
			vel.dx = p.c.pos.x - c.pos.x > 0 ? normSpeed : -normSpeed;
		}
		if (wandering) {
//			if (nextPoint != null)
//				System.out.println(c.box.contains((int)nextPoint.x, (int)nextPoint.y) + " " + c + " " + nextPoint.x + " " + nextPoint.y);
			if (nextPoint == null || c.box.contains(nextPoint.x, nextPoint.y)) {
				int newDist = (int) ((Math.random() * 50) + 20) * (Math.random() < .5 ? 1 : -1);
				nextPoint = new Point(startingPos.x + newDist, startingPos.y + c.box.height / 2);
				checkIfWillFall();
//				System.out.println("HI " + newDist);
			}
			if (Math.abs(startingPos.y - c.box.y - c.box.height / 2) > -5) {
				startingPos.y = c.box.y + c.box.height / 2;
				nextPoint.y = c.box.y + c.box.height / 2;
			}
			vel.dx = nextPoint.x - c.pos.x > 0 ? normSpeed : -normSpeed;
			facingRight = nextPoint.x > c.pos.x;
		}
	}
	
	public void checkIfWillFall() {
		if (platformStandingOn == null) {
			nextPoint.x = startingPos.x;
		}
		else if (Math.random() < .999) {
			nextPoint.x = Math.max(nextPoint.x, platformStandingOn.box.x);
			nextPoint.x = Math.min(nextPoint.x, platformStandingOn.box.x + platformStandingOn.box.width);
		}
	}
	
	public void checkIfDestInsideWall(Collider[] clds) {
		
	}
	
	public boolean hitPlayer(Player p) {
		if (!attacking)
			return false;
		for (HitBox hb : allHbs[enemyNum]) {
			if ((hb.facingRight == facingRight) && hb.frame == anims[facingRight?0:1][currentAnim].currentFrame) {
				if (hb.collidesWithPlayer(new Point(c.pos.x - allColliders[enemyNum].pos.x, c.pos.y - allColliders[enemyNum].pos.y), p.c))
					return true;
			}
		}
		return false;
	}
	
	private void checkCollisions(Collider[] clds, Player p) {
		for (Collider cld : clds) {
			checkSingleCollision(cld);
		}
	}
	
	private void checkSingleCollision(Collider cld) {
		if (c.collidesWith(cld)) {	
			c.update(-vel.dx, 0);
			if (!c.collidesWith(cld)) {
				if (vel.dx > 0) {
					c.setX(cld.box.x - c.box.width);
				} else if (vel.dx < 0) {
					c.setX(cld.box.x + cld.box.width);
				}
			} else {
				c.update(vel.dx, -vel.dy);
					if (vel.dy > 0) {
						c.setY(cld.box.y - c.box.height);
					} else if (vel.dy < 0) {
						c.setY(cld.box.y + cld.box.height);
					}
					if (!c.collidesWith(cld))  {
						vel.dy = 2;
						jumping = false;
						platformStandingOn = cld;
						return;
					}
			}
		}
//		if (c.collidesWith(cld)) {
//			int offsetX = 0;
//			int inc = (int) (-vel.dx / Math.abs(vel.dx));
//			while (c.collidesWith(cld) && inc != 0) {
//				if (Math.abs(offsetX) >= Math.abs(vel.dx))
//					break;
//				c.update(inc, 0);
//				offsetX += inc;
//			}
//			if (!c.collidesWith(cld))
//				return;
//			c.update(-offsetX, 0);
//
//			int offsetY = 0;
//			inc = (int) (-vel.dy / Math.abs(vel.dy));
//			while (c.collidesWith(cld) && inc != 0) {
//				if (Math.abs(offsetY) >= Math.abs(vel.dy))
//					break;
//				c.update(0, inc);
//				offsetY += inc;
//			}
//			if (!c.collidesWith(cld))  {
//				vel.dy = 2;
//				jumping = false;
//				platformStandingOn = cld;
//				return;
//			}
//			
//			
//			c.update(offsetX, offsetY);
//		}
	}
	
	private void updateAnim(boolean attack) {
		int newAnim = currentAnim;
		if (!attack) {
			if (vel.dx == 0) {
				newAnim = 1;
			} else {
				newAnim = 0;
			}
		} else {
			newAnim = 2;
			if (anims[facingRight?0:1][currentAnim].currentFrame == 0)
				attacking = false;
		}
		if (newAnim != currentAnim) {
			anims[facingRight?0:1][currentAnim].resetFrame();
			currentAnim = newAnim;
		}
		anims[facingRight?0:1][currentAnim].nextFrame();
	}
	
	public void damage(double damuuge) {
		hb.damage(damuuge);
	}
	
	public boolean isDead() {
		return hb.isDead();
	}
	
	
	public void draw(Graphics g, int leftX, int topY) {
		Point nP = new Point(c.pos.x - allColliders[enemyNum].pos.x - leftX, c.pos.y - allColliders[enemyNum].pos.y - topY);
		anims[facingRight?0:1][currentAnim].draw(g, nP); 
		hb.draw(c, g, leftX, topY);
//		if (attacking) {
//			for (HitBox hb : allHbs[enemyNum]) {
//				if ((hb.facingRight == facingRight) && hb.frame == anims[facingRight?0:1][currentAnim].currentFrame) {
//					hb.draw(new Point(c.pos.x - allColliders[enemyNum].pos.x - leftX,  c.pos.y - allColliders[enemyNum].pos.y - topY), g);
//				}
//			}
//		}
//		g.setColor(Color.white);
//		g.drawRect(c.box.x - leftX, c.box.y - topY, c.box.width, c.box.height);
	}
	
	public static void readAllInfo() throws IOException {
		allAnims = new AnimatedGIF[allEnemies.length][][];
		allColliders = new Collider[allEnemies.length];
		allHbs = (ArrayList<HitBox>[])(new ArrayList[allEnemies.length]);
		for (int i = 0; i < allEnemies.length; i++) {
			Scanner in = new Scanner(new File("resources/Enemies/0"
					+ allEnemies[i] + "/_Info.dat"));
			String actions = in.nextLine().split(" ")[1].trim();
			allAnims[i] = new AnimatedGIF[2][actions.length()];
			for (int j = 0; j < actions.length(); j++) {
				String gifFileName = "";
				for (int k = 0; k < allActionsC.length; k++)
					if (allActionsC[k] == actions.charAt(j))
						gifFileName = allActions[k];
				allAnims[i][0][j] = new AnimatedGIF();
				allAnims[i][0][j].read("/Enemies/0" + allEnemies[i] + "/"
						+ gifFileName + ".gif");
				allAnims[i][0][j].shrinkToSize(60, 60);
				allAnims[i][1][j] = new AnimatedGIF();
				allAnims[i][1][j].flip(allAnims[i][0][j].getImages());
			}

			String[] collisionBox = in.nextLine().split(" ");
			allColliders[i] = new Collider(Integer.parseInt(collisionBox[1]),
					Integer.parseInt(collisionBox[2]),
					Integer.parseInt(collisionBox[3]),
					Integer.parseInt(collisionBox[4]), true);
			
			in = new Scanner(new File("resources/Enemies/0"
					+ allEnemies[i] + "/_HitBoxes.dat"));
			while (in.hasNext()) {
				in.next();
				int frame = in.nextInt();
				int numRects = in.nextInt();
				in.nextLine();
				Rectangle[] right = new Rectangle[numRects];
				Rectangle[] left = new Rectangle[numRects];
				for (int k = 0; k < numRects; k++) {
					right[k] = new Rectangle(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
					in.nextLine();
					left[k] = new Rectangle(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
					in.nextLine();
				}
				allHbs[i] = new ArrayList<HitBox>();
				allHbs[i].add(new HitBox(frame, right, true));
				allHbs[i].add(new HitBox(frame, left, false));
			}
		}
		infoRead = true;
	}
}

class HitBox {
	public Rectangle[] hbs;
	public int frame;
	public boolean facingRight;
	
	public HitBox(int frame, Rectangle[] hbs, boolean fR) {
		this.frame = frame;
		this.hbs = hbs;
		facingRight = fR;
	}
	
	public void draw(Point emPos, Graphics g) {
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
		g.setColor(Color.red);
		for (Rectangle r : hbs) {
			Rectangle temp = new Rectangle(r.x + (int)emPos.x, r.y + (int)emPos.y, r.width, r.height);
			g.fillRect(temp.x, temp.y, temp.width, temp.height);
		}
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	// Must subtract out allColliders for emPos
	public boolean collidesWithPlayer(Point emPos, Collider playerCld) {
		for (Rectangle r : hbs) {
			Rectangle temp = new Rectangle(r.x + (int)emPos.x, r.y + (int)emPos.y, r.width, r.height);
			if (temp.intersects(playerCld.box))
				return true;
		}
		return false;
	}
}

class HealthBar {
	private static final int WIDTH = 20, HEIGHT = 2;
	private double health = 100, maxHealth = 100;
	
	public HealthBar() {
		
	}
	
	public void damage(double damuuge) {
		health -= damuuge;
	}
	
	public boolean isDead() {
		return health < 0;
	}
	
	public void draw(Collider em, Graphics g, int leftX, int topY) {
		if (health == maxHealth || health <= 0)
			return;
		g.setColor(Color.red);
		g.fillRect(em.box.x + em.box.width / 2- WIDTH / 2 - leftX, em.box.y - 10 - HEIGHT - topY, WIDTH, HEIGHT);
		g.setColor(Color.green);
		g.fillRect(em.box.x + em.box.width / 2- WIDTH / 2 - leftX, em.box.y - 10 - HEIGHT - topY, (int)(health / maxHealth * WIDTH), HEIGHT);
	}
}