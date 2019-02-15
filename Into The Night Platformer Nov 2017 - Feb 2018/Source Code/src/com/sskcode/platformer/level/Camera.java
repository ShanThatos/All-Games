package com.sskcode.platformer.level;

import java.awt.Graphics;

import com.sskcode.platformer.entities.Player;
import com.sskcode.platformer.physics.Collider;
import com.sskcode.platformer.physics.Point;

public class Camera {
	// Camera box
	private Collider c;
	
	// Movement
	private int xThresMin, xThresMax; 
	private int yThresMin, yThresMax;
	
	// Player
	private Player p;
	
	// Level
	private Level l;
	
	public Camera(int x, int y, int w, int h, Player p, Level l) {
		c = new Collider(x, y, w, h, true);
		this.p = p;
		this.l = l;
		
		xThresMin = w / 2 - 100;
		xThresMax = w / 2 + 100;
		yThresMin = h / 2 - 50;
		yThresMax = h / 2 + 50;
	}
	
	public void update() {
		if (p.getPosition().x > c.pos.x + xThresMax) {
			c.setX(p.getPosition().x - xThresMax);
		}
		else if (p.getPosition().x < c.pos.x + xThresMin) {
			c.setX(p.getPosition().x - xThresMin);
		}
		if (p.getPosition().y > c.pos.y + yThresMax) {
			c.setY(p.getPosition().y - yThresMax);
		}
		else if (p.getPosition().y < c.pos.y + yThresMin) {
			c.setY(p.getPosition().y - yThresMin);
		}
	}
	
	public Point getPos() {
		return c.pos;
	}
	
	public void render(Graphics g) {
		
		l.drawBackGround(g, c);
		l.draw(g, c);
		p.draw(g, (int)c.pos.x, (int)c.pos.y);
	}
}
