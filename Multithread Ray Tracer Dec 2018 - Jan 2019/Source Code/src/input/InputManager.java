package input;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import engine.geom.Vec3;
import game.GamePanel;

public class InputManager implements KeyListener, MouseMotionListener {
	
	// Keys
	private static boolean[] keys = new boolean[256];
	
	// Mouse
	private static Vec3 mouseDiff = new Vec3(0, 0, 0);
	private static Robot mouseController;
	
	public InputManager() {
		try {
			mouseController = new Robot();
		} catch (Exception e) {}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDiff.setDxyz(e.getX() - GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2 - e.getY(), 0);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseDiff.setDxyz(e.getX() - GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2 - e.getY(), 0);
	}
	
	public static Vec3 getMouseDiff() {
		mouseController.mouseMove(GamePanel.centerX, GamePanel.centerY);
		
		return mouseDiff;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		keys[e.getKeyCode()] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	public static boolean[] getKeys() {
		return keys;
	}
}
