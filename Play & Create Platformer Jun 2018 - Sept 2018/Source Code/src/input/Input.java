package input;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements MouseListener, MouseMotionListener, KeyListener {
	
	// Mouse Inputs
	private static Point mousePos, mousePress, mouseRel;
	private static boolean mousePressed;
	
	// Keyboard Inputs
	private static boolean[] keys = new boolean[512];
	
	public Input() {}
	
	// --- Keyboard
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	// --- Mouse
	@Override
	public void mouseDragged(MouseEvent e) {
		mousePos = e.getPoint();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos = e.getPoint();
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		mousePress = e.getPoint();
		mousePressed = true;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseRel = e.getPoint();
		mousePressed = false;
	}
	
	public static Point mousePos() {
		return mousePos;
	}
	public static Point mousePress() {
		return mousePress;
	}
	public static Point mouseRel() {
		return mouseRel;
	}
	public static boolean isMousePressed() {
		return mousePressed;
	}
	
	public static boolean isKeyDown(KeyEvent e) {
		return keys[e.getKeyCode()];
	}
	public static boolean AK_U() {
		return keys[KeyEvent.VK_UP];
	}
	public static boolean AK_D() {
		return keys[KeyEvent.VK_DOWN];
	}
	public static boolean AK_L() {
		return keys[KeyEvent.VK_LEFT];
	}
	public static boolean AK_R() {
		return keys[KeyEvent.VK_RIGHT];
	}
	public static boolean isSpaceDown() {
		return keys[KeyEvent.VK_SPACE];
	}
}
