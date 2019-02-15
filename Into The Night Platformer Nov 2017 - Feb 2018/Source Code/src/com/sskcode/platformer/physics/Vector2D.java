package com.sskcode.platformer.physics;

public class Vector2D {
	public double dx, dy;
	
	public Vector2D(double x, double y) {
		dx = x;
		dy = y;
	}
	
	public void add(Vector2D v2D) {
		dx += v2D.dx;
		dy += v2D.dy;
	}
	
	public void addX(double dx) {
		this.dx += dx;
	}
	public void addY(double dy) {
		this.dy += dy;
	}
	public void setX(double dx) {
		this.dx = dx;
	}
	public void setY(double dy) {
		this.dy = dy;
	}
	public void multiply(double x) {
		dx *= x;
		dy *= x;
	}
}
