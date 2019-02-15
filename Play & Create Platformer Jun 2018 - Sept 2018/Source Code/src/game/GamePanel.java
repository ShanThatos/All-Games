package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import input.Input;

public class GamePanel extends JPanel {
	
	public static final int WIDTH = 800, HEIGHT = 600;
	
	private GameManager gm;
	
	public GamePanel() {
		createPanel();
		gm = new GameManager();
	}
	
	private void createPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		Input input = new Input();
		addKeyListener(input);
		addMouseListener(input);
	}
	
	public void update() {
		requestFocus();
		gm.update();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		gm.display((Graphics2D) g); 
	}
}
