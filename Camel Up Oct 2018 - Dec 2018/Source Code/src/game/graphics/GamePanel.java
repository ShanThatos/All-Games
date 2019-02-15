package game.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import game.board.GameManager;
import game.board.RoundEndDisplay;
import game.input.GraphicsHandler;

public class GamePanel extends JPanel {
	
	// Game Manager
	private GameManager gm;
	
	// Graphics Handler
	private GraphicsHandler input;
	
	public GamePanel(GameManager gm, GraphicsHandler input) {
		super();
		this.gm = gm;
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT));
		this.input = input;
		
		addMouseListener(input);
		addMouseMotionListener(input);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gm.drawGame(g);
		if (RoundEndDisplay.isDisplaying()) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f));
			gg.setColor(Color.black);
			gg.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
		}
		input.drawInputs(g);
	}
}
