package game;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import engine.EngineManager;
import input.InputManager;

public class GamePanel extends JPanel {
	
	public static final int WIDTH = 800, HEIGHT = 600;
	public static int centerX, centerY;
	
	private GameManager gm;
	private InputManager input;
	
	public GamePanel() {
		createPanel();
		gm = new GameManager();
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "Blank_Cursor");
		setCursor(blankCursor);
		input = new InputManager();
		setFocusable(true);
		addKeyListener(input);
		addMouseMotionListener(input);
	}
	
	private void createPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void update(double deltaTime) {
		requestFocus();
		centerX = this.getLocationOnScreen().x + WIDTH / 2;
		centerY = this.getLocationOnScreen().y + HEIGHT / 2;
		gm.update(deltaTime);
	}
	
	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		g.drawImage(EngineManager.getDisplay(), 0, 0, WIDTH, HEIGHT, null);
	}
}
