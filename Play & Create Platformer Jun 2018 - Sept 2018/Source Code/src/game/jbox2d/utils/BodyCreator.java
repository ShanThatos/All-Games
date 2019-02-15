package game.jbox2d.utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import game.level.GameWorld;

public class BodyCreator {
	
	private static GameWorld world;
	
	public static void setGameWorld(GameWorld world) {
		BodyCreator.world = world;
	}
	public static GameWorld getGameWorld() {
		return world;
	}
	
	public static PolygonShape createPolygonShape(Path2D path) {
		PolygonShape ps = new PolygonShape();
		ArrayList<Vec2> vertices = new ArrayList<>();
		
		PathIterator it = path.getPathIterator(AffineTransform.getScaleInstance(1, 1));
		
		while (!it.isDone()) {
			float[] coords = new float[2];
			int type = it.currentSegment(coords);
			if (type != PathIterator.SEG_CLOSE)
				vertices.add(new Vec2(coords[0], coords[1]));
			it.next();
		}
		
		Vec2[] verts = new Vec2[vertices.size()];
		for (int i = 0; i < verts.length; i++)
			verts[i] = vertices.get(i);
		ps.set(verts, verts.length);
		return ps;
	}
	
	public static BodyDef createBodyDef(Vec2 center, BodyType bt) {
		BodyDef bd = new BodyDef();
		bd.setPosition(center);
		bd.setType(bt);
		return bd;
	}
	
	public static FixtureDef createFixtureDef(Shape shape, float density, float friction, float restitution) {
		FixtureDef fd = new FixtureDef();
		fd.setShape(shape);
		fd.setDensity(density);
		fd.setFriction(friction);
		fd.setRestitution(restitution);
		return fd;
	}
	
	public static Body createBody(BodyDef bd, FixtureDef fd) {
		Body body = world.createBody(bd);
		body.createFixture(fd);
		return body;
	}
}
