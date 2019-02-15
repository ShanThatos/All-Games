package main;

import static main.MainState.MENU;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import game.GamePanel;
import menu.MenuPanel;

public class MainFrame extends JFrame implements Runnable {
	
	// Settings
	private static final int FPS = 60;
	private static boolean running;
	
	// Current Frame State
	private static MainState frameState = MENU;
	
	// Frame Panels
	private MenuPanel menuPanel;
	private GamePanel gamePanel;
	
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
	
	private void update(double deltaTime) {
		checkPanels();
		switch (frameState) {
		case GAME:
			gamePanel.update(deltaTime);
			break;
		case MENU:
			break;
		}
	}
	
	public void repaint() {
		checkPanels();
		switch (frameState) {
		case GAME:
			gamePanel.repaint();
			break;
		case MENU:
			break;
		}
	}
	
	private void checkPanels() {
		switch (frameState) {
		case MENU:
			if (menuPanel == null) { 
				menuPanel = new MenuPanel();
				setContentPane(menuPanel);
				pack();
				setLocationRelativeTo(null);
				revalidate();
			}
			menuPanel.repaint();
			break;
		case GAME:
			if (gamePanel == null) {
				gamePanel = new GamePanel();
				setContentPane(gamePanel);
				pack();
				setLocationRelativeTo(null);
				revalidate();
			}
			gamePanel.repaint();
			break;
		}
	}
	
	@Override
	public void run() {
		start();
		
		long deltaTime = System.nanoTime();
		long frameTime = System.nanoTime();
		while (running) {
			frameTime = System.nanoTime();
			try {
				repaint();
				deltaTime = System.nanoTime() - deltaTime;
				update(deltaTime / 100000.0);
				deltaTime = System.nanoTime();
			} catch (Exception e) {}
			frameTime = System.nanoTime() - frameTime;
			setTitle("FPS: " + 1000000000.0 / frameTime);
		}
	}
	
	public static void changeState(MainState frameState) {
		MainFrame.frameState = frameState;
	}
	public static void main(String[] args) {
		new MainFrame("Template");
	}
}