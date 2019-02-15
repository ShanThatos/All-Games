package com.sskcode.platformer.level;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.sskcode.external.imageprocessing.AnimatedGIF;
import com.sskcode.platformer.main.GamePanel;
import com.sskcode.platformer.physics.Point;
import com.sskcode.platformer.physics.Vector2D;

public class Cutscene {
	private Character[] chs;
	public Rectangle trigger = null;
	public boolean playOnStart = false;
	private BufferedImage background;
	
	private Script spt;
	
	public boolean cutsceneDone = false;
	public int timeToDeath = 50;
	
	public Cutscene(Scanner in) throws IOException {
		// Background
		in.next(); getBackground(in.nextLine());
		
		// Trigger
		in.next(); 
		String[] lin = in.nextLine().trim().split(" ");
		if (lin.length == 1)
			playOnStart = lin[0].trim().equals("START");
		else {
			trigger = new Rectangle(Integer.parseInt(lin[0]), Integer.parseInt(lin[1]), Integer.parseInt(lin[2]), Integer.parseInt(lin[3]));
		}
		// Characters
		readCharacters(in);
		// Lines
		spt = new Script(in);
	}
	
	public void getBackground(String fileLoc) throws IOException {
		background = ImageIO.read(new File(fileLoc.trim()));
		
	}
	
	public void readCharacters(Scanner in) throws IOException {
		in.next();
		chs = new Character[in.nextInt()];
		for (int i = 0; i < chs.length; i++) {
			chs[i] = new Character(in.next().replace(":", ""), in.nextInt(), in.nextInt(), in.next().equals("R"), in.nextInt(), in.nextInt(), in.nextInt());
			in.nextLine();
			chs[i].addActions(in);
		}
	}
	
	public void update() {
		spt.update(chs);
		for (int i = 0; i < chs.length; i++) {
			chs[i].update();
		}
		if (spt.done)
			timeToDeath--;
		cutsceneDone = timeToDeath <= 0;
	}
	
	public void draw(Graphics g) {
		if (!spt.done) {
			g.drawImage(background, 0, 0, null);
			g.setColor(Color.black);
			g.fillRect(0, 0, GamePanel.WIDTH, 100);
			g.fillRect(0, GamePanel.HEIGHT - 100, GamePanel.WIDTH, 100);
			for (Character c : chs)
				c.draw(g);
			spt.drawCurrentLine(g);
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)((50 - timeToDeath) / 50.0)));
			g.setColor(Color.white);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
}

class Character {
	public String name;
	public Point pos, dest;
	public Vector2D vel;
	public boolean facingRight;
	public Action[] acts;
	public int currentAct = 0;
	public int sX, sY;
	
	public Character(String name, int x, int y, boolean facingRight, int numActions, int shrinkSizeX, int shrinkSizeY) {
		this.name = name;
		pos = new Point(x, y);
		dest = new Point(x, y);
		this.facingRight = facingRight;
		acts = new Action[numActions];
		sX = shrinkSizeX;
		sY = shrinkSizeY;
		vel = new Vector2D(0, 0);
	}
	
	public void update() {
		if (dest.distance(pos) > 5) {
			vel.dx = dest.x < pos.x ? -2 : 2;
			currentAct = 1;
		} else {
			vel.dx = 0;
			currentAct = 0;
		}
		pos.x += vel.dx;
		acts[currentAct].update(facingRight);
	}
	
	public void addActions(Scanner in) throws IOException {
		for (int i = 0; i < acts.length; i++) {
			acts[i] = new Action(in.next(), in.next().equals("PNG"), in.nextLine(), sX, sY);
		}
	}
	
	public void draw(Graphics g) {
		acts[currentAct].draw(g, pos, facingRight);
	}
}

class Action {
	public String actionName;
	public AnimatedGIF ag;
	public AnimatedGIF flipped;
	
	public Action(String actionName, boolean onePNG, String fileLoc, int sX, int sY) throws IOException {
		this.actionName = actionName;
		ag = new AnimatedGIF();
		if (onePNG)
			ag.read(ImageIO.read(new File(fileLoc.trim())));
		else {
			ag.read(fileLoc.trim());
		}
		ag.shrinkToSize(sX, sY);
		flipped = new AnimatedGIF();
		flipped.flip(ag.getImages());
	}
	
