package main;

import static main.MainState.MENU;

import javax.swing.JFrame;

import create.CreatePanel;
import create.Manager;
import game.GamePanel;
import menu.MenuPanel;

public class MainFrame extends JFrame implements Runnable {
	
	// Settings
	public static final int FPS = 60;
	private static boolean running;
	
	// Current Frame State
	private static MainState frameState = MENU;
	
	// Frame Panels
	private MenuPanel menuPanel;
	private GamePanel gamePanel;
	private CreatePanel createPanel;
	
	public MainFrame() {
		super("Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		menuPanel = new MenuPanel();
		setContentPane(menuPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		(new Thread(this)).start();
	}
	
	private void start() {
		running = true;
	}
	
	private void update() {
		switch (frameState) {
		case MENU:
			break;
		case GAME:
			if (gamePanel == null) {
				gamePanel = new GamePanel();
				setContentPane(gamePanel);
				revalidate();
			}
			gamePanel.update();
			break;
		case CREATE:
			if (createPanel == null) {
				createPanel = new CreatePanel(new Manager());
				setContentPane(createPanel);
				revalidate();
				pack();
				setLocationRelativeTo(null);
			}
			createPanel.update();
			break;
		default:
			break;
		}
	}
	
	public void repaint() {
		switch (frameState) {
		case MENU:
			if (menuPanel == null) { 
				menuPanel = new MenuPanel();
				setContentPane(menuPanel);
				revalidate();
			}
			menuPanel.repaint();
			break;
		case GAME:
			if (gamePanel == null) {
				gamePanel = new GamePanel();
				setContentPane(gamePanel);
				revalidate();
				pack();
			}
			gamePanel.repaint();
			break;
		case CREATE:
			if (createPanel == null) {
				createPanel = new CreatePanel(new Manager());
				setContentPane(createPanel);
				revalidate();
				pack();
				setLocationRelativeTo(null);
			}
			createPanel.repaint();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void run() {
		long start, elapsed, wait, targetTime = 1000 / FPS;
		start();
		
		while (running) {
			repaint();
			start = System.nanoTime();
			update();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if (wait < 5)
				wait = 10;
			try { Thread.sleep(wait); } catch (Exception e) {}
		}
	}
	
	public static void changeState(MainState frameState) {
		MainFrame.frameState = frameState;
	}
}