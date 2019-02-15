package com.sskcode.platformer.entities;

import com.sskcode.external.imageprocessing.AnimatedGIF;
import com.sskcode.external.imageprocessing.RotatedImage;
import com.sskcode.platformer.physics.Vector2D;
import com.sskcode.platformer.enemies.Enemy;
import com.sskcode.platformer.main.GamePanel;
import com.sskcode.platformer.physics.Collider;
import com.sskcode.platformer.physics.Point;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.io.IOException;
import java.io.File;

public class Player {
	// Animation
	private AnimatedGIF[][] Anims;
	private int currentAnim;
	public boolean facingRight = true;
	private boolean[] keys = new boolean[256];

	// Collider
	public Collider c;
	private int offsetX, offsetY;

	// Movement & Actions
	private Vector2D vel;
	private static final double grav = .5, maxdy = 6;
	private boolean canJump = false, jumping = false;
	private boolean canBeHit = false, gotHit = false, isHit = false;
	private double jumpForce = -7;
	

	// UI
	private HealthBar hb;

	// Weapon
	private Bow b;

	public Player(int x, int y) {
		vel = new Vector2D(0, 0);
		try {
			readInfo(x, y);
		} catch (IOException e) {
		}
		try {
			hb = new HealthBar(60, GamePanel.HEIGHT - 40);
		} catch (IOException e) {
		}
	}

	private void readInfo(int x, int y) throws IOException {
		Scanner in = new Scanner(new File("resources/Player/_Info.dat"));
		offsetX = in.nextInt();
		offsetY = in.nextInt();
		int w = in.nextInt();
		int h = in.nextInt();
		c = new Collider(x + offsetX, y + offsetY, w, h, true);
		in.close();

		Anims = new AnimatedGIF[2][5];
		for (int i = 0; i < Anims[0].length; i++)
			Anims[0][i] = new AnimatedGIF();
		Anims[0][0].read(ImageIO.read(new File(
				"resources/Player/0Animations/soldier.png")));
		Anims[0][1].read("/Player/0Animations/soldier_walk.gif");
		Anims[0][2].read("/Player/0Animations/soldier_jump.gif");
		Anims[0][2].standStillEnd = true;
		Anims[0][3].read("/Player/0Animations/soldier_run.gif");
		Anims[0][4].read("/Player/0Animations/soldier_hit.gif");
		Anims[0][4].standStillEnd = true;
		// Anims[0][0].read("/Player/0Animations/Archer_Idle.gif");
		// Anims[0][1].read("/Player/0Animations/Archer_Walk.gif");
		// Anims[0][2].read("/Player/0Animations/Archer_Jump.gif");
		// Anims[0][3].read("/Player/0Animations/Archer_Run.gif");
		for (int i = 0; i < Anims[1].length; i++) {
			Anims[1][i] = new AnimatedGIF();
			Anims[1][i].flip(Anims[0][i].getImages());
			Anims[1][i].standStillEnd = Anims[0][i].standStillEnd;
		}

		b = new Bow();
	}

	public void updateBow(boolean drawn, int mouseX, int mouseY, double leftX,
			double topY) {
		b.updateBow(drawn, c.pos, facingRight, mouseX, mouseY, leftX, topY);
	}

	public void shoot() {
		String type = "";
		if (keys[KeyEvent.VK_SHIFT])
			type = "Grappling";
		b.shoot(type, vel);
	}

	public void update(Collider[] clds, Enemy[] ems) {
		updateVel();
		checkIfHit(ems);
		c.update(vel.dx, vel.dy);
		checkCollisions(clds, ems);
		updateAnim();
		updateArrows(clds, ems);
	}

	private void checkIfHit(Enemy[] ems) {
		gotHit = false;
		for (Enemy em : ems) {
			if (em.hitPlayer(this)) { 
				gotHit = true;
				if (gotHit && canBeHit) {
					if (!isHit) {
						isHit = true;
						vel.add(new Vector2D(em.facingRight ? 4 : -4, jumping ? 0:-3));
						hb.damage(10);
					}
				}
				canBeHit = false;
				break;
			}
		}
		if (!gotHit) 
			canBeHit = true;
	}
	
	private void updateArrows(Collider[] clds, Enemy[] ems) {
		b.updateArrows(clds, ems);
	}

