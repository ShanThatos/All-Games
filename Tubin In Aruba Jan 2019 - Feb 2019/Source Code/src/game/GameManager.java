package game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import entities.Background;
import entities.Boulder;
import entities.Coin;
import entities.DrawableEntity;
import entities.Entity;
import entities.Player;
import entities.ScoreBoard;

public class GameManager {
	
	private long t;
	
	private Player ply;
	private Background bg;
	
	private LinkedList<DrawableEntity> drawables;
	
	public final int[] yLanes = {GamePanel.HEIGHT / 2 - 64, GamePanel.HEIGHT / 2, GamePanel.HEIGHT / 2 + 64, GamePanel.HEIGHT / 2 + 128};
	
	public GameManager() {
		Entity.resetMultiplier(); 
		Entity.setGameManager(this);
		ply = new Player();
		drawables = new LinkedList<>();
		bg = new Background();
		drawables.add(new ScoreBoard(ply));
		drawables.add(ply);
		drawables.add(bg);
	}
	
	public void update(double dt) {
		checkCols();
		for (int i = drawables.size() - 1; i >= 0; i--) { 
			drawables.get(i).update(dt);
			if (drawables.get(i).isEntityDestroyed()) 
				drawables.remove(i);
		}
		if (System.nanoTime() - t > 500000000) { 
			ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
			Collections.shuffle(temp);
			drawables.add(new Coin(GamePanel.WIDTH, yLanes[temp.get(0)], (int) (Math.random() * 3)));
			drawables.add(new Boulder(GamePanel.WIDTH, yLanes[temp.get(1)] - 32, 0));
			if (Math.random() < .3)
				drawables.add(new Boulder(GamePanel.WIDTH, yLanes[temp.get(2)] - 32, 0));
			Entity.increaseSpeed(1.01);
			t = System.nanoTime();
			Collections.sort(drawables);
		}
	}
	private void checkCols() {
		for (int i = 0; i < drawables.size(); i++) 
			for (int j = i + 1; j < drawables.size(); j++)
				drawables.get(i).collides(drawables.get(j));
	}
	
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		for (int i = 0; i < drawables.size(); i++) 
			drawables.get(i).draw(g);
	}
}
