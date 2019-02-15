package coltexpress.game;

import java.awt.Dimension;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;

import coltexpress.frame.EndPanel;
import coltexpress.main.Runner;

public class GameFrame extends JFrame {
	
	// Random Stuff
	private static final long serialVersionUID = 1L;
	
	// Dimensions
	public static final int WIDTH = 1000, HEIGHT = 800;
	
	public static GamePanel gp;
	
	public GameFrame(String title) throws ConcurrentModificationException{
		super(title);
		gp = new GamePanel();
		setContentPane(gp);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(new Dimension(1000, 800));
		setLocation(700, 100);
		setVisible(true);
		
		while (!gp.gm.gameDone) {
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		if (Runner.debugMode)
			System.out.println("GAME ENDED");
		setContentPane(new EndPanel(gp.gm.animIDWinner, gp.gm.lootGained));
		revalidate();
		getContentPane().repaint();
	}
}