	private void updateAnim() {
		Anims[facingRight ? 0 : 1][currentAnim].nextFrame();
		int newAnim = 0;

		boolean right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		boolean left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		boolean jump = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_W]
				|| keys[KeyEvent.VK_UP];
		boolean shifting = keys[KeyEvent.VK_SHIFT] && !b.drawn;

		if (isHit) {
			newAnim = 4;
		} else if (jumping) {
			newAnim = 2;
		} else if (right && !left) {
			if (!facingRight)
				facingRight = shifting;
			if (!b.drawn)
				facingRight = true;
			newAnim = 1;
			if (shifting)
				newAnim = 3;
		} else if (left && !right) {
			if (facingRight)
				facingRight = !shifting;
			if (!b.drawn)
				facingRight = false;
			newAnim = 1;
			if (shifting)
				newAnim = 3;
		}
		if (currentAnim != newAnim) {
			Anims[facingRight ? 0 : 1][currentAnim].resetFrame();
			currentAnim = newAnim;
		}
	}

	private void checkCollisions(Collider[] clds, Enemy[] ems) {
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
				if (isHit) {
					vel.dx = 0;
				}
			} else {
				c.update(vel.dx, -vel.dy);
					if (vel.dy > 0) {
						c.setY(cld.box.y - c.box.height);
					} else if (vel.dy < 0) {
						c.setY(cld.box.y + cld.box.height);
					}
					jumping = false;
					if (isHit) {
						vel.dx = 0;
						isHit = false;
					}
					canJump = !keys[KeyEvent.VK_SPACE] && !keys[KeyEvent.VK_W]
							&& !keys[KeyEvent.VK_UP];
					vel.dy = grav;
			}
		}
	}
	
	
	private void updateVel() {
		vel.dy += grav;
		vel.dy = Math.min(vel.dy, maxdy);
		boolean right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		boolean left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		boolean jump = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_W]
				|| keys[KeyEvent.VK_UP];
		boolean shifting = keys[KeyEvent.VK_SHIFT] && !b.drawn;
		
		if (!isHit) {
			if (right && left || !right && !left)
				vel.dx = 0;
			else {
				if (right)
					vel.dx = shifting ? 2.5 : 1.5 * (jumping ? .3:1);
				if (left)
					vel.dx = shifting ? -2.5 : -1.5 * (jumping ? .3:1);
			}
		} else {
			canJump = false;
		}
		if (canJump && jump) {
			canJump = false;
			jumping = true;
			vel.dy += (shifting ? 1.2 : 1) * jumpForce;
		}

	}

	public void draw(Graphics g, int leftX, int topY) {
		Point nP = new Point(c.pos.x - offsetX - leftX, c.pos.y - offsetY
				- topY);
		Anims[facingRight ? 0 : 1][currentAnim].draw(g, nP);
		hb.draw(g);

		b.drawBow(g, c.pos, facingRight, leftX, topY);
	}

	public void addKey(KeyEvent k) {
		keys[k.getKeyCode()] = true;
	}

	public void removeKey(KeyEvent k) {
		keys[k.getKeyCode()] = false;
	}

	public Point getPosition() {
		return c.pos;
	}
	public Point getMiddlePosition() {
		Point ret = new Point(c.pos.x + c.box.width / 2, c.pos.y + c.box.height / 2);
		return ret;
	}
}

class HealthBar {
	public BufferedImage GreenBar, RedBar, Head;
	public double health = 100, maxHealth = 100;
	private Point pos;

	public HealthBar(int x, int y) throws IOException {
		pos = new Point(x, y);
		GreenBar = ImageIO.read(new File(
				"resources/Player/0UI/Health Bar/Green Bar.png"));
		RedBar = ImageIO.read(new File(
				"resources/Player/0UI/Health Bar/Red Bar.png"));
		Head = ImageIO.read(new File(
				"resources/Player/0UI/Head/Soldier_Head.png"));
	}

	public boolean damage(double damage) {
		health -= damage;
		health = Math.max(0, health);
		return health == 0;
	}

	public void draw(Graphics g) {
		g.drawImage(RedBar, (int) pos.x, (int) pos.y, (int) pos.x
				+ (int) maxHealth, (int) pos.y + 30, 0, 0, RedBar.getWidth(),
				RedBar.getHeight(), null);
		g.drawImage(GreenBar, (int) pos.x, (int) pos.y, (int) pos.x
				+ (int) health, (int) pos.y + 30, 0, 0,
				(int) (GreenBar.getWidth() * (double) health / maxHealth),
				GreenBar.getHeight(), null);
		g.drawImage(Head, (int) pos.x - Head.getWidth() - 3, (int) pos.y + 8
				- Head.getHeight() / 2, null);
	}
}

