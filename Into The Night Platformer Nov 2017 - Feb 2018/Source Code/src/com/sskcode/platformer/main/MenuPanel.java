package com.sskcode.platformer.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.sskcode.external.imageprocessing.AnimatedGIF;
import com.sskcode.platformer.UI.UIButton;
import com.sskcode.platformer.entities.Platform;
import com.sskcode.platformer.physics.Collider;
import com.sskcode.platformer.physics.Point;

public class MenuPanel extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	// Dimensions
	public static final int WIDTH = 800;//640;
	public static final int HEIGHT = 600;//480;
	
	// Thread
	private Thread thread;
	
	// Game Specs
	public boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// Game Parts
	public boolean ready = false;
	
	// Loading Screen
	private AnimatedGIF playerRunning;
	private Point p;
	private Platform ground;
	private Background bg;
	
	// UI
	public UIButton play;
	
	// Decision
	public boolean playGame = false;
	
	public MenuPanel(MainPanel mnp) {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		setBackground(new Color(0, 0, 0));
		
		setLayout(null);
		
		UIButton.readAllInfo();
		play = new UIButton("Play");
		play.setBounds(250, 290, 80, 40);
		
		add(play);
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		if (!ready) return;
		bg.draw(g);
		playerRunning.draw(g, p);
		ground.draw(g, 0, 0);
		
		play.paintComponent(g);
	}
	
	public void update() {
		if (!ready) return;
		bg.p.x -= 2;
		
		if (bg.p.x + bg.i.getWidth() <= WIDTH)
			bg.p.x += bg.i.getWidth();
		
		playerRunning.nextFrame();
		ground.setX(ground.pos.x - 3);
	}
	
	public void init() {
		playerRunning = new AnimatedGIF();
		playerRunning.read("/Player/0Animations/soldier_run.gif");
		playerRunning.setAnimSpeed(10);
		double scale = 200 / (playerRunning.getImages()[0].getHeight() + 0.0);
		playerRunning.shrinkToSize(200, 200);
		p = new Point(WIDTH / 2 - playerRunning.getImages()[0].getWidth() / 2, HEIGHT/ 2 - playerRunning.getImages()[0].getHeight() / 2);
		
		Scanner in = null;
		try { in = new Scanner(new File("resources/Player/_Info.dat"));} catch (FileNotFoundException e) {}
		
		Collider temp  = new Collider(in.nextInt(),in.nextInt(),in.nextInt(), in.nextInt(), false);
		
		ground = new Platform(0, (int)p.y + (int)(scale * (temp.box.y + temp.box.height)), 100000, HEIGHT / 2 + 100, false);
		bg = new Background();
		
		setBackground(new Color(10, 0, 0));
		
		ready = true;
	}
	
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		running = true;
	}
	
	public void run() {
		init();
		long start, elapsed, wait;
		
		while (running) {
			start = System.nanoTime();
			update();
			elapsed  = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if (wait < 0) wait = 5;
			try { Thread.sleep(wait);} catch(Exception e) {}
			repaint();
		}
	}
}

class Background {
	public Point p;
	public BufferedImage i;
	
	public Background() {
		try { i = ImageIO.read(new File("resources/TitleScreen/background.png")); } catch (IOException e) {}
		p = new Point(0, 0);
	}
	
	public void draw(Graphics g) {
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f));
		g.drawImage(i, (int)p.x - i.getWidth(), (int)p.y, null);
		g.drawImage(i, (int)p.x, (int)p.y, null);
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
