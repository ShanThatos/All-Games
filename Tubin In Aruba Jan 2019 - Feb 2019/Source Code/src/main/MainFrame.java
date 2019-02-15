package main;

import static main.MainState.MENU;

import javax.swing.JFrame;

import game.GamePanel;
import menu.MenuPanel;

public class MainFrame extends JFrame implements Runnable {
	
	// Settings
	private static final int MAX_FPS = 60;
	private static boolean running;
	
	// Current Frame State
	private static MainState frameState = MENU;
	
	// Frame Panels
	private static MenuPanel menuPanel;
	private static GamePanel gamePanel;
	
	public MainFrame(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuPanel = new MenuPanel();
		setContentPane(menuPanel);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		(new Thread(this)).start();
	}
	
	private void start() {
		
		running = true;
	}
	
	private void update(double dt) {
		checkPanels();
		switch (frameState) {
		case GAME:
			gamePanel.update(dt);
			break;
		}
	}
	
	public void repaint() {
		checkPanels();
		switch (frameState) {
		case MENU:
			menuPanel.repaint();
			break;
		case GAME:
			gamePanel.repaint();
			break;
		}
	}
	private void checkPanels() {
		switch (frameState) {
		case MENU:
			if (menuPanel == null) { 
				menuPanel = new MenuPanel();
				setContentPane(menuPanel);
				revalidate();
			}
			break;
		case GAME:
			if (gamePanel == null) {
				gamePanel = new GamePanel();
				setContentPane(gamePanel);
				revalidate();
			}
			break;
		}
	}
	
	
	@Override
	public void run() {
		start();
		final double INV_FPS = 1000000000.0 / MAX_FPS;
		double deltaTime, startTime = System.nanoTime();
		while (running) {
			repaint();
			deltaTime = System.nanoTime() - startTime;
			update(deltaTime);
			startTime = System.nanoTime();
			if (INV_FPS > deltaTime) {
				long nano = (long) (INV_FPS - deltaTime);
				try { Thread.sleep(nano / 1000000, (int) (nano % 1000000)); }
				catch (Exception e) {}
			}
		}
	}
	
	public static void changeState(MainState frameState) {
		MainFrame.frameState = frameState;
	}
	public static void resetAllPanels() {
		gamePanel = null;
		menuPanel = null;
	}
	public static void main(String[] args) {
		new MainFrame("Tubin In Aruba");
	}
}