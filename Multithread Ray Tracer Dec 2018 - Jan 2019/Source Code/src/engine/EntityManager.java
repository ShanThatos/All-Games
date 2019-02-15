package engine;

import java.awt.Color;
import java.util.LinkedList;

import engine.geom.Point3D;

public class EntityManager {
	
	private static LinkedList<Entity> entities;
	
	public static void init() {
		entities = new LinkedList<Entity>();
		entities.add(new Entity("Sphere", new Point3D(110, 0, 0), 5, Color.red));
		entities.add(new Entity("Sphere", new Point3D(130, 0, 0), 8, Color.red));
		entities.add(new Entity("Sphere", new Point3D(145, 0, 0), 8, Color.blue));
		entities.add(new Entity("Sphere", new Point3D(160, 0, 0), 8, Color.red));
		entities.add(new Entity("Sphere", new Point3D(200, 0, 0), 30, new Color(255, 204, 102)));
		
//		entities.add(new Entity("Cube", new Point3D(100, 20, 100), 40, Color.white));
		
	}
	
	public static LinkedList<Entity> getEntities() {
		return entities;
	}
}
