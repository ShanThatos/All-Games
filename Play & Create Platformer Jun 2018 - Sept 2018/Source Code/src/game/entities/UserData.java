package game.entities;

import java.util.Set;
import java.util.TreeSet;

import org.jbox2d.dynamics.Body;

public class UserData {
	
	private static int ids = 0;
	
	private int dataID;
	private Set<EntityType> type;
	
	private Body body;
	
	
	
	public UserData(Body body, EntityType type) {
		dataID = ids++;
		this.body = body;
		this.type = new TreeSet<>();
		this.type.add(type);
	}
	
	public int getID() {
		return dataID;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	public Body getBody() {
		return body;
	}
	
	public void addType(EntityType type) {
		this.type.add(type);
	}
	public void removeType(EntityType type) {
		this.type.remove(type);
	}
	public boolean isType(EntityType type) {
		return this.type.contains(type);
	}
}