class Bow {
	private RotatedImage[] i;
	public boolean drawn = false;
	private boolean facingRight = false;
	private double power;
	private double ang;
	private Point p;

	private ArrayList<Arrow> projs = new ArrayList<>();

	public Bow() throws IOException {
		power = 1;
		ang = 0;

		i = new RotatedImage[2];
		i[0] = new RotatedImage(ImageIO.read(new File(
				"resources/Player/0Bow&Arrow/bow.png")), 45);
		i[1] = new RotatedImage(ImageIO.read(new File(
				"resources/Player/0Bow&Arrow/bowWithArrow.png")), 45);
	}

	public void updateBow(boolean d, Point playPos, boolean facingRight,
			int mouseX, int mouseY, double camOffsetX, double camOffsetY) {
		drawn = d;
		this.facingRight = facingRight;
		// Get Position of bow on screen
		Point p = new Point(playPos.x - camOffsetX, playPos.y - camOffsetY);
		p.x += (facingRight ? 5 : -2);
		p.y += 12;
		ang = Math.atan((mouseY - p.y) / (mouseX - p.x));
		if (mouseX < p.x)
			ang -= Math.PI;
		this.p = new Point(p.x + camOffsetX, p.y + camOffsetY);
		// p = new Point(playPos.x - offsetX, playPos.y - offsetY);
		// p.x += (facingRight?5:-5);
		// p.y += 5;
		// ang = Math.atan((mouseY - offsetY - p.y) * 1.0 / (mouseX - offsetX -
		// p.x));
		// if (mouseX - p.x - offsetX < 0)
		// ang -= Math.PI;
	}

	public void shoot(String arrowType, Vector2D playerVel) {
		double ang = this.ang;
		// System.out.println(Math.toDegrees(ang));
		if (facingRight) {
			if (ang < -Math.PI / 2 + Math.PI / 8) {
				ang = -Math.PI / 2 + Math.PI / 8;
			}
		} else {
			if (ang > -Math.PI / 2 - Math.PI / 8)
				ang = -Math.PI / 2 - Math.PI / 8;
		}

		Vector2D temp = new Vector2D(power * Math.cos(ang) + playerVel.dx, power
				* Math.sin(ang) + playerVel.dy);

		projs.add(new Arrow(p.x, p.y, temp));
	}

	public void updateArrows(Collider[] clds, Enemy[] ems) {
		for (int i = 0; i < projs.size(); i++) {
			if (projs.get(i).updateArrow(clds, ems))
				projs.remove(i--);
		}
	}

	public void drawBow(Graphics g, Point playPos, boolean facingRight,
			int camOffsetX, int camOffsetY) {
		if (drawn)
			power = Math.min(power + .1, 8);
		else
			power = 1;
		this.facingRight = facingRight;
		Point p = new Point(playPos.x - camOffsetX, playPos.y - camOffsetY);
		p.x += (facingRight ? 5 : -2);
		p.y += 12;
		this.p = new Point(p.x + camOffsetX, p.y + camOffsetY);
		// p.x += facingRight?5:-5;
		// p.y += 5;
		double drawAng = ang;
		// System.out.println(Math.toDegrees(drawAng) + " " + drawAng);
		if (facingRight) {
			if (drawAng < -Math.PI / 2 + Math.PI / 8) {
				drawAng = -Math.PI / 2 + Math.PI / 8;
			}
		} else {
			if (drawAng > -Math.PI / 2 - Math.PI / 8)
				drawAng = -Math.PI / 2 - Math.PI / 8;
		}

		g.drawImage(i[drawn ? 1 : 0].getRotatedImage(drawAng), (int) p.x,
				(int) p.y, (int) p.x + i[drawn ? 1 : 0].getWidth(), (int) p.y
						+ i[drawn ? 1 : 0].getHeight(), 0, 0,
				i[drawn ? 1 : 0].getWidth(), i[drawn ? 1 : 0].getHeight(), null);

		drawArrows(g, camOffsetX, camOffsetY);
	}

	public void drawArrows(Graphics g, int camOffsetX, int camOffsetY) {
		for (Arrow a : projs) {
			a.drawArrow(g, camOffsetX, camOffsetY);
		}
	}
}
