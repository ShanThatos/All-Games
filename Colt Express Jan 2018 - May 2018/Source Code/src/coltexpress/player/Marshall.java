package coltexpress.player;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import coltexpress.game.GameManager;

public class Marshall {
	
	// GameManager
	public static GameManager gm;
	
	// Train Car
	public int currentTrainCar;
	public boolean onTopOfTrain = false;
	
	// Animator
	public Animator animator;
	
	public Marshall(GameManager gm) throws IOException {
		Marshall.gm = gm;
		currentTrainCar = 0;
		
		animator = new Animator(6);
	}
	
	public void draw(Graphics g, int x, int y) {
//		int xx = gm.tr.train[currentTrainCar].pos.x + 70 + (currentTrainCar == 0 ? 50 : 0);
//		int yy = onTopOfTrain ? 480 : 548 + (currentTrainCar == 0 ? 20 : 0);
		
		if (!animator.animating)
			animator.drawPlayer(g, new Point(x, y));
		else 
			animator.drawPlayerAnimation(g);
	}
}
