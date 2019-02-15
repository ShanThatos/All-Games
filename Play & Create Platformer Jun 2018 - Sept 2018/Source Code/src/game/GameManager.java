package game;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JOptionPane;

import create.Map;
import game.level.Level;
import game.player.Player;

public class GameManager {
	
	// In Game
	private boolean inGame;
	
	// Player
	private Player p;
	// Camera
	private Camera cam;
	// Level
	private Level currentLevel;
	
	public GameManager() {
		try { startLevel("testing.map"); } catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
				    "File testing.map wasn't found or was invalid. ",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	private void startLevel(String fileName) throws ClassNotFoundException, IOException {
		currentLevel = new Level(Map.readObjectFromFile(fileName));
		cam = new Camera();
		cam.setFocus(currentLevel.getPlayerStartPos());
		cam.setBackground(currentLevel.getBackground(), currentLevel.getBackgroundTopLeft());
		p = new Player(currentLevel.getPlayerStartPos());
		inGame = true;
	}
	
	public void update() {
		if (inGame) {
			currentLevel.update();
			cam.update(p.getScreenPosition());
			p.update();
		}
	}
	
	public void display(Graphics2D g) {
		if (inGame) {
			cam.displayEnv(g);
			cam.displaySprites(g, p);
		}
	}
}
