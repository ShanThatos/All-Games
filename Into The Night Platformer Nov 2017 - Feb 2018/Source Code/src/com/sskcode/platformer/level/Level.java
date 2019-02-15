package com.sskcode.platformer.level;

import com.sskcode.platformer.enemies.Enemy;
import com.sskcode.platformer.entities.Ground;
import com.sskcode.platformer.entities.Platform;
import com.sskcode.platformer.entities.Player;
import com.sskcode.platformer.entities.Wall;
import com.sskcode.platformer.main.GamePanel;
import com.sskcode.platformer.physics.Collider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Level {

	// Colliders
	public Platform[] clds;

	// Enemies
	public Enemy[] ems;

	// Info
	private int levelNum;
	public Point startingPos;

	// Background
	private Background bg;
	
	// Level Objects
	public LevelObjects LOS;
	
	// Cutscene Info
	public Cutscene[] cutscenes;
	private String[] cutsceneInfo;
	public int currentScene = 0;
	public boolean inCutscene = false;

	public Level(int lN) throws IOException {
		levelNum = lN;
		Scanner in = null;
		in = new Scanner(new File("resources/Levels/"
				+ (levelNum < 10 ? "0" : "") + levelNum + "/_Info.dat"));
		startingPos = new Point(in.nextInt(), in.nextInt());
		readColliders(in);
		if (!Enemy.infoRead)
			Enemy.readAllInfo();
		readEnemies(in);
		bg = new Background(levelNum);
		
		LevelObjects.readAllInfo(lN);
		LOS = new LevelObjects();	
		LOS.createObjects(clds);
		
		readCutscenes(in);
		
	}

	public void readColliders(Scanner in) throws IOException {
		int nC = in.nextInt();
		clds = new Platform[nC];
		for (int i = 0; i < nC; i++) {
			String type = in.next();
			int x = in.nextInt(), y = in.nextInt();
			int w = in.nextInt(), h = in.nextInt();
			boolean collide = in.next().equals("Y");
			in.nextLine();
			switch (type) {
			case "Wall":
				clds[i] = new Wall(x, y, w, h, collide);
				break;
			case "Ground":
				clds[i] = new Ground(x, y, w, h, collide);
				break;
			default:
				clds[i] = new Platform(x, y, w, h, collide);
			}
		}
		in.nextLine();
	}

	public void readEnemies(Scanner in) {
		int nE = in.nextInt();
		ems = new Enemy[nE];
		for (int i = 0; i < nE; i++) {
			String type = in.next();
			ems[i] = new Enemy(in.nextInt(), in.nextInt(), type);
		}
		in.nextLine();
		in.nextLine();
	}
	
	public void readCutscenes(Scanner in) throws IOException {
//		cutscenes = new Cutscene[in.nextInt()];
//		in.nextLine();
//		for (int i = 0; i < cutscenes.length; i++) {
//			cutscenes[i] = new Cutscene(in);
//			if (cutscenes[i].playOnStart)
//				inCutscene = true;
//		}
		cutsceneInfo = new String[in.nextInt()];
		cutscenes = new Cutscene[cutsceneInfo.length];
		in.nextLine();
		for (int i = 0; i < cutsceneInfo.length; i++) {
			String temp = "";
			temp += in.nextLine() + "\n";
			temp += in.nextLine() + "\n";
			if (temp.contains("START"))
				inCutscene = true;
			String chs = in.nextLine() + "\n";
			temp += chs;
			int numChar = Integer.parseInt(chs.split(" ")[1]);
			for (int j = 0; j < numChar; j++) {
				String ch = in.nextLine() + "\n";
				temp += ch;
				int numActs = Integer.parseInt(ch.split(" ")[4]);
				for (int k = 0; k < numActs; k++) {
					temp += in.nextLine() + "\n";
				}
			}
			int numLines = in.nextInt();
			in.nextLine();
			temp += numLines + "\n";
			for (int j = 0; j < numLines; j++)
				temp += in.nextLine() + "\n";
			cutsceneInfo[i] = temp;
		}
	}
	
	public void updateCutscene() {
		if (cutscenes[currentScene] == null) {
			try {
				cutscenes[currentScene] = new Cutscene(new Scanner(cutsceneInfo[currentScene]));
			} catch (IOException e) {}
		}
		cutscenes[currentScene].update();
		inCutscene = !cutscenes[currentScene].cutsceneDone;
		if (!inCutscene) {
			cutscenes[currentScene] = null;
		}
	}
	
	public void update(Player p) {
		updateEnemies(p);
	}
	
	private void updateEnemies(Player p) {
		ArrayList<Enemy> temp = new ArrayList<Enemy>();
		for (Enemy em : ems) {
			em.update(clds, p);
			if (!em.isDead()) {
				temp.add(em);
			}
		}
		ems = new Enemy[temp.size()];
		temp.toArray(ems);
	}
	
	public void drawBackGround(Graphics g, Collider c) {
		bg.draw(g);
		LOS.draw(g, c.box.y, c.box.x);
	}

	public void draw(Graphics g, Collider c) {
		for (Platform p : clds) {
			if (c.box.intersects(p.box)) {
				p.draw(g, (int) c.pos.x, (int) c.pos.y);
			}
		}
		drawEnemies(g, c);
	}

	private void drawEnemies(Graphics g, Collider c) {
		if (ems != null) {
			for (Enemy e : ems) {
				if (e != null) {
					e.draw(g, (int) c.pos.x, (int) c.pos.y);
				}
			}
		}
	}

	public void drawCutscene(Graphics g) {
		cutscenes[currentScene].draw(g);
	}
}

class Background {
	private BufferedImage[] elem;

	public Background(int lvlN) throws IOException {
		File folder = new File("resources/Levels/" + (lvlN < 10 ? "0" : "")
				+ lvlN + "/Background/");
		elem = new BufferedImage[folder.list().length];
		int i = 0;
		for (File f : folder.listFiles())
			elem[i++] = ImageIO.read(f);
	}

	public void draw(Graphics g) {
		for (BufferedImage i : elem) {
			g.drawImage(i, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, 0, 0,
					i.getWidth(), i.getHeight(), null);
		}
	}
}