package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import input.InputManager;

public class GamePanel extends JPanel {
	
	public static final int WIDTH = 800, HEIGHT = 600;
	
	private GameManager gm;
	
	public GamePanel() {
		createPanel();
		gm = new GameManager();
		addKeyListener(new InputManager());
	}
	
	private void createPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
	}
	
	public void update(double dt) {
		requestFocus();
		gm.update(dt);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		gm.draw((Graphics2D) g);
	}
}
