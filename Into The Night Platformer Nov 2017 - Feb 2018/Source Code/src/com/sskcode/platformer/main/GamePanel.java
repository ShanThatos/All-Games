package com.sskcode.platformer.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.JPanel;

import com.sskcode.platformer.entities.Player;
import com.sskcode.platformer.level.Camera;
import com.sskcode.platformer.level.Level;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	
	// Dimensions
	public static final int WIDTH = 800;//640;
	public static final int HEIGHT = 600;//480;
	
	// Thread
	private Thread thread;
	
	// Game Specs
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// Game Parts
	private boolean ready = false;
	private Level l;
	private Player p;
	private Camera c;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_ESCAPE)
					System.exit(0);
				if (ready)
					p.addKey(k);
				if (l.inCutscene) {
					if (k.getKeyCode() == KeyEvent.VK_ENTER) {
						l.cutscenes[l.currentScene].cutsceneDone = true;
						l.inCutscene = false;
						l.cutscenes[l.currentScene] = null;
					}
				}
			}
			public void keyReleased(KeyEvent k) {
				if (ready)
					p.removeKey(k);
			}
			public void keyTyped(KeyEvent k) {}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (ready && !l.inCutscene)
					p.updateBow(true, e.getX(), e.getY(), c.getPos().x, c.getPos().y);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				if (ready && !l.inCutscene)
					p.updateBow(false, e.getX(), e.getY(), c.getPos().x, c.getPos().y);
			}
		});
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if (ready && !l.inCutscene)
					p.updateBow(true, e.getX(), e.getY(), c.getPos().x, c.getPos().y);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (ready && !l.inCutscene) {
					p.updateBow(false, e.getX(), e.getY(), c.getPos().x, c.getPos().y);
					p.shoot();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		if (!ready) return;
		if (!l.inCutscene)
			c.render(g);
		else {
			l.drawCutscene(g);
		}
	}
	
	public void update() {
//		System.out.println("MAX: " + Runtime.getRuntime().maxMemory());
//		System.out.println("TOTAL: " + Runtime.getRuntime().totalMemory());
//		System.out.println("FREE: " + Runtime.getRuntime().freeMemory());
		if (!l.inCutscene ) {
			p.update(l.clds, l.ems);
			l.update(p);
			c.update();
		} else {
			l.updateCutscene();
		}
	}
	
	public void init() {
		requestFocus();
		try {
			l = new Level(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		p = new Player(l.startingPos.x, l.startingPos.y);
		c = new Camera(0, 0, WIDTH, HEIGHT, p, l);
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
