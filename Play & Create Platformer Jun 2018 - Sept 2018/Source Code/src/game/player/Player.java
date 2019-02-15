package game.player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;

import game.entities.EntityType;
import game.entities.UserData;
import game.jbox2d.utils.BodyCreator;
import game.jbox2d.utils.Convert;
import input.Input;

public class Player {
	
	// Body
	private Body body;
	// Settings
	private final float MAX_HORIZ_VEL = 5, RUN_FORCE = 10, JUMP_FORCE = 2.5f;
	private boolean canJump;
	
	// Shape
	private float size = .8f;
	
	public Player(Vec2 center) {
		createBody(center);
	}
	private void createBody(Vec2 center) {
		center.set(center.x, center.y);
		
		Path2D shape = new Path2D.Float();
		shape.moveTo(-size / 2, -size / 2);
		shape.lineTo(size / 2, -size / 2);
		shape.lineTo(size / 2, size / 2);
		shape.lineTo(-size / 2, size / 2);
		shape.closePath();
		body = BodyCreator.createBody(
				BodyCreator.createBodyDef(
						center
						, BodyType.DYNAMIC), 
				BodyCreator.createFixtureDef(BodyCreator.createPolygonShape(shape),
						1.0f, 0.4f, 0.1f));
		
		body.setUserData(new UserData(body, EntityType.PLAYER));
	}
	
	public Vec2 getScreenPosition() {
		Vec2 pos = Convert.W2S(body.getPosition());
		return pos;
	}
	
	public void display(Graphics2D g) {
		Vec2 pos = getScreenPosition();
		g.translate(pos.x, pos.y);
		g.rotate(-body.getAngle());
		g.setColor(Color.black);
		int pixelSize = (int) (size * 64);
		g.fillRect(-pixelSize / 2, -pixelSize / 2, pixelSize, pixelSize);
	}
	
	public void update() {
		move();
	}
	private void move() {
		if (Input.AK_R() ^ Input.AK_L()) 
			body.applyForceToCenter(new Vec2(Input.AK_R() ? RUN_FORCE : -RUN_FORCE, 0));
		if (Input.AK_D() ^ Input.AK_U()) {
			if (Input.AK_U()) {
				if (isTouchingGround())
					canJump = true;
				if (canJump) {
					body.applyLinearImpulse(new Vec2(0, JUMP_FORCE), body.getPosition(), true);
					canJump = false;
				}
			}
		}
		
		Vec2 vel = body.getLinearVelocity();
		if (Math.abs(vel.x) > MAX_HORIZ_VEL) 
			vel.set(vel.x > 0 ? MAX_HORIZ_VEL : -MAX_HORIZ_VEL, vel.y);
	}
	private boolean isTouchingGround() {
		boolean touchingGround = false;
		ContactEdge ce = body.getContactList();
		while (ce != null) {
			Contact c = ce.contact;
			if (c.isTouching() && Vec2.dot(c.getManifold().localNormal, new Vec2(0, 1)) > 0) { 
				touchingGround = true;
				break;
			}
			ce = ce.next;
		}
		return touchingGround;
	}
}

class PlayerContactListener implements ContactListener {

	@Override
	public void beginContact(Contact c) {
		System.out.println(c.getManifold().localNormal);
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