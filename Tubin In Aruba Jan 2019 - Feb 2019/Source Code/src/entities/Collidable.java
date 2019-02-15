package entities;

import java.awt.geom.Rectangle2D;

public abstract class Collidable extends Entity {
	protected Rectangle2D.Double colBox;
	protected Rectangle2D temp;
	
	public Collidable() {}
	public Collidable(String entityName, double x, double y, double vx, double vy) {
		super(entityName, x, y, vx, vy);
	}

	private void setTempBox() {
		temp = new Rectangle2D.Double(x + colBox.x, y + colBox.y, colBox.width, colBox.height);
	}
	
	public boolean collides(Collidable c) {
		if (c == this)
			return false;
		if (colBox == null) 
			return false;
		if (c.colBox == null)
			return false;
		
		setTempBox();
		c.setTempBox();
		boolean intersects = temp.intersects(c.temp); 
		if (intersects) {
			onCollide(c);
			c.onCollide(this);
		}
		return intersects;
	}
	
	protected void onCollide(Collidable c) {}
}
