package entities;

import game.GameManager;

public abstract class Entity {
	
	protected static GameManager gm;
	private static double DT_MULTIPLIER = .000000003;
	protected double x, y, vx, vy;
	protected String entityName;
	protected boolean entityDestroyed;
	
	public Entity() {
		entityName = "Unnamed";
	}
	public Entity(String entityName, double x, double y, double vx, double vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.entityName = entityName;
	}
	
	public abstract void update(double dt);
	
	protected void moveUpdate(double dt) {
		x += vx * dt * DT_MULTIPLIER;
		y += vy * dt * DT_MULTIPLIER;
	}
	
	public boolean isEntityDestroyed() {
		return entityDestroyed;
	}
	
	public String toString() {
		return entityName;
	}
	
	public static void increaseSpeed(double k) {
		DT_MULTIPLIER *= k;
	}
	
	public static void setGameManager(GameManager gm) {
		Entity.gm = gm;
	}
	public static void resetMultiplier() {
		DT_MULTIPLIER = .000000003;
	}
}
