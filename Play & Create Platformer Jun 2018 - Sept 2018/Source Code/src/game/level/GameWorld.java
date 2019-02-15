package game.level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import game.jbox2d.utils.BodyCreator;
import main.MainFrame;

public class GameWorld extends World {
	
	private static final Vec2 GRAVITY = new Vec2(0f, -12f);
	
	public GameWorld() {
		super(GRAVITY);
		BodyCreator.setGameWorld(this);
//		new WorldFrame(this);
	}
	
	public void update() {
		step(1f / MainFrame.FPS, 40, 30);
	}
}

class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact c) {
		
	}
	@Override
	public void endContact(Contact c) {
		
	}
	@Override
	public void postSolve(Contact c, ContactImpulse ci) {
		
	}
	@Override
	public void preSolve(Contact c, Manifold m) {
		
	}
}


class WorldFrame extends JFrame implements Runnable {
	
	public WorldFrame(GameWorld world) {
		super("JBox2D World");
		WorldPanel worldPanel = new WorldPanel(world);
		setContentPane(worldPanel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		(new Thread(this)).start();
	}
	
	@Override
	public void run() {
		while (true) {
			repaint();
			try { Thread.sleep(16); } catch (Exception e) {}
		}
	}
}

class WorldPanel extends JPanel {
	
	// Dimensions
	public static final int WIDTH = 800, HEIGHT = 600;
	
	// GameWorld
	private GameWorld world;
	
	public WorldPanel(GameWorld world) {
		super();
		this.world = world;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.black);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.translate(WIDTH / 2, 0);
		AffineTransform old = g.getTransform();
		
		Body body = world.getBodyList();
		while (body != null) {
			g.setTransform(old);
			Vec2 pos = Convert2.W2S(body.getPosition());
			g.translate(pos.x, pos.y);
			g.setColor(Color.white);
			g.fillRect(-1, -1, 2, 2);
			g.rotate(-body.getAngle());
			renderSingleBody(g, body);
			body = body.getNext();
		}
	}
	private void renderSingleBody(Graphics2D g, Body body) {
		Fixture fixture = body.getFixtureList();
		while (fixture != null) {
			switch (fixture.getType()) {
			case CHAIN:
				break;
			case CIRCLE:
				break;
			case EDGE:
				break;
			case POLYGON:
				drawPolygonShape(g, (PolygonShape) fixture.getShape());
				break;
			default:
				break;
			}
			fixture = fixture.getNext();
		}
	}
	private void drawPolygonShape(Graphics2D g, PolygonShape ps) {
		Path2D path = new Path2D.Double();
		Vec2 v1 = ps.getVertex(0);
		path.moveTo(v1.x, -v1.y);
		for (int i = 1; i < ps.getVertexCount(); i++) {
			Vec2 vi = ps.getVertex(i);
			path.lineTo(vi.x, -vi.y);
		}
		path.closePath();
		
		g.setColor(Color.white);
		g.draw(path.createTransformedShape(AffineTransform.getScaleInstance(Convert2.pixelPerMeter, Convert2.pixelPerMeter)));
	}
}

class Convert2 {
	
	public static final int pixelPerMeter = 16;
	
	public static Vec2 W2S(Vec2 v) {
		Vec2 ret = v.clone();
		ret.mulLocal(pixelPerMeter);
		ret.y = WorldPanel.HEIGHT - ret.y;
		return ret;
	}
	public static float W2S(float d) {
		return d * pixelPerMeter;
	}
	
	public static Vec2 S2W(Vec2 v) {
		Vec2 ret = v.clone();
		ret.y = WorldPanel.HEIGHT - ret.y;
		ret.mulLocal(1f / pixelPerMeter);
		return ret;
	}
	public static float S2W(float d) {
		return d / pixelPerMeter;
	}
}