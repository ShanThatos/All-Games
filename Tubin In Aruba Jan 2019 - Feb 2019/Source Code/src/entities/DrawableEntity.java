package entities;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class DrawableEntity extends Collidable implements Comparable<DrawableEntity> {
	
	protected static final boolean drawColBoxes = false;
	
	private static final String[] priority = {"Unnamed", "Background", "Boulder", "Coin", "Player", "UI2", "UI1"};
	
	protected int drawingPriority;
	
	public DrawableEntity() {}
	public DrawableEntity(String entityName, double x, double y, double vx, double vy) {
		super(entityName, x, y, vx, vy);
	}
	
	private int getDrawingPriority() {
		if (drawingPriority == 0) { 
			for (int i = 0; i < priority.length; i++)
				if (priority[i].equals(entityName)) {
					drawingPriority = i;
					return drawingPriority;
				}
			return drawingPriority = priority.length;
		}
		return drawingPriority;
	}
	public int compareTo(DrawableEntity o) {
		int diff = getDrawingPriority() - o.getDrawingPriority();
		if (diff != 0)
			return diff;
		return 1;
	}
	public abstract void draw(Graphics2D gr);
	protected void drawColBox(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		if (drawColBoxes && colBox != null) {
			g.translate(x, y);
			g.setColor(new Color(180, 0, 0, 80));
			g.fill(colBox);
		}
	}
}
