package com.sskcode.platformer.entities;

import java.awt.Rectangle;

import com.sskcode.platformer.physics.Point;

public class Trigger {
	public Rectangle box;
	public Point pos;
	
	public Trigger(int x, int y, int w, int h) {
		box = new Rectangle(x, y, w, h);
		pos = new Point(x, y);
	}
	
	public void update(double dx, double dy) {
		pos.x += dx;
		pos.y += dy;
		box.x = (int)pos.x;
		box.y = (int)pos.y;
	}
}
