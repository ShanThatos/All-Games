package coltexpress.train;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import coltexpress.game.GameManager;
import coltexpress.loot.Loot;

public class TrainCar {
	
	// GameManager
	private GameManager gm;
	
	// Names, Loot & Image for each Train Car
	private static String[] trainCarNames;
	private static String[] trainCarData;
	private static BufferedImage[] trainCarImages;
	public static int numTypes;
	
	public static BufferedImage wheel;
	public double rotationLOL;
	
	public static BufferedImage steam;
	public static boolean displayingSteam = true;
	public static double steamY;
	// Index for Train Car
	private int trainCarID;
	private int trainCarIndex;
	
	// Showing Loot?
	public boolean showingLoot;
	
	// Graphics Stuff
	public Point pos;
	
	public TrainCar(GameManager gm, int carID, int x, int y, int index) {
		this.gm = gm;
		if (gm.allLoot == null)
			gm.allLoot = new ArrayList<Loot>();
//		allPlys = new ArrayList<Player>();
		trainCarID = carID;
		trainCarIndex = index;
		
		Scanner lootScan = new Scanner(trainCarData[trainCarID]);
		int[] numEach = new int[3];
		for (int i = 0; i < 3; i++)
			numEach[i] = lootScan.nextInt();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < numEach[i]; j++) {
				Random r = new Random();
				int randR = i, randC = r.nextInt(Loot.lootBeingUsed[i].length);
				while (Loot.lootBeingUsed[randR][randC]) {
					randC = r.nextInt(Loot.lootBeingUsed[i].length);
				}
				
				// LootBeingUsed index is marked true in constructor
				gm.allLoot.add(new Loot(randR, randC, index));
			}
		}
		
		lootScan.close();
		pos = new Point(x, y);
	}
	
	public void draw(Graphics g) {
		drawTrainCar(g);
//		drawPlayers(g);
	}
	private void drawTrainCar(Graphics g) {
		BufferedImage bi = trainCarImages[trainCarID];
		if (trainCarID == 0) {
			rotationLOL = (rotationLOL - .1) % Math.PI;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationLOL, wheel.getWidth() / 2, wheel.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			int xx = pos.x + 38, yy = pos.y + 109;
			int xx2 = pos.x + 73;
			int xx3 = pos.x + 140;
			int diameter = 34;
			g.drawImage(op.filter(wheel, null), xx3, yy, xx3 + diameter, yy + diameter, 0, 0, wheel.getWidth(), wheel.getHeight(), null);
			g.drawImage(bi, pos.x - 50, pos.y, pos.x + 210, pos.y + 150, 0, 0, bi.getWidth(), bi.getHeight(), null);
			g.drawImage(op.filter(wheel, null), xx, yy, xx + diameter, yy + diameter, 0, 0, wheel.getWidth(), wheel.getHeight(), null);
			g.drawImage(op.filter(wheel, null), xx2, yy, xx2 + diameter, yy + diameter, 0, 0, wheel.getWidth(), wheel.getHeight(), null);
		}
		else {
			g.drawImage(bi, pos.x, pos.y, pos.x + 210, pos.y + 150, 0, 0, bi.getWidth(), bi.getHeight(), null);
			rotationLOL = (rotationLOL - .1) % Math.PI;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationLOL, wheel.getWidth() / 2, wheel.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			int xx = pos.x + 45, yy = pos.y + 109;
			int xx2 = pos.x + 128;
			int diameter = 34;
			g.drawImage(op.filter(wheel, null), xx, yy, xx + diameter, yy + diameter, 0, 0, wheel.getWidth(), wheel.getHeight(), null);
			g.drawImage(op.filter(wheel, null), xx2, yy, xx2 + diameter, yy + diameter, 0, 0, wheel.getWidth(), wheel.getHeight(), null);
		}
		
		if (showingLoot) {
			int xx = pos.x + 50;
			int i = 0;
			int yy = pos.y + 180;
			for (Loot l : gm.allLoot) {
				if (l.inTrainCar && (l.trainCarID == trainCarIndex) && !l.onTopOfTrain) {
					l.draw(g, xx + (l.trainCarID == 0 ? 55 : 0), yy, 20, 20, false);
					xx += 25;
					i++;
					if (i == 3) {
						i = 0;
						xx = pos.x + 50;
						yy += 25;
					}
				}
			}
			xx = pos.x + 50;
			i = 0;
			yy = pos.y - 80;
			for (Loot l : gm.allLoot) {
				if (l.inTrainCar && (l.trainCarID == trainCarIndex) && l.onTopOfTrain) {
					l.draw(g, xx + (l.trainCarID == 0 ? 55 : 0), yy, 20, 20, false);
					xx += 25;
					i++;
					if (i == 3) {
						i = 0;
						xx = pos.x + 50;
						yy += 25;
					}
				}
			}
		}
	}
	
	public static void readAllTrains() throws IOException {
		Scanner in = new Scanner(new File("Files/Train/allTrainCars.dat"));
		numTypes = in.nextInt();
		in.nextLine();
		trainCarNames = new String[numTypes];
		trainCarData= new String[numTypes];
		trainCarImages = new BufferedImage[numTypes];
		
		for (int i = 0; i < numTypes; i++) {
			trainCarNames[i] = in.next().trim();
			trainCarData[i] = in.nextLine().trim();
		}
		in.close();
		
		String[] tempFileNames = {"Locomotive", "Train Car1", "Train Car1", "Train Car1", "Train Car1", "Train Car1", "Train Car1"};
		
		for (int i = 0; i < numTypes; i++) {
			trainCarImages[i] = ImageIO.read(new File("Files/Train/Images/" + tempFileNames[i] + ".png"));
		}
		wheel = ImageIO.read(new File("Files/Train/Images/Wheel.png"));
		steam = ImageIO.read(new File("Files/Train/Images/Steam.png"));
		steamY = steam.getHeight() - 100;
	}
	
	public void checkShowLoot(Point p) {
		showingLoot = p.x >= pos.x  + 35 && p.x <= pos.x + 165 && p.y >= pos.y + 30 && p.y <= pos.y + 140;
	}
}