	public void update(boolean fR) {
		if (fR)
			ag.nextFrame();
		if (!fR)
			flipped.nextFrame();
	}
	
	public void draw(Graphics g, Point pos, boolean fR) {
		if (fR) {
			ag.draw(g, pos);
		} else {
			flipped.draw(g, pos);
		}
	}
}

class Script {
	public ArrayList<ScriptLine> lines;
	public static BufferedImage txtBub;
	public int currentLine;
	
	public boolean done;
	
	public Script(Scanner in) {
		lines = new ArrayList<>();
		currentLine = 0;
		int numLines = in.nextInt();
		in.nextLine();
		for (int i = 0; i < numLines; i++) {
			String speaker = in.next().replace(":", "");
			String[] cuts = in.nextLine().trim().split("~");
			
			for (String ct : cuts)
				lines.add(new ScriptLine(speaker, ct));
		}
		try {
			txtBub = ImageIO.read(new File("resources/TitleScreen/TextBubble.png"));
		} catch (IOException e) {}
	}
	
	public void update(Character[] chs) {
		if (currentLine >= lines.size()) {
			done = true;
			return;
		}
		lines.get(currentLine).update();
		if (lines.get(currentLine).done) {
			currentLine++;
		}
		
		if (currentLine >= lines.size()) {
			done = true;
			return;
		}
		
		if (lines.get(currentLine).action) {
			for (int i = 0; i < chs.length; i++) {
				if (!lines.get(currentLine).actionDone && chs[i].name.equals(lines.get(currentLine).speaker)) {
					chs[i].dest.x = chs[i].dest.x + lines.get(currentLine).effect.dx;
					chs[i].currentAct = lines.get(currentLine).anim - 1;
					chs[i].facingRight = lines.get(currentLine).facingRight;
					lines.get(currentLine).actionDone = true;
				}
			}
		}
	}
	
	public void drawCurrentLine(Graphics g) {
		if (currentLine >= lines.size())
			return;
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .9f));
		
		int x = 20, y = GamePanel.HEIGHT - txtBub.getHeight() - 20;
		int spaceWidth = 10;
		Font font  = new Font("Arial", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.black);
		
		if (lines.get(currentLine).action)
			return;
		g.drawImage(txtBub, x, y, x + GamePanel.WIDTH - 40, y + txtBub.getHeight(), 0, 0, txtBub.getWidth(), txtBub.getHeight(), null);
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f));
		
		FontMetrics temp = g.getFontMetrics();
		int xMin = 40, xMax = x + GamePanel.WIDTH - 50;
		g.drawString(lines.get(currentLine).speaker + ":", xMin, y + 10 + temp.getHeight());
		for (int i = 0, cX = xMin, cY = y + 40; i < lines.get(currentLine).revealed; i++) {
			if (i >= lines.get(currentLine).line.length)
				break;
			ScriptLine sl = lines.get(currentLine);
			if (cX + temp.stringWidth(sl.line[i]) >= xMax) {
				cX = xMin;
				cY += temp.getHeight() + 5;
			}
			g.drawString(sl.line[i], cX, cY + temp.getHeight());
			cX += temp.stringWidth(sl.line[i]) + spaceWidth;
		}
		
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}

class ScriptLine {
	public String speaker;
	public String[] line;
	public int revealed = 0;
	public int revealSpeed = 10, cR = 0, timeToDeath = 100;
	public boolean done = false;
	
	// Action
	public boolean action, actionDone = false, facingRight;
	public int anim;
	public Vector2D effect;
	
	public ScriptLine(String speaker, String line) {
		this.speaker = speaker.trim();
		this.line = line.trim().split(" ");
		action = speaker.contains("<");
		if (action) {
			anim = Integer.parseInt(speaker.substring(1, 2));
			this.speaker = this.line[0];
			effect = new Vector2D(Integer.parseInt(this.line[1]), Integer.parseInt(this.line[2]));
			facingRight = this.line[3].contains("R");
		}
	}
	
	public void update() {
		cR++;
		if (cR == revealSpeed) {
			revealed++;
			cR = 0;
		}
		if (revealed >= line.length) {
			timeToDeath--;
			done = timeToDeath <= 0;
		}
	}
}