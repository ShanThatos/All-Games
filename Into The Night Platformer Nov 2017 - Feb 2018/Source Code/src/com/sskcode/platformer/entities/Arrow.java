package com.sskcode.platformer.entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sskcode.external.imageprocessing.RotatedImage;
import com.sskcode.platformer.enemies.Enemy;
import com.sskcode.platformer.physics.Collider;
import com.sskcode.platformer.physics.Point;
import com.sskcode.platformer.physics.Vector2D;

public class Arrow {
	// Image
	public RotatedImage i;
	public static BufferedImage arrow;
	// Movement
	public Point pos;
	public Vector2D vel;
	public static final double gravAcc = .1;
	// Collider
	public Collider c;
	public boolean collided = false;
	public int timeToDeath = 300;

	public Arrow(double x, double y, Vector2D v) {
		pos = new Point(x, y);
		this.vel = v;
		if (arrow == null)
			try {
				arrow = ImageIO.read(new File(
						"resources/Player/0Bow&Arrow/Arrow.png"));
			} catch (IOException e) {
			}
		i = new RotatedImage(arrow, 0);

		double ang = Math.atan(v.dy / v.dx);
		if (v.dx < 0)
			ang += Math.PI;
		int arrowLength = arrow.getWidth() / 2;
		c = new Collider((int) (pos.x + arrowLength * Math.cos(ang) + 5), (int) (pos.y + arrowLength * Math.sin(ang) + 5), 2, 2, true);
	}

	public boolean updateArrow(Collider[] clds, Enemy[] ems) {
		if (!collided) {
			vel.dy += gravAcc;
			pos.x += vel.dx;
			pos.y += vel.dy;
			double ang = Math.atan(vel.dy / vel.dx);
			if (vel.dx < 0)
				ang += Math.PI;
			int arrowLength = arrow.getWidth() / 2;
			c = new Collider((int) (pos.x + arrowLength * Math.cos(ang) + 5), (int) (pos.y + arrowLength * Math.sin(ang) + 5), 2, 2, true);
			for (Collider cld : clds)
				checkCollision(cld);
			for (Enemy em : ems)
				checkIfHitEnemy(em);
		} else {
			timeToDeath--;
		}
		return timeToDeath == 0;
	}
	
	private void checkCollision(Collider cld) {
		if (cld.collidesWith(c))
			collided = true;
//			vel.dx *= -1;
	}
	
	private void checkIfHitEnemy(Enemy em) {
		if (em.c.collidesWith(c)) {
			em.damage(30);
			collided = true;
			timeToDeath = 0;
		}
	}
	
	public void drawArrow(Graphics g, int offsetX, int offsetY) {
//		g.setColor(Color.red);
//		g.fillRect(c.box.x - offsetX, c.box.y - offsetY, c.box.width,
//				c.box.height);
		
		if (collided) {
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, timeToDeath / 300.0f));
		}
		g.drawImage(i.getRotatedImage(vel), (int) pos.x - offsetX, (int) pos.y
				- offsetY, (int) pos.x - offsetX + i.getWidth(), (int) pos.y
				- offsetY + i.getHeight(), 0, 0, i.getWidth(), i.getHeight(),
				null);
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
}