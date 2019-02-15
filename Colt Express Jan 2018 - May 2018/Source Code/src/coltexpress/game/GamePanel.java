package coltexpress.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import coltexpress.audio.AudioManager;
import coltexpress.player.Animator;


public class GamePanel extends JPanel implements Runnable {

	// Random Stuff
	private static final long serialVersionUID = 1L;

	// Thread to control automatic background separately from actual game thread
	private Thread thread;
	// Game Specs
	private boolean running, ready;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	// Background
	private Background bg;
	
	// Game Manager
	public GameManager gm;	
	private boolean gmReady;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		setFocusable(true);
		requestFocus();
		setLayout(null);
		try {
			gm = new GameManager();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gm.addMouseListenerToPanel(this);
		AudioManager.loopAudio("music", .5);
		gmReady = true;
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
			thread.setName("Game Panel Thread");
		}
		running = true;
	}

	private void init() throws IOException {
		bg = new Background();
		ready = true;
	}

	private void update() {
		if (!ready || !gmReady) return;
		gm.tr.update();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!ready || !gmReady) return;
		paintBackground(g);
		paintTrain(g);
		gm.draw(g);
	}
	
	private void paintTrain(Graphics g) {
		gm.tr.draw(g);
	}
	
	private void paintBackground(Graphics g) {
		bg.drawBackground(g);
	}

	@Override
	public void run() {
		try {
			init();
		} catch (Exception e) {
		}
		long start, elapsed, wait;

		while (running) {
			start = System.nanoTime();
			update();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if (wait < 0)
				wait = 5;
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
			}
			repaint();
			
			if (gm.gameDone)
				break;
		}
	}
	
	public void dispose() {
		thread.interrupt();
	}
}
