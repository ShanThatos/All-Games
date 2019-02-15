package com.sskcode.platformer.physics;

import java.awt.Rectangle;

public class Collider {
	public Rectangle box;
	public Point pos;
	public boolean collide;
	
	public Collider(int x, int y, int w, int h, boolean c) {
		box = new Rectangle(x, y, w, h);
		pos = new Point(x, y);
		collide = c;
	}
	
	public void update(double dx, double dy) {
		pos.x += dx;
		pos.y += dy;
		box.x = (int)pos.x;
		box.y = (int)pos.y;
	}
	
	public boolean collidesWith(Collider c) {
		return box.intersects(c.box) && collide && c.collide;
	}
	
	public void setX(double x) {
		pos.x = x;
		box.x = (int) x;
	}
	
	public void setY(double y) {
		pos.y = y;
		box.y = (int) y;
	}
	
	public String toString() {
		return "X: " + box.x + ", Y: " + box.y + " Width: " + box.width + " Height: " + box.height;
	}
}
