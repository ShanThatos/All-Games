package coltexpress.train;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

import coltexpress.game.GameFrame;
import coltexpress.game.GameManager;
import coltexpress.player.Player;

public class Train {
	
	// Array of TrainCars
	public TrainCar[] train;
	// Train Track
	private BufferedImage track;
	private int trackX = 0;
	
	// Number of cars
	public static int NUM_TRAIN_CARS;
	
	
	public Train(GameManager gm, int trainSize) throws IOException {
		train = new TrainCar[trainSize];
		NUM_TRAIN_CARS = trainSize;
		// Building Random Train:
		ArrayList<Integer> randOrder = new ArrayList<Integer>();
		for (int i = 1; i < TrainCar.numTypes; i++)
			randOrder.add(i);
		Collections.shuffle(randOrder);
		// Locomotive at the front!
		randOrder.add(0, 0);
		
		for (int i = 0, x = 150, y = 475; i < train.length; i++, x += 130) {
			train[i] = new TrainCar(gm, randOrder.get(i), x, y, i);
			if (i == 0)
				x += 30;
		}
		track = ImageIO.read(new File("Files/Train/Images/Train Track.png"));
	}
	
	public void update() {
		trackX += 10;
		if (trackX >= GameFrame.WIDTH)
			trackX = 0;
	}
	
//	public void addPlayerToTrainCar(int i, Player p) {
//		if (!train[i].allPlys.contains(p))
//			train[i].allPlys.add(p);
//	}
//	public void movePlayerToNewCar(int i1, int i2, Player p) {
//		for (int i = 0; i < train[i1].allPlys.size(); i++)
//			if (train[i1].allPlys.get(i) == p)
//				train[i1].allPlys.remove(i);
//		train[i2].allPlys.add(p);
//	}
	
	public void draw(Graphics g) {
		int trackY = 618;
		g.drawImage(track, trackX - GameFrame.WIDTH, trackY, trackX, trackY + 20, 0, 0, track.getWidth(), track.getHeight(), null);
		g.drawImage(track, trackX, trackY, trackX + GameFrame.WIDTH, trackY + 20, 0, 0, track.getWidth(), track.getHeight(), null);
		drawTrain(g);
	}
	private void drawTrain(Graphics g) {
		for (TrainCar tc : train)
			tc.draw(g);
	}
}
