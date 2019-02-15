package com.sskcode.platformer.level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.sskcode.platformer.entities.Platform;
import com.sskcode.platformer.physics.Point;

public class LevelObjects {
	public ArrayList<LO> objects;
	
	public LevelObjects() {
		objects = new ArrayList<LO>();
	}
	
	public void createObjects(Platform[] pts) {
		for (Platform p : pts) {
			if (p.box.width > 200) {
				Random r = new Random();
				r.setSeed(100);
				for (int x = p.box.x + r.nextInt(100) + 50; x + 50 < p.box.x + p.box.width; x += LO.inc[r.nextInt(LO.inc.length)]) {
					objects.add(new LO("bush", x, p.box.y));
				}
			}
		}
	}
	
	public void draw(Graphics g, int topY, int leftX) {
		for (LO lo : objects) {
			lo.draw(g, topY, leftX);
		}
	}
	
	public static void readAllInfo(int lvlNum) throws IOException {
		LO.readAllInfo(lvlNum);
	}
}

class LO {
	public int imgNumR, imgNumC;
	public String name;
	public Point pos;
	
	public static BufferedImage[][] allObjects;
	public static TreeMap<String, Integer> numEachType; 
	public static final int[] inc = {50, 100, 150}; // X increments 
	// Choose seed with random, follow ^
	public static String[] types = {"bush"};	
	public LO(String type, int x, int y) {
		name = type;
		imgNumR = indexOf(name);
		imgNumC = (int)(Math.random() * numEachType.get(name));
		pos = new Point(x, y - allObjects[imgNumR][imgNumC].getHeight() + 10);
	}
	
	public void draw(Graphics g, int topY, int leftX) {
		g.drawImage(allObjects[imgNumR][imgNumC], (int)pos.x - leftX, (int)pos.y - topY, null);
	}
	
	public static void readAllInfo(int lvlNum) throws IOException {
		numEachType = new TreeMap<>();
		for (String a : types)
			numEachType.put(a, 0);
		
		allObjects = new BufferedImage[types.length][];
		
		File folder = new File("resources/Levels/" + (lvlNum < 10 ? "0":"") + lvlNum + "/Tiles/LevelObjects/");
		for (File f : folder.listFiles()) {
			for (String a : types) {
				if (f.getName().contains(a)) {
					numEachType.put(a, numEachType.get(a) + 1);
				}
			}
		}
		for (int i = 0; i < types.length; i++) {
			allObjects[i] = new BufferedImage[numEachType.get(types[i])];
			for (int j = 0; j < allObjects[i].length; j++) {
				allObjects[i][j] = ImageIO.read(new File(folder.getPath() + "/" + types[i] + j + ".png"));
			}
		}
	}
	
	public int indexOf(String type) {
		int i = 0;
		for (String t : types) {
			if (type.contains(t))
				return i;
			i++;
		}
		return -1;
	}
}