package game;

import java.awt.event.KeyEvent;

import engine.geom.Orbital;
import engine.geom.Point3D;
import engine.geom.SMath;
import engine.geom.Vec3;
import input.InputManager;

public class Camera {
	
	// Settings
	public static final double FOV = Math.toRadians(70);
	
	// Location
	private Point3D center;
	private Orbital rot;
	
	public Camera() {
		setCenter(new Point3D(0, 0, 150));
		setRot(new Orbital(0, 0));
	}
	
	public void update(double deltaTime) {
		move(deltaTime);
		rotate(deltaTime);
	}
	
	private void move(double deltaTime) {
		boolean[] keys = InputManager.getKeys();
		boolean forward = keys[KeyEvent.VK_W], 
				backward = keys[KeyEvent.VK_S], 
				right = keys[KeyEvent.VK_D], 
				left = keys[KeyEvent.VK_A], 
				up = keys[KeyEvent.VK_SPACE], 
				down = keys[KeyEvent.VK_SHIFT];
		
		Vec3 vel = new Vec3(0, 0, 0);
		if (forward ^ backward) {
			double ang = rot.getR1() + (backward ? Math.PI : 0);
			Vec3 vt = new Vec3(Math.cos(ang), Math.sin(ang), 0);
			vel = SMath.addVec(vel, vt);
		}
		if (right ^ left) {
			double ang = rot.getR1() + (left ? Math.PI / 2 : -Math.PI / 2);
			Vec3 vt = new Vec3(Math.cos(ang), Math.sin(ang), 0);
			vel = SMath.addVec(vel, vt);
		}
		if (up ^ down) {
			vel = SMath.addVec(vel, new Vec3(0, 0, up ? 1 : -1));
		}
		if (vel.getLengthSquared() > 0) {
			vel = vel.getUnitVector().getScaledVector(deltaTime);
			center.translate(vel.getDx(), vel.getDy(), vel.getDz());
		}
	}
	private void rotate(double deltaTime) {
		Vec3 mouseDiff = InputManager.getMouseDiff();
		rot.setR1(rot.getR1() - deltaTime * (mouseDiff.getDx() / GamePanel.WIDTH));
		rot.setR2(Math.max(Math.min(rot.getR2() + deltaTime * (mouseDiff.getDy() / GamePanel.HEIGHT), Math.PI / 2), -Math.PI / 2));
	}

	public Point3D getCenter() {
		return center;
	}
	public Orbital getRot() {
		return rot;
	}
	public void setCenter(Point3D center) {
		this.center = center;
	}
	public void setRot(Orbital rot) {
		this.rot = rot;
	}
}
